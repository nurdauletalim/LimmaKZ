package kz.reself.limma.catalog.service;

import kz.reself.limma.catalog.model.Model;
import kz.reself.limma.catalog.model.ModelDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IModelService {
    List<Model> getModelsIterable();

    List<Model> getModelsByBrand(Integer brandId);

    List<ModelDTO> getModelsDTOByBrand(Integer brandId);

    Model getModelById(Integer id);

    Model addModel(Model Model);

    Model updateModel(Model Model);

    void deleteModel(Model Model);

    void deleteModelById(Integer id);

    Page<Model> getModels(Pageable pageable);

    Page<Model> getModelsSearchString(String searchString, Pageable pageable);

    Model getModelsByProductId(Integer id);

    List<Model> getModelsByBrandDisplayNameAndCategoryId(String brandDisplayName, Integer categoryId);

    Page<Model> getModelsByBrandPageable(Integer id, Pageable pageableRequest);

    Page<Model> getModelsSearchStringAndBrandId(String searchString, int brandId, Pageable pageableRequest);
}
