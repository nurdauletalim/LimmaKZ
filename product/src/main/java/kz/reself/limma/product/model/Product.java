package kz.reself.limma.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String value;

    private Integer price;

    private String description;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "published_date")
    private Timestamp publishedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id",  insertable = false, updatable = false)
    @JsonIgnore
    private Category category;

    private String condition;

    private State state;

//    @OneToMany(targetEntity = Image.class, mappedBy = "product", cascade = CascadeType.ALL)
//    private Set<Image> list;

//    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

    @Column(name = "organization_id")
    private Integer organizationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", insertable = false, updatable = false)
    @JsonIgnore
    private Organization organization;

    @Column(name = "model_id")
    private Integer modelId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "model_id",insertable = false,updatable = false)
    private Model model;
}
