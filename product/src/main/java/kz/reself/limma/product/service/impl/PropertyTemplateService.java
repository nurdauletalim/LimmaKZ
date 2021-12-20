package kz.reself.limma.product.service.impl;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import kz.reself.limma.product.model.PropertyTemplate;
import kz.reself.limma.product.model.State;
import kz.reself.limma.product.repository.PropertyTemplateRepository;
import kz.reself.limma.product.service.IPropertyTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyTemplateService implements IPropertyTemplateService {
    @Autowired
    private PropertyTemplateRepository propertyTemplateRepository;

    @Override
    public Page<PropertyTemplate> findAllPropertyTemplatesPageable(Pageable pageable) throws InternalException {
        return propertyTemplateRepository.findAll(pageable);
    }

    @Override
    public Page<PropertyTemplate> findAllSearchString(String searchString, Pageable pageable) throws InternalException {
        return propertyTemplateRepository.findAllSearchString(searchString, pageable);
    }

    @Override
    public Page<PropertyTemplate> findAllActivePropertyTemplatesPageable(Pageable pageable) throws InternalException {
        return propertyTemplateRepository.findAllByState(State.ACTIVE, pageable);
    }

    @Override
    public List<PropertyTemplate> findAllPropertyTemplatesIterable() throws InternalException {
        return propertyTemplateRepository.findAll();
    }

    @Override
    public PropertyTemplate findPropertyTemplateById(Integer id) throws InternalException {
        return propertyTemplateRepository.getById(id);
    }

    @Override
    public PropertyTemplate createPropertyTemplate(PropertyTemplate propertyTemplate) throws InternalException {
        propertyTemplate.setState(State.ACTIVE);
        return propertyTemplateRepository.saveAndFlush(propertyTemplate);
    }

    @Override
    public PropertyTemplate updatePropertyTemplate(PropertyTemplate propertyTemplate) throws InternalException {
        propertyTemplate.setState(propertyTemplateRepository.getById(propertyTemplate.getId()).getState());
        return propertyTemplateRepository.save(propertyTemplate);
    }

    @Override
    public void deletePropertyTemplateById(Integer id) throws InternalException {
        PropertyTemplate propertyTemplate = propertyTemplateRepository.getById(id);
        propertyTemplate.setState(State.DEACTIVE);
        updatePropertyTemplate(propertyTemplate);
    }

    @Override
    public List<PropertyTemplate> findAllActiveTemplates() throws InternalError {
        return propertyTemplateRepository.getAllByState(State.ACTIVE);
    }

    @Override
    public PropertyTemplate findPropertyTemplateByCategoryId(Integer categoryId) throws InternalException {
        return propertyTemplateRepository.getPropertyTemplateByCategoryId(categoryId);
    }
}
