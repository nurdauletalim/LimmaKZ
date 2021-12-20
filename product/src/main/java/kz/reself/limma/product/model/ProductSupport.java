package kz.reself.limma.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "product_support")
public class ProductSupport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_id")
    private Integer productId;

    @ManyToOne
    @JoinColumn(name = "product_id",insertable = false,updatable = false)
    @JsonIgnore
    private Product product;

    private String comment;

    private Status status;

    @Column(name = "manager_id")
    private Integer accountId;

    @ManyToOne
    @JoinColumn(name = "manager_id",insertable = false,updatable = false)
    @JsonIgnore
    private Account account;
}
