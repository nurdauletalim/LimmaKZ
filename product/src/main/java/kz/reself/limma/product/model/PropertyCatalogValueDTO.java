package kz.reself.limma.product.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PropertyCatalogValueDTO implements Serializable {
    private Integer id;
    private Integer count;
    private String value;
    private String displayName;
    private Integer catalogId;
    private PropertyCatalog catalog;
}
