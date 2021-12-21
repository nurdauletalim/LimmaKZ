package kz.reself.limma.account.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Table
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private String roles;

    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "chat_id")
    private Long chatId;
}
