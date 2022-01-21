package kz.reself.limma.filestorage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kz.reself.limma.filestorage.util.EnumTypePostgreSql;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.sql.Timestamp;

@TypeDefs({
        @TypeDef(name = "enum_postgre", typeClass = EnumTypePostgreSql.class)
})
@Data
@Entity
@Table(name = "_FILE")
@EqualsAndHashCode()
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    @JsonIgnore
    protected Timestamp createdAt;

    @Column(name = "created_by", updatable = false, nullable = false)
    @JsonIgnore
    protected String createdBy;

    @Column(name = "updated_at")
    @JsonIgnore
    protected Timestamp updatedAt;

    @Column(name = "updated_by")
    @JsonIgnore
    protected String updatedBy;

    @Column(name = "deleted_at")
    @JsonIgnore
    protected Timestamp deletedAt;

    @Column(name = "deleted_by")
    @JsonIgnore
    protected String deletedBy;

    @Column(name = "file_name")
    private String filename;

    @Column(name = "file_type")
    private String fileType;

    @Column
    private String bucket;

    @Column(name = "storage_url")
    private String storageUrl;

    @Column
    private Long size;

    @Column(name = "file_extension")
    private String fileExtension;

}
