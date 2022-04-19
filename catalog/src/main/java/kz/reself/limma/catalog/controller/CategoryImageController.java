package kz.reself.limma.catalog.controller;

import io.swagger.annotations.Api;
import kz.reself.limma.catalog.model.CategoryImage;
import kz.reself.limma.catalog.service.ICategoryImageService;
import kz.reself.limma.catalog.utils.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Api(tags = {"Image"}, description = "Управление image")
public class CategoryImageController extends CommonService {

    @Autowired
    ICategoryImageService iCategoryImageService;

    public static final String PRIVATE_URL = "/private/category/image";
    public static final String PUBLIC_URL = "/public/category/image";

    @RequestMapping(value = PUBLIC_URL + "/read/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        return builder(success(iCategoryImageService.findImageById(id)));
    }

    @RequestMapping(value = PUBLIC_URL + "/read/all/parentId/{parentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllByParentId(@PathVariable Integer parentId) {
        return builder(success(iCategoryImageService.getAllImageByParentId(parentId)));
    }

    @RequestMapping(value = PUBLIC_URL + "/readByCategoryId/{categoryId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllImagesByCategoryId(@PathVariable("categoryId") Integer categoryId) {
        return builder(success(iCategoryImageService.getAllImageByCategoryId(categoryId)));
    }

    @RequestMapping(value = PRIVATE_URL + "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createImage(@RequestBody CategoryImage categoryImage) {
        return ResponseEntity.ok(iCategoryImageService.createImage(categoryImage));
    }

    @RequestMapping(value = PUBLIC_URL + "/all/{categoryId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllByCategoryId(@PathVariable("categoryId") Integer categoryId) throws IOException {
        return builder(success(iCategoryImageService.getImagesById(categoryId)));
    }

    @RequestMapping(value = PUBLIC_URL + "/delete/{imageId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteImage(@PathVariable(name = "imageId") Integer imageId) {
        iCategoryImageService.deleteImageById(imageId);
        return builder(success("success"));
    }

}
