package kz.reself.limma.gatewaycrm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String token;
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String roles;
    private Long organizationId;

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                '}';
    }


}
