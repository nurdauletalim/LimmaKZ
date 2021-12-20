package kz.reself.limma.product.model;

import lombok.Data;

@Data
public class ProductDTOViewInfo {
    private int id;
    private Integer price;
    private Integer modelId;
    private Model model;

    public ProductDTOViewInfo(Product product){
        id = product.getId();
        price = product.getPrice();
        modelId = product.getModelId();
        model = product.getModel();
    }

    public ProductDTOViewInfo(){
    }

    public ProductDTOViewInfo(Object[] product){
        id = (Integer) product[0];
        modelId = (Integer) product[1];
        price =(Integer) product[2];
    }

}
