package kz.reself.limma.catalog.service.impl;

import kz.reself.limma.catalog.model.ModelImage;
import kz.reself.limma.catalog.model.RImages;
import kz.reself.limma.catalog.repository.ModelImageRepository;
import kz.reself.limma.catalog.service.IModelImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModelImageService implements IModelImageService {

    @Autowired
    private ModelImageRepository modelImageRepository;

    @Override
    public Page<ModelImage> findAllImagesPageable(Pageable pageable) {
        return this.modelImageRepository.findAll(pageable);
    }

    @Override
    public List<ModelImage> findAllImagesIterable() {
        return this.modelImageRepository.findAll();
    }

    @Override
    public List<ModelImage> getAllImageByModelId(Integer modelId) {
        return this.modelImageRepository.findAllByModelId(modelId);
    }

    @Override
    public ModelImage findImageById(Integer id) {
        return this.modelImageRepository.getById(id);
    }

    @Override
    public ModelImage createImage(ModelImage image) {
        return this.modelImageRepository.save(image);
    }

    @Override
    public List<ModelImage> updateImage(List<ModelImage> image) {
        return this.modelImageRepository.saveAll(image);
    }

    @Override
    public void deleteImageById(Integer id) {
        ModelImage image = this.modelImageRepository.getOne(id);
        this.modelImageRepository.delete(image);

    }

    @Override
    public List<RImages> getImagesById(Integer modelId) {
//        TODO
//        List<RImages> rImagesList = new ArrayList<>();
//        List<ModelImage> images = getAllImageByModelId(modelId);
//        for (ModelImage img : images) {
//            rImagesList.add(new RImages(
//                    "product/" + img.getModelId() + "/" + img.getId(),
//                    img.getData(),
//                    img.getId(),
//                    img.getData().length,
//                    img.getId().toString()
//            ));
//        }
        return null;
    }
}
