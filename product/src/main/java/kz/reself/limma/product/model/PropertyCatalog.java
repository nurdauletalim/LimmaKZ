package kz.reself.limma.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "property_catalog")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PropertyCatalog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String code;
    @Enumerated(EnumType.ORDINAL)
    private State state;
}
