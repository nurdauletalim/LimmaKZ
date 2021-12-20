package kz.reself.limma.product.service;

import kz.reself.limma.product.model.PropertyValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPropertyValueService {
    Page<PropertyValue> findAllPropertyValues(Pageable pageable);

    List<PropertyValue> findAllIterable();

    PropertyValue getPropertyValueById(Integer id);

    PropertyValue createPropertyValue(PropertyValue propertyValue);

    PropertyValue updatePropertyValue(PropertyValue propertyValue);

    List<PropertyValue> getAllByProductId(Integer id);

    void delete(Integer id);

    List<PropertyValue> updateProductValues(List<PropertyValue> propertyValues);
}
