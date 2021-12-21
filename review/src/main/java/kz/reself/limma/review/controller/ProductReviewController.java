package kz.reself.limma.review.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kz.reself.limma.review.model.Review;
import kz.reself.limma.review.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1")
@Api(value = "ProductReviewController")
public class ProductReviewController {

    public static final String PRIVATE_URL = "/private";
    public static final String PUBLIC_URL = "/public";

    @Autowired
    private IReviewService productReviewService;

    @RequestMapping(value = PUBLIC_URL + "/reviews/iterable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get() {
        return new ResponseEntity<>(productReviewService.getProductReviewsIterable(), HttpStatus.OK);
    }

    @ApiOperation(value = "Product review create")
    @RequestMapping(value = PUBLIC_URL + "/product/review/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> create(@RequestBody Review review) {
        return new ResponseEntity<>(productReviewService.addProductReview(review), HttpStatus.OK);
    }

    @ApiOperation(value = "Get reviews by id")
    @RequestMapping(value = PUBLIC_URL + "/reviews/{id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getById(@Param(value="id")Integer id){
        return new ResponseEntity<>(productReviewService.getProductReviewById(id), HttpStatus.OK);
    }
}
