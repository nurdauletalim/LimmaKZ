package kz.reself.limma.product.controller;

import io.swagger.annotations.*;
import kz.reself.limma.product.constant.PageableConstant;
import kz.reself.limma.product.model.PropertyTemplate;
import kz.reself.limma.product.repository.PropertyTemplateRepository;
import kz.reself.limma.product.service.IPropertyTemplateService;
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
@Api(tags = {"Property Template"}, description = "Управление Property Template")
public class PropertyTemplateController extends CommonService {

    @Autowired
    private IPropertyTemplateService propertyTemplateService;

    @Autowired
    private PropertyTemplateRepository propertyTemplateRepository;

    @ApiOperation(value = "Получить список Property Template pageable", tags = {"Property Template"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.(По умолчанию по 'id')", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "sortDirection", dataType = "string", value = "Указывает на тип сортировки.(По умолчанию по 'asc')", allowableValues = "asc,desc", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.(По умолчанию page равно на 0)", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице..(По умолчанию size равно на 5)", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что Property Template существуют и возвращает.")
    })
    @RequestMapping(value = "/v1/public/property/templates/read/all/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

        Page<PropertyTemplate> propertyTemplates;
        if (allRequestParams.containsKey("searchString")) {
            propertyTemplates = this.propertyTemplateService.findAllSearchString(allRequestParams.get("searchString"),pageableRequest);
        }
        else if (allRequestParams.containsKey("state")) {
            propertyTemplates = this.propertyTemplateRepository.state(Integer.parseInt(allRequestParams.get("state")), pageableRequest);
        } else {
            propertyTemplates = this.propertyTemplateService.findAllPropertyTemplatesPageable(pageableRequest);
        }

        return builder(success(propertyTemplates));
    }

    @ApiOperation(value = "Получить список Property Template iterable", tags = {"Property Template"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = "/v1/public/property/templates/read/all/iterable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readIterable() {
        return builder(success(propertyTemplateService.findAllPropertyTemplatesIterable()));
    }

    @ApiOperation(value = "Получить список Active Property Template", tags = {"Property Template"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = "/v1/public/property/templates/read/all/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readActive() {
        return builder(success(propertyTemplateService.findAllActiveTemplates()));
    }

    @ApiOperation(value = "Получить Property Template", tags = {"Property Template"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запрошенная запись найдена"),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная запись не найдена.")
    })
    @RequestMapping(value = "/v1/public/property/templates/read/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        return builder(success(propertyTemplateService.findPropertyTemplateById(id)));
    }

    @ApiOperation(value = "Создать Property Template", tags = {"Property Template"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/v1/private/property/templates/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@RequestBody PropertyTemplate propertyTemplate) {
        return builder(success(propertyTemplateService.createPropertyTemplate(propertyTemplate)));
    }

    @ApiOperation(value = "Обновить Property Template", tags = {"Property Template"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/v1/public/property/templates/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> update(@RequestBody PropertyTemplate propertyTemplate) {
        return builder(success(propertyTemplateService.updatePropertyTemplate(propertyTemplate)));
    }
    @ApiOperation(value = "Удалить Property Template", tags = {"Property Template"})
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/v1/public/property/templates/delete/{propertyTemplateId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteKiInformedConsent(@PathVariable(name = "propertyTemplateId") Integer propertyTemplateId) {
        propertyTemplateService.deletePropertyTemplateById(propertyTemplateId);
        return builder(success("success"));
    }

    @ApiOperation(value = "Получить Property Template по Category", tags = {"Property Template"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запрошенная запись найдена"),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная запись не найдена.")
    })
    @RequestMapping(value = "/v1/public/property/templates/read/category/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getByCategoryId(@PathVariable("id") Integer id) {
        return builder(success(propertyTemplateService.findPropertyTemplateByCategoryId(id)));
    }
}
