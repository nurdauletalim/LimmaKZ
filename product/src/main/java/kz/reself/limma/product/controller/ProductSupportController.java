package kz.reself.limma.product.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import kz.reself.limma.product.constant.PageableConstant;
import kz.reself.limma.product.model.ProductSupport;
import kz.reself.limma.product.repository.ProductSupportRepository;
import kz.reself.limma.product.service.IProductSupportService;
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
@Api(tags = {"Product Support"})
public class ProductSupportController extends CommonService {

    @Autowired
    private IProductSupportService iProductSupportService;

    @Autowired
    private ProductSupportRepository productSupportRepository;

    @RequestMapping(value = "/v1/public/product/supports/read/all/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
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

        Page<ProductSupport> productSupport;
        final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortBy));
        if (allRequestParams.containsKey("searchString")) {
            productSupport = this.iProductSupportService.findSearchString(allRequestParams.get("searchString"), pageableRequest);
        } else if (allRequestParams.containsKey("managerId") && allRequestParams.containsKey("status")) {
            productSupport = this.iProductSupportService.getAllPageableByManagerIdAndStatus(Integer.valueOf(allRequestParams.get("managerId")), allRequestParams.get("status"), pageableRequest);
        } else if (allRequestParams.containsKey("exceptStatus") && allRequestParams.containsKey("managerId")) {
            productSupport = this.iProductSupportService.getAllByStatusNotAndManagerId(Integer.valueOf(allRequestParams.get("managerId")), allRequestParams.get("exceptStatus"), pageableRequest);
        } else if (allRequestParams.containsKey("exceptStatus")) {
            productSupport = this.iProductSupportService.getAllByStatusNot(allRequestParams.get("exceptStatus"), pageableRequest);
        } else if (allRequestParams.containsKey("managerId")) {
            productSupport = this.iProductSupportService.getAllPageableByManagerId(Integer.valueOf(allRequestParams.get("managerId")), pageableRequest);
        } else if (allRequestParams.containsKey("status")) {
            productSupport = this.iProductSupportService.getAllPageableByStatus(allRequestParams.get("status"), pageableRequest);
        } else {
            productSupport = this.iProductSupportService.getAllPageable(pageableRequest);
        }
        return builder(success(productSupport));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CONTENT_MANAGER')")
    @RequestMapping(value = "/v1/private/product/supports/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> create(@RequestBody ProductSupport productSupport) {
        return builder(success(iProductSupportService.create(productSupport)));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CONTENT_MANAGER')")
    @RequestMapping(value = "/v1/private/product/supports/delete/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        iProductSupportService.delete(id);
        return builder(success("success"));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CONTENT_MANAGER')")
    @RequestMapping(value = "/v1/private/product/supports/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> update(@RequestBody ProductSupport productSupport) {
        return builder(success(iProductSupportService.update(productSupport)));
    }

    @RequestMapping(value = "/v1/public/product/supports/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getById(@PathVariable(name = "id") Integer id) {
        return builder(success(iProductSupportService.getById(id)));
    }

    @RequestMapping(value = "/v1/public/supports/read/history/{productId}/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getHistoryOfModeration(@PathVariable("productId") Integer productId, @ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
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

        return builder(success(iProductSupportService.getAllProductId(pageableRequest, productId)));
    }
}
