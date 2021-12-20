package kz.reself.limma.product.repository;

import kz.reself.limma.product.model.PropertyValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyValueRepository extends JpaRepository<PropertyValue, Integer> {
    List<PropertyValue> getAllByProductId(Integer productId);
    @Query(value = "select * from property_value where key in (select key from property where template_id = (select id from property_template where category_id = (select category_id from product where id = ?1)) and main = true) and product_id =?1", nativeQuery = true)
    List<PropertyValue> getAllMainByProductId(Integer productId);

    @Query(value = "select * from property_value where key in (select key from property where template_id = (select id from property_template where category_id = (select category_id from product where id = ?1)) and main = false) and product_id =?1", nativeQuery = true)
    List<PropertyValue> getAllNotMainByProductId(Integer productId);

    @Query(value = "select count(*) from property_value where (value = ?1 and product_id in (select id from product where state = 0 and category_id = ?2))", nativeQuery = true)
    Integer countPropertyValueByValue(String value,Integer categoryId);
}
