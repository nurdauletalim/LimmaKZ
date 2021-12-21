package kz.reself.limma.filestorage.service;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import kz.reself.limma.filestorage.model.Image;
import kz.reself.limma.filestorage.model.RImages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IImageService {

    Page<Image> findAllImagesPageable(Pageable pageable) throws InternalException;

    List<Image> findAllImagesIterable() throws InternalException;

    List<Image> getAllImageByProductId(Integer productId);

    Image findImageById(Integer id);

    Image createImage(Image image);

    List<Image> updateImage(List<Image> image);

    void deleteImageById(Integer id);

    List<RImages> getImagesById(Integer productId);
}
