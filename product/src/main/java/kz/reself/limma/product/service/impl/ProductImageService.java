package kz.reself.limma.product.service.impl;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import kz.reself.limma.product.model.ProductImage;
import kz.reself.limma.product.repository.ProductImageRepository;
import kz.reself.limma.product.service.IProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageService implements IProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Override
    public List<ProductImage> getAllImagesByProductId(Integer id) {
        return this.productImageRepository.findAllByProductId(id);
    }

    @Override
    public ProductImage findImageById(Integer id) {
        return this.productImageRepository.getById(id);
    }

    @Override
    public ProductImage createImage(ProductImage productImage) {
        return this.productImageRepository.save(productImage);
    }

    @Override
    public void deleteImageById(Integer id) {
        this.productImageRepository.deleteById(id);
    }

}
