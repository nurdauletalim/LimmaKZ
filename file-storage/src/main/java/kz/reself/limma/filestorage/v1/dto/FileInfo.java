package kz.reself.limma.filestorage.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * FileInfo
 */
public class FileInfo {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("storageUrl")
    private String storageUrl;

    @JsonProperty("name")
    private String name;

    @JsonProperty("size")
    private Long size;

    @JsonProperty("fileExtension")
    private String fileExtension;

    @JsonProperty("createdAt")
    @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime createdAt;

    public FileInfo id(UUID id) {
        this.id = id;
        return this;
    }

    /**
     * File's identifier
     *
     * @return id
     */
    @ApiModelProperty(example = "3afe5b11-152c-46de-963b-8655dbc8602b", value = "File's identifier")

    @Valid

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public FileInfo storageUrl(String storageUrl) {
        this.storageUrl = storageUrl;
        return this;
    }

    /**
     * File's url in storage
     *
     * @return storageUrl
     */
    @ApiModelProperty(example = "http://minio.dev.cef.kz/testbacket/img/3afe5b11-152c-46de-963b-8655dbc8602b.png", value = "File's url in storage")


    public String getStorageUrl() {
        return storageUrl;
    }

    public void setStorageUrl(String storageUrl) {
        this.storageUrl = storageUrl;
    }

    public FileInfo name(String name) {
        this.name = name;
        return this;
    }

    /**
     * File name
     *
     * @return name
     */
    @ApiModelProperty(example = "filename.png", value = "File name")


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileInfo size(Long size) {
        this.size = size;
        return this;
    }

    /**
     * File size in bytes
     * minimum: 0
     *
     * @return size
     */
    @ApiModelProperty(example = "123456", value = "File size in bytes")

    @Min(0L)
    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public FileInfo fileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
        return this;
    }

    /**
     * Get fileExtension
     *
     * @return fileExtension
     */
    @ApiModelProperty(value = "")

    @Valid

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public FileInfo createdAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * File upload time in 'YYYY-MM-DDTHH:MM:SSZ' format
     *
     * @return createdAt
     */
    @ApiModelProperty(example = "2021-04-27T10:21:53Z", value = "File upload time in 'YYYY-MM-DDTHH:MM:SSZ' format")

    @Valid

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FileInfo fileInfo = (FileInfo) o;
        return Objects.equals(this.id, fileInfo.id) &&
                Objects.equals(this.storageUrl, fileInfo.storageUrl) &&
                Objects.equals(this.name, fileInfo.name) &&
                Objects.equals(this.size, fileInfo.size) &&
                Objects.equals(this.fileExtension, fileInfo.fileExtension) &&
                Objects.equals(this.createdAt, fileInfo.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, storageUrl, name, size, fileExtension, createdAt);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FileInfo {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    storageUrl: ").append(toIndentedString(storageUrl)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    size: ").append(toIndentedString(size)).append("\n");
        sb.append("    fileExtension: ").append(toIndentedString(fileExtension)).append("\n");
        sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

