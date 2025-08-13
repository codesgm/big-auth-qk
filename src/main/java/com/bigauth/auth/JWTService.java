package com.bigauth.auth;

import com.bigauth.user.User;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class JWTService {

    @ConfigProperty(name = "jwt.expiration-seconds")
    Long jwtExpirationSeconds;

    @ConfigProperty(name = "jwt.issuer")
    String jwtIssuer;

    @ConfigProperty(name = "jwt.audience")
    String jwtAudience;

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
                .expiresIn(Duration.ofSeconds(jwtExpirationSeconds))
                .sign();
    }
}
