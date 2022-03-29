package kz.reself.limma.catalog.service.impl;

import kz.reself.limma.catalog.constant.JobConst;
import kz.reself.limma.catalog.constant.PageableConstant;
import kz.reself.limma.catalog.model.Brand;
import kz.reself.limma.catalog.model.BrandDTO;
import kz.reself.limma.catalog.model.Model;
import kz.reself.limma.catalog.model.State;
import kz.reself.limma.catalog.repository.BrandRepository;
import kz.reself.limma.catalog.repository.CategoryRepository;
import kz.reself.limma.catalog.repository.ModelRepository;
import kz.reself.limma.catalog.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandService implements IBrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private Producer producer;

//    @Autowired
//    private ProductRepository productRepository;

    @Override
    public Brand createBrand(Brand brand) {
        brand.setState(State.ACTIVE);
        return brandRepository.save(brand);
    }

    @Override
    public void deleteBrand(Integer id) {
        Brand brand = brandRepository.getByIdAndState(id,State.ACTIVE);
        if (brand!=null){
            brand.setState(State.DEACTIVE);
            brandRepository.saveAndFlush(brand);
            List<Model> modelList = modelRepository.findAllByBrandId(brand.getId());
            for (Model model:modelList) {
                producer.sendMessage(JobConst.TOPIC, model.getId());
            }
            modelRepository.deactiveAllModelsByBrandId(brand.getId());
        }
//        brandRepository.deleteById(id);
    }

    @Override
    public List<Brand> getBrandsIterable() {
        return brandRepository.getAllByState(State.ACTIVE);
    }

    @Override
    public List<BrandDTO> getBrandsIterableWithCategoryName() {
        List<BrandDTO> brandDTOList = new ArrayList<>();
        List<Brand> brandList = brandRepository.getAllByState(State.ACTIVE);
        for (Brand brand: brandList) {
            BrandDTO brandDTO = new BrandDTO(brand);
            brandDTO.setCategoryName(categoryRepository.findById(brand.getCategoryId()).get().getName());
            brandDTOList.add(brandDTO);
        }
        return brandDTOList;
    }

    @Override
    public List<Brand> getBrandsByCategoryId(Integer id) {
        return brandRepository.getAllByCategory_IdAndState(id,State.ACTIVE);
    }

    @Override
    public Brand updateBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Page<Brand> getPageable(Pageable pageable) {
        return brandRepository.findAllPageable(pageable);
    }

    @Override
    public Brand getBrandById(Integer id) {
        return brandRepository.getByIdAndState(id, State.ACTIVE);
    }

    @Override
    public Page<Brand> findSearchString(String searchString, Pageable pageableRequest) {
        return brandRepository.findAllPageableSearchString(searchString, pageableRequest);
    }
}
