package kz.reself.limma.promotion.model;

import lombok.Data;
import java.util.List;

@Data
public class PromotionDTO {
    int id;
    List<PromotionImage> images;
    public PromotionDTO(){}

    public PromotionDTO(int id){
        this.id = id;
    }
}
