package kz.reself.limma.catalog.service;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import kz.reself.limma.catalog.model.ModelImage;
import kz.reself.limma.catalog.model.RImages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IModelImageService {

    Page<ModelImage> findAllImagesPageable(Pageable pageable) throws InternalException;

    List<ModelImage> findAllImagesIterable() throws InternalException;

    List<ModelImage> getAllImageByModelId(Integer modelId);

    ModelImage findImageById(Integer id);

    ModelImage createImage(ModelImage image);

    List<ModelImage> updateImage(List<ModelImage> image);

    void deleteImageById(Integer id);

    List<RImages> getImagesById(Integer modelId);
}
