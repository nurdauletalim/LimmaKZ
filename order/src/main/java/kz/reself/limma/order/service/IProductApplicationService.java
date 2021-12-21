package kz.reself.limma.order.service;

import kz.reself.limma.order.model.ApplicationDTO;
import kz.reself.limma.order.model.ProductApplication;
import kz.reself.limma.order.model.ProductApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductApplicationService {
    Page<ProductApplication> getAllApplicationPageable(Pageable pageable);

    List<ProductApplication> getAllApplicationsIterable();

    ProductApplication getProductApplicationById(Integer id);

    List<ProductApplication> getApplicationsByOrganizationId(Integer id);

    Page<ProductApplication> getAllProductId(Integer productId, Pageable pageableRequest);

    ProductApplication createApplication(ProductApplication productApplication);

    ProductApplication updateApplication(ProductApplication productApplication);

    void deleteApplication(ProductApplication productApplication);

    void deleteApplicationById(Integer id);

    ApplicationDTO updateApplicationDTO(ApplicationDTO applicationDTO);

    ApplicationDTO getDTOById(Integer id);

    List<ApplicationDTO> getAllDTObyOrgId(Integer id);

    Page<ApplicationDTO> getAllDTObyOrgIdPageable(Integer id, Pageable pageable);

    List<ApplicationDTO> findAllDTOSIterable();

    Page<ApplicationDTO> findAllDTOSPageable(Pageable pageable);

    Page<ApplicationDTO> findAllDTOSByProductPageable(Integer productId, Pageable pageable);

    Page<ApplicationDTO> findAllDTOByProductAndManagerPageable(Integer productId, Integer managerId, Pageable pageable);

    List<ProductApplication> getApplicationsByContacts(String contact);

    Page<ApplicationDTO> getAllByStatusPageable(ProductApplicationStatus status, Pageable pageable);

    Page<ApplicationDTO> getByStatusAndOrganizationPageable(Integer status, Integer orgId, Pageable pageable);

    Page<ApplicationDTO> findAllDTOSearchString(String searchString, Pageable pageableRequest);

    Page<ApplicationDTO> findAllDTOStatus(Integer status, Pageable pageable);

    Page<ApplicationDTO> findAllDTOOrgIdSearchString(Integer id, String searchString, Pageable pageableRequest);

    Integer getAmountApplicationByOrganizationId(Integer organizationId, String interval);

    Boolean checkApplication(String name,String contact, Integer productId);
}
