package kz.reself.limma.product.controller;

import io.swagger.annotations.*;
import kz.reself.limma.product.constant.PageableConstant;
import kz.reself.limma.product.model.PropertyCatalog;
import kz.reself.limma.product.repository.PropertyCatalogRepository;
import kz.reself.limma.product.service.IPropertyCatalogService;
import kz.reself.limma.product.utils.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping()
@Api(tags = {"Property Catalog"}, description = "Управление Property Catalog")
public class PropertyCatalogController extends CommonService {

    @Autowired
    private IPropertyCatalogService propertyCatalogService;

    @Autowired
    private PropertyCatalogRepository propertyCatalogRepository;

    @ApiOperation(value = "Получить список PropertyCatalog pageable", tags = {"Property Catalog"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.(По умолчанию по 'id')", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "sortDirection", dataType = "string", value = "Указывает на тип сортировки.(По умолчанию по 'asc')", allowableValues = "asc,desc", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.(По умолчанию page равно на 0)", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице..(По умолчанию size равно на 5)", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что Property Catalog существуют и возвращает.")
    })
    @RequestMapping(value = "/v1/public/property/catalogs/read/all/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

        Page<PropertyCatalog> propertyCatalogs;
        if (allRequestParams.containsKey("searchString")) {
            propertyCatalogs = this.propertyCatalogService.findAllPropertyCatalogsPageableSearchString(allRequestParams.get("searchString"),pageableRequest);
        }
        else if (allRequestParams.containsKey("state")) {
            propertyCatalogs = this.propertyCatalogRepository.state(Integer.parseInt(allRequestParams.get("state")), pageableRequest);
        } else {
            propertyCatalogs = this.propertyCatalogService.findAllPropertyCatalogsPageable(pageableRequest);
        }
        return builder(success(propertyCatalogs));
    }

    @ApiOperation(value = "Получить список Property Catalog iterable", tags = {"Property Catalog"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = "/v1/public/property/catalogs/read/all/iterable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readIterable() {
        return builder(success(propertyCatalogService.findAllPropertyCatalogsIterable()));
    }

    @ApiOperation(value = "Получить список Active Property Catalog", tags = {"Property Catalog"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = "/v1/public/property/catalogs/read/all/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readActive() {
        return builder(success(propertyCatalogService.findAllActivePropertyCatalogs()));
    }

    @ApiOperation(value = "Получить Property Catalog", tags = {"Property Catalog"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запрошенная запись найдена"),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная запись не найдена.")
    })
    @RequestMapping(value = "/v1/public/property/catalogs/read/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        return builder(success(propertyCatalogService.findPropertyCatalogById(id)));
    }

    @ApiOperation(value = "Создать Property Catalog", tags = {"Property Catalog"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/v1/private/property/catalogs/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@RequestBody PropertyCatalog propertyCatalog) {
        return builder(success(propertyCatalogService.createPropertyCatalog(propertyCatalog)));
    }

    @ApiOperation(value = "Обновить Property Catalog", tags = {"Property Catalog"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/v1/public/property/catalogs/update", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> update(@RequestBody PropertyCatalog propertyCatalog) {
        return builder(success(propertyCatalogService.updatePropertyCatalog(propertyCatalog)));
    }

    @ApiOperation(value = "Удалить Property Catalog", tags = {"Property Catalog"})
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/v1/public/property/catalogs/delete/{propertyCatalogId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteKiInformedConsent(@PathVariable(name = "propertyCatalogId") Integer propertyCatalogId) {
        propertyCatalogService.deletePropertyCatalogById(propertyCatalogId);
        return builder(success("success"));
    }

    @RequestMapping(value = "/v1/public/property/catalogs/read/code/{catalogCode}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getCatalogByCode(@PathVariable(name = "catalogCode") String catalogCode) {
        return builder(success(propertyCatalogService.getCatalogByCode(catalogCode)));
    }

}
