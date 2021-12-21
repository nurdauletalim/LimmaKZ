package kz.reself.limma.filestorage.repository;

import kz.reself.limma.filestorage.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    Image getById(Integer id);
    List<Image> findAllByIdIn(List<Integer> imageIds);
    List<Image> findAllByProductId(Integer id);

    Integer countAllByProductId(Integer productId);

}
