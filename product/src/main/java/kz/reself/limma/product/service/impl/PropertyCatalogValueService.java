package kz.reself.limma.product.service.impl;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import kz.reself.limma.product.model.PropertyCatalogValue;
import kz.reself.limma.product.model.PropertyCatalogValueDTO;
import kz.reself.limma.product.repository.PropertyCatalogValueRepository;
import kz.reself.limma.product.repository.PropertyValueRepository;
import kz.reself.limma.product.service.IPropertyCatalogValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PropertyCatalogValueService implements IPropertyCatalogValueService {
    @Autowired
    private PropertyCatalogValueRepository propertyCatalogValueRepository;

    @Autowired
    private PropertyValueRepository propertyValueRepository;

    @Override
    public Page<PropertyCatalogValue> findAllPropertyCatalogValuesPageable(Pageable pageable) throws InternalException {
        return propertyCatalogValueRepository.findAll(pageable);
    }

    @Override
    public List<PropertyCatalogValue> findAllPropertyCatalogValuesIterable() throws InternalException {
        return propertyCatalogValueRepository.findAll();
    }

    @Override
    public PropertyCatalogValue findPropertyCatalogValueById(Integer id) throws InternalException {
        return propertyCatalogValueRepository.getById(id);
    }

    @Override
    public PropertyCatalogValue findPropertyCatalogValueByDisplayName(String displayName) throws InternalException {
        return propertyCatalogValueRepository.getByDisplayName(displayName);
    }

    @Override
    public PropertyCatalogValue createPropertyCatalogValue(PropertyCatalogValue propertyCatalogValue) throws InternalException {
        return propertyCatalogValueRepository.saveAndFlush(propertyCatalogValue);
    }

    @Override
    public PropertyCatalogValue updatePropertyCatalogValue(PropertyCatalogValue propertyCatalogValue) {
        return this.propertyCatalogValueRepository.save(propertyCatalogValue);
    }

    @Override
    public void deletePropertyCatalogValueById(Integer id) throws InternalException {
        PropertyCatalogValue category = this.propertyCatalogValueRepository.getOne(id);
        this.propertyCatalogValueRepository.delete(category);
    }

    @Override
    public List<PropertyCatalogValue> findAllByCatalogId(Integer id) throws InternalException {
        return propertyCatalogValueRepository.getPropertyCatalogValueByCatalogId(id);
    }

    public List<PropertyCatalogValueDTO> findAllAndCount(Integer id, Integer categoryId) throws InternalException {
        List<PropertyCatalogValue> propertyCatalogValueList = propertyCatalogValueRepository.getPropertyCatalogValueByCatalogId(id);
        ArrayList<PropertyCatalogValueDTO> propertyCatalogValueDTOList = new ArrayList<>() ;
        for (PropertyCatalogValue p:propertyCatalogValueList) {
            PropertyCatalogValueDTO PropertyCatalogValueDTO = new PropertyCatalogValueDTO();
            PropertyCatalogValueDTO.setCatalog(p.getCatalog());
            PropertyCatalogValueDTO.setCatalogId(p.getCatalogId());
            PropertyCatalogValueDTO.setDisplayName(p.getDisplayName());
            PropertyCatalogValueDTO.setId(p.getId());
            PropertyCatalogValueDTO.setValue(p.getValue());
            PropertyCatalogValueDTO.setCount(propertyValueRepository.countPropertyValueByValue(p.getValue(),categoryId));
            propertyCatalogValueDTOList.add(PropertyCatalogValueDTO);
        }
        return propertyCatalogValueDTOList;
    }
}
