package kz.reself.limma.catalog.model;

import lombok.Data;

@Data
public class BrandDTO {
    private Integer id;

    private Integer categoryId;

    private String categoryName;

    private String value;
    private String displayName;
    private State state;

    public BrandDTO(Brand brand){
        id = brand.getId();
        categoryId = brand.getCategoryId();
        value = brand.getValue();
        displayName = brand.getDisplayName();
        state = brand.getState();
    }
}
