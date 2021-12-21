package kz.reself.limma.promotion.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "promotion_image")
@Data
public class PromotionImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data")
    private byte[] data;


    @Column(name = "promotion_id")
    private Integer promotionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "promotion_id", insertable = false, updatable = false)
    private Promotion promotion;

}
