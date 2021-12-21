package kz.reself.limma.promotion.service;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import kz.reself.limma.promotion.model.PromotionImage;
import kz.reself.limma.promotion.service.impl.RImages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPromotionImageService {
    Page<PromotionImage> findAllImagesPageable(Pageable pageable) throws InternalException;

    List<PromotionImage> findAllImagesIterable() throws InternalException;

    List<PromotionImage> getAllImageByPromotionId(Integer promotionId);

    PromotionImage findImageById(Integer id);

    PromotionImage createImage(PromotionImage image);

    List<PromotionImage> updateImage(List<PromotionImage> image);

    void deleteImageById(Integer id);

    List<RImages> getImagesById(Integer promotionId);
}
