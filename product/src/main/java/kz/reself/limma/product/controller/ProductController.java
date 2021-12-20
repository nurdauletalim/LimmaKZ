package kz.reself.limma.product.controller;

import io.swagger.annotations.*;
import kz.reself.limma.product.constant.PageableConstant;
import kz.reself.limma.product.model.*;
import kz.reself.limma.product.repository.ProductRepository;
import kz.reself.limma.product.repository.PropertyValueRepository;
import kz.reself.limma.product.service.IProductService;
import kz.reself.limma.product.service.IPropertyValueService;
import kz.reself.limma.product.utils.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping()
@Api(tags = {"Product"}, description = "Управление продуктами", authorizations = {@Authorization(value = "bearerAuth")})
public class ProductController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private IProductService productService;
    @Autowired
    private IPropertyValueService propertyValueService;
    @Autowired
    private ProductRepository productRepository;
//    @Autowired
//    private ImageRepository imageRepository;
    @Autowired
    private PropertyValueRepository propertyValueRepository;

    @ApiOperation(value = "Получить список продуктов листами", tags = {"Product"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.(По умолчанию по 'id')", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "sortDirection", dataType = "string", value = "Указывает на тип сортировки.(По умолчанию по 'asc')", allowableValues = "asc,desc", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.(По умолчанию page равно на 0)", paramType = "query"),
            @ApiImplicitParam(name = "searchString", dataType = "string", value = "Поле для введение стринга для поиска", paramType = "query"),
            @ApiImplicitParam(name = "category", dataType = "string", value = "Поле для введение стринга для поиска", paramType = "query"),
            @ApiImplicitParam(name = "state", dataType = "string", value = "Поле для введение стринга для поиска", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице..(По умолчанию size равно на 5)", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что Product существуют и возвращает.")
    })
    @RequestMapping(value = "/v1/public/products/read/all/dto/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readAllDTOPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        Sort.Direction sortDirection = Sort.Direction.ASC;

        int pageNumber = PageableConstant.PAGE_NUMBER;

        int pageSize = PageableConstant.PAGE_SIZE;

        String searchString = "";

        String sortBy = PageableConstant.ID_FIELD_NAME;

        if (allRequestParams.containsKey("page")) {
            pageNumber = Integer.parseInt(allRequestParams.get("page"));
        }
        if (allRequestParams.containsKey("size")) {
            pageSize = Integer.parseInt(allRequestParams.get("size"));
        }
        if (allRequestParams.containsKey("sortDirection")) {

            if (allRequestParams.get("sortDirection").equals(PageableConstant.SORT_DIRECTION_DESC))
                sortDirection = Sort.Direction.DESC;

        }
        if (allRequestParams.containsKey("sort")) {
            sortBy = allRequestParams.get("sort");
        }

        Page<ProductDTOWithImageCount> products;
        final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortBy));
        if (allRequestParams.containsKey("searchString") && allRequestParams.containsKey("state") && allRequestParams.containsKey("category")) {
            products = this.productService.searchStateCategoryPageable
                    (allRequestParams.get("searchString"), Integer.parseInt(allRequestParams.get("category")), Integer.parseInt(allRequestParams.get("state")), pageableRequest);
        } else if (allRequestParams.containsKey("searchString") && allRequestParams.containsKey("state")) {
            products = productService.searchStatePageable(allRequestParams.get("searchString"), Integer.parseInt(allRequestParams.get("state")), pageableRequest);
        } else if (allRequestParams.containsKey("searchString") && allRequestParams.containsKey("organization")) {
            products = productService.searchStringAndOrganizationPageable(allRequestParams.get("searchString"), Integer.parseInt(allRequestParams.get("organization")), pageableRequest);
        }  else if (allRequestParams.containsKey("state") && allRequestParams.containsKey("organization")) {
            products = productService.organizationStatePageable(Integer.parseInt(allRequestParams.get("organization")), Integer.parseInt(allRequestParams.get("state")), pageableRequest);
        } else if (allRequestParams.containsKey("organization")) {
            products = productService.organizationPageable(Integer.parseInt(allRequestParams.get("organization")), pageableRequest);
        } else if (allRequestParams.containsKey("state")) {
            products = productService.statePageable(Integer.parseInt(allRequestParams.get("state")), pageableRequest);
        } else if (allRequestParams.containsKey("searchString")){
            products = productService.searchPageable(allRequestParams.get("searchString"), pageableRequest);
        } else {
            products = productService.findAllPageable(pageableRequest);
        }

        return builder(success(products));
    }

    @ApiOperation(value = "Получить список всех активных продуктов", tags = {"Product"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = "/v1/public/products/read/all/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readActive(@RequestParam Map<String, String> allRequestParams) {

        String search = "";


        List<Product> products;
        if (allRequestParams.containsKey("searchString") && allRequestParams.containsKey("state")) {
            products = productService.findAllActive(allRequestParams.get("searchString"));
        } else if (allRequestParams.containsKey("state")) {
            products = productRepository.findAllState();
        } else {
            products = productRepository.searchString(allRequestParams.get("searchString"));
        }
        List<ProductDTO> productDTOS = new ArrayList<>();

        int similar;
        boolean lastTwoSimilar = false;
        for (int i = 0; i < products.size() - 1; i++) {
            productDTOS.add(new ProductDTO(products.get(i)));
            if (productDTOS.get(productDTOS.size()-1).getModel() != null) {
                List<PropertyValue> propertyValues = propertyValueRepository.getAllByProductId(products.get(i).getId());
                if (propertyValues != null) {
                    for (PropertyValue propertyValue : propertyValues) {
                        if (propertyValue.getKey().equalsIgnoreCase("capacity")) {
                            productDTOS.get(productDTOS.size()-1).setCapacity(propertyValue.getValue());
                            break;
                        }
                    }
                }
            }
            for (int j = i + 1; j < products.size(); j++) {
                if (products.get(i).getModel() != null && products.get(j).getModel() != null) {
                    if (products.get(i).getModel().getDisplayName().equalsIgnoreCase(products.get(j).getModel().getDisplayName())) {
                        List<PropertyValue> propertyValuesFirst = propertyValueRepository.getAllByProductId(products.get(i).getId());
                        List<PropertyValue> propertyValuesSecond = propertyValueRepository.getAllByProductId(products.get(j).getId());
                        similar = 0;
                        for (PropertyValue propertyValue : propertyValuesFirst) {
                            for (PropertyValue value : propertyValuesSecond) {
                                if (propertyValue.getKey().equalsIgnoreCase(value.getKey())) {
                                    if (propertyValue.getValue().equalsIgnoreCase(value.getValue())) {
                                        similar++;
                                        break;
                                    }
                                }
                            }
                        }
                        if (similar == propertyValuesFirst.size()) {
                            boolean added = false;
                            for (int k = 0; k < productDTOS.get(i).getProducts().size(); k++) {
                                if (productDTOS.get(i).getProducts().get(k).getPrice() > products.get(j).getPrice()) {
                                    productDTOS.get(i).getProductsList().add(k, products.get(j));
                                    added = true;
                                    break;
                                }
                            }
                            if (!added) {
                                productDTOS.get(i).getProductsList().add(products.get(j));
                            }
                            List<Product> clone = products.stream().collect(Collectors.toList());
                            clone.remove(i);
                            products = clone;
                            if (i == products.size() - 1 && j == products.size()) {
                                lastTwoSimilar = true;
                            }
                            break;
                        }
                    }
                }
            }
        }
        if (!lastTwoSimilar) {
            if (products.size() > 0) {
                productDTOS.add(new ProductDTO(products.get(products.size() - 1)));
            }
        }

        return builder(success(productDTOS));
    }

    @ApiOperation(value = "Получить определенный продукт", tags = {"Product"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запрошенная запись найдена"),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная запись не найдена.")
    })
    @RequestMapping(value = "/v1/public/products/read/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getProductById(@PathVariable("id") Integer id) {
        return builder(success(productService.findProductById(id)));
    }

    @ApiOperation(value = "Получить группы продуктов по моделям", tags = {"Product"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запрошенная запись найдена"),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная запись не найдена.")
    })
    @RequestMapping(value = "/v1/public/products/read/group/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getProductsGroupByModel(@PathVariable("id") Integer id) {
        return builder(success(productService.findProductDTOById(id)));
    }

    @ApiOperation(value = "Получить свойства продукта по категории", tags = {"Product"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запрошенная запись найдена"),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная запись не найдена.")
    })
    @RequestMapping(value = "/v1/public/products/read/properties/{categoryId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllProductProperties(@PathVariable("categoryId") Integer categoryId) {
        return builder(success(productService.getAllProductProperties(categoryId)));
    }

    @RequestMapping(value = "/v1/public/products/state", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> changeState(@RequestParam Integer id, @RequestParam Integer state) {
        return builder(success(productService.changeStateProduct(id, State.BOOKED)));
    }


    @ApiOperation(value = "Создать продукт", tags = {"Product"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CONTENT_MANAGER')")
    @RequestMapping(value = "/v1/private/products/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@RequestBody Product product) {
        return builder(success(productService.createProduct(product)));
    }

    @RequestMapping(value = "/v1/public/products/property/values/{productId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getPropertyValueList(@PathVariable("productId") Integer productId) {
        return builder(success(productService.getPropertyValueList(productId)));
    }

    @RequestMapping(value = "/v1/public/products/property/values/main/{productId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getMainPropertyValueList(@PathVariable("productId") Integer productId) {
        return builder(success(productService.getMainPropertyValueList(productId)));
    }

    @RequestMapping(value = "/v1/public/products/property/values/notmain/{productId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getNotMainPropertyValueList(@PathVariable("productId") Integer productId) {
        return builder(success(productService.getNotMainPropertyValueList(productId)));
    }

    @ApiOperation(value = "Обновить Product", tags = {"Product"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CONTENT_MANAGER')")
    @RequestMapping(value = "/v1/public/products/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> update(@RequestBody Product product) {
        return builder(success(productService.updateProduct(product)));
    }

    @ApiOperation(value = "Удалить Product", tags = {"Product"})
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CONTENT_MANAGER')")
    @RequestMapping(value = "/v1/public/products/delete/{productId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteKiInformedConsent(@PathVariable(name = "productId") Integer productId) {
        productService.deleteProductById(productId);
        return builder(success("success"));
    }

    EntityManager em;

    @RequestMapping(value = "/v1/public/products/read/all/filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getfilter(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);

        Root<Product> book = cq.from(Product.class);
//        Predicate authorNamePredicate = cb.equal(book.get("author"), authorName);
        Predicate titlePredicate = cb.like(book.get("state"), "%" + "active" + "%");
        cq.where(titlePredicate);

        TypedQuery<Product> query = em.createQuery(cq);
        return builder(success(query.getResultList()));
    }

    @ApiOperation(value = "Получить список product По свойствам", tags = {"Product"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", dataType = "string", value = ""),
            @ApiImplicitParam(name = "minPrice", dataType = "int", value = ""),
            @ApiImplicitParam(name = "maxPrice", dataType = "int", value = ""),
            @ApiImplicitParam(name = "pageNumber", dataType = "int", value = ""),
            @ApiImplicitParam(name = "pageSize", dataType = "int", value = ""),
            @ApiImplicitParam(name = "sort", dataType = "string", value = ""),
            @ApiImplicitParam(name = "sortBy", dataType = "string", value = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = "/v1/public/products/read/all/properties", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getProductsWithParameters(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams0, @RequestParam Map<String, String> allRequestParams, @RequestBody Map<String, String[]> properties) {
        Sort.Direction sortDirection = Sort.Direction.ASC;

        int pageNumber = PageableConstant.PAGE_NUMBER;

        int pageSize = PageableConstant.PAGE_SIZE;


        String sortBy = PageableConstant.ID_FIELD_NAME;

        if (allRequestParams.containsKey("page")) {
            pageNumber = Integer.parseInt(allRequestParams.get("page"));
        }
        if (allRequestParams.containsKey("size")) {
            pageSize = Integer.parseInt(allRequestParams.get("size"));
        }
        if (allRequestParams.containsKey("sortDirection")) {

            if (allRequestParams.get("sortDirection").equals(PageableConstant.SORT_DIRECTION_DESC))
                sortDirection = Sort.Direction.DESC;

        }
        if (allRequestParams.containsKey("sort")) {
            sortBy = allRequestParams.get("sort");
        }
        final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortBy));

        return builder(success(productService.getAllProductDTOWithCertainParameters(allRequestParams0, properties, pageableRequest)));

//        Page<Product> productsPage = productService.getAllProductWithCertainParameters(allRequestParams, properties);
//        return builder(success(productsPage));
    }

    @RequestMapping(value = "/v1/public/products/read/category", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getDisplayCategoryProducts(@RequestParam Integer categoryId) {
        return builder(success(productService.getDisplayCategoryProducts(categoryId, 4)));
    }

    @RequestMapping(value = "/v1/public/products/read/group/date", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getLastProductsByPublishedDate(@RequestParam Integer count) {
        return builder(success(productService.getLastProductsSortByPublishedDate(count)));
    }

    @RequestMapping(value = "/v1/public/products/read/group/properties/category/{categoryId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getProductGroupByProperties(@PathVariable("categoryId") Integer categoryId, @ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        Sort.Direction sortDirection = Sort.Direction.ASC;

        int pageNumber = PageableConstant.PAGE_NUMBER;

        int pageSize = PageableConstant.PAGE_SIZE;


        String sortBy = PageableConstant.ID_FIELD_NAME;

        if (allRequestParams.containsKey("page")) {
            pageNumber = Integer.parseInt(allRequestParams.get("page"));
        }
        if (allRequestParams.containsKey("size")) {
            pageSize = Integer.parseInt(allRequestParams.get("size"));
        }
        if (allRequestParams.containsKey("sortDirection")) {

            if (allRequestParams.get("sortDirection").equals(PageableConstant.SORT_DIRECTION_DESC))
                sortDirection = Sort.Direction.DESC;

        }
        if (allRequestParams.containsKey("sort")) {
            sortBy = allRequestParams.get("sort");
        }
        final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortBy));

        return builder(success(productService.getProductGroupByProperties(categoryId, pageableRequest)));

    }

    @RequestMapping(value = "/v1/public/products/read/dto/group/properties/category/{categoryId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getProductDTOGroupByProperties(@PathVariable("categoryId") Integer categoryId, @ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        Sort.Direction sortDirection = Sort.Direction.ASC;

        int pageNumber = PageableConstant.PAGE_NUMBER;

        int pageSize = PageableConstant.PAGE_SIZE;


        String sortBy = PageableConstant.ID_FIELD_NAME;

        if (allRequestParams.containsKey("page")) {
            pageNumber = Integer.parseInt(allRequestParams.get("page"));
        }
        if (allRequestParams.containsKey("size")) {
            pageSize = Integer.parseInt(allRequestParams.get("size"));
        }
        if (allRequestParams.containsKey("sortDirection")) {

            if (allRequestParams.get("sortDirection").equals(PageableConstant.SORT_DIRECTION_DESC))
                sortDirection = Sort.Direction.DESC;

        }
        if (allRequestParams.containsKey("sort")) {
            sortBy = allRequestParams.get("sort");
        }
        final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortBy));

        return builder(success(productService.getProductDTOGroupByProperties(categoryId, pageableRequest)));

    }

    @RequestMapping(value = "/v1/public/products/filter", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> filterProducts(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams0, @RequestParam Map<String, String> allRequestParams, @RequestBody Map<String, String[]> properties) {
        return builder(success(productService.filterProducts(allRequestParams0,allRequestParams, properties)));
    }

    @RequestMapping(value = "v1/public/products/count/organization/{organizationId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAmount(@PathVariable(name = "organizationId") Integer organizationId) {
        return builder(success(productRepository.getAmountByOrganizationId(organizationId)));
    }

    @RequestMapping(value = "v1/public/products/income/organization/{organizationId}/{interval}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getIncome(@PathVariable(name = "organizationId") Integer organizationId, @PathVariable(name = "interval") String interval) {
        return builder(success(productService.getIncomeByOrganizationId(organizationId,interval)));
    }

    @RequestMapping(value = "v1/public/products/count/sold/organization/{organizationId}/{interval}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getSoldAmount(@PathVariable(name = "organizationId") Integer organizationId, @PathVariable(name = "interval") String interval) {
        return builder(success(productService.getSoldAmountByOrganizationId(organizationId,interval)));
    }

}
