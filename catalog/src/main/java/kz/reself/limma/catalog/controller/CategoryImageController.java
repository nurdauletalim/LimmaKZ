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

//    @Autowired
//    ImageRepository repository;

    @RequestMapping(value = PUBLIC_URL + "/read/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        return builder(success(iCategoryImageService.findImageById(id)));
    }

    @RequestMapping(value = PUBLIC_URL + "/read/all/parentId/{parentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllByParentId(@PathVariable Integer parentId) {
        return builder(success(iCategoryImageService.getAllImageByParentId(parentId)));
    }

    // TODO get from Minio service
//    @RequestMapping(value = "/v1/public/category/image/read/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<?> getAll() {
//        return builder(success(repository.findAll()));
//    }

    @RequestMapping(value = PUBLIC_URL + "/readByCategoryId/{categoryId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllImagesByCategoryId(@PathVariable("categoryId") Integer categoryId) {
        return builder(success(iCategoryImageService.getAllImageByCategoryId(categoryId)));
    }

    @RequestMapping(value = PRIVATE_URL + "/create", method = RequestMethod.POST, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> createImage(@RequestBody CategoryImage image) {
        return builder(success(iCategoryImageService.createImage(image)));
    }

    @RequestMapping(value = PUBLIC_URL + "/update/{categoryId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updateImage(@RequestBody List<MultipartFile> files, @PathVariable("categoryId") Integer categoryId) throws IOException {
        List<CategoryImage> images = new ArrayList<>();
        for (MultipartFile file: files) {
            CategoryImage image = new CategoryImage();
            image.setData(file.getBytes());
            image.setCategoryId(categoryId);
            images.add(image);
        }
        return builder(success(iCategoryImageService.updateImage(images)));
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
