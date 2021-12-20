package kz.reself.limma.product.service;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import kz.reself.limma.product.model.PropertyCatalogValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPropertyCatalogValueService {
    Page<PropertyCatalogValue> findAllPropertyCatalogValuesPageable(Pageable pageable) throws InternalException;
    List<PropertyCatalogValue> findAllPropertyCatalogValuesIterable() throws InternalException;
    PropertyCatalogValue findPropertyCatalogValueById(Integer id) throws InternalException;
    PropertyCatalogValue findPropertyCatalogValueByDisplayName(String displayName) throws InternalException;
    PropertyCatalogValue createPropertyCatalogValue(PropertyCatalogValue propertyCatalog) throws InternalException;
    PropertyCatalogValue updatePropertyCatalogValue(PropertyCatalogValue propertyCatalogValue) throws InternalException;
    void deletePropertyCatalogValueById(Integer id) throws InternalException;
    List<PropertyCatalogValue> findAllByCatalogId(Integer id) throws InternalException;
}
