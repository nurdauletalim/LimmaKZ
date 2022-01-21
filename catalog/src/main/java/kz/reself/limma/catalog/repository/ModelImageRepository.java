package kz.reself.limma.catalog.repository;

import kz.reself.limma.catalog.model.ModelImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelImageRepository extends JpaRepository<ModelImage, Integer> {
    ModelImage getById(Integer id);
    List<ModelImage> findAllByIdIn(List<Integer> imageIds);
    List<ModelImage> findAllByModelId(Integer id);

}
