package tower_of_fisa.paydeuk.api_gateway.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import tower_of_fisa.paydeuk.api_gateway.error.ErrorDefineCode;
import tower_of_fisa.paydeuk.api_gateway.error.GlobalErrorResponder;
import tower_of_fisa.paydeuk.api_gateway.error.exception.custom.JwtValidationException401;
import tower_of_fisa.paydeuk.api_gateway.error.exception.custom.NoSuchRefreshTokenCookieException404;
import tower_of_fisa.paydeuk.api_gateway.util.JwtValidator;

@Slf4j
@Component
public class RefreshTokenValidationGatewayFilter extends AbstractGatewayFilterFactory<RefreshTokenValidationGatewayFilter.Config> {
    private final JwtValidator jwtValidator;
    private final GlobalErrorResponder globalErrorResponder;

    public RefreshTokenValidationGatewayFilter(
            JwtValidator jwtValidator,
            GlobalErrorResponder globalErrorResponder
    ) {
        super(Config.class);
        this.jwtValidator = jwtValidator;
        this.globalErrorResponder = globalErrorResponder;
    }

    @Override
    public GatewayFilter apply(RefreshTokenValidationGatewayFilter.Config Config) {

        return (exchange,chain) -> {
            log.error("========== Refresh Token Validation Filter =========");
            HttpCookie refreshTokenCookie = exchange.getRequest()
                    .getCookies()
                    .getFirst("refreshToken");

            if (refreshTokenCookie == null) {
                return globalErrorResponder.respond(
                        exchange,
                        HttpStatus.BAD_REQUEST,
                        new NoSuchRefreshTokenCookieException404(ErrorDefineCode.REFRESH_TOKEN_COOKIE_NOT_FOUND));
            } else {
                String token = refreshTokenCookie.getValue();
                try {
                    jwtValidator.isTokenInvalid(token);
                } catch (Exception e) {
                    return globalErrorResponder.respond(exchange, HttpStatus.UNAUTHORIZED, new JwtValidationException401(e));
                }
            }

            return chain.filter(exchange);
        };
    }

    @Getter
    @Setter
    public static class Config {
        // 필터와 관련된 설정값을 추가할 수 있음
    }
}
