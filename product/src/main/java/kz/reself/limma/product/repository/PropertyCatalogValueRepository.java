package kz.reself.limma.product.repository;

import kz.reself.limma.product.model.PropertyCatalogValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyCatalogValueRepository extends JpaRepository<PropertyCatalogValue, Integer> {
    PropertyCatalogValue getById(Integer id);
    PropertyCatalogValue getByDisplayName(String displayName);
    List<PropertyCatalogValue> getPropertyCatalogValueByCatalogId(Integer id);
}
