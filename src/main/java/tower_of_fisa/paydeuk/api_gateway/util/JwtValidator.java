package tower_of_fisa.paydeuk.api_gateway.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
@RequiredArgsConstructor
public class JwtValidator {

    @Value("${jwt.secret-key}")
    private String rawKey;
    private Key secretKey;


    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(rawKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 토큰 유효성 검증
     *
     * @param token JWT 토큰
     * @return boolean
     * @throws ExpiredJwtException 토큰이 만료되었습니다.
     * @throws SignatureException 토큰의 서명이 올바르지 않습니다.
     * @throws MalformedJwtException jwt의 양식이 올바르지 않습니다.
     * @throws UnsupportedJwtException
     */
    public void isTokenInvalid(String token) throws ExpiredJwtException {
                    Jwts .parserBuilder()
                            .setSigningKey(secretKey)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

    }
}

