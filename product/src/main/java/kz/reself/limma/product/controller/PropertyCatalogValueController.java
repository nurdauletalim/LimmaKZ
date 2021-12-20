package kz.reself.limma.product.controller;

import io.swagger.annotations.*;
import kz.reself.limma.product.constant.PageableConstant;
import kz.reself.limma.product.model.PropertyCatalogValue;
import kz.reself.limma.product.repository.PropertyCatalogRepository;
import kz.reself.limma.product.service.impl.PropertyCatalogValueService;
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
@Api(tags = {"Property Catalog Value"}, description = "Управление Property Catalog Value")
public class PropertyCatalogValueController extends CommonService {

    @Autowired
    private PropertyCatalogValueService propertyCatalogValueService;

    @Autowired
    private PropertyCatalogRepository propertyCatalogRepository;

    @ApiOperation(value = "Получить список Property Catalog Value pageable", tags = {"Property Catalog Value"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.(По умолчанию по 'id')", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "sortDirection", dataType = "string", value = "Указывает на тип сортировки.(По умолчанию по 'asc')", allowableValues = "asc,desc", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.(По умолчанию page равно на 0)", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице..(По умолчанию size равно на 5)", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что Property Catalog Value существуют и возвращает.")
    })
    @RequestMapping(value = "/v1/public/property/catalog/values/read/all/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

        Page<PropertyCatalogValue> categories = this.propertyCatalogValueService.findAllPropertyCatalogValuesPageable(pageableRequest);

        return builder(success((categories)));
    }

    @ApiOperation(value = "Получить список Property Catalog Value iterable", tags = {"Property Catalog Value"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = "/v1/public/property/catalog/values/read/all/iterable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readIterable() {
        return builder(success(propertyCatalogValueService.findAllPropertyCatalogValuesIterable()));
    }

    @ApiOperation(value = "Получить Property Catalog Value", tags = {"Property Catalog Value"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запрошенная запись найдена"),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная запись не найдена.")
    })
    @RequestMapping(value = "/v1/public/property/catalog/values/read/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        return builder(success(propertyCatalogValueService.findPropertyCatalogValueById(id)));
    }

    @ApiOperation(value = "Создать Property Catalog Value", tags = {"Property Catalog Value"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/v1/private/property/catalog/values/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@RequestBody PropertyCatalogValue propertyCatalogValue) {
        propertyCatalogValue.setCatalog(propertyCatalogRepository.getById(propertyCatalogValue.getCatalogId()));
        return builder(success(propertyCatalogValueService.createPropertyCatalogValue(propertyCatalogValue)));
    }

    @ApiOperation(value = "Обновить Property Catalog Value", tags = {"Property Catalog Value"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/v1/public/property/catalog/values/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> update(@RequestBody PropertyCatalogValue propertyCatalogValue) {
        propertyCatalogValue.setCatalog(propertyCatalogRepository.getById(propertyCatalogValue.getCatalogId()));
        return builder(success(propertyCatalogValueService.updatePropertyCatalogValue(propertyCatalogValue)));
    }

    @ApiOperation(value = "Удалить Property Catalog Value", tags = {"Property Catalog Value"})
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/v1/public/property/catalog/values/delete/{propertyCatalogValueId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteKiInformedConsent(@PathVariable(name = "propertyCatalogValueId") Integer propertyCatalogValueId) {
        propertyCatalogValueService.deletePropertyCatalogValueById(propertyCatalogValueId);
        return builder(success("success"));
    }

    @ApiOperation(value = "Получить список по Property Catalog Value Id iterable", tags = {"Property Catalog Value"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = "/v1/public/property/catalog/values/read/{id}/iterable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readIterable(@PathVariable("id") Integer id, @RequestParam("categoryId") Integer categoryId) {
//        return builder(success(propertyCatalogValueService.findAllByCatalogId(id,categoryId)));
        return builder(success(propertyCatalogValueService.findAllByCatalogId(id)));
    }
}
