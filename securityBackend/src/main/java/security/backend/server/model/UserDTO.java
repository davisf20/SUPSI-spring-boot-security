package security.backend.server.model;

import lombok.*;

/**
 * Class used for communication with client
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String password;
    private String role;
}
