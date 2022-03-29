package kz.reself.limma.catalog.repository;

import kz.reself.limma.catalog.model.Category;
import kz.reself.limma.catalog.model.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category getById(Integer id);

    List<Category> getAllByState(State state);

    Page<Category> findAllByState(State state, Pageable pageable);

    @Query(value = "Select * FROM category WHERE state = 0 and LOWER(name) like %?1%", nativeQuery = true)
    Page<Category> findAllSearchString(String searchString, Pageable pageable);

    @Query(value = "Select * FROM category WHERE state = ?1", nativeQuery = true)
    Page<Category> state(Integer state, Pageable pageable);

    @Query(value = "Select * FROM category WHERE parent_category_id IS NULL", nativeQuery = true)
    List<Category> getGrandparents();

    @Query(value = "Select * FROM category WHERE parent_category_id = ?1", nativeQuery = true)
    List<Category> getParents(Integer id);

    @Query(value = "Select * FROM category WHERE parent_category_id = ?1", nativeQuery = true)
    List<Category> getChildren(Integer id);
}
