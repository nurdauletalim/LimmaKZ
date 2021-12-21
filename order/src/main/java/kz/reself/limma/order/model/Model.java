package kz.reself.limma.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "model")
@Data
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String value;
    private String displayName;
    private String description;
    @Column(name = "brand_id")
    private int brandId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id",  insertable = false, updatable = false)
    @JsonIgnore
    private Brand brand;
    private State state;

    @JsonManagedReference
    @OneToMany(mappedBy = "model")
    private List<ModelImage> images = new ArrayList<>();

}
