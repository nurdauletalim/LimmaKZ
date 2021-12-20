package kz.reself.limma.product.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDTOWithImageCount {
    private int id;
    private String name;
    private Integer price;
    private String description;
    private Integer categoryId;
    private Timestamp publishedDate;
    private Category category;
    private String condition;
    private State state;
    private Integer imageCount;
    private Integer organizationId;
//    private Organization organization;
    private Model model;
    private String capacity;
    private List<Image> images = new ArrayList<>();

    public ProductDTOWithImageCount(Product product){
        id = product.getId();
        name = product.getName();
        price = product.getPrice();
        description = product.getDescription();
        categoryId = product.getCategoryId();
        publishedDate = product.getPublishedDate();
        category = product.getCategory();
        condition = product.getCondition();
        state = product.getState();
        organizationId = product.getOrganizationId();
//        organization = product.getOrganization();
        model = product.getModel();
    }

}
