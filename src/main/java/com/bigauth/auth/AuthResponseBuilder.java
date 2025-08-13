package com.bigauth.auth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;

@ApplicationScoped
public class AuthResponseBuilder {

    @Inject
    CookieService cookieService;

    @ConfigProperty(name = "frontend.url")
    String frontendUrl;

    public Response buildSuccessResponse(InternalAuthResponse authResponse) {
        String redirectUrl = String.format(
            "%s/auth/success?access_token=%s&expires_in=%d",
            frontendUrl,
            authResponse.getAccessToken(),
            authResponse.getExpiresIn()
        );
        
        return Response.temporaryRedirect(URI.create(redirectUrl))
            .cookie(cookieService.createRefreshTokenCookie(authResponse.getRefreshToken()))
            .build();
    }

    public Response buildErrorResponse(String error) {
        String errorUrl = frontendUrl + "/login?error=" + error;
        return Response.temporaryRedirect(URI.create(errorUrl)).build();
    }

    public Response buildRefreshResponse(InternalAuthResponse response) {
        return Response.ok(response.toPublicResponse())
            .cookie(cookieService.createRefreshTokenCookie(response.getRefreshToken()))
            .build();
    }

    public Response buildLogoutResponse() {
        return Response.ok("Logged out")
            .cookie(cookieService.createExpiredRefreshTokenCookie())
            .build();
    }
}
