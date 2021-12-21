package kz.reself.limma.catalog.service;

import kz.reself.limma.catalog.model.Brand;
import kz.reself.limma.catalog.model.BrandDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBrandService {
    Brand createBrand(Brand brand);
    void deleteBrand(Integer id);
    List<Brand> getBrandsIterable();
    List<BrandDTO> getBrandsIterableWithCategoryName();
    List<Brand> getBrandsByCategoryId(Integer id);
    Brand updateBrand(Brand brand);
    Page<Brand> getPageable(Pageable pageable);

    Brand getBrandById(Integer id);

    Page<Brand> findSearchString(String searchString, Pageable pageableRequest);
}
