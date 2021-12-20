package kz.reself.limma.product.service;

import kz.reself.limma.product.model.ProductSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductSupportService {
    ProductSupport create(ProductSupport productSupport);
    ProductSupport update(ProductSupport productSupport);
    void delete(Integer id);
    ProductSupport getById(Integer id);
    Page<ProductSupport> getAllPageable(Pageable pageable);
    Page<ProductSupport> getAllPageableByManagerId(Integer managerId,Pageable pageable);
    Page<ProductSupport> getAllPageableByStatus(String status,Pageable pageable);
    Page<ProductSupport> getAllPageableByManagerIdAndStatus(Integer managerId,String status,Pageable pageable);
    List<ProductSupport> getAllIterable();
    List<ProductSupport> getAllByStatus(String status);

    Page<ProductSupport> getAllProductId(Pageable pageable, Integer productId);

    Page<ProductSupport> findSearchString(String searchString, Pageable pageableRequest);

    Page<ProductSupport> getAllByStatusNotAndManagerId(Integer managerId, String status, Pageable pageable);

    Page<ProductSupport> getAllByStatusNot(String status, Pageable pageable);
}
