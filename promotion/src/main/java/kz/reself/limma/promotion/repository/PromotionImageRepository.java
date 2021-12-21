package kz.reself.limma.promotion.repository;

import kz.reself.limma.promotion.model.PromotionImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionImageRepository extends JpaRepository<PromotionImage, Integer> {

    PromotionImage getById(Integer id);

    List<PromotionImage> findAllByIdIn(List<Integer> imageIds);

    List<PromotionImage> findAllByPromotionId(Integer id);
}
