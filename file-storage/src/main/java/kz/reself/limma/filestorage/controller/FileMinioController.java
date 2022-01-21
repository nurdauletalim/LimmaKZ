package kz.reself.limma.filestorage.controller;


import io.minio.errors.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;
import kz.reself.limma.filestorage.dto.FileDto;
import kz.reself.limma.filestorage.dto.MessageDto;
import kz.reself.limma.filestorage.enam.FileSize;
import kz.reself.limma.filestorage.model.FileEntity;
import kz.reself.limma.filestorage.service.impl.MinioFileServiceImpl;
import kz.reself.limma.filestorage.util.ErrorMessageConstants;
import kz.reself.limma.filestorage.util.ExpectationFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/private/v1/minio/file")
@RequiredArgsConstructor
public class FileMinioController {

    private final MinioFileServiceImpl minioFileService;

    @GetMapping("/{id}")
    public FileDto getTemplateFile(@PathVariable Long id) {
        return minioFileService.getOne(id);
    }

    @GetMapping("/test/list")
    public List<FileEntity> getTemplateFileList() {
        return minioFileService.getList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> deleteTemplateFile(@PathVariable Long id) {
        try {
            minioFileService.deleteOne(id);
            return ResponseEntity.ok(new MessageDto("Deleted the file successfully: " + id));
        } catch (Exception e) {
            throw new ExpectationFailedException(String.format(ErrorMessageConstants.DELETE_ERROR.MESSAGE, e.getMessage()), ErrorMessageConstants.DELETE_ERROR.ERROR_CODE);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<FileEntity> uploadLampFileTemplate(@RequestParam("file") MultipartFile file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, InsufficientDataException, ServerException, InternalException, InvalidResponseException, ErrorResponseException {
        return ResponseEntity.ok(minioFileService.saveFile(file));
    }

    @PostMapping("/upload/multi")
    public ResponseEntity<List<FileEntity>> uploadLampFileTemplateMulti(@RequestPart("file") List<MultipartFile> file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, InsufficientDataException, ServerException, InternalException, InvalidResponseException, ErrorResponseException {
        return ResponseEntity.ok(minioFileService.saveFileMulti(file));
    }

    @GetMapping("/file-entity/{id}")
    public ResponseEntity<FileEntity> getFileById(@PathVariable Long id) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, InsufficientDataException, ServerException, InternalException, InvalidResponseException, ErrorResponseException {
        return ResponseEntity.ok(minioFileService.getFileEntity(id));
    }
    @RequestMapping(path = "/download/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@PathVariable Long id) throws IOException, InsufficientDataException, NoSuchAlgorithmException, InvalidResponseException, InternalException, ErrorResponseException, XmlParserException, ServerException, InvalidKeyException {
        FileEntity fileEntity = minioFileService.getFileEntity(id);
        ByteArrayResource resource = minioFileService.getFile(id);
        String fileName = URLEncoder.encode(fileEntity.getFilename(), "UTF-8");
        fileName = URLDecoder.decode(fileName, "ISO8859_1");

        return ResponseEntity.ok()
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(resource);
    }
    @GetMapping("/prefiew/{id}")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", dataType = "String", value = "Размер файла (small or medium)", paramType = "query")
    })
    public void downloadFile(@PathVariable Long id, @ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams, HttpServletResponse response) throws IOException, InsufficientDataException, NoSuchAlgorithmException, InvalidResponseException, InternalException, ErrorResponseException, XmlParserException, ServerException, InvalidKeyException {
        ByteArrayResource resource = minioFileService.getFile(id);
        FileEntity fileEntity = minioFileService.getFileEntity(id);
        InputStream is = resource.getInputStream();
        response.setHeader("Content-Type", fileEntity.getFileType());
        response.setContentType(fileEntity.getFileType());
        if (fileEntity.getFileType().contains("svg")) {
            IOUtils.copy(is, response.getOutputStream());
        } else {
            FileSize fileSize = FileSize.MEDIUM;
            if (allRequestParams.containsKey("size")) {
                fileSize = allRequestParams.get("size").equalsIgnoreCase("small") ? FileSize.SMALL
                        : FileSize.MEDIUM;
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(resizeImage(ImageIO.read(is), fileSize.getWidth(), fileSize.getHeight()), "png", os);
            InputStream resizeImage = new ByteArrayInputStream(os.toByteArray());
            IOUtils.copy(resizeImage, response.getOutputStream());
        }
        response.flushBuffer();
    }

    BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.SCALE_DEFAULT);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
}
