package kz.reself.limma.catalog.service.impl;

import kz.reself.limma.catalog.model.Organization;
import kz.reself.limma.catalog.repository.OrganizationRepository;
import kz.reself.limma.catalog.service.IOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService implements IOrganizationService {
    @Autowired
    OrganizationRepository organizationRepository;

    @Override
    public Page<Organization> getOrganizations(Pageable pageable) {
        return organizationRepository.findAll(pageable);
    }

    @Override
    public Page<Organization> findAllSearchString(String searchString, Pageable pageable) {
        return organizationRepository.findAllSearchString(searchString, pageable);
    }

    @Override
    public List<Organization> getOrganizationsIterable() {
        return organizationRepository.findAll();
    }

    @Override
    public Organization getOrganizationById(Integer id) {
        return organizationRepository.getById(id);
    }

    @Override
    public Organization addOrganization(Organization organization) {
        return organizationRepository.saveAndFlush(organization);
    }

    @Override
    public void deleteOrganization(Organization organization) {
        organizationRepository.delete(organization);
    }

    @Override
    public Organization updateOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    @Override
    public void deleteOrganizationById(Integer id) {
        organizationRepository.delete(organizationRepository.getById(id));
    }
}
