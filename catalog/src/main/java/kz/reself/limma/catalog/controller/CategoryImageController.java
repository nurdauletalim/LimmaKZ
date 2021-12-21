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
@RequestMapping()
@Api(tags = {"Image"}, description = "Управление image")
public class CategoryImageController extends CommonService {

    @Autowired
    ICategoryImageService iCategoryImageService;

//    @Autowired
//    ImageRepository repository;

    @RequestMapping(value = "/v1/public/category/image/read/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        return builder(success(iCategoryImageService.findImageById(id)));
    }

    @RequestMapping(value = "/v1/public/category/image//read/all/parentId/{parentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllByParentId(@PathVariable Integer parentId) {
        return builder(success(iCategoryImageService.getAllImageByParentId(parentId)));
    }

    // TODO get from Minio service
//    @RequestMapping(value = "/v1/public/category/image/read/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<?> getAll() {
//        return builder(success(repository.findAll()));
//    }

    @RequestMapping(value = "/v1/public/category/image/readByCategoryId/{categoryId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllImagesByCategoryId(@PathVariable("categoryId") Integer categoryId) {
        return builder(success(iCategoryImageService.getAllImageByCategoryId(categoryId)));
    }

    @RequestMapping(value = "/v1/private/category/image/create", method = RequestMethod.POST, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> createImage(@RequestBody CategoryImage image) {
        return builder(success(iCategoryImageService.createImage(image)));
    }

    @RequestMapping(value = "/v1/public/category/image/update/{categoryId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

    @RequestMapping(value = "/v1/public/category/image/all/{categoryId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllByCategoryId(@PathVariable("categoryId") Integer categoryId) throws IOException {
        return builder(success(iCategoryImageService.getImagesById(categoryId)));
    }

    @RequestMapping(value = "/v1/public/category/image/delete/{imageId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteImage(@PathVariable(name = "imageId") Integer imageId) {
        iCategoryImageService.deleteImageById(imageId);
        return builder(success("success"));
    }

}
