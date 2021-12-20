package kz.reself.limma.product.service.impl;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import kz.reself.limma.product.model.PropertyCatalog;
import kz.reself.limma.product.model.State;
import kz.reself.limma.product.repository.PropertyCatalogRepository;
import kz.reself.limma.product.service.IPropertyCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyCatalogService implements IPropertyCatalogService {
    @Autowired
    private PropertyCatalogRepository propertyCatalogRepository;


    @Override
    public Page<PropertyCatalog> findAllPropertyCatalogsPageable(Pageable pageable) throws InternalException {
        return propertyCatalogRepository.findAll(pageable);
    }

    @Override
    public Page<PropertyCatalog> findAllPropertyCatalogsPageableSearchString(String searchString, Pageable pageable) throws InternalException {
        return propertyCatalogRepository.findAllSearchString(searchString, pageable);
    }

    @Override
    public List<PropertyCatalog> findAllPropertyCatalogsIterable() throws InternalException {
        return propertyCatalogRepository.findAll();
    }

    @Override
    public List<PropertyCatalog> findAllActivePropertyCatalogs() throws InternalException {
        return propertyCatalogRepository.findAllByState(State.ACTIVE);
    }

    @Override
    public PropertyCatalog findPropertyCatalogById(Integer id) throws InternalException {
        return propertyCatalogRepository.getById(id);
    }

    @Override
    public PropertyCatalog createPropertyCatalog(PropertyCatalog propertyCatalog) throws InternalException {
        propertyCatalog.setState(State.ACTIVE);
        return propertyCatalogRepository.saveAndFlush(propertyCatalog);
    }

    @Override
    public PropertyCatalog updatePropertyCatalog(PropertyCatalog propertyCatalog) {
        propertyCatalog.setState(propertyCatalogRepository.getById(propertyCatalog.getId()).getState());
        return this.propertyCatalogRepository.save(propertyCatalog);
    }

    @Override
    public void deletePropertyCatalogById(Integer id) throws InternalException {
        PropertyCatalog catalog = this.propertyCatalogRepository.getOne(id);
        catalog.setState(State.DEACTIVE);
        updatePropertyCatalog(catalog);
    }

    @Override
    public PropertyCatalog getCatalogByCode(String catalogCode) throws InternalException {
        return propertyCatalogRepository.getByCode(catalogCode);
    }

}
