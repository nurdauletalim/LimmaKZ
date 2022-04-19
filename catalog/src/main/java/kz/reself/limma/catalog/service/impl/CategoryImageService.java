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
    public List<CategoryImage> getAllImageByCategoryId(Integer categoryId) {
        return this.categoryImageRepository.findAllByCategoryId(categoryId);
    }

    @Override
    public List<CategoryImage> getAllImageByParentId(Integer categoryId) {
        return this.categoryImageRepository.findAllByParentId(categoryId);
    }

    @Override
    public CategoryImage findImageById(Integer id) {
        return this.categoryImageRepository.findById(id).get();
    }

    @Override
    public CategoryImage createImage(CategoryImage categoryImage) {
        return this.categoryImageRepository.save(categoryImage);
    }

    @Override
    public void deleteImageById(Integer id) {
        this.categoryImageRepository.deleteById(id);
    }

    @Override
    public List<CategoryImage> getImagesById(Integer categoryId) {
        return categoryImageRepository.findAllByCategoryId(categoryId);
    }

}
