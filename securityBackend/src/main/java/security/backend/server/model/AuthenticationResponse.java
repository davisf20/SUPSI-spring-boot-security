package security.backend.server.model;

import lombok.*;

/**
 * Response after authentication
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
}
