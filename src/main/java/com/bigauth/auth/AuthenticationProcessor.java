package com.bigauth.auth;

import io.quarkus.oidc.UserInfo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class AuthenticationProcessor {

    @Inject
    AuthService authService;

    @Inject
    AuthResponseBuilder responseBuilder;

    public Response processGoogleAuthentication(JsonWebToken idToken, UserInfo userInfo) {
        try {
            InternalAuthResponse authResponse = authService.processGoogleAuth(idToken, userInfo);
            return responseBuilder.buildSuccessResponse(authResponse);
        } catch (Exception e) {
            return responseBuilder.buildErrorResponse("auth_failed");
        }
    }

    public Response processTokenRefresh(String refreshToken) {
        if (refreshToken == null) {
            return Response.status(401).entity("Refresh token missing").build();
        }
        
        try {
            InternalAuthResponse response = authService.refreshAccessToken(refreshToken);
            return responseBuilder.buildRefreshResponse(response);
        } catch (Exception e) {
            return Response.status(401).entity("Invalid refresh token").build();
        }
    }
}
