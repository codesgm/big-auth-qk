package com.bigauth.auth;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;

@ApplicationScoped
public class JWTService {

    @ConfigProperty(name = "jwt.expiration-seconds")
    Long jwtExpirationSeconds;

    public String generateToken(String googleId, String email, String name) {
        return Jwt.issuer("big-auth-ms")
                .upn(email)
                .subject(googleId)
                .claim("name", name)
                .expiresIn(Duration.ofSeconds(jwtExpirationSeconds))
                .sign();
    }
}
