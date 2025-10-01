package com.webserver.evrentalsystem.utils;

import com.webserver.evrentalsystem.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Value("${JWT_ACCESS_TOKEN_EXPIRATION_MS}")
    private long jwtAccessTokenExpirationMs;

    @Value("${JWT_REFRESH_TOKEN_EXPIRATION_MS}")
    private long jwtRefreshTokenExpirationMs;

    public String generateJwtAccessToken(User user) {
        return generateJwtToken(user, jwtAccessTokenExpirationMs);
    }

    public String generateJwtRefreshToken(User user) {
        return generateJwtToken(user, jwtRefreshTokenExpirationMs);
    }

    private String generateJwtToken(User user, long jwtExpirationMs) {
        return Jwts.builder()
                .setSubject(user.getPhone())
                .claim(Constant.Attribute.ROLE, user.getRole().getValue())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUserPhoneFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String getRoleFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().get("role", String.class);
    }

    public boolean invalidateJwtToken(String authToken) {
        Logger.printf(authToken);
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            Logger.printf("validateJwtToken: true");
            return false;
        } catch (Exception e) {
            Logger.printf("Invalid JWT signature: " + e.getMessage());
        }
        return true;
    }

    private Key getSigningKey() {
        byte[] keyBytes = this.jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
