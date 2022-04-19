package kz.reself.limma.catalog.service;

import kz.reself.limma.catalog.model.CategoryImage;
import kz.reself.limma.catalog.model.RImages;
import kz.reself.limma.catalog.utils.error.InternalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryImageService {

    List<CategoryImage> getAllImageByCategoryId(Integer categoryId);

    List<CategoryImage> getAllImageByParentId(Integer categoryId);

    CategoryImage findImageById(Integer id);

    CategoryImage createImage(CategoryImage categoryImage);

    void deleteImageById(Integer id);

    List<CategoryImage> getImagesById(Integer categoryId);
}
