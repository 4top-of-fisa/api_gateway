package tower_of_fisa.paydeuk.api_gateway.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import tower_of_fisa.paydeuk.api_gateway.error.ErrorDefineCode;
import tower_of_fisa.paydeuk.api_gateway.error.GlobalErrorResponder;
import tower_of_fisa.paydeuk.api_gateway.error.exception.custom.InvalidAuthorizationHeaderException400;
import tower_of_fisa.paydeuk.api_gateway.error.exception.custom.JwtValidationException401;
import tower_of_fisa.paydeuk.api_gateway.util.JwtValidator;

@Slf4j
@Component
public class AccessTokenValidationGatewayFilter extends AbstractGatewayFilterFactory<AccessTokenValidationGatewayFilter.Config> {

    private final JwtValidator jwtValidator;
    private final GlobalErrorResponder globalErrorResponder;

    public AccessTokenValidationGatewayFilter(
            JwtValidator jwtValidator,
            GlobalErrorResponder globalErrorResponder
    ) {
        super(AccessTokenValidationGatewayFilter.Config.class);
        this.jwtValidator = jwtValidator;
        this.globalErrorResponder = globalErrorResponder;
    }

    @Override
    public GatewayFilter apply(AccessTokenValidationGatewayFilter.Config config) {
        return (exchange,chain) -> {
            log.error("========== Access Token Validation Filter =========");

            // JWT 검증 로직
            ServerHttpRequest request = exchange.getRequest();
            String authHeader = request.getHeaders().getFirst("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return globalErrorResponder.respond(exchange, HttpStatus.BAD_REQUEST, new InvalidAuthorizationHeaderException400(ErrorDefineCode.INVALID_AUTH_HEADER));
            }

            String token = authHeader.substring(7); // Remove "Bearer "

            try {
                jwtValidator.isTokenInvalid(token);
                String username = jwtValidator.extractUsername(token);

                ServerHttpRequest modifiedRequest = request.mutate()
                        .header("X-User-Name", username)
                        .build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            } catch (Exception e) {
                return globalErrorResponder.respond(exchange, HttpStatus.UNAUTHORIZED, new JwtValidationException401(e));
            }
        };
    }

    @Getter
    @Setter
    public static class Config {
        // 필터와 관련된 설정값을 추가할 수 있음
    }
}
