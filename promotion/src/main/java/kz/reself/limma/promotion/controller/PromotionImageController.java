package kz.reself.limma.promotion.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kz.reself.limma.promotion.model.PromotionImage;
import kz.reself.limma.promotion.repository.ImageRepository;
import kz.reself.limma.promotion.service.IPromotionImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class PromotionImageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PromotionImageController.class);
    @Autowired
    IPromotionImageService iPromotionImageService;
    @Autowired
    ImageRepository repository;

    @RequestMapping(value = "/v1/public/promotion/image/read/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(iPromotionImageService.findImageById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/public/promotion/image/read/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }


    @RequestMapping(value = "/v1/public/promotion/image/readByPromotionId/{promotionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllImagesByPromotionId(@PathVariable("promotionId") Integer promotionId) {
        return new ResponseEntity<>(iPromotionImageService.getAllImageByPromotionId(promotionId), HttpStatus.OK);
    }

    @ApiOperation(value = "Создать Image", tags = {"Image"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
    @RequestMapping(value = "/v1/private/promotion/image/create", method = RequestMethod.POST, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> createImage(@RequestBody PromotionImage image) {

        return new ResponseEntity<>(iPromotionImageService.createImage(image), HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/public/promotion/image/update/{promotionId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updateImage(@RequestBody List<MultipartFile> files, @PathVariable("promotionId") Integer promotionId) throws IOException {
        List<PromotionImage> images = new ArrayList<>();
        for (MultipartFile file : files) {
            PromotionImage image = new PromotionImage();
            image.setData(file.getBytes());
            image.setPromotionId(promotionId);
            images.add(image);
        }
        return new ResponseEntity<>(iPromotionImageService.updateImage(images), HttpStatus.OK);

    }

    @RequestMapping(value = "/v1/public/promotion/image/all/{promotionId}", method = RequestMethod.GET)
    public ResponseEntity<?> updateImage(@PathVariable("promotionId") Integer promotionId) throws IOException {
        return new ResponseEntity<>(iPromotionImageService.getImagesById(promotionId), HttpStatus.OK);

    }


    @ApiOperation(value = "Удалить Image", tags = {"Image"})
    @RequestMapping(value = "/v1/public/promotion/image/delete/{imageId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteImage(@PathVariable(name = "imageId") Integer imageId) {
        iPromotionImageService.deleteImageById(imageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
