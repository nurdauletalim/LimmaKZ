package kz.reself.limma.filestorage.service.impl;

import io.minio.errors.*;
import kz.reself.limma.filestorage.configuration.MinioConfig;
import kz.reself.limma.filestorage.configuration.ModelMapperImpl;
import kz.reself.limma.filestorage.dto.FileDto;
import kz.reself.limma.filestorage.enam.FileExtension;
import kz.reself.limma.filestorage.exception.BadRequestException;
import kz.reself.limma.filestorage.model.FileEntity;
import kz.reself.limma.filestorage.repository.FileRepository;
import kz.reself.limma.filestorage.service.MinioFileService;
import kz.reself.limma.filestorage.util.MinioUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.tika.Tika;
import org.apache.tika.io.FilenameUtils;
import org.apache.tika.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MinioFileServiceImpl implements MinioFileService {

    private final Tika tika;

    @Autowired
    private FileRepository repository;

    @Autowired
    private MinioConfig.MinioProperties params;

    @Autowired
    private ModelMapperImpl modelMapper;

    @Autowired
    private MinioUtil minioUtil;

    @Value("${file.allowedMimeTypes}")
    private List<String> allowedMimeTypes;

    public MinioFileServiceImpl() {
        this.tika = new Tika();
    }

    @Override
    public FileDto getOne(Long id) {
        Optional<FileEntity> fileEntity = repository.findById(id);
        if (!fileEntity.isPresent()) {
            throw new EntityNotFoundException("Template File not found id: " + id);
        }

        return modelMapper.map(fileEntity, FileDto.class);
    }

    @Override
    public FileEntity getFileEntity(Long id) {
        return repository.getOne(id);
    }


    @Override
    public List<FileEntity> getList() {
     return repository.findAll();
    }

    @Override
    public void deleteOne(Long id) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        FileEntity fileEntity = repository.getById(id);
        if (fileEntity == null) {
            throw new EntityNotFoundException("Template File not found id: " + id);
        }

        String bucket = fileEntity.getBucket() != null ? fileEntity.getBucket() : params.getBucket();
        minioUtil.deleteFile(bucket, fileEntity.getFilename());
        repository.delete(fileEntity);
    }


    @Override
    public FileEntity saveFile(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("storage.upload.error");
        }

        var mimeType = file.getContentType() != null ? file.getContentType() : this.tika.detect(bytes);

        if (mimeType == null) {
            throw new BadRequestException("storage.upload.mime-type-unable-to-detect", null);
        }

//        FileExtension fileExtension = getFileExtensionByMimeType(mimeType);
        String originalFileName = file.getOriginalFilename();
        String fileName = FilenameUtils.getName(originalFileName);
        System.out.println("originalFileName:" +  originalFileName);
        System.out.println("fileName:" + fileName);

        FileEntity fileEntity = new FileEntity();
        String bucket = "limma";
        if (!minioUtil.bucketExists(bucket))
            minioUtil.makeBucket("limma");

        fileEntity.setFileExtension(mimeType);
        fileEntity.setFilename(fileName);
        fileEntity.setSize(file.getSize());
        fileEntity.setFileType(file.getContentType());
        fileEntity.setBucket(bucket);
        fileEntity = repository.saveAndFlush(fileEntity);
        minioUtil.uploadFile(file.getInputStream(), bucket, fileEntity.getId().toString(), file.getContentType());
        String publicUrl = params.getEndpoint() + params.getBucket() + '/' + fileEntity.getId();
        fileEntity.setStorageUrl(publicUrl);
        return repository.saveAndFlush(fileEntity);
    }

    @Override
    public List<FileEntity> saveFileMulti(List<MultipartFile> files) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        List<FileEntity> fileEntities = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {

            byte[] bytes;
            try {
                bytes = files.get(i).getBytes();
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new RuntimeException("storage.upload.error");
            }

            var mimeType = files.get(i).getContentType() != null ? files.get(i).getContentType() : this.tika.detect(bytes);

            if (mimeType == null) {
                throw new BadRequestException("storage.upload.mime-type-unable-to-detect", null);
            }

//        FileExtension fileExtension = getFileExtensionByMimeType(mimeType);
            String originalFileName = files.get(i).getOriginalFilename();
            String fileName = FilenameUtils.getName(originalFileName);

            FileEntity fileEntity = new FileEntity();
            String bucket = "limma";
            if (minioUtil.bucketExists(bucket))
                minioUtil.makeBucket("limma");

            minioUtil.uploadFile(files.get(i).getInputStream(), bucket, fileName, files.get(i).getContentType());

            String publicUrl = params.getEndpoint() + "/" + params.getBucket() + "/" + fileName;

            fileEntity.setFileExtension(mimeType);
            fileEntity.setStorageUrl(publicUrl);
            fileEntity.setFilename(fileName);
            fileEntity.setSize(files.get(i).getSize());
            fileEntity.setFileType(files.get(i).getContentType());
            fileEntity.setBucket(bucket);
            fileEntities.add(fileEntity);
        }
        return repository.saveAll(fileEntities);
    }

    @Override
    public ByteArrayResource getFile(Long id) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        FileEntity fileEntity = repository.getById(id);
        if (fileEntity == null) {
            throw new EntityNotFoundException("Template File not found id: " + id);
        }

        String bucket = fileEntity.getBucket() != null ? fileEntity.getBucket() : params.getBucket();
        try (InputStream inputStream = minioUtil.downloadFile(bucket, fileEntity.getFilename())) {
            byte[] data = IOUtils.toByteArray(inputStream);
            return new ByteArrayResource(data);
        }
    }

    @Override
    public ByteArrayResource getFile(String filename) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String bucket = params.getBucket();
        InputStream inputStream = minioUtil.downloadFile(bucket, filename);
        byte[] data = IOUtils.toByteArray(inputStream);
        return new ByteArrayResource(data);
    }

    private FileExtension getFileExtensionByMimeType(String mimeType) {
        Supplier<FileExtension> exception = () -> {
            throw new BadRequestException("file.unacceptable_extension", new String[]{mimeType});
        };
        if (allowedMimeTypes.contains(mimeType)) {

            System.out.println(mimeType);
            switch (mimeType) {
                case "application/pdf":
                    return FileExtension.PDF;
                case "image/jpeg":
                    return FileExtension.JPG;
                case "image/png":
                    return FileExtension.PNG;
                case "image/svg+xml":
                    return FileExtension.SVG;
                case "application/xml":
                case "text/xml":
                    return FileExtension.XML;
                default:
                    return exception.get();
            }
        } else {
            return exception.get();
        }
    }
}
