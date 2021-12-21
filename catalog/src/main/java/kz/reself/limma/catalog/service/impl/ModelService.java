package kz.reself.limma.catalog.service.impl;

import kz.reself.limma.catalog.constant.JobConst;
import kz.reself.limma.catalog.model.Brand;
import kz.reself.limma.catalog.model.Model;
import kz.reself.limma.catalog.model.ModelDTO;
import kz.reself.limma.catalog.model.State;
import kz.reself.limma.catalog.repository.BrandRepository;
import kz.reself.limma.catalog.repository.ModelRepository;
import kz.reself.limma.catalog.service.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModelService implements IModelService {
    @Autowired
    ModelRepository modelRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    private Producer producer;

    @Override
    public List<Model> getModelsIterable() {
        return modelRepository.getAllByState(State.ACTIVE);
    }

    @Override
    public List<Model> getModelsByBrand(Integer brandId) {
        return modelRepository.findAllByBrandIdAndState(brandId, State.ACTIVE);
    }

    @Override
    public List<ModelDTO> getModelsDTOByBrand(Integer brandId) {
        List<Object[]> models =  modelRepository.findAllDTOByBrandIdAndState(brandId, State.ACTIVE);
        List<ModelDTO> modelDTOS = new ArrayList<>();
        for (int i = 0; i < models.size(); i++) {
            modelDTOS.add(new ModelDTO(models.get(i)));
        }
        return modelDTOS;
    }

    @Override
    public Model getModelById(Integer id) {
        return modelRepository.getByIdAndState(id, State.ACTIVE);
    }

    @Override
    public Model getByDisplayNameAndState(String value) {
        return modelRepository.getByDisplayNameAndState(value, State.ACTIVE);
    }

    @Override
    public Model addModel(Model Model) {
        String value;
        value = Model.getDisplayName().replaceAll(" ","_");
        Model.setValue(value);
        Model.setState(State.ACTIVE);
        return modelRepository.save(Model);
    }

    @Override
    public Model updateModel(Model Model) {
        return modelRepository.save(Model);
    }

    @Override
    public void deleteModel(Model Model) {
        if (Model!=null){
            Model.setState(State.DEACTIVE);
            modelRepository.saveAndFlush(Model);
            producer.sendMessage(JobConst.TOPIC, Model.getId());
        }
    }

    @Override
    public void deleteModelById(Integer id) {
        Model model = modelRepository.getByIdAndState(id, State.ACTIVE);
        if (model!=null){
            model.setState(State.DEACTIVE);
            modelRepository.saveAndFlush(model);
            producer.sendMessage(JobConst.TOPIC, model.getId());
        }
    }

    @Override
    public Page<Model> getModels(Pageable pageable) {
        return modelRepository.findAllByState(pageable,State.ACTIVE);
    }

    @Override
    public Page<Model> getModelsSearchString(String searchString, Pageable pageable) {
        return modelRepository.findAllByName(searchString, pageable);
    }

    @Override
    public Model getModelsByProductId(Integer id) {
        return modelRepository.getByProductId(id);
    }

    @Override
    public List<Model> getModelsByBrandDisplayNameAndCategoryId(String brandDisplayName, Integer categoryId) {
        Brand brand = brandRepository.getByCategoryIdAndDisplayNameAndState(categoryId, brandDisplayName,State.ACTIVE);
        if (brand!=null) {
            return modelRepository.findAllByBrandIdAndState(brand.getId(), State.ACTIVE);
        }
        return null;
    }

    @Override
    public Page<Model> getModelsByBrandPageable(Integer id, Pageable pageableRequest) {
        return modelRepository.findAllByBrandIdAndStatePageable(id,State.ACTIVE,pageableRequest);
    }

    @Override
    public Page<Model> getModelsSearchStringAndBrandId(String searchString, int brandId, Pageable pageableRequest) {
        return modelRepository.findAllByNameAndBrandId(searchString,brandId, pageableRequest);
    }
}
