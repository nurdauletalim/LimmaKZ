package kz.reself.limma.product.service;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import kz.reself.limma.product.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IProductService {

    Product findProductById(Integer id) throws InternalException;

    ProductDTO findProductDTOById(Integer id) throws InternalException;

    Product createProduct(Product product) throws InternalException;

    Product changeStateProduct(Integer id, State state) throws InternalException;

    Product updateProduct(Product product) throws InternalException;

    void deleteProductById(Integer id) throws InternalException;

    List<Property> getAllProductProperties(Integer categoryId);

    Map<String, Object> getPropertyValueList(Integer productId);

    Map<String, Object> getMainPropertyValueList(Integer productId);

    Map<String, Object> getNotMainPropertyValueList(Integer productId);

    Page<ProductDTO> getAllProductDTOWithCertainParameters(Map<String, String> allRequestParams, Map<String, String[]> properties,Pageable pageableRequest);

    List<Product> findAllActive(String searchString);

    List<Product> getDisplayCategoryProducts(Integer categoryId, Integer quantity);

    List<Product> getLastProductsSortByPublishedDate(Integer count);

    Page<Product> getProductGroupByProperties(Integer categoryId, Pageable pageableRequest) throws InternalException;

    Page<ProductDTOViewInfo> getProductDTOGroupByProperties(Integer categoryId, Pageable pageableRequest) throws InternalException;

    Page<Product> filterProducts(Map<String, String> allRequestParams0, Map<String, String> allRequestParams, Map<String, String[]> properties);

    Integer getIncomeByOrganizationId(Integer organizationId, String interval);

    Integer getSoldAmountByOrganizationId(Integer organizationId, String interval);

    Page<ProductDTOWithImageCount> searchStateCategoryPageable(String search, Integer categoryId, Integer state, Pageable pageable);

    Page<ProductDTOWithImageCount> findAllPageable(Pageable pageable);

    Page<ProductDTOWithImageCount> searchPageable(String search, Pageable pageable);

    Page<ProductDTOWithImageCount> statePageable (Integer state, Pageable pageable);

    Page<ProductDTOWithImageCount> organizationPageable(Integer orgId, Pageable pageable);

    Page<ProductDTOWithImageCount> organizationStatePageable(Integer orgId, Integer state, Pageable pageable);

    Page<ProductDTOWithImageCount> searchStringAndOrganizationPageable(String searchString, Integer orgId, Pageable pageable);

    Page<ProductDTOWithImageCount> searchStatePageable (String searchString, Integer state, Pageable pageable);
}
