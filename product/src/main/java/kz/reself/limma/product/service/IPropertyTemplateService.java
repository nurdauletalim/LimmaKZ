package kz.reself.limma.product.service;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import kz.reself.limma.product.model.PropertyTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPropertyTemplateService {
    Page<PropertyTemplate> findAllPropertyTemplatesPageable(Pageable pageable) throws InternalException;
    Page<PropertyTemplate> findAllSearchString(String searchString, Pageable pageable) throws InternalException;
    Page<PropertyTemplate> findAllActivePropertyTemplatesPageable(Pageable pageable) throws InternalException;
    List<PropertyTemplate> findAllPropertyTemplatesIterable() throws InternalException;
    PropertyTemplate findPropertyTemplateById(Integer id) throws InternalException;
    PropertyTemplate createPropertyTemplate(PropertyTemplate propertyTemplate) throws InternalException;
    PropertyTemplate updatePropertyTemplate(PropertyTemplate propertyTemplate) throws InternalException;
    void deletePropertyTemplateById(Integer id) throws InternalException;
    List<PropertyTemplate> findAllActiveTemplates() throws InternalError;
    PropertyTemplate findPropertyTemplateByCategoryId(Integer categoryId) throws InternalException;
}
