package kz.reself.limma.filestorage.util;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.ZonedDateTime;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public abstract class BaseEntity<T> extends BaseIdEntity<T> {

    @Version
    @Builder.Default
    protected Short version = 0;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @Builder.Default
    protected ZonedDateTime createdAt = ZonedDateTime.now();

    @UpdateTimestamp
    @Column(nullable = false)
    @Builder.Default
    protected ZonedDateTime updatedAt = ZonedDateTime.now();

    protected ZonedDateTime deletedAt;
}
