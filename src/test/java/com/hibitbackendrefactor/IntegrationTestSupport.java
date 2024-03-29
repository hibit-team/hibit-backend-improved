package com.hibitbackendrefactor;

import com.hibitbackendrefactor.auth.domain.TokenRepository;
import com.hibitbackendrefactor.auth.dto.LoginMember;
import com.hibitbackendrefactor.common.DatabaseCleaner;
import com.hibitbackendrefactor.config.ExternalApiConfig;
import com.hibitbackendrefactor.global.config.JpaAuditingConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static com.hibitbackendrefactor.common.fixtures.MemberFixtures.FANCY_ID;

@Import(JpaAuditingConfig.class)
@ActiveProfiles("test")
@SpringBootTest(classes = ExternalApiConfig.class)
public abstract class IntegrationTestSupport {
    protected static final LoginMember LOGIN_MEMBER = new LoginMember(FANCY_ID);

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private TokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
        tokenRepository.deleteAll();
    }
}
