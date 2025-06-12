package tower_of_fisa.paydeuk.api_gateway.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
@Slf4j
@Configuration
public class PreFlightCorsConfiguration {

    private static final String ALLOWED_HEADERS = String.join(",",
            "x-requested-with", "authorization",
            "refreshtoken", "Access-Control-Allow-Origin", "Content-Type",
            "credential", "X-AUTH-TOKEN", "X-CSRF-TOKEN");

    private static final String ALLOWED_METHODS = "GET, PUT, POST, DELETE, PATCH, OPTIONS";
    private static final String ALLOWED_CREDENTIALS = "true";
    private static final String EXPOSE_HEADERS = String.join(",",
            "*", "Authorization", "Refreshtoken", "authorization", "refreshtoken");
    private static final String MAX_AGE = "3600";

    private static final List<String> ALLOWED_ORIGINS = List.of(
            "https://paydeuk.com",
            "https://www.paydeuk.com"
    );

    @Bean
    public WebFilter corsFilter() {
        log.info("PreFlightCorsConfiguration corsFilter() initialized");

        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            ServerHttpResponse response = ctx.getResponse();
            String requestOrigin = request.getHeaders().getOrigin();

            if (CorsUtils.isPreFlightRequest(request)) {
                log.info("Handling preflight CORS request from origin: {}", requestOrigin);

                if (requestOrigin != null && ALLOWED_ORIGINS.contains(requestOrigin)) {
                    response.getHeaders().set("Access-Control-Allow-Origin", requestOrigin);
                }

                response.getHeaders().set("Access-Control-Allow-Methods", ALLOWED_METHODS);
                response.getHeaders().set("Access-Control-Allow-Headers", ALLOWED_HEADERS);
                response.getHeaders().set("Access-Control-Allow-Credentials", ALLOWED_CREDENTIALS);
                response.getHeaders().set("Access-Control-Max-Age", MAX_AGE);
                response.getHeaders().set("Access-Control-Expose-Headers", EXPOSE_HEADERS);

                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }

            // 일반 요청 처리 시에도 Allow-Origin 설정 필요
            if (requestOrigin != null && ALLOWED_ORIGINS.contains(requestOrigin)) {
                response.getHeaders().set("Access-Control-Allow-Origin", requestOrigin);
                response.getHeaders().set("Access-Control-Allow-Credentials", ALLOWED_CREDENTIALS);
            }

            return chain.filter(ctx);
        };
    }
}