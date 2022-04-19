package kz.reself.limma.product.repository;

import kz.reself.limma.product.model.PropertyCatalog;
import kz.reself.limma.product.model.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PropertyCatalogRepository extends JpaRepository<PropertyCatalog, Integer> {

    List<PropertyCatalog> findAllByState(State state);

    @Query(value = "Select * FROM property_catalog WHERE state = ?1", nativeQuery = true)
    Page<PropertyCatalog> state(Integer state, Pageable pageable);

    @Query(value = "Select * FROM property_catalog WHERE LOWER(name) like %?1% or LOWER(code) like %?1%", nativeQuery = true)
    Page<PropertyCatalog> findAllSearchString(String searchString, Pageable pageable);

    PropertyCatalog getByCode(String code);
}
