package kz.reself.limma.promotion.service.impl;

import kz.reself.limma.promotion.model.PromotionImage;
import kz.reself.limma.promotion.repository.PromotionImageRepository;
import kz.reself.limma.promotion.service.IPromotionImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionImageService implements IPromotionImageService {
    @Autowired
    private PromotionImageRepository promotionImageRepository;

    @Override
    public Page<PromotionImage> findAllImagesPageable(Pageable pageable) {
        return this.promotionImageRepository.findAll(pageable);
    }

    @Override
    public List<PromotionImage> findAllImagesIterable() {
        return this.promotionImageRepository.findAll();
    }

    @Override
    public List<PromotionImage> getAllImageByPromotionId(Integer promotionId) {
        return this.promotionImageRepository.findAllByPromotionId(promotionId);
    }

    @Override
    public PromotionImage findImageById(Integer id) {
        return this.promotionImageRepository.getById(id);
    }

    @Override
    public PromotionImage createImage(PromotionImage image) {
        return this.promotionImageRepository.save(image);
    }

    @Override
    public List<PromotionImage> updateImage(List<PromotionImage> image) {
        return this.promotionImageRepository.saveAll(image);
    }

    @Override
    public void deleteImageById(Integer id) {
        PromotionImage image = this.promotionImageRepository.getOne(id);
        this.promotionImageRepository.delete(image);

    }

    @Override
    public List<RImages> getImagesById(Integer promotionId) {
        List<RImages> rImagesList = new ArrayList<>();
        List<PromotionImage> images = getAllImageByPromotionId(promotionId);
        for (PromotionImage img : images) {
            rImagesList.add(new RImages(
                    "product/" + img.getPromotionId() + "/" + img.getId(),
                    img.getData(),
                    img.getId(),
                    img.getData().length,
                    img.getId().toString()
            ));
        }
        return rImagesList;
    }
}
