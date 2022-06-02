package kz.reself.limma.catalog.controller;

import io.swagger.annotations.*;
import kz.reself.limma.catalog.constant.PageableConstant;
import kz.reself.limma.catalog.model.Organization;
import kz.reself.limma.catalog.service.IOrganizationService;
import kz.reself.limma.catalog.utils.CommonService;
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
@RequestMapping("/api/v1")
@Api(tags = {"Organization"}, description = "Управление organization")
public class OrganizationController extends CommonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationController.class);
    @Autowired
    IOrganizationService organizationService;

    public static final String PRIVATE_URL = "/private/organizations";
    public static final String PUBLIC_URL = "/public/organizations";

    @ApiOperation(value = "Получить список Organization pageable", tags = {"Organization"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.(По умолчанию по 'id')", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "sortDirection", dataType = "string", value = "Указывает на тип сортировки.(По умолчанию по 'asc')", allowableValues = "asc,desc", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.(По умолчанию page равно на 0)", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице..(По умолчанию size равно на 5)", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что Organization существуют и возвращает.")
    })
    @RequestMapping(value = PUBLIC_URL + "/read/all/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readAllOrganizationPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
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

        Page<Organization> organizations;
        if (allRequestParams.containsKey("searchString")) {
            organizations = this.organizationService.findAllSearchString(allRequestParams.get("searchString"),pageableRequest);
        }
        else {
            organizations = this.organizationService.getOrganizations(pageableRequest);
        }
        return builder(success(organizations));
    }

    @ApiOperation(value = "Получить список Organization iterable", tags = {"Organization"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = PUBLIC_URL + "/read/all/iterable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readAllOrganizationIterable() {
        return builder(success(organizationService.getOrganizationsIterable()));
    }

    @ApiOperation(value = "Удалить Organization", tags = {"Organization"})
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = PUBLIC_URL + "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteOrganizationById(@PathVariable(name = "id") Integer organizationId) {
        organizationService.deleteOrganizationById(organizationId);
        return builder(success("success"));
    }

    @ApiOperation(value = "Обновить Organization", tags = {"Organization"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = PUBLIC_URL + "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> update(@RequestBody Organization organization) {
        return builder(success(organizationService.updateOrganization(organization)));
    }


    @RequestMapping(value = PUBLIC_URL + "/read/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getOrganizationById(@PathVariable("id") Integer id) {
        return builder(success(organizationService.getOrganizationById(id)));
    }

    @ApiOperation(value = "Создать Organization", tags = {"Organization"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = PRIVATE_URL + "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createOrganization(@RequestBody Organization organization) {
        return builder(success(organizationService.addOrganization(organization)));
    }

}
