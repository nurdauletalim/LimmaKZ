package kz.reself.limma.product.repository;

import kz.reself.limma.product.model.ProductSupport;
import kz.reself.limma.product.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSupportRepository extends JpaRepository<ProductSupport, Integer> {
    ProductSupport getByIdAndIdIsNotNull(Integer id);

    void deleteById(Integer id);

    List<ProductSupport> getAllByStatus(Status status);

    Page<ProductSupport> getAllByAccountId(Integer accountId, Pageable pageable);

    Page<ProductSupport> getAllByStatus(Status status, Pageable pageable);

    Page<ProductSupport> getAllByStatusAndAccountId(Status status, Integer id, Pageable pageable);

    Page<ProductSupport> getAllByProductId(Integer productId, Pageable pageable);

    @Query(value = "Select * FROM product_support WHERE LOWER(comment) like %?1% or product_id in (Select id FROM product WHERE (LOWER(name) like %?1% OR LOWER(description) like %?1%))", nativeQuery = true)
    Page<ProductSupport> findAllPageableSearchString(String searchString, Pageable pageableRequest);

    Page<ProductSupport> getAllByAccountIdAndStatusNot(Integer managerId, Status status, Pageable pageable);

    Page<ProductSupport> getAllByStatusNot(Status status, Pageable pageable);

    ProductSupport getByProductId(Integer prodId);
}
