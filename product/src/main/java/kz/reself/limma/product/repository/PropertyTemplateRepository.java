package kz.reself.limma.product.repository;

import kz.reself.limma.product.model.PropertyTemplate;
import kz.reself.limma.product.model.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyTemplateRepository extends JpaRepository<PropertyTemplate, Integer> {
    List<PropertyTemplate> getAllByState(State state);
    Page<PropertyTemplate> findAllByState(State state, Pageable pageable);
    PropertyTemplate getFirstByCategoryId(Integer catalogId);
    PropertyTemplate getPropertyTemplateByCategoryId(Integer categoryId);

    @Query(value = "Select * FROM property_template WHERE state = ?1", nativeQuery = true)
    Page<PropertyTemplate> state(Integer state, Pageable pageable);

    @Query(value = "Select * FROM property_template WHERE LOWER(name) like %?1%", nativeQuery = true)
    Page<PropertyTemplate> findAllSearchString(String searchString, Pageable pageable);
}
