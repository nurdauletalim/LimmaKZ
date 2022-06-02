package kz.reself.limma.catalog.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "model_image")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ModelImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "model_id")
    private Integer objectId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "model_id", insertable = false, updatable = false)
    private Model model;

}
