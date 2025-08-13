package com.bigauth.auth;

import com.bigauth.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InternalAuthResponse {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private UserDTO user;

    public AuthResponseDTO toPublicResponse() {
        return new AuthResponseDTO(accessToken, expiresIn, user);
    }
}
