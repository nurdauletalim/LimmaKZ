package kz.reself.limma.product.service.impl;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import kz.reself.limma.product.model.Property;
import kz.reself.limma.product.model.PropertyTemplate;
import kz.reself.limma.product.repository.ProductRepository;
import kz.reself.limma.product.repository.PropertyRepository;
import kz.reself.limma.product.repository.PropertyTemplateRepository;
import kz.reself.limma.product.service.IPropertyCatalogService;
import kz.reself.limma.product.service.IPropertyService;
import kz.reself.limma.product.service.IPropertyTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService implements IPropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PropertyTemplateRepository propertyTemplateRepository;

    @Autowired
    private IPropertyCatalogService iPropertyCatalogService;

    @Autowired
    private IPropertyTemplateService iPropertyTemplateService;

    @Override
    public Page<Property> findAllPropertiesPageable(Pageable pageable) throws InternalException {
        return propertyRepository.findAll(pageable);
    }

    @Override
    public List<Property> findAllPropertiesIterable() throws InternalException {
        return propertyRepository.findAll();
    }

    @Override
    public Property findPropertyById(Integer id) throws InternalException {
        return propertyRepository.getById(id);
    }

    @Override
    public Property createProperty(Property property) throws InternalException {
        property.setCatalog(iPropertyCatalogService.findPropertyCatalogById(property.getCatalogId()));
        property.setTemplate(iPropertyTemplateService.findPropertyTemplateById(property.getTemplateId()));
        property.setDisplayName(property.getKey());
        return propertyRepository.save(property);
    }

    @Override
    public Property updateProperty(Property property) throws InternalException {
        property.setDisplayName(property.getKey());
        return propertyRepository.save(property);
    }

    @Override
    public void deletePropertyById(Integer id) throws InternalException {
        propertyRepository.deleteById(id);
    }

    @Override
    public List<Property> getPropertyByTemplateId(Integer id) {
        return propertyRepository.getAllByTemplateId(id);
    }

    @Override
    public List<Property> getPropertyByTemplateCategoryId(Integer id) {
        return propertyRepository.getAllByTemplateCategoryId(id);
    }

    @Override
    public Property changeMain(Integer propertyId) {
        Property property =  propertyRepository.getById(propertyId);
        PropertyTemplate propertyTemplate =  propertyTemplateRepository.getById(property.getTemplateId());
        property.setMain(!property.isMain());
        List<Property> propertyList = propertyRepository.getAllByTemplateId(property.getTemplateId());
        productRepository.setDefaultValue(propertyTemplate.getCategoryId());
        for (Property item:propertyList) {
            if (item.isMain()) {
                productRepository.changeValue(propertyTemplate.getCategoryId(), item.getKey());
            }
        }
        return propertyRepository.save(property);
    }
}
