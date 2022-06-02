package kz.reself.limma.account.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
@Data
public class AccountDTO {
    private Long id;

    private String username;

    private String password;

    private String roles;

    private Boolean superAdmin;

    private Long organizationId;

    private Long chatId;
}
