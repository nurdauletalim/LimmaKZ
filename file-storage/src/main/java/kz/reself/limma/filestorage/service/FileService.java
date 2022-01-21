package kz.reself.limma.filestorage.service;


import kz.reself.limma.filestorage.enam.FileExtension;
import kz.reself.limma.filestorage.model.FileEntity;
import kz.reself.limma.filestorage.v1.dto.FileInfo;

public interface FileService {
    void saveFile(FileEntity file);

    FileInfo getFileInfo(Long fileId);

    FileExtension getFileExtensionByMimeType(String mimeType);
}
