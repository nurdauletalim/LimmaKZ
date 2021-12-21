package kz.reself.limma.promotion.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
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
    @HystrixCommand(
            fallbackMethod = "findAllImagesPageable",
            threadPoolKey = "alternativeMethod",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "100"),
                    @HystrixProperty(name = "maxQueuesize", value = "50"),
            })
    public Page<PromotionImage> findAllImagesPageable(Pageable pageable) {
        return this.promotionImageRepository.findAll(pageable);
    }

    @Override
    @HystrixCommand(
            fallbackMethod = "findAllImagesIterable",
            threadPoolKey = "alternativeMethod",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "100"),
                    @HystrixProperty(name = "maxQueuesize", value = "50"),
            })
    public List<PromotionImage> findAllImagesIterable() {
        return this.promotionImageRepository.findAll();
    }

    @Override
    @HystrixCommand(
            fallbackMethod = "getAllImageByPromotionId",
            threadPoolKey = "alternativeMethod",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "100"),
                    @HystrixProperty(name = "maxQueuesize", value = "50"),
            })
    public List<PromotionImage> getAllImageByPromotionId(Integer promotionId) {
        return this.promotionImageRepository.findAllByPromotionId(promotionId);
    }

    @Override
    @HystrixCommand(
            fallbackMethod = "findImageById",
            threadPoolKey = "alternativeMethod",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "100"),
                    @HystrixProperty(name = "maxQueuesize", value = "50"),
            })
    public PromotionImage findImageById(Integer id) {
        return this.promotionImageRepository.getById(id);
    }

    @Override
    @HystrixCommand(
            fallbackMethod = "createImage",
            threadPoolKey = "alternativeMethod",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "100"),
                    @HystrixProperty(name = "maxQueuesize", value = "50"),
            })
    public PromotionImage createImage(PromotionImage image) {
        return this.promotionImageRepository.save(image);
    }

    @Override
    @HystrixCommand(
            fallbackMethod = "updateImage",
            threadPoolKey = "alternativeMethod",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "100"),
                    @HystrixProperty(name = "maxQueuesize", value = "50"),
            })
    public List<PromotionImage> updateImage(List<PromotionImage> image) {
        return this.promotionImageRepository.saveAll(image);
    }

    @Override
    @HystrixCommand(
            fallbackMethod = "deleteImageById",
            threadPoolKey = "alternativeMethod",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "100"),
                    @HystrixProperty(name = "maxQueuesize", value = "50"),
            })
    public void deleteImageById(Integer id) {
        PromotionImage image = this.promotionImageRepository.getOne(id);
        this.promotionImageRepository.delete(image);

    }

    @Override
    @HystrixCommand(
            fallbackMethod = "getImagesById",
            threadPoolKey = "alternativeMethod",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "100"),
                    @HystrixProperty(name = "maxQueuesize", value = "50"),
            })
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

    public String alternativeMethod(){
        return "Something get wrong";
    }
}
