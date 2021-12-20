package kz.reself.limma.product.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "property_value")
@Data
public class PropertyValue implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "product_id")
    private Integer productId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id", insertable = false, updatable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JsonIgnore
//    private Product product;

    private String key;
    private String value;
}
