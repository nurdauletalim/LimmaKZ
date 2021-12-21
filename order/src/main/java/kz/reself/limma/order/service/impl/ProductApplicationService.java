package kz.reself.limma.order.service.impl;

import kz.reself.limma.order.model.*;
import kz.reself.limma.order.repository.ProductApplicationRepository;
import kz.reself.limma.order.repository.ProductRepository;
import kz.reself.limma.order.service.IProductApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class ProductApplicationService implements IProductApplicationService {

    @Autowired
    ProductApplicationRepository productApplicationRepository;

    @Autowired
    ProductRepository productRepository;

//    @Autowired
//    private LimmaBot limmaBot;

    @Override
    public Page<ProductApplication> getAllApplicationPageable(Pageable pageable) {
        return productApplicationRepository.findAll(pageable);
    }

    @Override
    public List<ProductApplication> getAllApplicationsIterable() {
        return productApplicationRepository.findAll();
    }

    @Override
    public ProductApplication getProductApplicationById(Integer id) {
        return productApplicationRepository.getById(id);
    }

    @Override
    public List<ProductApplication> getApplicationsByOrganizationId(Integer id) {
        return productApplicationRepository.getAllByProductOrganizationId(id);
    }

    @Override
    public Page<ProductApplication> getAllProductId(Integer productId, Pageable pageableRequest) {
        return productApplicationRepository.getAllByProductId(productId, pageableRequest);
    }

    @Override
    public ProductApplication createApplication(ProductApplication productApplication) {
        if (productApplication.getContact().matches("([+][7])[ ][7][0-9]{2}[ ][0-9]{3}[ ][0-9]{2}[ ][0-9]{2}")
                && productApplication.getName().matches("^(?![\\s.]+$)[a-zA-Z\\s.]*(?![\\s.]+$)[а-яА-Я\\s.]*") && productApplicationRepository.findProductApplicationByProductIdAndStatus(productApplication.getProductId(), ProductApplicationStatus.NEW) == null) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            productApplication.setStatus(ProductApplicationStatus.NEW);
            productApplication.setRegistered(timestamp);
            productApplication.setProduct(productRepository.getById(productApplication.getProductId()));
            productApplication.getProduct().setState(State.BOOKED);
            productApplication.setClosed(null);
            productApplication.setTaken(null);
            productApplication.setManagerId(null);
            // TODO get from account service
//            List<Account> accounts = accountService.getAccountsIterable();
//            for (Account account : accounts) {
//                if (account.getChatId() != null)
//                    limmaBot.sendEvent(account.getChatId(), productApplication.toString());
//            }
            return productApplicationRepository.saveAndFlush(productApplication);
        } else {
            return null;
        }

    }

    @Override
    public ProductApplication updateApplication(ProductApplication productApplication) {
        return productApplicationRepository.save(productApplication);
    }

    @Override
    public void deleteApplication(ProductApplication productApplication) {
        productApplicationRepository.delete(productApplication);
    }

    @Override
    public void deleteApplicationById(Integer id) {
        ProductApplication application = productApplicationRepository.getById(id);
        if (application != null) {
            Product product = productRepository.getById(application.getProductId());
            if (product != null) {
                if (product.getState() == State.BOOKED) {
                    product.setState(State.ACTIVE);
                    productRepository.saveAndFlush(product);
                }
            }
        }
        productApplicationRepository.delete(getProductApplicationById(id));
    }

    @Override
    public ApplicationDTO updateApplicationDTO(ApplicationDTO applicationDTO) {
        ProductApplication productApplication = new ProductApplication();
        productApplication.setAccount(applicationDTO.getAccount());
        productApplication.setContact(applicationDTO.getContact());
        productApplication.setComment(applicationDTO.getComment());
        productApplication.setClosed(applicationDTO.getClosed());
        productApplication.setId(applicationDTO.getId());
        productApplication.setRegistered(productApplicationRepository.getById(applicationDTO.getId()).getRegistered());
        productApplication.setManagerId(applicationDTO.getManagerId());
        productApplication.setEmail(applicationDTO.getEmail());
        productApplication.setProduct(applicationDTO.getProduct());
        productApplication.setProductId(applicationDTO.getProductId());
        productApplication.setStatus(applicationDTO.getStatus());
        productApplication.setTaken(applicationDTO.getTaken());
        productApplication.setName(applicationDTO.getName());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if (applicationDTO.getStatus().equals(ProductApplicationStatus.DONE)) {
            Product product = productRepository.getById(applicationDTO.getProductId());
            productApplication.setClosed(timestamp);
            product.setState(State.SOLD);
            productRepository.save(product);
        } else if (applicationDTO.getStatus().equals(ProductApplicationStatus.REJECTED)) {
            Product product = productRepository.getById(applicationDTO.getProductId());
            product.setState(State.ACTIVE);
            productRepository.save(product);
        } else if (applicationDTO.getStatus().equals(ProductApplicationStatus.IN_PROGRESS)) {
            productApplication.setTaken(timestamp);
        }
        productApplicationRepository.save(productApplication);
        return getDTOById(productApplication.getId());
    }

    @Override
    public ApplicationDTO getDTOById(Integer id) {
        ProductApplication productApplication = productApplicationRepository.getById(id);
        ApplicationDTO applicationDTO = new ApplicationDTO();
        if (applicationDTO.getAccount() != null)
            applicationDTO.setAccount(productApplication.getAccount());
        if (productApplication.getContact() != null)
            applicationDTO.setContact(productApplication.getContact());
        else
            applicationDTO.setContact(null);
        applicationDTO.setComment(productApplication.getComment());
        applicationDTO.setClosed(productApplication.getClosed());
        applicationDTO.setRegistered(productApplication.getRegistered());
        //  //TODO get from Minio Service with Kafka
//        if (imageService.getAllImageByProductId(productApplication.getProductId()).size() > 0) {
//            applicationDTO.setImage(imageService.getAllImageByProductId(productApplication.getProductId()).get(0).getData());
//        }
        applicationDTO.setId(productApplication.getId());
        applicationDTO.setManagerId(productApplication.getManagerId());
        applicationDTO.setEmail(productApplication.getEmail());
        applicationDTO.setProduct(productApplication.getProduct());
        applicationDTO.setProductId(productApplication.getProductId());
        applicationDTO.setStatus(productApplication.getStatus());
        applicationDTO.setTaken(productApplication.getTaken());
        applicationDTO.setName(productApplication.getName());
        return applicationDTO;
    }

    @Override
    public List<ApplicationDTO> getAllDTObyOrgId(Integer id) {
        List<ProductApplication> productApplications = productApplicationRepository.getAllByProductOrganizationId(id);
        List<ApplicationDTO> applicationDTOS = new ArrayList<>();
        for (ProductApplication productApplication : productApplications) {
            ApplicationDTO applicationDTO = new ApplicationDTO();
            applicationDTO.setAccount(productApplication.getAccount());
            applicationDTO.setContact(productApplication.getContact());
            applicationDTO.setComment(productApplication.getComment());
            applicationDTO.setClosed(productApplication.getClosed());
            applicationDTO.setRegistered(productApplication.getRegistered());
            //  //TODO get from Minio Service with Kafka
//            if (imageService.getAllImageByProductId(productApplication.getProductId()).size() > 0) {
//                applicationDTO.setImage(imageService.getAllImageByProductId(productApplication.getProductId()).get(0).getData());
//            }
            applicationDTO.setId(productApplication.getId());
            applicationDTO.setManagerId(productApplication.getManagerId());
            applicationDTO.setEmail(productApplication.getEmail());
            applicationDTO.setProduct(productApplication.getProduct());
            applicationDTO.setStatus(productApplication.getStatus());
            applicationDTO.setTaken(productApplication.getTaken());
            applicationDTO.setName(productApplication.getName());
            applicationDTOS.add(applicationDTO);
        }
        return applicationDTOS;
    }

    @Override
    public Page<ApplicationDTO> getAllDTObyOrgIdPageable(Integer id, Pageable pageable) {

        Page<ProductApplication> productApplicationsPageable = productApplicationRepository.getAllByProductOrganizationIdPageable(id, pageable);

        Page<ApplicationDTO> applicationDTOPageable = productApplicationsPageable.map(new Function<ProductApplication, ApplicationDTO>() {
            @Override
            public ApplicationDTO apply(ProductApplication productApplication) {
                ApplicationDTO applicationDTO = new ApplicationDTO();
                applicationDTO.setAccount(productApplication.getAccount());
                applicationDTO.setContact(productApplication.getContact());
                applicationDTO.setComment(productApplication.getComment());
                applicationDTO.setClosed(productApplication.getClosed());
                applicationDTO.setRegistered(productApplication.getRegistered());
//                //TODO get from Minio Service with Kafka
//                if (imageService.getAllImageByProductId(productApplication.getProductId()).size() > 0) {
//                    applicationDTO.setImage(imageService.getAllImageByProductId(productApplication.getProductId()).get(0).getData());
//                }
                applicationDTO.setId(productApplication.getId());
                applicationDTO.setManagerId(productApplication.getManagerId());
                applicationDTO.setEmail(productApplication.getEmail());
                applicationDTO.setProduct(productApplication.getProduct());
                applicationDTO.setStatus(productApplication.getStatus());
                applicationDTO.setTaken(productApplication.getTaken());
                applicationDTO.setName(productApplication.getName());

                return applicationDTO;
            }
        });

        return applicationDTOPageable;
    }

    @Override
    public List<ApplicationDTO> findAllDTOSIterable() {
        List<ProductApplication> productApplications = productApplicationRepository.findAll();
        List<ApplicationDTO> applicationDTOS = new ArrayList<>();
        for (ProductApplication productApplication : productApplications) {
            ApplicationDTO applicationDTO = new ApplicationDTO();
            applicationDTO.setAccount(productApplication.getAccount());
            applicationDTO.setContact(productApplication.getContact());
            applicationDTO.setComment(productApplication.getComment());
            applicationDTO.setClosed(productApplication.getClosed());
            applicationDTO.setRegistered(productApplication.getRegistered());
//           //TODO get from Minio Service with Kafka
//            if (imageService.getAllImageByProductId(productApplication.getProductId()).size() > 0) {
//                applicationDTO.setImage(imageService.getAllImageByProductId(productApplication.getProductId()).get(0).getData());
//            }
            applicationDTO.setId(productApplication.getId());
            applicationDTO.setManagerId(productApplication.getManagerId());
            applicationDTO.setEmail(productApplication.getEmail());
            applicationDTO.setProduct(productApplication.getProduct());
            applicationDTO.setStatus(productApplication.getStatus());
            applicationDTO.setTaken(productApplication.getTaken());
            applicationDTO.setName(productApplication.getName());
            applicationDTOS.add(applicationDTO);
        }
        return applicationDTOS;
    }

    @Override
    public Page<ApplicationDTO> findAllDTOSPageable(Pageable pageable) {

        Page<ProductApplication> productApplicationsPageable = productApplicationRepository.findAll(pageable);

        Page<ApplicationDTO> applicationDTOPageable = productApplicationsPageable.map(new Function<ProductApplication, ApplicationDTO>() {
            @Override
            public ApplicationDTO apply(ProductApplication productApplication) {
                ApplicationDTO applicationDTO = new ApplicationDTO();
                applicationDTO.setAccount(productApplication.getAccount());
                applicationDTO.setContact(productApplication.getContact());
                applicationDTO.setComment(productApplication.getComment());
                applicationDTO.setClosed(productApplication.getClosed());
                applicationDTO.setRegistered(productApplication.getRegistered());
//               // TODO get
//                if (imageService.getAllImageByProductId(productApplication.getProductId()).size() > 0) {
//                    applicationDTO.setImage(imageService.getAllImageByProductId(productApplication.getProductId()).get(0).getData());
//                }
                applicationDTO.setId(productApplication.getId());
                if (productApplication.getAccount() != null) {
                    applicationDTO.setManagerId(productApplication.getAccount().getId());
                }
                applicationDTO.setEmail(productApplication.getEmail());
                applicationDTO.setProduct(productApplication.getProduct());
                applicationDTO.setProductId(productApplication.getProduct().getId());
                applicationDTO.setStatus(productApplication.getStatus());
                applicationDTO.setTaken(productApplication.getTaken());
                applicationDTO.setName(productApplication.getName());

                return applicationDTO;
            }
        });

        return applicationDTOPageable;
    }

    @Override
    public Page<ApplicationDTO> findAllDTOSByProductPageable(Integer productId, Pageable pageable) {
        Page<ProductApplication> productApplicationsPageable = productApplicationRepository.getAllByProductId(productId, pageable);

        Page<ApplicationDTO> applicationDTOPageable = productApplicationsPageable.map(new Function<ProductApplication, ApplicationDTO>() {
            @Override
            public ApplicationDTO apply(ProductApplication productApplication) {
                ApplicationDTO applicationDTO = new ApplicationDTO();
                applicationDTO.setAccount(productApplication.getAccount());
                applicationDTO.setContact(productApplication.getContact());
                applicationDTO.setComment(productApplication.getComment());
                applicationDTO.setClosed(productApplication.getClosed());
                applicationDTO.setRegistered(productApplication.getRegistered());
//                //TODO get from Minio Service with Kafka
//                if (imageService.getAllImageByProductId(productApplication.getProductId()).size() > 0) {
//                    applicationDTO.setImage(imageService.getAllImageByProductId(productApplication.getProductId()).get(0).getData());
//                }
                applicationDTO.setId(productApplication.getId());
                applicationDTO.setManagerId(productApplication.getManagerId());
                applicationDTO.setEmail(productApplication.getEmail());
                applicationDTO.setProduct(productApplication.getProduct());
                applicationDTO.setStatus(productApplication.getStatus());
                applicationDTO.setTaken(productApplication.getTaken());
                applicationDTO.setName(productApplication.getName());

                return applicationDTO;
            }
        });

        return applicationDTOPageable;
    }

    @Override
    public Page<ApplicationDTO> findAllDTOByProductAndManagerPageable(Integer productId, Integer managerId, Pageable pageable) {
        Page<ProductApplication> productApplicationsPageable = productApplicationRepository.getAllByProductIdAndManagerId(productId, managerId, pageable);

        Page<ApplicationDTO> applicationDTOPageable = productApplicationsPageable.map(new Function<ProductApplication, ApplicationDTO>() {
            @Override
            public ApplicationDTO apply(ProductApplication productApplication) {
                ApplicationDTO applicationDTO = new ApplicationDTO();
                applicationDTO.setAccount(productApplication.getAccount());
                applicationDTO.setContact(productApplication.getContact());
                applicationDTO.setComment(productApplication.getComment());
                applicationDTO.setClosed(productApplication.getClosed());
                applicationDTO.setRegistered(productApplication.getRegistered());
//                //TODO get from Minio Service with Kafka
//                if (imageService.getAllImageByProductId(productApplication.getProductId()).size() > 0) {
//                    applicationDTO.setImage(imageService.getAllImageByProductId(productApplication.getProductId()).get(0).getData());
//                }
                applicationDTO.setId(productApplication.getId());
                applicationDTO.setManagerId(productApplication.getManagerId());
                applicationDTO.setEmail(productApplication.getEmail());
                applicationDTO.setProduct(productApplication.getProduct());
                applicationDTO.setStatus(productApplication.getStatus());
                applicationDTO.setTaken(productApplication.getTaken());
                applicationDTO.setName(productApplication.getName());

                return applicationDTO;
            }
        });

        return applicationDTOPageable;
    }

    @Override
    public Page<ApplicationDTO> findAllDTOSearchString(String searchString, Pageable pageableRequest) {
        List<ProductApplication> productApplications = productApplicationRepository.findAllSearchString(searchString);
        List<ApplicationDTO> applicationDTOS = new ArrayList<>();
        int start = (int) pageableRequest.getOffset();
        int end = Math.min((start + pageableRequest.getPageSize()), productApplications.size());

        for (ProductApplication productApplication : productApplications.subList(start, end)) {
            ApplicationDTO applicationDTO = new ApplicationDTO();
            applicationDTO.setAccount(productApplication.getAccount());
            applicationDTO.setContact(productApplication.getContact());
            applicationDTO.setComment(productApplication.getComment());
            applicationDTO.setClosed(productApplication.getClosed());
            applicationDTO.setRegistered(productApplication.getRegistered());
//            //TODO get from Minio Service with Kafka
//            if (imageService.getAllImageByProductId(productApplication.getProductId()).size() > 0) {
//                applicationDTO.setImage(imageService.getAllImageByProductId(productApplication.getProductId()).get(0).getData());
//            }
            applicationDTO.setId(productApplication.getId());
            applicationDTO.setManagerId(productApplication.getManagerId());
            applicationDTO.setEmail(productApplication.getEmail());
            applicationDTO.setProduct(productApplication.getProduct());
            applicationDTO.setStatus(productApplication.getStatus());
            applicationDTO.setTaken(productApplication.getTaken());
            applicationDTO.setName(productApplication.getName());
            applicationDTOS.add(applicationDTO);
        }

        return new PageImpl<ApplicationDTO>(applicationDTOS);
    }

    @Override
    public List<ProductApplication> getApplicationsByContacts(String contact) {
        return productApplicationRepository.getAllByContact(contact);
    }

    @Override
    public Page<ApplicationDTO> getAllByStatusPageable(ProductApplicationStatus status, Pageable pageable) {
        List<ProductApplication> productApplications = productApplicationRepository.getAllByStatus(status);
        Collections.sort(productApplications, Collections.reverseOrder());
        List<ApplicationDTO> applicationDTOS = new ArrayList<>();
        for (ProductApplication productApplication : productApplications) {
            ApplicationDTO applicationDTO = new ApplicationDTO();
            applicationDTO.setAccount(productApplication.getAccount());
            applicationDTO.setContact(productApplication.getContact());
            applicationDTO.setComment(productApplication.getComment());
            applicationDTO.setClosed(productApplication.getClosed());
            applicationDTO.setRegistered(productApplication.getRegistered());
//           //TODO get from Minio Service with Kafka -> getAllImageByProductId
//            if (imageService.getAllImageByProductId(productApplication.getProductId()).size() > 0) {
//                applicationDTO.setImage(imageService.getAllImageByProductId(productApplication.getProductId()).get(0).getData());
//            }
            applicationDTO.setId(productApplication.getId());
            applicationDTO.setManagerId(productApplication.getManagerId());
            applicationDTO.setEmail(productApplication.getEmail());
            applicationDTO.setProduct(productApplication.getProduct());
            applicationDTO.setStatus(productApplication.getStatus());
            applicationDTO.setTaken(productApplication.getTaken());
            applicationDTO.setName(productApplication.getName());
            applicationDTOS.add(applicationDTO);
        }
        return new PageImpl<>(applicationDTOS);
    }

    @Override
    public Page<ApplicationDTO> getByStatusAndOrganizationPageable(Integer status, Integer orgId, Pageable pageable) {
        Page<ProductApplication> productApplicationsPageable = productApplicationRepository.getAllByProductOrganizationIdAndStatus(orgId, status, pageable);

        Page<ApplicationDTO> applicationDTOPageable = productApplicationsPageable.map(new Function<ProductApplication, ApplicationDTO>() {
            @Override
            public ApplicationDTO apply(ProductApplication productApplication) {
                ApplicationDTO applicationDTO = new ApplicationDTO();
                applicationDTO.setAccount(productApplication.getAccount());
                applicationDTO.setContact(productApplication.getContact());
                applicationDTO.setComment(productApplication.getComment());
                applicationDTO.setClosed(productApplication.getClosed());
                applicationDTO.setRegistered(productApplication.getRegistered());
//              //TODO get from Minio Service with Kafka -> getAllImageByProductId
//                if (imageService.getAllImageByProductId(productApplication.getProductId()).size() > 0) {
//                    applicationDTO.setImage(imageService.getAllImageByProductId(productApplication.getProductId()).get(0).getData());
//                }
                applicationDTO.setId(productApplication.getId());
                applicationDTO.setManagerId(productApplication.getManagerId());
                applicationDTO.setEmail(productApplication.getEmail());
                applicationDTO.setProduct(productApplication.getProduct());
                applicationDTO.setStatus(productApplication.getStatus());
                applicationDTO.setTaken(productApplication.getTaken());
                applicationDTO.setName(productApplication.getName());

                return applicationDTO;
            }
        });

        return applicationDTOPageable;
    }

    @Override
    public Page<ApplicationDTO> findAllDTOStatus(Integer status, Pageable pageable) {
        Page<ProductApplication> productApplicationsPageable = productApplicationRepository.allByStatus(status, pageable);

        Page<ApplicationDTO> applicationDTOPageable = productApplicationsPageable.map(new Function<ProductApplication, ApplicationDTO>() {
            @Override
            public ApplicationDTO apply(ProductApplication productApplication) {
                ApplicationDTO applicationDTO = new ApplicationDTO();
                applicationDTO.setAccount(productApplication.getAccount());
                applicationDTO.setContact(productApplication.getContact());
                applicationDTO.setComment(productApplication.getComment());
                applicationDTO.setClosed(productApplication.getClosed());
                applicationDTO.setRegistered(productApplication.getRegistered());
//               //TODO get from Minio Service with Kafka -> getAllImageByProductId
//                if (imageService.getAllImageByProductId(productApplication.getProductId()).size() > 0) {
//                    applicationDTO.setImage(imageService.getAllImageByProductId(productApplication.getProductId()).get(0).getData());
//                }
                applicationDTO.setId(productApplication.getId());
                applicationDTO.setManagerId(productApplication.getManagerId());
                applicationDTO.setEmail(productApplication.getEmail());
                applicationDTO.setProduct(productApplication.getProduct());
                applicationDTO.setStatus(productApplication.getStatus());
                applicationDTO.setTaken(productApplication.getTaken());
                applicationDTO.setName(productApplication.getName());

                return applicationDTO;
            }
        });

        return applicationDTOPageable;
    }

    @Override
    public Page<ApplicationDTO> findAllDTOOrgIdSearchString(Integer id, String searchString, Pageable pageableRequest) {
        Page<ProductApplication> productApplicationsPageable = productApplicationRepository.getAllByProductOrganizationIdSearchStringPageable(id, searchString, pageableRequest);

        Page<ApplicationDTO> applicationDTOPageable = productApplicationsPageable.map(new Function<ProductApplication, ApplicationDTO>() {
            @Override
            public ApplicationDTO apply(ProductApplication productApplication) {
                ApplicationDTO applicationDTO = new ApplicationDTO();
                applicationDTO.setAccount(productApplication.getAccount());
                applicationDTO.setContact(productApplication.getContact());
                applicationDTO.setComment(productApplication.getComment());
                applicationDTO.setClosed(productApplication.getClosed());
                applicationDTO.setRegistered(productApplication.getRegistered());
//               //TODO get from Minio Service with Kafka -> getAllImageByProductId
//                if (imageService.getAllImageByProductId(productApplication.getProductId()).size() > 0) {
//                    applicationDTO.setImage(imageService.getAllImageByProductId(productApplication.getProductId()).get(0).getData());
//                }
                applicationDTO.setId(productApplication.getId());
                applicationDTO.setManagerId(productApplication.getManagerId());
                applicationDTO.setEmail(productApplication.getEmail());
                applicationDTO.setProduct(productApplication.getProduct());
                applicationDTO.setStatus(productApplication.getStatus());
                applicationDTO.setTaken(productApplication.getTaken());
                applicationDTO.setName(productApplication.getName());

                return applicationDTO;
            }
        });

        return applicationDTOPageable;
    }

    @Override
    public Integer getAmountApplicationByOrganizationId(Integer organizationId, String interval) {
        if (interval.equalsIgnoreCase("all")) {
            return productApplicationRepository.getAmountApplicationByOrganizationId(organizationId);
        } else {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            if (interval.equalsIgnoreCase("year")) {
                Date intervalTime2 = new Date();
                intervalTime2.setTime(currentTime.getTime()/1000 - 365*24*60*60);
                Timestamp intervalTime = new Timestamp(intervalTime2.getTime()*1000);
                return productApplicationRepository.getAmountApplicationByOrganizationIdInterval(organizationId, intervalTime,currentTime);

            } else if (interval.equalsIgnoreCase("month")) {
                Date intervalTime2 = new Date();
                intervalTime2.setTime(currentTime.getTime()/1000 - 30*24*60*60);
                Timestamp intervalTime = new Timestamp(intervalTime2.getTime()*1000);
                return productApplicationRepository.getAmountApplicationByOrganizationIdInterval(organizationId, intervalTime,currentTime);

            }
            else if (interval.equalsIgnoreCase("week")) {
                Date intervalTime2 = new Date();
                intervalTime2.setTime(currentTime.getTime()/1000 - 7*24*60*60);
                Timestamp intervalTime = new Timestamp(intervalTime2.getTime()*1000);
                return productApplicationRepository.getAmountApplicationByOrganizationIdInterval(organizationId, intervalTime,currentTime);

            }else if (interval.equalsIgnoreCase("day")) {
                Date intervalTime2 = new Date();
                intervalTime2.setTime(currentTime.getTime()/1000 - 24*60*60);
                Timestamp intervalTime = new Timestamp(intervalTime2.getTime()*1000);
                return productApplicationRepository.getAmountApplicationByOrganizationIdInterval(organizationId, intervalTime,currentTime);

            }
            return null;
        }
    }

    @Override
    public Boolean checkApplication(String name,String contact, Integer productId) {
        contact = contact.replaceAll(" ","");
        List<Integer> applicationIds= productApplicationRepository.checkApplication(
                name,contact,productId);
        if (applicationIds.size()>0){
            if (applicationIds.size()==1){
                deleteApplicationById(applicationIds.get(0));
                return true;
            }
        }
        return false;
    }
}
