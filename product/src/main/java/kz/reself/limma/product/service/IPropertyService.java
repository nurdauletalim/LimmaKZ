package kz.reself.limma.product.service;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import kz.reself.limma.product.model.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPropertyService {
    Page<Property> findAllPropertiesPageable(Pageable pageable) throws InternalException;
    List<Property> findAllPropertiesIterable() throws InternalException;
    Property findPropertyById(Integer id) throws InternalException;
    Property createProperty(Property property) throws InternalException;
    Property updateProperty(Property property) throws InternalException;
    void deletePropertyById(Integer id) throws InternalException;
    List<Property> getPropertyByTemplateId(Integer id);
    List<Property> getPropertyByTemplateCategoryId(Integer id);

    Property changeMain(Integer propertyId);
}
