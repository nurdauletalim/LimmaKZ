package kz.reself.limma.catalog.service;

import kz.reself.limma.catalog.model.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrganizationService {
    Page<Organization> getOrganizations(Pageable pageable);
    Page<Organization> findAllSearchString(String searchString, Pageable pageable);

    List<Organization> getOrganizationsIterable();

    Organization getOrganizationById(Integer id);

    Organization addOrganization(Organization organization);

    void deleteOrganization(Organization organization);

    Organization updateOrganization(Organization organization);

    void deleteOrganizationById(Integer id);
}
