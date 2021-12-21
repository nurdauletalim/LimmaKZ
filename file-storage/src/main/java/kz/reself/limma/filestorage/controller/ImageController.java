package kz.reself.limma.filestorage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kz.reself.limma.filestorage.model.Image;
import kz.reself.limma.filestorage.repository.ImageRepository;
import kz.reself.limma.filestorage.service.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
public class ImageController {

    @Autowired
    IImageService iImageService;
    @Autowired
    ImageRepository repository;

    @RequestMapping(value = "/v1/public/images/read/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(iImageService.findImageById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/public/images/read/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }


    @RequestMapping(value = "/v1/public/images/read/product/{productId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllImagesByProductId(@PathVariable("productId") Integer productId) {
        return new ResponseEntity<>(iImageService.getAllImageByProductId(productId), HttpStatus.OK);
    }

    @ApiOperation(value = "Создать Image", tags = {"Image"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
    @RequestMapping(value = "/v1/private/images/create", method = RequestMethod.POST, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> createImage(@RequestBody Image image) {

        return new ResponseEntity<>(iImageService.createImage(image), HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/public/images/update/{productId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updateImage(@RequestBody List<MultipartFile> files, @PathVariable("productId") Integer productId) throws IOException {
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            Image image = new Image();
            image.setData(file.getBytes());
            image.setProductId(productId);
            images.add(image);
        }
        return new ResponseEntity<>(iImageService.updateImage(images), HttpStatus.OK);

    }

    @RequestMapping(value = "/v1/public/images/read/all/{productId}", method = RequestMethod.GET)
    public ResponseEntity<?> updateImage( @PathVariable("productId") Integer productId) throws IOException {
        return new ResponseEntity<>(iImageService.getImagesById(productId), HttpStatus.OK);

    }


    @ApiOperation(value = "Удалить Image", tags = {"Image"})
    @RequestMapping(value = "/v1/public/images/delete/{imageId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteImage(@PathVariable(name = "imageId") Integer imageId) {
        iImageService.deleteImageById(imageId);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
