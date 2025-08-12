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

    @GET
    @Path("/google")
    public Response loginWithGoogle() {
        return Response.temporaryRedirect(URI.create("/auth/callback")).build();
    }

    @GET
    @Path("/callback")
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    public Response googleCallback() {
        AuthResponseDTO authResponse = authService.processGoogleAuth(idToken, userInfo);
        return Response.ok(authResponse).build();
    }
}
