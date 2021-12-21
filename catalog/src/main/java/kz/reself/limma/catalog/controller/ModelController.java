package kz.reself.limma.catalog.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import kz.reself.limma.catalog.constant.PageableConstant;
import kz.reself.limma.catalog.model.Model;
import kz.reself.limma.catalog.service.impl.ModelService;
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
@Api(tags = {"Model"}, description = "Управление model", authorizations = {@Authorization(value = "bearerAuth")})
public class ModelController extends CommonService {

    public static final String PRIVATE_URL = "/private/models";
    public static final String PUBLIC_URL = "/public/models";

    @Autowired
    private ModelService modelService;

    @RequestMapping(value = PUBLIC_URL + "/read/all/iterable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get() {
        return builder(success(modelService.getModelsIterable()));
    }

    @RequestMapping(value = PUBLIC_URL + "/read/brand/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getModelsByCategoryIdAndBrandId(@PathVariable Integer id) {
        return builder(success(modelService.getModelsByBrand(id)));
    }

    @RequestMapping(value = PUBLIC_URL + "/read/dto/brand/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getModelsDTOByCategoryIdAndBrandId(@PathVariable Integer id) {
        return builder(success(modelService.getModelsDTOByBrand(id)));
    }

    @RequestMapping(value = PUBLIC_URL + "/read/brand/pageable/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getModelsByBrandIdPageable(@PathVariable Integer id) {
        Sort.Direction sortDirection = Sort.Direction.ASC;
        int pageNumber = PageableConstant.PAGE_NUMBER;

        int pageSize = PageableConstant.PAGE_SIZE;

        String sortBy = PageableConstant.ID_FIELD_NAME;

        final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortBy));

        return builder(success(modelService.getModelsByBrandPageable(id, pageableRequest)));
    }

    @RequestMapping(value = PUBLIC_URL + "/read/product/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getModelsProductId(@PathVariable Integer id) {
        return builder(success(modelService.getModelsByProductId(id)));
    }

    @RequestMapping(value = PUBLIC_URL + "/read/brand", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getModelsByBrandDisplayName(@RequestParam String brandDisplayName, @RequestParam Integer categoryId) {
        return builder(success(modelService.getModelsByBrandDisplayNameAndCategoryId(brandDisplayName, categoryId)));
    }

    @RequestMapping(value = PUBLIC_URL + "/brandId/{brandId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getModelsByBrandId(@PathVariable(name = "brandId") Integer brandId) {
        return builder(success(modelService.getModelsByBrand(brandId)));
    }

//    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_CONTENT_MANAGER')")
    @RequestMapping(value = PUBLIC_URL + "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> create(@RequestBody Model model) {
        return builder(success(modelService.addModel(model)));
    }

    @ApiOperation(value = "Удалить Model", tags = {"Model"})
    @RequestMapping(value = PUBLIC_URL + "/delete/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> delete(@PathVariable(name = "id") Integer id) {
        modelService.deleteModelById(id);
        return builder(success("success"));
    }

    @RequestMapping(value = PUBLIC_URL + "/read/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return builder(success(modelService.getModelById(id)));
    }

    @RequestMapping(value = PUBLIC_URL + "/read/all/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readAllPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        Sort.Direction sortDirection = Sort.Direction.ASC;
        int pageNumber = PageableConstant.PAGE_NUMBER;
        int brandId = -1;

        int pageSize = PageableConstant.PAGE_SIZE;

        String searchString = "";

        String sortBy = PageableConstant.ID_FIELD_NAME;
        if (allRequestParams.containsKey("brandId")) {
            brandId = Integer.parseInt(allRequestParams.get("brandId"));
        }
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
        Page<Model> models;
        final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortBy));
        if (allRequestParams.containsKey("searchString")) {
            if (brandId == -1) {
                models = modelService.getModelsSearchString(allRequestParams.get("searchString"), pageableRequest);
                return builder(success(models));
            }
            else {
                models = modelService.getModelsSearchStringAndBrandId(allRequestParams.get("searchString"),brandId, pageableRequest);
                return builder(success(models));
            }
        }
        else if (brandId != -1) {
            models = modelService.getModelsByBrandPageable(brandId, pageableRequest);
            return builder(success(models));
        }

        return builder(success(modelService.getModels(pageableRequest)));
    }
}
