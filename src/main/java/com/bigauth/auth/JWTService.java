package com.bigauth.auth;

import com.bigauth.user.User;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class JWTService {

    @ConfigProperty(name = "jwt.expiration-seconds")
    Long jwtExpirationSeconds;

    @ConfigProperty(name = "jwt.refresh-expiration-seconds")
    Long jwtRefreshExpirationSeconds;

    @ConfigProperty(name = "jwt.issuer")
    String jwtIssuer;

    @ConfigProperty(name = "jwt.audience")
    String jwtAudience;

    @Inject
    JWTParser jwtParser;

    public String generateToken(User user) {
        return Jwt.issuer(jwtIssuer)
                .audience(jwtAudience)
                .subject(user.getId().toString())
                .upn(user.getEmail())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("google_id", user.getGoogleId())
                .claim("picture_url", user.getPictureUrl())
                .claim("roles", Set.of("USER"))
                .claim("system", "my-finance-account")
                .claim("type", "access")
                .expiresIn(Duration.ofSeconds(jwtExpirationSeconds))
                .sign();
    }

    public String generateRefreshToken(User user) {
        return Jwt.issuer(jwtIssuer)
                .audience(jwtAudience)
                .subject(user.getId().toString())
                .upn(user.getEmail())
                .claim("email", user.getEmail())
                .claim("type", "refresh")
                .expiresIn(Duration.ofSeconds(jwtRefreshExpirationSeconds))
                .sign();
    }

    public boolean validateRefreshToken(String token) {
        try {
            JsonWebToken jwt = jwtParser.parse(token);
            
            if (!jwtIssuer.equals(jwt.getIssuer())) {
                return false;
            }
            
            if (!jwt.getAudience().contains(jwtAudience)) {
                return false;
            }
            
            String tokenType = jwt.getClaim("type");
            if (!"refresh".equals(tokenType)) {
                return false;
            }
            
            return jwt.getExpirationTime() > System.currentTimeMillis() / 1000;
        } catch (ParseException e) {
            return false;
        }
    }

    public String extractUserIdFromRefreshToken(String token) {
        try {
            JsonWebToken jwt = jwtParser.parse(token);
            return jwt.getSubject();
        } catch (ParseException e) {
            throw new SecurityException("Invalid refresh token");
        }
    }
}
