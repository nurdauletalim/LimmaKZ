package kz.reself.limma.promotion.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import kz.reself.limma.product.model.Product;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "image")
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data")
    private byte[] data;


    @Column(name = "product_id")
    private Integer productId;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JsonBackReference
//    @JoinColumn(name = "product_id", insertable = false, updatable = false)
//    private Product product;

}
