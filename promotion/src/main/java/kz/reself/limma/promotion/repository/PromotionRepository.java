package kz.reself.limma.promotion.repository;

import kz.reself.limma.promotion.model.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    Promotion getByIdAndIdIsNotNull(Integer id);
//    List<Promotion> getAllByOrganization(Organization organization);
    //    @Query(value = "Select * FROM promotion WHERE (published_date <= ?1 and expiration_date >= ?1) and state = true", nativeQuery = true)
    @Query(value = "Select * FROM promotion WHERE expiration_date >= ?1 and state = true", nativeQuery = true)
    List<Promotion> getAllActive(Timestamp timestamp);

    @Query(value = "Select * FROM promotion WHERE (published_date <= ?1 and expiration_date >= ?1) and state = true limit ?2", nativeQuery = true)
    List<Promotion> getAllActiveCount(Timestamp timestamp,Integer count);

    @Query(value = "Select promotion.id FROM promotion WHERE (published_date <= ?1 and expiration_date >= ?1) and state = true limit ?2", nativeQuery = true)
    List<Object[]> getAllActiveDTO(Timestamp timestamp, Integer count);

    @Query(value = "Select * FROM promotion", nativeQuery = true)
    Page<Promotion> getAllPageable(Pageable pageable);

    @Query(value = "Select * FROM promotion WHERE LOWER(title) like %?1% or lower(description) like %?1%", nativeQuery = true)
    Page<Promotion> findAllPageableSearchString(String searchString, Pageable pageableRequest);

}
