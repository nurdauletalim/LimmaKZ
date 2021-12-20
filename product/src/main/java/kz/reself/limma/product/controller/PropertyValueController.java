package kz.reself.limma.product.controller;

import io.swagger.annotations.*;
import kz.reself.limma.product.constant.PageableConstant;
import kz.reself.limma.product.model.PropertyValue;
import kz.reself.limma.product.service.IPropertyValueService;
import kz.reself.limma.product.utils.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping()
@Api(tags = "PropertyValue",description = "PropertyValue controller")
public class PropertyValueController extends CommonService {
    
    @Autowired
    private IPropertyValueService iPropertyValueService;

    @ApiOperation(value = "Получить список Property Value pageable", tags = {"Property Value"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.(По умолчанию по 'id')", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "sortDirection", dataType = "string", value = "Указывает на тип сортировки.(По умолчанию по 'asc')", allowableValues = "asc,desc", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.(По умолчанию page равно на 0)", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице..(По умолчанию size равно на 5)", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что Property Value существуют и возвращает.")
    })
    @RequestMapping(value = "/v1/public/property/values/read/all/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

        Page<PropertyValue> propertyValues = this.iPropertyValueService.findAllPropertyValues(pageableRequest);

        return builder(success(propertyValues));
    }

    @ApiOperation(value = "Получить список Property Value iterable", tags = {"Property Value"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = "/v1/public/property/values/read/all/iterable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readIterable() {
        return builder(success(iPropertyValueService.findAllIterable()));
    }

    @ApiOperation(value = "Получить список Active Property Value", tags = {"Property Value"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = "/v1/public/property/values/read/all/{productId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readActive(@PathVariable(name = "productId") Integer id) {
        return builder(success(iPropertyValueService.getAllByProductId(id)));
    }

    @ApiOperation(value = "Получить Property Value", tags = {"Property Value"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запрошенная запись найдена"),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная запись не найдена.")
    })
    @RequestMapping(value = "/v1/public/property/values/read/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        return builder(success(iPropertyValueService.getPropertyValueById(id)));
    }

    @ApiOperation(value = "Создать Property Value", tags = {"Property Value"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
    @RequestMapping(value = "/v1/private/property/values/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@RequestBody PropertyValue propertyValue) {
        return builder(success(iPropertyValueService.createPropertyValue(propertyValue)));
    }

    @ApiOperation(value = "Обновить Property Value", tags = {"Property Value"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
    @RequestMapping(value = "/v1/public/property/values/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> update(@RequestBody PropertyValue propertyValue) {
        return builder(success(iPropertyValueService.updatePropertyValue(propertyValue)));
    }

    @ApiOperation(value = "Обновить Property Value", tags = {"Property Value"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
    @RequestMapping(value = "/v1/public/property/values/update/all", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updateAll(@RequestBody List<PropertyValue> propertyValues) {
        return builder(success(iPropertyValueService.updateProductValues(propertyValues)));
    }
    @ApiOperation(value = "Удалить Property Value", tags = {"Property Value"})
    @RequestMapping(value = "/v1/public/property/values/delete/{propertyId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> delete(@PathVariable(name = "propertyId") Integer propertyId) {
        iPropertyValueService.delete(propertyId);
        return builder(success("success"));
    }


}
