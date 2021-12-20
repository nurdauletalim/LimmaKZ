package kz.reself.limma.product.service.impl;

import kz.reself.limma.product.model.Product;
import kz.reself.limma.product.model.PropertyValue;
import kz.reself.limma.product.repository.ProductRepository;
import kz.reself.limma.product.repository.PropertyValueRepository;
import kz.reself.limma.product.service.IProductService;
import kz.reself.limma.product.service.IPropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PropertyValueService implements IPropertyValueService {

    @Autowired
    PropertyValueRepository propertyValueRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private IProductService productService;

    @Override
    public Page<PropertyValue> findAllPropertyValues(Pageable pageable) {
        return propertyValueRepository.findAll(pageable);
    }

    @Override
    public List<PropertyValue> findAllIterable() {
        return propertyValueRepository.findAll();
    }

    @Override
    public PropertyValue getPropertyValueById(Integer id) {
        return propertyValueRepository.getOne(id);
    }

    @Override
    public PropertyValue createPropertyValue(PropertyValue propertyValue) {
        return propertyValueRepository.saveAndFlush(propertyValue);
    }

    @Override
    public PropertyValue updatePropertyValue(PropertyValue propertyValue) {
        return propertyValueRepository.save(propertyValue);
    }

    @Override
    public List<PropertyValue> getAllByProductId(Integer id) {
        return propertyValueRepository.getAllByProductId(id);
    }

    @Override
    public void delete(Integer id) {
        propertyValueRepository.deleteById(id);
    }

    @Override
    public List<PropertyValue> updateProductValues(List<PropertyValue> propertyValues) {
        propertyValueRepository.saveAll(propertyValues);
        return setValueOfProduct(propertyValues);
    }

    public List<PropertyValue> setValueOfProduct(List<PropertyValue> propertyValues){
        if (propertyValues.size() > 0 && propertyValues.get(0).getProductId() != null) {
//            List<PropertyValue> values = propertyValueRepository.getAllMainByProductId(propertyValues.get(0).getProductId());
            Map<String, Object> map = new HashMap<>();
            map = productService.getMainPropertyValueList(propertyValues.get(0).getProductId() );
            Product product = productRepository.getById(propertyValues.get(0).getProductId());
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                product.setValue(product.getName() + "/" + ((PropertyValue) entry.getValue()).getValue());
            }
            productRepository.saveAndFlush(product);
        }
        return propertyValues;
    }
}
