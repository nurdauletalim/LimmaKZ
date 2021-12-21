package kz.reself.limma.filestorage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "parent_category_id")
    private Integer parentCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_category_id", insertable = false, updatable = false)
    @JsonIgnore
    private Category parentCategory;

    @JsonIgnore
    private State state;

//    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<CategoryImage> images = new ArrayList<>();

}
