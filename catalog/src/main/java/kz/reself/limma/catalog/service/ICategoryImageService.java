package kz.reself.limma.catalog.service;

import kz.reself.limma.catalog.model.CategoryImage;
import kz.reself.limma.catalog.model.RImages;
import kz.reself.limma.catalog.utils.error.InternalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryImageService {

    Page<CategoryImage> findAllImagesPageable(Pageable pageable) throws InternalException;

    List<CategoryImage> findAllImagesIterable() throws InternalException;

    List<CategoryImage> getAllImageByCategoryId(Integer categoryId);

    List<CategoryImage> getAllImageByParentId(Integer categoryId);

    CategoryImage findImageById(Integer id);

    CategoryImage createImage(CategoryImage image);

    List<CategoryImage> updateImage(List<CategoryImage> image);

    void deleteImageById(Integer id);

    List<RImages> getImagesById(Integer categoryId);
}
