package kz.reself.limma.filestorage.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "category_image")
@Data
public class CategoryImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data")
    private byte[] data;

    @Column(name = "category_id")
    private Integer categoryId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

}
