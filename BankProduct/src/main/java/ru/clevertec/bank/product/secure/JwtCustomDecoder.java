package ru.clevertec.bank.product.secure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import javax.crypto.SecretKey;
import java.time.Instant;

public class JwtCustomDecoder implements JwtDecoder {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            JwsHeader header = claimsJws.getHeader();
            Claims claimsBody = claimsJws.getPayload();
            Instant expiration = claimsBody.getExpiration().toInstant();
            Instant issuedAt = claimsBody.getIssuedAt().toInstant();
            return new Jwt(token, issuedAt, expiration, header, claimsBody);
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException ex) {
            throw new BadJwtException("Presented token isn't valid");
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
