package kz.reself.limma.gatewaycrm.model;

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
    private String password;
    private String roles;

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                '}';
    }


}
