package kz.reself.limma.catalog.repository;

import kz.reself.limma.catalog.model.Model;
import kz.reself.limma.catalog.model.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<Model,Integer> {
    Model getByIdAndState(Integer id, State state);
    Model getByDisplayNameAndState(String value, State state);

    @Query(value = "select * from model where state = 0 and id = (select model_id from product where id = ?1)", nativeQuery = true)
    Model getByProductId(Integer productId);
    List<Model> findAllByBrandIdAndState(Integer id, State state);
    @Query(value = "select id,display_name from model where state = 0 and brand_id = ?1", nativeQuery = true)
    List<Object[]> findAllDTOByBrandIdAndState(Integer id, State state);
    List<Model> findAllByBrandId(Integer id);
    List<Model> getAllByState(State state);
    Page<Model> findAllByState(Pageable pageable, State state);

    @Query(value = "SELECT * FROM model WHERE state = 0 and LOWER(display_name) like %?1%", nativeQuery = true)
    Page<Model> findAllByName(String searchString, Pageable pageable);

    @Query(value = "SELECT * FROM model WHERE state = 0 and LOWER(display_name) like %?1% and brand_id = ?2", nativeQuery = true)
    Page<Model> findAllByNameAndBrandId(String searchString,Integer brandId, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "UPDATE model SET state = 1 where brand_id = ?1 and state = 0", nativeQuery = true)
    void deactiveAllModelsByBrandId(Integer brandId);

    @Query(value = "SELECT * FROM model WHERE state = 0 and brand_id = ?1", nativeQuery = true)
    Page<Model> findAllByBrandIdAndStatePageable(Integer id, State active, Pageable pageableRequest);
}
