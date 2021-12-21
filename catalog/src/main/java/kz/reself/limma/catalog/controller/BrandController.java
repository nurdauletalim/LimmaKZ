package kz.reself.limma.catalog.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kz.reself.limma.catalog.constant.PageableConstant;
import kz.reself.limma.catalog.model.Brand;
import kz.reself.limma.catalog.service.IBrandService;
import kz.reself.limma.catalog.utils.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Api(tags = {"Brand"},value = "Brand")
public class BrandController extends CommonService {

    public static final String PRIVATE_URL = "/private/brands";
    public static final String PUBLIC_URL = "/public/brands";

    @Autowired
    private IBrandService iBrandService;

    @RequestMapping(value = PUBLIC_URL + "/read/all/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readAllPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        Sort.Direction sortDirection = Sort.Direction.ASC;

        int pageNumber = PageableConstant.PAGE_NUMBER;

        int pageSize = PageableConstant.PAGE_SIZE;

        String sortBy = PageableConstant.ID_FIELD_NAME;

        if (allRequestParams.containsKey("page")) {
            pageNumber = Integer.parseInt(allRequestParams.get("page"));
        }
        if (allRequestParams.containsKey("size")) {
            pageSize = Integer.parseInt(allRequestParams.get("size"));
        }
        if (allRequestParams.containsKey("sortDirection")) {

            if (allRequestParams.get("sortDirection").equals(PageableConstant.SORT_DIRECTION_DESC))
                sortDirection = Sort.Direction.DESC;

        }
        if (allRequestParams.containsKey("sort")) {
            sortBy = allRequestParams.get("sort");
        }


        final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortBy));

//        Page<Brand> brands = this.iBrandService.getPageable(pageableRequest);
        Page<Brand> brands;
        if (allRequestParams.containsKey("searchString")) {
            brands = this.iBrandService.findSearchString(allRequestParams.get("searchString"),pageableRequest);
        } else {
            brands = this.iBrandService.getPageable(pageableRequest);
        }

        return builder(success(brands));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Create Brand")
    @RequestMapping(value = PUBLIC_URL + "/create",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createBrand(@RequestBody Brand brand){
        return builder(success(iBrandService.createBrand(brand)));
    }

    @ApiOperation(value = "Get all brands")
    @RequestMapping(value = PUBLIC_URL + "/read/all/iterable",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllBrandsIterable(){
        return builder(success(iBrandService.getBrandsIterable()));
    }

    @RequestMapping(value = PUBLIC_URL + "/read/all/dto/iterable",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllBrandsWithCategoryNameIterable(){
        return builder(success(iBrandService.getBrandsIterableWithCategoryName()));
    }

    @ApiOperation(value = "Get all brands by category")
    @RequestMapping(value = PUBLIC_URL + "/read/category/{categoryId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllBrandsByCategoryId(@PathVariable Integer categoryId){
        return builder(success(iBrandService.getBrandsByCategoryId(categoryId)));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Delete Brand")
    @RequestMapping(value = PUBLIC_URL + "/delete/{id}",method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> deleteBrand(@PathVariable Integer id){
        iBrandService.deleteBrand(id);
        return builder(success("success"));
    }

    @ApiOperation(value = "Get brand by id")
    @RequestMapping(value = PUBLIC_URL + "/read/{id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getBrandById(@PathVariable Integer id){
        return builder(success(iBrandService.getBrandById(id)));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = PUBLIC_URL + "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> update(@RequestBody Brand brand) {
        return builder(success(iBrandService.updateBrand(brand)));
    }
}
