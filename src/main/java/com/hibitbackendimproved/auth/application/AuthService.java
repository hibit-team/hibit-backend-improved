package com.hibitbackendimproved.auth.application;

import com.hibitbackendimproved.auth.domain.AuthToken;
import com.hibitbackendimproved.auth.domain.OAuthToken;
import com.hibitbackendimproved.auth.domain.OAuthTokenRepository;
import com.hibitbackendimproved.auth.dto.OAuthMember;
import com.hibitbackendimproved.auth.dto.request.TokenRenewalRequest;
import com.hibitbackendimproved.auth.dto.response.AccessAndRefreshTokenResponse;
import com.hibitbackendimproved.auth.dto.response.AccessTokenResponse;
import com.hibitbackendimproved.auth.event.MemberSavedEvent;
import com.hibitbackendimproved.auth.exception.ServerErrorOAuthException;
import com.hibitbackendimproved.member.domain.Member;
import com.hibitbackendimproved.member.domain.MemberRepository;
import com.hibitbackendimproved.profile.domain.Profile;
import com.hibitbackendimproved.profile.domain.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final ProfileRepository profileRepository;
    private final TokenCreator tokenCreator;
    private final ApplicationEventPublisher eventPublisher;
    private final Map<String, OAuthUri> oauthUriProviders;
    private final Map<String, OAuthClient> oauthClients;
    private final Logger log = LoggerFactory.getLogger(AuthService.class);

    public AuthService(final MemberRepository memberRepository, final OAuthTokenRepository oAuthTokenRepository, final ProfileRepository profileRepository,
                       final TokenCreator tokenCreator, final ApplicationEventPublisher eventPublisher,
                       final List<OAuthUri> oauthUris, final List<OAuthClient> oauthClients) {
        this.memberRepository = memberRepository;
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.profileRepository = profileRepository;
        this.tokenCreator = tokenCreator;
        this.eventPublisher = eventPublisher;
        this.oauthUriProviders = oauthUris.stream()
                .collect(Collectors.toMap(OAuthUri::getProviderName, Function.identity()));
        this.oauthClients = oauthClients.stream()
                .collect(Collectors.toMap(OAuthClient::getProviderName, Function.identity()));
    }

    public String generateOAuthUri(final String oauthProvider, final String redirectUri) {
        final String oauthProviderName = oauthProvider.trim();
        log.info("oauth provider name: {}", oauthProviderName);

        if (!oauthUriProviders.containsKey(oauthProviderName)) {
            throw new ServerErrorOAuthException("제공된 OAuth Provider가 아닙니다.");
        }
        return oauthUriProviders.get(oauthProviderName).generate(redirectUri);
    }

    public OAuthMember handleOAuth(final String oauthProvider, final String code, final String redirectUri) {
        if (!oauthUriProviders.containsKey(oauthProvider)) {
            throw new ServerErrorOAuthException("제공된 OAuth Provider가 아닙니다.");
        }
        OAuthClient client = oauthClients.get(oauthProvider);
        return client.getOAuthMember(code, redirectUri);
    }

    @Transactional
    public AccessAndRefreshTokenResponse generateAccessAndRefreshToken(final OAuthMember oAuthMember) {
        Member foundMember = findMember(oAuthMember);

        OAuthToken oAuthToken = getOAuthToken(oAuthMember, foundMember);
        oAuthToken.change(oAuthMember.getRefreshToken());

        AuthToken authToken = tokenCreator.createAuthToken(foundMember.getId());
        return new AccessAndRefreshTokenResponse(authToken.getAccessToken(), authToken.getRefreshToken());
    }

    private Member findMember(final OAuthMember oAuthMember) {
        String socialId = oAuthMember.getSocialId();
        if (memberRepository.existsBySocialId(socialId)) {
            return memberRepository.getBySocialIdOrThrow(socialId);
        }
        return saveMember(oAuthMember);
    }

    private OAuthToken getOAuthToken(final OAuthMember oAuthMember, final Member member) {
        Long memberId = member.getId();
        if (oAuthTokenRepository.existsByMemberId(memberId)) {
            return oAuthTokenRepository.getByMemberId(memberId);
        }
        return oAuthTokenRepository.save(new OAuthToken(member, oAuthMember.getRefreshToken()));
    }

    private Member saveMember(final OAuthMember oAuthMember) {
        Member savedMember = memberRepository.save(oAuthMember.toMember());

        profileRepository.save(new Profile(
                savedMember,
                oAuthMember.getNickname(),
                oAuthMember.getProfileImage(),
                null
        ));
        eventPublisher.publishEvent(new MemberSavedEvent(savedMember.getId()));
        return savedMember;
    }

    public AccessTokenResponse generateAccessToken(final TokenRenewalRequest tokenRenewalRequest) {
        String refreshToken = tokenRenewalRequest.getRefreshToken();
        AuthToken authToken = tokenCreator.renewAuthToken(refreshToken);
        return new AccessTokenResponse(authToken.getAccessToken());
    }

    public Long extractMemberId(final String accessToken) {
        Long memberId = tokenCreator.extractPayLoad(accessToken);
        memberRepository.validateExistById(memberId);
        return memberId;
    }

    @Transactional
    public void deleteToken(final Long id) {
        oAuthTokenRepository.deleteAllByMemberId(id);
    }
}
