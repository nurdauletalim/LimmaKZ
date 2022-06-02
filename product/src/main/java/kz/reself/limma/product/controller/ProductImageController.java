package kz.reself.limma.product.controller;

import io.swagger.annotations.Api;
import kz.reself.limma.product.model.ProductImage;
import kz.reself.limma.product.service.IProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@Api(tags = {"Image"}, description = "Управление image")
public class ProductImageController {

    @Autowired
    IProductImageService iProductImageService;

    public static final String PRIVATE_URL = "/private/product/image";
    public static final String PUBLIC_URL = "/public/product/image";

    @RequestMapping(value = PUBLIC_URL + "/read/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(iProductImageService.findImageById(id));
    }

    @RequestMapping(value = PUBLIC_URL + "/all/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllImagesByProductId(@PathVariable("id") Integer productId) {
        return ResponseEntity.ok(iProductImageService.getAllImagesByProductId(productId));
    }

    @RequestMapping(value = PRIVATE_URL + "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createImage(@RequestBody ProductImage productImage) {
        return ResponseEntity.ok(iProductImageService.createImage(productImage));
    }

    @RequestMapping(value = PUBLIC_URL + "/delete/{imageId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteImage(@PathVariable(name = "imageId") Integer imageId) {
        iProductImageService.deleteImageById(imageId);
        return ResponseEntity.ok("success");
    }
}
