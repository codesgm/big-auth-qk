package com.bigauth.auth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.NewCookie;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class CookieService {

    @ConfigProperty(name = "jwt.refresh-expiration-seconds")
    Long refreshTokenMaxAge;

    @ConfigProperty(name = "cookie.secure", defaultValue = "false")
    boolean cookieSecure;

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private static final String AUTH_PATH = "/auth";

    public NewCookie createRefreshTokenCookie(String refreshToken) {
        return new NewCookie.Builder(REFRESH_TOKEN_COOKIE_NAME)
            .value(refreshToken)
            .httpOnly(true)
            .secure(cookieSecure)
            .sameSite(NewCookie.SameSite.STRICT)
            .path(AUTH_PATH)
            .maxAge(refreshTokenMaxAge.intValue())
            .build();
    }

    public NewCookie createExpiredRefreshTokenCookie() {
        return new NewCookie.Builder(REFRESH_TOKEN_COOKIE_NAME)
            .value("")
            .httpOnly(true)
            .secure(cookieSecure)
            .sameSite(NewCookie.SameSite.STRICT)
            .path(AUTH_PATH)
            .maxAge(0)
            .build();
    }
}
