package kz.reself.limma.product.service;

import kz.reself.limma.product.model.ProductImage;

import java.util.List;

public interface IProductImageService {

    List<ProductImage> getAllImagesByProductId(Integer id);

    ProductImage findImageById(Integer id);

    ProductImage createImage(ProductImage id);

    void deleteImageById(Integer id);
}
