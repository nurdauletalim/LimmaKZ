package kz.reself.limma.filestorage.repository;

import kz.reself.limma.filestorage.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    FileEntity getById(Long id);
}
