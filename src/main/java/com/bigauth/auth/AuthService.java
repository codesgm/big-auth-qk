package com.bigauth.auth;

import com.bigauth.user.User;
import com.bigauth.user.UserDTO;
import com.bigauth.user.UserRepository;
import io.quarkus.oidc.IdToken;
import io.quarkus.oidc.UserInfo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class AuthService {

    @Inject
    JWTService jwtService;

    @Inject
    UserRepository userRepository;

    @ConfigProperty(name = "jwt.expiration-seconds")
    Long jwtExpirationSeconds;

    @ConfigProperty(name = "jwt.refresh-expiration-seconds")
    Long jwtRefreshExpirationSeconds;

    @Transactional
    public AuthResponseDTO processGoogleAuth(JsonWebToken idToken, UserInfo userInfo) {
        String googleId = idToken.getSubject();
        String email = userInfo.getEmail();
        String name = userInfo.getName();
        String pictureUrl = userInfo.getString("picture");

        User user = userRepository.findByGoogleId(googleId);
        
        if (user == null) {
            user = new User();
            user.setGoogleId(googleId);
            user.setEmail(email);
            user.setName(name);
            user.setPictureUrl(pictureUrl);
        } else {
            user.setEmail(email);
            user.setName(name);
            user.setPictureUrl(pictureUrl);
        }

        user = userRepository.save(user);

        String accessToken = jwtService.generateToken(user.getGoogleId(), user.getEmail(), user.getName());
        UserDTO userDTO = UserDTO.from(user);

        return new AuthResponseDTO(accessToken, jwtExpirationSeconds, userDTO);
    }
}
