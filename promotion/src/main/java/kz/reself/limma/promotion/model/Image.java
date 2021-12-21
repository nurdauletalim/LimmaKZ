package kz.reself.limma.promotion.model;

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

}
