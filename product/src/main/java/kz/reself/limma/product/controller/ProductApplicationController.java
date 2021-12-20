package kz.reself.limma.product.controller;

import io.swagger.annotations.*;
import kz.reself.limma.product.model.ProductApplication;
import kz.reself.limma.product.constant.PageableConstant;
import kz.reself.limma.product.model.ApplicationDTO;
import kz.reself.limma.product.model.ProductApplicationStatus;
import kz.reself.limma.product.repository.ProductApplicationRepository;
import kz.reself.limma.product.service.IProductApplicationService;
import kz.reself.limma.product.utils.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping()
@Api(tags = "Application",description = "Управление Application")
public class ProductApplicationController extends CommonService {


    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private IProductApplicationService iProductApplicationService;

    @Autowired
    private ProductApplicationRepository productApplicationRepository;

    @ApiOperation(value = "Получить список Application pageable", tags = {"Application"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.(По умолчанию по 'id')", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "sortDirection", dataType = "string", value = "Указывает на тип сортировки.(По умолчанию по 'asc')", allowableValues = "asc,desc", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.(По умолчанию page равно на 0)", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице..(По умолчанию size равно на 5)", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что Application существуют и возвращает.")
    })
    @RequestMapping(value = "/v1/public/applications/read/all/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readAllApplicationPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
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

        Page<kz.reself.limma.product.model.ProductApplication> allApplicationPageable = this.iProductApplicationService.getAllApplicationPageable(pageableRequest);

        return builder(success(allApplicationPageable));
    }

    @ApiOperation(value = "Получить список ApplicationDTO pageable", tags = {"ApplicationDTO"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.(По умолчанию по 'id')", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "sortDirection", dataType = "string", value = "Указывает на тип сортировки.(По умолчанию по 'asc')", allowableValues = "asc,desc", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.(По умолчанию page равно на 0)", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице..(По умолчанию size равно на 5)", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что ApplicationDTO существуют и возвращает.")
    })
    @RequestMapping(value = "/v1/public/applications/read/DTO/all/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readAllApplicationDTOPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        Sort.Direction sortDirection = Sort.Direction.DESC;

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

        Page<ApplicationDTO> allApplicationPageable;
        if (allRequestParams.containsKey("searchString")) {
            allApplicationPageable = this.iProductApplicationService.findAllDTOSearchString(allRequestParams.get("searchString"),pageableRequest);
        } else if (allRequestParams.containsKey("status")) {
            allApplicationPageable = this.iProductApplicationService.findAllDTOStatus(Integer.parseInt(allRequestParams.get("status")), pageableRequest);
        } else if (allRequestParams.containsKey("productId")) {
            allApplicationPageable = this.iProductApplicationService.findAllDTOSByProductPageable(Integer.parseInt(allRequestParams.get("productId")), pageableRequest);
        } else if (allRequestParams.containsKey("productId") && allRequestParams.containsKey("managerId")) {
            allApplicationPageable = this.iProductApplicationService.findAllDTOByProductAndManagerPageable(
                    Integer.parseInt(allRequestParams.get("productId")), Integer.parseInt(allRequestParams.get("managerId")), pageableRequest);
        } else {
            allApplicationPageable = this.iProductApplicationService.findAllDTOSPageable(pageableRequest);
        }

        return builder(success(allApplicationPageable));
    }

    @ApiOperation(value = "Получить список ApplicationDTO pageable", tags = {"ApplicationDTO"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.(По умолчанию по 'id')", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "sortDirection", dataType = "string", value = "Указывает на тип сортировки.(По умолчанию по 'asc')", allowableValues = "asc,desc", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.(По умолчанию page равно на 0)", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице..(По умолчанию size равно на 5)", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что ApplicationDTO существуют и возвращает.")
    })
    @RequestMapping(value = "/v1/public/applications/read/DTO/organization/{orgId}/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readAllOrganizationApplicationDTOPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams,@PathVariable(name = "orgId") Integer orgId) {
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

        Page<ApplicationDTO> allApplicationPageable;
        if (allRequestParams.containsKey("searchString")) {
            allApplicationPageable = this.iProductApplicationService.findAllDTOOrgIdSearchString(orgId, allRequestParams.get("searchString"),pageableRequest);
        }else {
            allApplicationPageable = this.iProductApplicationService.getAllDTObyOrgIdPageable(orgId,pageableRequest);
        }

        return builder(success(allApplicationPageable));
    }

    @ApiOperation(value = "Получить список Application iterable", tags = {"Application"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = "/v1/public/applications/read/all/iterable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readApplicationIterable() {
        return builder(success(iProductApplicationService.getAllApplicationsIterable()));
    }


    @ApiOperation(value = "Получить Application", tags = {"Application"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запрошенная запись найдена"),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная запись не найдена.")
    })
    @RequestMapping(value = "/v1/public/applications/read/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        return builder(success(iProductApplicationService.getProductApplicationById(id)));
    }

    @ApiOperation(value = "Получить Application by Organization", tags = {"Application"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запрошенная запись найдена"),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная запись не найдена.")
    })
    @RequestMapping(value = "/v1/public/applications/read/all/organization/{organizationId}/iterable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getApplicationsByOrganization(@PathVariable("organizationId") Integer id) {
        return builder(success(iProductApplicationService.getApplicationsByOrganizationId(id)));
    }

    @ApiOperation(value = "Создать Application", tags = {"Application"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
    @RequestMapping(value = "/v1/public/applications/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createApplication(@RequestBody ProductApplication productApplication) {
        return builder(success(iProductApplicationService.createApplication(productApplication)));
    }

    @ApiOperation(value = "Обновить Application", tags = {"Application"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
    @RequestMapping(value = "/v1/public/applications/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updateApplication(@RequestBody ProductApplication productApplication) {
        return builder(success(iProductApplicationService.updateApplication(productApplication)));
    }

    @ApiOperation(value = "Удалить Application", tags = {"Application"})
    @RequestMapping(value = "/v1/public/applications/delete/{applicationId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteApplicationById(@PathVariable(name = "applicationId") Integer id) {
        iProductApplicationService.deleteApplicationById(id);
        return builder(success("success"));
    }

    @ApiOperation(value = "Получить список ApplicationDTO iterable", tags = {"ApplicationDTO"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = "/v1/public/applications/read/DTO/all/iterable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readApplicationDTOIterable() {
        return builder(success(iProductApplicationService.findAllDTOSIterable()));
    }


    @ApiOperation(value = "Получить ApplicationDTO", tags = {"ApplicationDTO"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запрошенная запись найдена"),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная запись не найдена.")
    })
    @RequestMapping(value = "/v1/public/applications/read/DTO/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDTO(@PathVariable("id") Integer id) {
        return builder(success(iProductApplicationService.getDTOById(id)));
    }

    @ApiOperation(value = "Получить ApplicationDTO by Organization", tags = {"ApplicationDTO"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запрошенная запись найдена"),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная запись не найдена.")
    })
    @RequestMapping(value = "/v1/public/applications/read/DTO/all/organization/{organizationId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getApplicationDTOsByOrganization(@PathVariable("organizationId") Integer id) {
        return builder(success(iProductApplicationService.getAllDTObyOrgId(id)));
    }

    @ApiOperation(value = "Обновить ApplicationDTO", tags = {"ApplicationDTO"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
    @RequestMapping(value = "/v1/public/applications/update/DTO", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updateApplicationDTO(@RequestBody ApplicationDTO productApplicationDTO) {
        return builder(success(iProductApplicationService.updateApplicationDTO(productApplicationDTO)));
    }

    @RequestMapping(value = "/v1/public/applications/read/all/{contact}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getApplicationsDTOByContact(@PathVariable("contact") String contact) {
        return builder(success(iProductApplicationService.getApplicationsByContacts(contact)));
    }

    @RequestMapping(value = "/v1/public/applications/check", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getApplicationsDTOByContact(@RequestParam("name") String name,@RequestParam("contact") String contact,@RequestParam("productId") Integer productId) {
//        System.out.println(application);
        return builder(success(iProductApplicationService.checkApplication(name, contact, productId)));
    }

    @ApiOperation(value = "Получить список ApplicationDTO pageable", tags = {"ApplicationDTO"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.(По умолчанию по 'id')", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "sortDirection", dataType = "string", value = "Указывает на тип сортировки.(По умолчанию по 'asc')", allowableValues = "asc,desc", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.(По умолчанию page равно на 0)", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице..(По умолчанию size равно на 5)", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что ApplicationDTO существуют и возвращает.")
    })
    @RequestMapping(value = "/v1/public/applications/read/DTO/status/{status}/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readAllApplicationDTOPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams,@PathVariable(name = "status") ProductApplicationStatus status) {
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

        Page<ApplicationDTO> allApplicationPageable = this.iProductApplicationService.getAllByStatusPageable(status, pageableRequest);

        return builder(success(allApplicationPageable));
    }

    @RequestMapping(value = "/v1/public/applications/read/DTO/status/{status}/organization/{orgId}/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readByStatusAndOrganization(@RequestParam Map<String, String> allRequestParams, @PathVariable(name = "status") Integer status,
                                                         @PathVariable(name = "orgId") Integer orgId) {
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

        Page<ApplicationDTO> allApplicationPageable = this.iProductApplicationService.getByStatusAndOrganizationPageable(status, orgId, pageableRequest);

        return builder(success(allApplicationPageable));
    }

    @RequestMapping(value = "/v1/public/applications/count/organization/{organizationId}/{interval}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAmountByOrgId(@PathVariable(name = "organizationId") Integer organizationId, @PathVariable(name = "interval") String interval) {
        return builder(success(iProductApplicationService.getAmountApplicationByOrganizationId(organizationId,interval)));
    }

    @ApiOperation(value = "Получить список applications по категории", tags = {"Application"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.(По умолчанию по 'id')", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "sortDirection", dataType = "string", value = "Указывает на тип сортировки.(По умолчанию по 'asc')", allowableValues = "asc,desc", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.(По умолчанию page равно на 0)", paramType = "query"),
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Поле для введение стринга для поиска", paramType = "query"),
            @ApiImplicitParam(name = "category", dataType = "string", value = "Поле для введение стринга для поиска", paramType = "query"),
            @ApiImplicitParam(name = "state", dataType = "string", value = "Поле для введение стринга для поиска", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице..(По умолчанию size равно на 5)", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что Applications существуют и возвращает.")
    })
    @RequestMapping(value = "/v1/public/applications/read/history/{productId}/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getHistoryOfApplications(@PathVariable("productId") Integer productId, @ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
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

        return builder(success(productApplicationRepository.getAllByProductId(productId, pageableRequest)));
    }

}