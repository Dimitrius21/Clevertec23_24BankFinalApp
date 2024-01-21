package ru.clevertec.bank.product.secure;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import ru.clevertec.bank.product.service.JwtService;

import javax.crypto.SecretKey;
import java.time.Instant;

/**
 * Класс с декодером полученного Jwt-Token
 */

public class JwtCustomDecoder implements JwtDecoder {
/*    @Value("${jwt.secret}")
    private String secret;*/

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build();
            io.jsonwebtoken.Jwt<?, ?> parsedToken = parser.parse(token);

            Header header = parsedToken.getHeader();
            Claims claimsBody = (Claims) parsedToken.getPayload();
            Header claimsHeader = parsedToken.getHeader();
            Instant expiration = claimsBody.getExpiration().toInstant();
            Instant issuedAt = claimsBody.getIssuedAt().toInstant();

            Jwt jwt = new Jwt(token, issuedAt, expiration, header, claimsBody);
            return jwt;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException ex) {
            throw new BadJwtException("Presented token isn't valid");
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
