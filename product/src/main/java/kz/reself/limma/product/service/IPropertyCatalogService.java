package kz.reself.limma.product.service;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import kz.reself.limma.product.model.PropertyCatalog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPropertyCatalogService {
    Page<PropertyCatalog> findAllPropertyCatalogsPageable(Pageable pageable) throws InternalException;
    Page<PropertyCatalog> findAllPropertyCatalogsPageableSearchString(String searchString, Pageable pageable) throws InternalException;
    List<PropertyCatalog> findAllPropertyCatalogsIterable() throws InternalException;
    List<PropertyCatalog> findAllActivePropertyCatalogs() throws InternalException;
    PropertyCatalog findPropertyCatalogById(Integer id) throws InternalException;
    PropertyCatalog createPropertyCatalog(PropertyCatalog propertyCatalog) throws InternalException;
    PropertyCatalog updatePropertyCatalog(PropertyCatalog propertyCatalog) throws InternalException;
    void deletePropertyCatalogById(Integer id) throws InternalException;
    PropertyCatalog getCatalogByCode(String catalogCode) throws InternalException;
}
