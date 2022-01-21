package kz.reself.limma.filestorage.mapper;


import kz.reself.limma.filestorage.model.FileEntity;
import kz.reself.limma.filestorage.v1.dto.FileInfo;

public class FileMapper {
    public static FileInfo toFileInfo(FileEntity fileEntity) {
        return new FileInfo()
                .name(fileEntity.getFilename())
                .size(fileEntity.getSize())
                .storageUrl(fileEntity.getStorageUrl())
                .fileExtension(fileEntity.getFileExtension());
    }
}
