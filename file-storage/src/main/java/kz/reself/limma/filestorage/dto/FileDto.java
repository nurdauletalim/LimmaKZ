package kz.reself.limma.filestorage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
    private UUID id;
    private String storageUrl;
    private long size;
    private String name;
    private String fileType;
//    private FileExtension fileExtension;
    private String bucket;
    private String versionId;
    private String uploadUser;
//    private FileStatus status;
}
