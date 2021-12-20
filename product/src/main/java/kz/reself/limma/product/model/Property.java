package kz.reself.limma.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "property")
@Data
public class Property implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "template_id")
    private Integer templateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", insertable = false, updatable = false)
    @JsonIgnore
    private PropertyTemplate template;

    private String key;
    @Column(name = "display_name")
    private String displayName;
    @Column(name = "catalog_property")
    private Boolean catalogProperty;

    @Column(name = "catalog_id")
    private Integer catalogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id", insertable = false, updatable = false)
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PropertyCatalog catalog;

    @Column(name = "sort_order")
    private Integer order;

    @Column(name = "main")
    private boolean main;

    @JsonIgnore
    private State state;

}
