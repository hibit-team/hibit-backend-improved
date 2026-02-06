package com.hibitbackendimproved.exceptionhandler;

import com.hibitbackendimproved.auth.exception.EmptyAuthorizationHeaderException;
import com.hibitbackendimproved.auth.exception.InvalidTokenException;
import com.hibitbackendimproved.auth.exception.NotFoundOAuthTokenException;
import com.hibitbackendimproved.auth.exception.NotFoundTokenException;
import com.hibitbackendimproved.auth.exception.OAuthException;
import com.hibitbackendimproved.member.exception.InvalidMemberException;
import com.hibitbackendimproved.member.exception.NotFoundMemberException;
import com.hibitbackendimproved.post.exception.InvalidContentException;
import com.hibitbackendimproved.post.exception.InvalidExhibitionException;
import com.hibitbackendimproved.post.exception.InvalidTitleException;
import com.hibitbackendimproved.post.exception.NotFoundImageFileException;
import com.hibitbackendimproved.post.exception.NotFoundPostStatusException;
import com.hibitbackendimproved.post.exception.NotFoundTogetherActivityException;
import com.hibitbackendimproved.profile.exception.InvalidProfileAlreadyException;
import com.hibitbackendimproved.profile.exception.InvalidProfileException;
import com.hibitbackendimproved.profile.exception.NicknameAlreadyTakenException;
import com.hibitbackendimproved.profile.exception.NotFoundProfileException;
import com.hibitbackendimproved.support.error.dto.ErrorReportRequest;
import com.hibitbackendimproved.support.error.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String INVALID_DTO_FIELD_ERROR_MESSAGE_FORMAT = "%s 필드는 %s (전달된 값: %s)";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentException(final MethodArgumentNotValidException e) {
        FieldError firstFieldError = e.getFieldErrors().get(0);
        String errorMessage = String.format(INVALID_DTO_FIELD_ERROR_MESSAGE_FORMAT, firstFieldError.getField()
                , firstFieldError.getDefaultMessage(), firstFieldError.getRejectedValue());

        ErrorResponse errorResponse = new ErrorResponse(errorMessage);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handLieInvalidRequestBodyException() {
        ErrorResponse errorResponse = new ErrorResponse("잘못된 형식의 RequestBody 입니다.");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({
            InvalidMemberException.class,
            InvalidTitleException.class,
            InvalidContentException.class,
            InvalidExhibitionException.class,
            InvalidProfileException.class,
            InvalidProfileAlreadyException.class,
            NicknameAlreadyTakenException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidBadRequestException(final RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({
            EmptyAuthorizationHeaderException.class,
            InvalidTokenException.class
    })
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(final RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler({
            NotFoundOAuthTokenException.class,
            NotFoundTokenException.class,
            NotFoundMemberException.class,
            NotFoundProfileException.class,
            NotFoundImageFileException.class,
            NotFoundPostStatusException.class,
            NotFoundPostStatusException.class,
            NotFoundTogetherActivityException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(final RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowedException() {
        ErrorResponse errorResponse = new ErrorResponse("지원하지 않는 HTTP 메서드 요청입니다.");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(OAuthException.class)
    public ResponseEntity<ErrorResponse> handleOAuthException(final RuntimeException e) {
        log.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(final Exception e, final HttpServletRequest request) {
        ErrorReportRequest errorReportRequest = new ErrorReportRequest(request, e);
        log.error(errorReportRequest.getLogMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse("서버에서 예상치 못한 에러가 발생했습니다.");
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
