package kz.reself.limma.promotion.service;

import kz.reself.limma.promotion.model.Promotion;
import kz.reself.limma.promotion.model.PromotionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPromotionService {
    Promotion getById(Integer id);
//    List<Promotion> getAllByOrganization(Organization organization);
    List<Promotion> getAllActive();
    List<Promotion> getAllActiveCount(Integer count);
    List<PromotionDTO> getAllActiveDTO(Integer count);
    Page<Promotion> getAllPageable(Pageable pageable);

    Promotion createPromotion(Promotion promotion);

    void deleteById(Integer promotionId);

    Page<Promotion> findSearchString(String searchString, Pageable pageableRequest);
}
