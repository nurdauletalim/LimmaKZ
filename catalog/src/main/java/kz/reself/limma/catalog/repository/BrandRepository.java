package kz.reself.limma.catalog.repository;

import kz.reself.limma.catalog.model.Brand;
import kz.reself.limma.catalog.model.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand,Integer> {
    Brand getByIdAndState(Integer id, State state);
    List<Brand> getAllByCategory_IdAndState(Integer categoryId, State state);
    List<Brand> getAllByState(State state);
    void deleteById(Integer id);
    Brand getByCategoryIdAndDisplayNameAndState(Integer categoryId, String s, State state);
    @Query(value = "Select * FROM brand WHERE state = 0 and LOWER(name) like %?1% ", nativeQuery = true)
    Page<Brand> findAllPageableSearchString(String searchString, Pageable pageableRequest);
    @Query(value = "Select * FROM brand WHERE state = 0 ", nativeQuery = true)
    Page<Brand> findAllPageable(Pageable pageableRequest);
}
