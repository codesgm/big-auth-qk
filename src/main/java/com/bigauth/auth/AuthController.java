package com.bigauth.auth;

import io.quarkus.oidc.IdToken;
import io.quarkus.oidc.UserInfo;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/auth")
public class AuthController {

    @Inject
    AuthenticationProcessor authProcessor;

    @Inject
    AuthResponseBuilder responseBuilder;

    @Inject
    @IdToken
    JsonWebToken idToken;

    @Inject
    UserInfo userInfo;

    @GET
    @Path("/google")
    @Authenticated
    public Response loginWithGoogle() {
        return authProcessor.processGoogleAuthentication(idToken, userInfo);
    }

    @GET
    @Path("/callback")
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    public Response googleCallback() {
        return authProcessor.processGoogleAuthentication(idToken, userInfo);
    }

    @POST
    @Path("/refresh")
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshToken(@CookieParam("refresh_token") String refreshToken) {
        return authProcessor.processTokenRefresh(refreshToken);
    }

    @POST
    @Path("/logout")
    public Response logout() {
        return responseBuilder.buildLogoutResponse();
    }
}
