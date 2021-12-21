package kz.reself.limma.product.controller;

import io.swagger.annotations.*;
import kz.reself.limma.product.constant.PageableConstant;
import kz.reself.limma.product.model.Property;
import kz.reself.limma.product.service.IPropertyService;
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
@RequestMapping("/api/v1")
@Api(tags = "Property", description = "Управление Property")
public class PropertyController extends CommonService {

    public static final String PRIVATE_URL = "/private/properties";
    public static final String PUBLIC_URL = "/public/properties";

    @Autowired
    private IPropertyService iPropertyService;

    @ApiOperation(value = "Получить список Property pageable", tags = {"Property"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.(По умолчанию по 'id')", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "sortDirection", dataType = "string", value = "Указывает на тип сортировки.(По умолчанию по 'asc')", allowableValues = "asc,desc", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.(По умолчанию page равно на 0)", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице..(По умолчанию size равно на 5)", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что Property существуют и возвращает.")
    })
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

        Page<Property> categories = this.iPropertyService.findAllPropertiesPageable(pageableRequest);

        return builder(success(categories));
    }

    @ApiOperation(value = "Получить список Property iterable", tags = {"Property"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = PUBLIC_URL + "/read/all/iterable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readIterable() {
        return builder(success(iPropertyService.findAllPropertiesIterable()));
    }

    @ApiOperation(value = "Получить Property", tags = {"Property"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запрошенная запись найдена"),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная запись не найдена.")
    })
    @RequestMapping(value = PUBLIC_URL + "/read/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        return builder(success(iPropertyService.findPropertyById(id)));
    }

    @ApiOperation(value = "Создать Property", tags = {"Property"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = PUBLIC_URL + "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@RequestBody Property property) {
        return builder(success(iPropertyService.createProperty(property)));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = PUBLIC_URL + "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> update(@RequestBody Property property) {
        return builder(success(iPropertyService.updateProperty(property)));
    }

    @ApiOperation(value = "Удалить Property", tags = {"Property"})
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = PUBLIC_URL + "/delete/{propertyId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteKiInformedConsent(@PathVariable(name = "propertyId") Integer propertyId) {
        iPropertyService.deletePropertyById(propertyId);
        return builder(success("success"));
    }

    @ApiOperation(value = "Получить список Property By Template", tags = {"Property"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = PUBLIC_URL + "/read/all/template/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> allPropertiesByTemplateId(@PathVariable(name = "id") Integer templateId) {
        return builder(success(iPropertyService.getPropertyByTemplateId(templateId)));
    }
    @ApiOperation(value = "Получить список Property By Template", tags = {"Property"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = PUBLIC_URL + "/read/all/category/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> allPropertiesByTemplateCategoryId(@PathVariable(name = "id") Integer categoryId) {
        return builder(success(iPropertyService.getPropertyByTemplateCategoryId(categoryId)));
    }

    @RequestMapping(value = PUBLIC_URL + "/change/main/{propertyId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> changeMain(@PathVariable(name = "propertyId") Integer propertyId) {
        return builder(success(iPropertyService.changeMain(propertyId)));
    }


}
