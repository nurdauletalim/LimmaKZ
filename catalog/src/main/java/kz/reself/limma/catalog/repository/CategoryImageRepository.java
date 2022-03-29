package kz.reself.limma.catalog.repository;

import kz.reself.limma.catalog.model.CategoryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryImageRepository extends JpaRepository<CategoryImage, Integer> {
    CategoryImage findByCategoryId(Integer categoryId);
    List<CategoryImage> findAllByCategoryId(Integer id);
    @Query(value = "SELECT * FROM category_image where category_id in " +
            "(select id from category where parent_category_id in " +
            "(select id from category where parent_category_id = ?1))", nativeQuery = true)
    List<CategoryImage> findAllByParentId(Integer parentId);
}
