package kz.reself.limma.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDTO {
    private int id;
    private String name;
    private Integer price;
    private String description;
    private Integer categoryId;
    private Timestamp publishedDate;
    private Category category;
    private String condition;
    private State state;
    private List<Image> images = new ArrayList<>();
    private Integer organizationId;
//    private Organization organization;
    private Model model;
    @JsonIgnore
    private List<Product> productsList = new ArrayList<>();
    private List<ProductDTOWithImageCount> products = new ArrayList<>();
    private String capacity;

    public ProductDTO(Product product){
        id = product.getId();
        name = product.getName();
        price = product.getPrice();
        description = product.getDescription();
        categoryId = product.getCategoryId();
        publishedDate = product.getPublishedDate();
        category = product.getCategory();
        condition = product.getCondition();
        state = product.getState();
        images = product.getImages();
        organizationId = product.getOrganizationId();
//        organization = product.getOrganization();
        model = product.getModel();
        productsList.add(product);
    }

}
