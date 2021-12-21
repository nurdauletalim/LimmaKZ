package kz.reself.limma.order.repository;

import kz.reself.limma.order.model.ProductApplication;
import kz.reself.limma.order.model.ProductApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ProductApplicationRepository extends JpaRepository<ProductApplication, Integer> {
    ProductApplication getById(Integer id);
    List<ProductApplication> getAllByProductOrganizationId(Integer id);
    List<ProductApplication> getAllByContact(String contact);
    List<ProductApplication> getAllByStatus(ProductApplicationStatus status);


    ProductApplication findProductApplicationByProductIdAndStatus(Integer productId, ProductApplicationStatus status);

    @Query(value = "Select * FROM product_application WHERE LOWER(comment) like %?1% or LOWER(email) like %?1% or LOWER(name) like %?1% or replace(contact,' ','') like %?1%", nativeQuery = true)
    List<ProductApplication> findAllSearchString(String searchString);

    @Query(value = "SELECT COUNT(id) FROM product_application WHERE (product_id in " +
            "(SELECT id FROM product WHERE organization_id = ?1) AND (status = 1 OR status = 0))", nativeQuery = true)
    Integer getAmountApplicationByOrganizationId(Integer orgId);

    @Query(value = "SELECT * FROM product_application WHERE product_id in (SELECT id FROM product WHERE organization_id = ?1) ORDER BY registered DESC", nativeQuery = true)
    Page<ProductApplication> getAllByProductOrganizationIdPageable(Integer id, Pageable pageable);

    @Query(value = "SELECT * FROM product_application WHERE (product_id in (SELECT id FROM product WHERE organization_id = ?1) and (lower(email) like %?2% or lower(name) like %?2% or lower(contact) like %?2% or lower(comment) like %?2% ))", nativeQuery = true)
    Page<ProductApplication> getAllByProductOrganizationIdSearchStringPageable(Integer id,String searchString, Pageable pageable);

    @Query(value = "SELECT * FROM product_application WHERE product_id in (SELECT id FROM product WHERE organization_id = ?1) AND status = ?2 ORDER BY registered DESC", nativeQuery = true)
    Page<ProductApplication> getAllByProductOrganizationIdAndStatus(Integer orgId, Integer status, Pageable pageable);

    Page<ProductApplication> getAllByProductId(Integer productId, Pageable pageable);

    Page<ProductApplication> getAllByProductIdAndManagerId(Integer productId, Integer managerId, Pageable pageable);

    @Query(value = "SELECT * FROM product_application WHERE status = ?1", nativeQuery = true)
    Page<ProductApplication> allByStatus(Integer status, Pageable pageable);

    @Query(value = "SELECT COUNT(id) FROM product_application WHERE (registered >= ?2 and registered <= ?3 )and (product_id in " +
            "(SELECT id FROM product WHERE organization_id = ?1) AND (status = 1 OR status = 0))", nativeQuery = true)
    Integer getAmountApplicationByOrganizationIdInterval(Integer organizationId, Timestamp intervalTime, Timestamp currentTime);

    @Query(value = "SELECT id FROM product_application WHERE name = ?1 and replace(contact,' ','') like %?2% and product_id = ?3 and (status = 0 or status = 1)", nativeQuery = true)
    List<Integer> checkApplication(String name, String contact, Integer productId);
}
