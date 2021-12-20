package kz.reself.limma.product.service.impl;

import kz.reself.limma.product.model.ProductSupport;
import kz.reself.limma.product.model.State;
import kz.reself.limma.product.model.Status;
import kz.reself.limma.product.repository.ProductSupportRepository;
import kz.reself.limma.product.service.IProductSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSupportService implements IProductSupportService {

    @Autowired
    private ProductSupportRepository productSupportRepository;

    @Autowired
    private ProductService productService;

    @Override
    public ProductSupport create(ProductSupport productSupport) {
        ProductSupport productSupport1 = productSupportRepository.getByProductId(productSupport.getProductId());
        if (productSupport1 != null) {
            productSupport1.setStatus(Status.UPDATED);
            productSupport1.getProduct().setState(State.VERIFICATION);
            productService.updateProduct(productSupport1.getProduct());
            return productSupportRepository.save(productSupport1);
        } else
            return productSupportRepository.save(productSupport);
    }

    @Override
    public ProductSupport update(ProductSupport productSupport) {
        return productSupportRepository.save(productSupport);
    }

    @Override
    public void delete(Integer id) {
        productSupportRepository.deleteById(id);
    }

    @Override
    public ProductSupport getById(Integer id) {
        return productSupportRepository.getById(id);
    }

    @Override
    public Page<ProductSupport> getAllPageable(Pageable pageable) {
        return productSupportRepository.findAll(pageable);
    }

    @Override
    public Page<ProductSupport> getAllPageableByManagerId(Integer managerId, Pageable pageable) {
        return productSupportRepository.getAllByAccountId(managerId,pageable);
    }

    @Override
    public Page<ProductSupport> getAllPageableByStatus(String status, Pageable pageable) {
        return productSupportRepository.getAllByStatus(Status.valueOf(status),pageable);
    }

    @Override
    public Page<ProductSupport> getAllPageableByManagerIdAndStatus(Integer managerId, String status, Pageable pageable) {
        return productSupportRepository.getAllByStatusAndAccountId(Status.valueOf(status),managerId,pageable);
    }

    @Override
    public List<ProductSupport> getAllIterable() {
        return productSupportRepository.findAll();
    }

    @Override
    public List<ProductSupport> getAllByStatus(String status) {
        return productSupportRepository.getAllByStatus(Status.valueOf(status));
    }

    @Override
    public Page<ProductSupport> getAllProductId(Pageable pageable,Integer productId) {
        return productSupportRepository.getAllByProductId(productId,pageable);
    }

    @Override
    public Page<ProductSupport> findSearchString(String searchString, Pageable pageableRequest) {
        return productSupportRepository.findAllPageableSearchString(searchString, pageableRequest);
    }

    @Override
    public Page<ProductSupport> getAllByStatusNotAndManagerId(Integer managerId, String status, Pageable pageable) {
        return productSupportRepository.getAllByAccountIdAndStatusNot( managerId, Status.valueOf(status), pageable);
    }

    @Override
    public Page<ProductSupport> getAllByStatusNot(String status, Pageable pageable) {
        return productSupportRepository.getAllByStatusNot(Status.valueOf(status), pageable);
    }
}
