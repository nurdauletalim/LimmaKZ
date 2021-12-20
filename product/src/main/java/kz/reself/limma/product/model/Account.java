package kz.reself.limma.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "account")
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private Boolean block;
    @Column(name = "organization_id")
    private Integer organizationId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Organization organization;
    private Role role;
    private String chatId;

}
