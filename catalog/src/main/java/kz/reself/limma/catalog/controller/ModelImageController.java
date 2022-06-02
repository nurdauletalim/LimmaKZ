package kz.reself.limma.catalog.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kz.reself.limma.catalog.model.ModelImage;
import kz.reself.limma.catalog.service.IModelImageService;
import kz.reself.limma.catalog.utils.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ModelImageController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationController.class);
    @Autowired
    IModelImageService iModelImageService;

    public static final String PRIVATE_URL = "/private/model/image";
    public static final String PUBLIC_URL = "/public/model/image";

    @RequestMapping(value = PUBLIC_URL + "/read/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        return builder(success(iModelImageService.findImageById(id)));
    }

//    @RequestMapping(value = "/v1/public/model/image/read/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<?> getAll() {
//        return builder(success(repository.findAll()));
//    }


    @RequestMapping(value = PUBLIC_URL + "/readByModelId/{modelId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllImagesByModelId(@PathVariable("modelId") Integer modelId) {
        return builder(success(iModelImageService.getAllImageByModelId(modelId)));
    }

    @ApiOperation(value = "Создать Image", tags = {"Image"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
    @RequestMapping(value = PRIVATE_URL + "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createImage(@RequestBody ModelImage image) {
        return builder(success(iModelImageService.createImage(image)));
    }
//    TODO
//    @RequestMapping(value = "/v1/public/model/image/update/{modelId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<?> updateImage(@RequestBody List<MultipartFile> files, @PathVariable("modelId") Integer modelId) throws IOException {
//        List<ModelImage> images = new ArrayList<>();
//        for (MultipartFile file : files) {
//            ModelImage image = new ModelImage();
//            image.setData(file.getBytes());
//            image.setModelId(modelId);
//            images.add(image);
//        }
//        return builder(success(iModelImageService.updateImage(images)));
//
//    }

    @RequestMapping(value = PUBLIC_URL + "/all/{modelId}", method = RequestMethod.GET)
    public ResponseEntity<?> updateImage( @PathVariable("modelId") Integer modelId) throws IOException {
        return builder(success(iModelImageService.getAllImageByModelId(modelId)));

    }


    @ApiOperation(value = "Удалить Image", tags = {"Image"})
    @RequestMapping(value = PUBLIC_URL + "/delete/{imageId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteImage(@PathVariable(name = "imageId") Integer imageId) {
        iModelImageService.deleteImageById(imageId);
        return builder(success("success"));
    }

}
