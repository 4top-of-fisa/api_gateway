package tower_of_fisa.paydeuk.api_gateway.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorDefineCode {
    INVALID_AUTH_HEADER("AUTH_01","API 요청의 Authorization Header가 올바르지 않습니다."),
    NO_REFRESH_TOKEN("AUTH_02", "Refresh Token이 존재하지 않습니다. "),
    EXPIRED_TOKEN("AUTH_02", "토큰이 만료되었습니다"),
    INVALID_ACCESS_TOKEN("AUTH_03", "유효하지 않은 토큰입니다."),
    REFRESH_TOKEN_COOKIE_NOT_FOUND("AUTH_04", "쿠키에 Refresh Token 이 존재하지 않습니다."),
    MALFORMED_TOKEN("AUTH_03", "");

    private final String code;
    private final String message;
}
