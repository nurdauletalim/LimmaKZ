package kz.reself.limma.filestorage.service.impl;

import kz.reself.limma.filestorage.enam.FileExtension;
import kz.reself.limma.filestorage.exception.BadRequestException;
import kz.reself.limma.filestorage.exception.ResourceNotFoundException;
import kz.reself.limma.filestorage.mapper.FileMapper;
import kz.reself.limma.filestorage.model.FileEntity;
import kz.reself.limma.filestorage.repository.FileRepository;
import kz.reself.limma.filestorage.service.FileService;
import kz.reself.limma.filestorage.v1.dto.FileInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Supplier;

@Service
@Log
@Transactional
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Value("${file.allowedMimeTypes}")
    private List<String> allowedMimeTypes;

    @Override
    public void saveFile(FileEntity file) {
        fileRepository.save(file);
    }

    @Override
    public FileInfo getFileInfo(Long fileId) {
        if (fileId != null) {
            FileEntity fileEntity = fileRepository.findById(fileId)
                    .orElseThrow(() -> new ResourceNotFoundException("file.not_found", new String[]{fileId.toString()}));
            return FileMapper.toFileInfo(fileEntity);
        } else {
            return new FileInfo();
        }
    }

    @Override
    public FileExtension getFileExtensionByMimeType(String mimeType) {
        Supplier<FileExtension> exception = () -> {
            throw new BadRequestException("file.unacceptable_extension", new String[]{mimeType});
        };
        if (allowedMimeTypes.contains(mimeType)) {
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
