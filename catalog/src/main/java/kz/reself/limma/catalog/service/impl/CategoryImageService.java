package kz.reself.limma.catalog.service.impl;

import kz.reself.limma.catalog.model.CategoryImage;
import kz.reself.limma.catalog.model.RImages;
import kz.reself.limma.catalog.repository.CategoryImageRepository;
import kz.reself.limma.catalog.service.ICategoryImageService;
import kz.reself.limma.catalog.utils.error.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryImageService implements ICategoryImageService {

    @Autowired
    private CategoryImageRepository categoryImageRepository;

    @Override
    public Page<CategoryImage> findAllImagesPageable(Pageable pageable) throws InternalException {
        return this.categoryImageRepository.findAll(pageable);
    }

    @Override
    public List<CategoryImage> findAllImagesIterable() throws InternalException {
        return this.categoryImageRepository.findAll();
    }

    @Override
    public List<CategoryImage> getAllImageByCategoryId(Integer categoryId) {
        return this.categoryImageRepository.findAllByCategoryId(categoryId);
    }

    @Override
    public List<CategoryImage> getAllImageByParentId(Integer categoryId) {
        return this.categoryImageRepository.findAllByParentId(categoryId);
    }

    @Override
    public CategoryImage findImageById(Integer id) {
        return this.categoryImageRepository.getById(id);
    }

    @Override
    public CategoryImage createImage(CategoryImage image) {
        return this.categoryImageRepository.save(image);
    }

    @Override
    public List<CategoryImage> updateImage(List<CategoryImage> image) {
        return this.categoryImageRepository.saveAll(image);
    }

    @Override
    public void deleteImageById(Integer id) {
        CategoryImage image = this.categoryImageRepository.getOne(id);
        this.categoryImageRepository.delete(image);
    }

    @Override
    public List<RImages> getImagesById(Integer categoryId) {
//        TODO
//        List<RImages> rImagesList = new ArrayList<>();
//        List<CategoryImage> images = getAllImageByCategoryId(categoryId);
//        for (CategoryImage img : images) {
//            rImagesList.add(new RImages(
//                    "product/" + img.getCategoryId() + "/" + img.getId(),
//                    img.getData(),
//                    img.getId(),
//                    img.getData().length,
//                    img.getId().toString()
//            ));
//        }
        return null;
    }

}
