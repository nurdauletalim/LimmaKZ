package kz.reself.limma.order.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "product_application")
@Data
public class ProductApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String contact;
    private String name;
    private String email;
    private Timestamp registered;


    @Column(name = "product_id")
    private Integer productId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Product product;

    @Column(name = "manager_id")
    private Integer managerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Account account;

    private String comment;


    private ProductApplicationStatus status;
    private Timestamp taken;
    private Timestamp closed;

    @Override
    public String toString() {
        return  "Phone Number:"+contact+"\n"+
                "Email:" +email+"\n"+
                "User:"+name+"\n"+
                "Product:" + product.getName();

    }
}
