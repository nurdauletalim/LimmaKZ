package kz.reself.limma.promotion.service.impl;

import kz.reself.limma.promotion.model.Promotion;
import kz.reself.limma.promotion.model.PromotionDTO;
import kz.reself.limma.promotion.repository.PromotionImageRepository;
import kz.reself.limma.promotion.repository.PromotionRepository;
import kz.reself.limma.promotion.service.IPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionService implements IPromotionService {
    @Autowired
    PromotionRepository promotionRepository;
    @Autowired
    PromotionImageRepository promotionImageRepository;

    @Override
    public Promotion getById(Integer id) {
        return promotionRepository.getById(id);
    }

//    @Override
//    public List<Promotion> getAllByOrganization(Organization organization) {
//        return promotionRepository.getAllByOrganization(organization);
//    }

    @Override
    public List<Promotion> getAllActive() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return promotionRepository.getAllActive(timestamp);
    }

    @Override
    public List<Promotion> getAllActiveCount(Integer count) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return promotionRepository.getAllActiveCount(timestamp,count);
    }

    @Override
    public List<PromotionDTO> getAllActiveDTO(Integer count) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        List<PromotionDTO> promotionDTOS = promotionRepository.getAllActiveDTO(timestamp,count);
//        for (int i = 0; i < promotionDTOS.size(); i++) {
//            promotionDTOS.get(i).setImages(promotionImageRepository.findAllByPromotionId(promotionDTOS.get(i).getId()));
//        }
//        return promotionDTOS;
        List<PromotionDTO> promotionDTOS = new ArrayList<>();
        List<Object[]> promotionObjects =  promotionRepository.getAllActiveDTO(timestamp,count);
        for (int i = 0; i < promotionObjects.size(); i++) {
            PromotionDTO promotionDTO = new PromotionDTO();
            promotionDTO.setId((Integer) promotionObjects.get(i)[0]);
            promotionDTO.setImages(promotionImageRepository.findAllByPromotionId(promotionDTO.getId()));
            promotionDTOS.add(promotionDTO);
        }
        return promotionDTOS;
    }

    @Override
    public Page<Promotion> getAllPageable(Pageable pageable) {
        return promotionRepository.getAllPageable(pageable);
    }

    @Override
    public Promotion createPromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    @Override
    public void deleteById(Integer promotionId) {
        promotionRepository.deleteById(promotionId);
    }

    @Override
    public Page<Promotion> findSearchString(String searchString, Pageable pageableRequest) {
        return promotionRepository.findAllPageableSearchString(searchString, pageableRequest);
    }
}
