package kz.reself.limma.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "property_catalog_value")
@Data
public class PropertyCatalogValue implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String value;
    @Column(name = "display_name")
    private String displayName;

    @Column(name = "property_catalog_id")
    private Integer catalogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_catalog_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private PropertyCatalog catalog;

}
