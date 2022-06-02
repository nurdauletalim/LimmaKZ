package kz.reself.limma.promotion.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kz.reself.limma.promotion.constant.PageableConstant;
import kz.reself.limma.promotion.model.Promotion;
import kz.reself.limma.promotion.service.impl.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Api(value = "Управление PROMOTION")
public class PromotionController {

    public static final String PRIVATE_URL = "/api/v1/private/promotions";
    public static final String PUBLIC_URL = "/api/v1/public/promotions";

    @Autowired
    PromotionService promotionService;

    @ApiOperation(value = "Get all promotions")
    @RequestMapping(value = PUBLIC_URL + "/read/all/iterable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get() {
        return new ResponseEntity<>(promotionService.getAllActive(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get all active count")
    @RequestMapping(value = PUBLIC_URL + "/count/active/iterable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getPromotionsCount(@Param(value = "count") Integer count) {
        return new ResponseEntity<>(promotionService.getAllActiveCount(count), HttpStatus.OK);
    }

    @ApiOperation(value = "Get all active DTO count")
    @RequestMapping(value = PUBLIC_URL + "/dto/count/active/iterable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getPromotionsDTO(@Param(value = "count") Integer count) {
        return new ResponseEntity<>(promotionService.getAllActiveDTO(count), HttpStatus.OK);
    }

    @ApiOperation(value = "Get promotions")
    @RequestMapping(value = PUBLIC_URL + "/read/all/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readAllPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        Sort.Direction sortDirection = Sort.Direction.ASC;

        int pageNumber = PageableConstant.PAGE_NUMBER;

        int pageSize = PageableConstant.PAGE_SIZE;

        String searchString = "";

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
        Page<Promotion> promotions;
        final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortBy));
        if (allRequestParams.containsKey("searchString") && allRequestParams.containsKey("state")) {
            promotions = promotionService.findSearchString(allRequestParams.get("searchString"),pageableRequest);
        }
        else {
            promotions = promotionService.getAllPageable(pageableRequest);
        }
        return new ResponseEntity<>(promotions, HttpStatus.OK);
    }

    @ApiOperation(value = "Get promotions by id")
    @RequestMapping(value = PUBLIC_URL + "/read/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(promotionService.getById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Get promotions by id")
    @RequestMapping(value = PRIVATE_URL + "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> create(@RequestBody Promotion promotion) {
        return new ResponseEntity<>(promotionService.createPromotion(promotion), HttpStatus.OK);
    }
}
