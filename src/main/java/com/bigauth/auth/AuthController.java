package com.bigauth.auth;

import io.quarkus.oidc.IdToken;
import io.quarkus.oidc.UserInfo;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.net.URI;

@Path("/auth")
public class AuthController {

    @Inject
    AuthService authService;

    @Inject
    @IdToken
    JsonWebToken idToken;

    @Inject
    UserInfo userInfo;

    @ConfigProperty(name = "frontend.url")
    String frontendUrl;

    @GET
    @Path("/google")
    @Authenticated
    public Response loginWithGoogle() {
        return Response.ok("Redirecting to Google...").build();
    }

    @GET
    @Path("/callback")
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    public Response googleCallback() {
        try {
            AuthResponseDTO authResponse = authService.processGoogleAuth(idToken, userInfo);
            
            String redirectUrl = String.format(
                "%s/auth/success?access_token=%s&expires_in=%d",
                frontendUrl,
                authResponse.getAccessToken(),
                authResponse.getExpiresIn()
            );
            
            return Response.temporaryRedirect(URI.create(redirectUrl)).build();
        } catch (Exception e) {
            String errorUrl = frontendUrl + "/login?error=auth_failed";
            return Response.temporaryRedirect(URI.create(errorUrl)).build();
        }
    }
}
