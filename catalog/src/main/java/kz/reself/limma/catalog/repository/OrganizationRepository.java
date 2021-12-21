package kz.reself.limma.catalog.repository;

import kz.reself.limma.catalog.model.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
    Organization getById(Integer id);
    @Query(value = "Select * FROM organization WHERE LOWER(name) like %?1%", nativeQuery = true)
    Page<Organization> findAllSearchString(String searchString, Pageable pageable);
}
