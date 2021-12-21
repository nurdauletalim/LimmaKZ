package kz.reself.limma.order.repository;

import kz.reself.limma.order.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product getById(Integer id);

    @Query(value = "SELECT * FROM product p1 " +
            "INNER JOIN (SELECT value, min(price) as price FROM product where state = ?1 and category_id = ?2 GROUP BY value) p2 " +
            "ON p1.value = p2.value " +
            "AND p1.price = p2.price", nativeQuery = true)
    Page<Product> findAllByStateAndCategoryIdGroupByValue(Integer state, Integer categoryId, Pageable pageable);

    @Query(value = "SELECT p1.id,p1.model_id,p1.price FROM product p1 " +
            "INNER JOIN (SELECT id, value, min(price) as price FROM product where state = ?1 and category_id = ?2 GROUP BY id, value) p2 " +
            "ON p1.value = p2.value " +
            "AND p1.price = p2.price " +
            "AND p1.id = p2.id", nativeQuery = true)
    Page<Object[]> findAllDTOByStateAndCategoryIdGroupByValue(Integer state, Integer categoryId, Pageable pageable);

    @Query(value = "Select * FROM product WHERE state = 0", nativeQuery = true)
    List<Product> findAllState();

    @Query(value = "Select * FROM product WHERE (LOWER(name) like %?1% OR LOWER(description) like %?1%) and state = 0 LIMIT 10", nativeQuery = true)
    List<Product> searchString(String search);

    @Query(value = "Select * FROM product WHERE (LOWER(name) like %?1% OR LOWER(description) like %?1%) and state = 0 LIMIT 10", nativeQuery = true)
    List<Product> searchActive(String searchString);

    @Query(value = "Select id, name, value, price, state, published_date, condition, organization_id, category_id, description, model_id FROM product WHERE LOWER(name) like %?1% OR LOWER(description) like %?1% AND state = ?3 AND category_id = ?2", nativeQuery = true)
    Page<Product> getAllBySearchCategoryState(String searchString, Integer category, Integer state, Pageable pageable);

    @Query(value = "Select * FROM product WHERE category_id = ?1 AND state = ?2", nativeQuery = true)
    Page<Product> categorystate(Integer category, Integer state, Pageable pageable);

    @Query(value = "Select * FROM product WHERE organization_id = ?1", nativeQuery = true)
    Page<Product> organization(Integer organization, Pageable pageable);

    @Query(value = "Select * FROM product WHERE state = ?1", nativeQuery = true)
    Page<Product> state(Integer state, Pageable pageable);

    @Query(value = "Select * FROM product WHERE category_id = ?1 and id in ?2 and price between ?3 AND ?4", nativeQuery = true)
    Page<Product> searchWithParameters(Integer categoryId, List<Integer> ids, Integer minPrice, Integer maxPrice, Pageable pageable);

    @Query(value = "Select * FROM product WHERE category_id = ?1 and price between ?2 AND ?3", nativeQuery = true)
    Page<Product> searchWithPrice(Integer categoryId, Integer minPrice, Integer maxPrice, Pageable pageable);

    @Query(value = "Select DISTINCT p.id " +
            "FROM product p LEFT JOIN property_value pv ON p.id = pv.product_id " +
            "WHERE category_id = ?1 and pv.key ilike ?2 and pv.value in ?3 AND p.price BETWEEN ?4 AND ?5 AND p.state = ?6", nativeQuery = true)
    List<Integer> findProductIds(Integer categoryId, String key, String[] value, Integer minPrice, Integer maxPrice, Integer state);

    @Query(value = "select * from product where state = ?1 AND published_date <= ?2 LIMIT 100", nativeQuery = true)
    List<Product> getExpiredActive(Integer state, Timestamp timestamp);

    @Query(value = "SELECT * FROM product p1 " +
            "INNER JOIN (SELECT name, min(price) as price FROM product where state = ?1 and category_id = ?2 GROUP BY name) p2 " +
            "ON p1.name = p2.name " +
            "AND p1.price = p2.price limit ?3", nativeQuery = true)
    List<Product> getDisplayCategoryProductsByStateAndCategoryId(Integer state, Integer categoryId, Integer quantity);

    @Query(value = "Select DISTINCT id FROM product WHERE model_id = ?1 and state = ?2", nativeQuery = true)
    List<Integer> getIdsByModel(Integer id, Integer state);

    @Query(value = "Select id FROM product WHERE model_id in (select id from model where brand_id = ?1) and state = ?2", nativeQuery = true)
    List<Integer> getIdsByBrand(Integer id, Integer state);

    @Query(value = "SELECT COUNT(id) FROM product WHERE organization_id = ?1", nativeQuery = true)
    Integer getAmountByOrganizationId(Integer organizationId);

    @Query(value = "SELECT SUM(price) FROM product WHERE id in (SELECT product_id FROM product_application" +
            " WHERE status = 2) AND organization_id = ?1", nativeQuery = true)
    Integer getIncomeByOrganizationId(Integer orgId);

    @Query(value = "SELECT COUNT(id) FROM product WHERE organization_id = ?1 AND state = 3", nativeQuery = true)
    Integer getSoldAmountByOrganizationId(Integer organizationId);

    @Query(value = "SELECT COUNT(id) FROM product WHERE id in (select product_id from product_application where organization_id = ?1 and (closed >= ?2 and closed <=?3))", nativeQuery = true)
    Integer getSoldAmountByOrganizationIdInterval(Integer organizationId, Timestamp intervalTime, Timestamp currentTime);

    @Query(value = "SELECT * FROM product p1 " +
            "INNER JOIN (SELECT value, min(price) as price FROM product where state = 0 and category_id = ?1 and price between  ?3 and ?4 GROUP BY value) p2 " +
            "ON p1.value = p2.value " +
            "AND p1.price = p2.price " +
            "AND p1.id in ?2 and state = 0", nativeQuery = true)
    Page<Product> searchWithParametersPageable(Integer categoryId, List<Integer> integers, Integer minPrice, Integer maxPrice, Pageable pageable);

    @Query(value = "SELECT * FROM product p1 " +
            "INNER JOIN (SELECT value, min(price) as price FROM product where state = 0 and category_id = ?1 and price between  ?2 and ?3 GROUP BY value) p2 " +
            "ON p1.value = p2.value " +
            "AND p1.price = p2.price", nativeQuery = true)
    Page<Product> searchWithPricePageable(Integer categoryId, Integer minPrice, Integer maxPrice, Pageable pageable);

    @Query(value = "SELECT * FROM product " +
            "where state = 0 and value = ?1", nativeQuery = true)
    List<Product> findAllByValueGroupByName(String value);

    @Transactional
    @Modifying
    @Query(value = "UPDATE product " +
            "SET value = concat( " +
            "value, " +
            "'/', " +
            "(select value from property_value where product_id = product.id and key = ?2)) where category_id = ?1", nativeQuery = true)
    void changeValue(Integer categoryId, String key);

    @Transactional
    @Modifying
    @Query(value = "UPDATE product SET value = name where category_id = ?1", nativeQuery = true)
    void setDefaultValue(Integer categoryId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE product SET state = 1 where model_id = ?1 and state = 0", nativeQuery = true)
    void deactiveAllProductByModelId(Integer modelId);

    @Query(value = "SELECT * FROM product p1 " +
            "INNER JOIN (SELECT id, value, min(price) as price FROM product where state =0 GROUP BY id, value) p2 " +
            "ON p1.value = p2.value " +
            "AND p1.price = p2.price " +
            "AND p1.id = p2.id " +
            "order by p1.published_date desc limit ?1", nativeQuery = true)
    List<Product> getLastProductsSortByPublishedDateGroupByValue(Integer count);

    @Query(value = "SELECT SUM(price) FROM product WHERE id in (SELECT product_id FROM product_application" +
            " WHERE status = 2 and (closed >= ?2 and closed <= ?3)) AND organization_id = ?1", nativeQuery = true)
    Integer getIncomeByOrganizationIdInterval(Integer organizationId, Timestamp intervalTime, Timestamp currentTime);

    @Query(value = "SELECT * FROM product LOWER(name) like %?1% OR LOWER(description) like %?1%", nativeQuery = true)
    Page<Product> searchPageable(String search, Pageable pageable);

    @Query(value = "Select * FROM product WHERE state = ?1", nativeQuery = true)
    Page<Product> statePageable(Integer state, Pageable pageable);

    @Query(value = "Select * FROM product WHERE organization_id = ?1", nativeQuery = true)
    Page<Product> organizationPageable(Integer organization, Pageable pageable);

    @Query(value = "Select * FROM product WHERE organization_id = ?1 AND state = ?2", nativeQuery = true)
    Page<Product> organizationStatePageable(Integer organization, Integer state, Pageable pageable);

    @Query(value = "Select * FROM product WHERE LOWER(name) like %?1% OR LOWER(description) like %?1% AND organization_id = ?2", nativeQuery = true)
    Page<Product> searchStringAndOrganizationPageable(String searchString, Integer organization, Pageable pageable);

    @Query(value = "Select * FROM product WHERE LOWER(name) like %?1% OR LOWER(description) like %?1% AND state = ?2", nativeQuery = true)
    Page<Product> searchStatePageable(String searchString, Integer state, Pageable pageable);
}
