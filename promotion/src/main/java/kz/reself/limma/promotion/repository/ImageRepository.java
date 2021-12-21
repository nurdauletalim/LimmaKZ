package kz.reself.limma.promotion.repository;

import kz.reself.limma.promotion.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    Image getByIdAndIdIsNotNull(Integer id);

    List<Image> findAllByIdIn(List<Integer> imageIds);

    List<Image> findAllByProductId(Integer id);

    Integer countAllByProductId(Integer productId);

}
