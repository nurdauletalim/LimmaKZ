package kz.reself.limma.filestorage.service.impl;


import kz.reself.limma.filestorage.model.Image;
import kz.reself.limma.filestorage.model.RImages;
import kz.reself.limma.filestorage.repository.ImageRepository;
import kz.reself.limma.filestorage.service.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService implements IImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Page<Image> findAllImagesPageable(Pageable pageable) {
        return this.imageRepository.findAll(pageable);
    }

    @Override
    public List<Image> findAllImagesIterable() {
        return this.imageRepository.findAll();
    }

    @Override
    public List<Image> getAllImageByProductId(Integer productId) {
        return this.imageRepository.findAllByProductId(productId);
    }

    @Override
    public Image findImageById(Integer id) {
        return this.imageRepository.getById(id);
    }

    @Override
    public Image createImage(Image image) {
        return this.imageRepository.save(image);
    }

    @Override
    public List<Image> updateImage(List<Image> image) {
        return this.imageRepository.saveAll(image);
    }

    @Override
    public void deleteImageById(Integer id) {
        Image image = this.imageRepository.getOne(id);
        this.imageRepository.delete(image);

    }

    @Override
    public List<RImages> getImagesById(Integer productId) {
        List<RImages> rImagesList = new ArrayList<>();
        List<Image> images = getAllImageByProductId(productId);
        for (Image img : images) {
            rImagesList.add(new RImages(
                    "product/" + img.getProductId() + "/" + img.getId(),
                    img.getData(),
                    img.getId(),
                    img.getData().length,
                    img.getId().toString()
            ));
        }
        return rImagesList;
    }
}
