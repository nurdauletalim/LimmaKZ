package kz.reself.limma.catalog.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "model_image")
@Data
public class ModelImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data")
    private byte[] data;


    @Column(name = "model_id")
    private Integer modelId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "model_id", insertable = false, updatable = false)
    private Model model;

}
