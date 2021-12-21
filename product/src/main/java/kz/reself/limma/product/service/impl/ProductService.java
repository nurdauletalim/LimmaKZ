package kz.reself.limma.product.service.impl;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import kz.reself.limma.product.constant.PageableConstant;
import kz.reself.limma.product.model.*;
import kz.reself.limma.product.repository.ProductRepository;
import kz.reself.limma.product.repository.PropertyTemplateRepository;
import kz.reself.limma.product.repository.PropertyValueRepository;
import kz.reself.limma.product.service.IProductService;
import kz.reself.limma.product.service.IPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository productRepository;
//    @Autowired
//    private ImageRepository imageRepository;
    @Autowired
    private PropertyTemplateRepository propertyTemplateRepository;
    @Autowired
    IPropertyService propertyService;
    @Autowired
    PropertyValueRepository propertyValueRepository;
    @Autowired
    RestTemplate restTemplate;

    @Override
    public Product findProductById(Integer id) {
        return this.productRepository.getById(id);
    }

    @Override
    public ProductDTO findProductDTOById(Integer id) {
        Product product = productRepository.getById(id);
        if (product != null) {
            ProductDTO productDTO = new ProductDTO(product);
            if (product.getState() == State.ACTIVE) {
                List<ProductDTOWithImageCount> productDTOWithImageCountList = new ArrayList<>();
                List<Product> productList = this.productRepository.findAllByValueGroupByName(product.getValue());
                for (Product productTemp : productList) {
                    ProductDTOWithImageCount productDTOWithImageCount = new ProductDTOWithImageCount(productTemp);
                    //TODO get from Minio Service -> findAllByProductId
//                    productDTOWithImageCount.setImages(imageRepository.findAllByProductId(productTemp.getId()));
                    productDTOWithImageCountList.add(productDTOWithImageCount);
                }
                productDTO.setProducts(productDTOWithImageCountList);
                return productDTO;
            } else if (product.getState() == State.BOOKED) {
                return productDTO;
            }
        }
        return null;
    }

    @Override
    public Product createProduct(Product product) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        product.setPublishedDate(timestamp);
        Product product1 = this.productRepository.getById(product.getId());
        if (product1 != null) {
            if (!product1.getModelId().equals(product.getModelId())) {
                Model model = restTemplate.getForObject("http://limmaCatalog/catalog/api/v1/public/models/read/"+ product.getModelId(), Model.class);
                if (model != null)
                product.setName(model.getDisplayName());
                int ind = product.getValue().indexOf("/");
                if (ind > 0) {
                    while (ind != product.getName().length() - 1) {
                        System.out.println(product.getName().length());
                        System.out.println(ind);
                        ind = product.getValue().substring(ind + 1).indexOf("/");
                        if (ind == -1 || ind == 0) {
                            break;
                        }
                    }
                    System.out.println(product.getValue());
                    if (ind > 0) {
                        product.setValue(product.getName() + product.getValue().substring(ind));
                    } else {
                        product.setValue(product.getName());
                    }

                } else {
                    product.setValue(product.getName());
                }
            }
        } else {
            product.setValue(product.getName());
        }
        System.out.println(product.getName());
        System.out.println(product.getValue());
        return this.productRepository.saveAndFlush(product);
    }

    @Override
    public Product updateProduct(Product product) {
        return this.productRepository.save(product);
    }

    @Override
    public void deleteProductById(Integer id) {
        Product product = this.productRepository.getOne(id);
        product.setState(State.DEACTIVE);
        updateProduct(product);
    }

    @Override
    public List<Property> getAllProductProperties(Integer categoryId) {
        PropertyTemplate template = propertyTemplateRepository.getFirstByCategoryId(categoryId);
        if (template != null) {
            List<Property> properties = propertyService.getPropertyByTemplateId(template.getId());
            return properties;
        }
        return null;
    }

    @Override
    public Map<String, Object> getPropertyValueList(Integer productId) {
        List<PropertyValue> values = propertyValueRepository.getAllByProductId(productId);
        Map<String, Object> map = new HashMap<>();
        values.forEach(value -> {
            map.put(value.getKey(), value);
        });
        return map;
    }

    @Override
    public Map<String, Object> getMainPropertyValueList(Integer productId) {
        List<PropertyValue> values = propertyValueRepository.getAllMainByProductId(productId);
        Map<String, Object> map = new HashMap<>();
        values.forEach(value -> {
            map.put(value.getKey(), value);
        });
        return map;
    }

    @Override
    public Map<String, Object> getNotMainPropertyValueList(Integer productId) {
        List<PropertyValue> values = propertyValueRepository.getAllNotMainByProductId(productId);
        Map<String, Object> map = new HashMap<>();
        values.forEach(value -> {
            map.put(value.getKey(), value);
        });
        return map;
    }

    @Override
    public Product changeStateProduct(Integer id, State state) throws InternalException {
        Product product = this.productRepository.getById(id);
        product.setState(state);
        updateProduct(product);
        return product;
    }

    @Override
    public Page<ProductDTO> getAllProductDTOWithCertainParameters(Map<String, String> allRequestParams, Map<String, String[]> properties, Pageable pageableRequest) {
        Integer categoryId = null;
        Integer minPrice = 0;
        Integer maxPrice = 10000000;
        Integer state = 0;

        if (allRequestParams.containsKey("categoryId")) {
            categoryId = Integer.parseInt(allRequestParams.get("categoryId"));
        }
        if (allRequestParams.containsKey("minPrice")) {
            minPrice = Integer.parseInt(allRequestParams.get("minPrice"));
        }
        if (allRequestParams.containsKey("maxPrice")) {
            maxPrice = Integer.parseInt(allRequestParams.get("maxPrice"));
        }


        Page<Product> productsPage = null;
        List<List<Integer>> productIds = new ArrayList<List<Integer>>();
        if (properties != null && !properties.isEmpty()) {
            for (String i : properties.keySet()) {
                if (i.equalsIgnoreCase("Model")) {
                    List<Integer> integerList = new ArrayList<>();
                    for (int j = 0; j < properties.get(i).length; j++) {
                        String value = properties.get(i)[j];
                        Model model = restTemplate.getForObject("http://limmaCatalog/catalog/api/v1/public/models/read/byDisplayName/" + value, Model.class);
                        if (model != null) {
                            for (Integer number : this.productRepository.getIdsByModel(
                                    model.getId(), 0)) {
                                integerList.add(number);
                            }
                        }
                    }
                    productIds.add(integerList);
                } else {
                    productIds.add(this.productRepository.findProductIds(categoryId, i, properties.get(i), minPrice, maxPrice, state));
                }
            }
            for (int i = 1; i < productIds.size(); i++)
                productIds.get(0).retainAll(productIds.get(i));

            List<String> pValues = new ArrayList<>();
            properties.forEach(new BiConsumer<String, String[]>() {
                @Override
                public void accept(String s, String[] strings) {
                    pValues.addAll(Arrays.asList(strings));
                }
            });
            List<String> keys = new ArrayList<>(properties.keySet());
            productsPage = this.productRepository.searchWithParameters(categoryId, productIds.get(0), minPrice, maxPrice, pageableRequest);
        } else if (minPrice > 0 || maxPrice < 1000000) {
            productsPage = this.productRepository.searchWithPrice(categoryId, minPrice, maxPrice, pageableRequest);
        } else {
            productsPage = productRepository.categorystate(categoryId, state, pageableRequest);
        }
        //
        Page<Product> productPage = productsPage;
        List<Product> productList = productPage.getContent();
        List<ProductDTO> productDTOS = new ArrayList<>();

        int similar;
        boolean lastTwoSimilar = false;
        for (int i = 0; i < productList.size() - 1; i++) {
            productDTOS.add(new ProductDTO(productList.get(i)));
            for (int j = i + 1; j < productList.size(); j++) {
                if (productList.get(i).getModel() != null && productList.get(j).getModel() != null) {
                    if (productList.get(i).getModel().getDisplayName().equalsIgnoreCase(productList.get(j).getModel().getDisplayName())) {
                        List<PropertyValue> propertyValuesFirst = propertyValueRepository.getAllByProductId(productList.get(i).getId());
                        List<PropertyValue> propertyValuesSecond = propertyValueRepository.getAllByProductId(productList.get(j).getId());
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
                            for (int k = 0; k < productDTOS.get(i).getProductsList().size(); k++) {
                                if (productDTOS.get(i).getProductsList().get(k).getPrice() > productList.get(j).getPrice()) {
                                    productDTOS.get(i).getProductsList().add(k, productList.get(j));
                                    added = true;
                                    break;
                                }
                            }
                            if (!added) {
                                productDTOS.get(i).getProductsList().add(productList.get(j));
                            }
                            List<Product> clone = productList.stream().collect(Collectors.toList());
                            clone.remove(i);
                            productList = clone;
                            if (i == productList.size() - 1 && j == productList.size()) {
                                lastTwoSimilar = true;
                            }
                            break;
                        }
                    }
                }
            }
        }
        if (!lastTwoSimilar) {
            if (productList.size() > 0) {
                productDTOS.add(new ProductDTO(productList.get(productList.size() - 1)));
            }
        }
        Page<ProductDTO> productDTOPage = new PageImpl<ProductDTO>(productDTOS);

        return productDTOPage;

    }

    @Override
    public List<Product> findAllActive(String searchString) {
        return productRepository.searchActive(searchString);
    }

    @Override
    public List<Product> getDisplayCategoryProducts(Integer categoryId, Integer quantity) {
        return this.productRepository.getDisplayCategoryProductsByStateAndCategoryId(0, categoryId, quantity);
    }

    @Override
    public List<Product> getLastProductsSortByPublishedDate(Integer count) {
        return this.productRepository.getLastProductsSortByPublishedDateGroupByValue(count);
    }

    @Override
    public Page<Product> getProductGroupByProperties(Integer categoryId, Pageable pageableRequest) throws InternalException {
        return this.productRepository.findAllByStateAndCategoryIdGroupByValue(0, categoryId, pageableRequest);
    }

    @Override
    public Page<ProductDTOViewInfo> getProductDTOGroupByProperties(Integer categoryId, Pageable pageableRequest) throws InternalException {
        Page<Object[]> products = this.productRepository.findAllDTOByStateAndCategoryIdGroupByValue(0, categoryId, pageableRequest);

        Page<ProductDTOViewInfo> productDTO3s = products.map(new Function<Object[], ProductDTOViewInfo>() {
            @Override
            public ProductDTOViewInfo apply(Object[] product) {
                ProductDTOViewInfo productDTOViewInfo = new ProductDTOViewInfo(product);
                Model model = restTemplate.getForObject("http://limmaCatalog/catalog/api/v1/public/models/read/"+ productDTOViewInfo.getModelId(), Model.class);
                productDTOViewInfo.setModel(model);
                return productDTOViewInfo;
            }
        });
        return productDTO3s;
    }

    @Override
    public Page<Product> filterProducts
            (Map<String, String> allRequestParams0, Map<String, String> allRequestParams, Map<String, String[]>
                    properties) {
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

        Integer categoryId = null;
        Integer minPrice = 0;
        Integer maxPrice = 10000000;
        Integer state = 0;

        if (allRequestParams.containsKey("categoryId")) {
            categoryId = Integer.parseInt(allRequestParams.get("categoryId"));
        }
        if (allRequestParams.containsKey("minPrice")) {
            minPrice = Integer.parseInt(allRequestParams.get("minPrice"));
        }
        if (allRequestParams.containsKey("maxPrice")) {
            maxPrice = Integer.parseInt(allRequestParams.get("maxPrice"));
        }

        List<List<Integer>> productIds = new ArrayList<List<Integer>>();
        if (properties != null && !properties.isEmpty()) {
            for (String i : properties.keySet()) {
                if (i.equalsIgnoreCase("Brand")) {
                    List<Integer> integerList = new ArrayList<>();
                    for (int j = 0; j < properties.get(i).length; j++) {
//                        //TODO get from Catalog Service -> getByCategoryIdAndDisplayNameAndState
//                        Brand brand = brandRepository.getByCategoryIdAndDisplayNameAndState(categoryId, properties.get(i)[j], State.ACTIVE);
//                        for (Integer number : this.productRepository.getIdsByBrand(brand.getId(), 0)) {
//                            integerList.add(number);
//                        }
                    }
                    productIds.add(integerList);
                } else if (i.equalsIgnoreCase("Model")) {
                    List<Integer> integerList = new ArrayList<>();
                    for (int j = 0; j < properties.get(i).length; j++) {
                        Model model = restTemplate.getForObject("http://limmaCatalog/catalog/api/v1/public/models/read/byDisplayName/" + properties.get(i)[j], Model.class);
                        if (model != null) {
                            for (Integer number : this.productRepository.getIdsByModel(model.getId(), 0)) {
                                integerList.add(number);
                            }
                        }
                    }
                    productIds.add(integerList);
                } else {
                    productIds.add(this.productRepository.findProductIds(categoryId, i, properties.get(i), minPrice, maxPrice, state));
                }
            }
            for (int i = 1; i < productIds.size(); i++)
                productIds.get(0).retainAll(productIds.get(i));

            List<String> pValues = new ArrayList<>();
            properties.forEach(new BiConsumer<String, String[]>() {
                @Override
                public void accept(String s, String[] strings) {
                    pValues.addAll(Arrays.asList(strings));
                }
            });
            List<String> keys = new ArrayList<>(properties.keySet());
            return this.productRepository.searchWithParametersPageable(categoryId, productIds.get(0), minPrice, maxPrice, pageableRequest);
        } else if (minPrice > 0 || maxPrice < 1000000) {
            return this.productRepository.searchWithPricePageable(categoryId, minPrice, maxPrice, pageableRequest);
        } else {
            return productRepository.findAllByStateAndCategoryIdGroupByValue(state, categoryId, pageableRequest);
        }

    }

    @Override
    public Integer getIncomeByOrganizationId(Integer organizationId, String interval) {
        if (interval.equalsIgnoreCase("all")) {
            System.out.println("default");
            System.out.println(productRepository.getIncomeByOrganizationId(organizationId));
            return productRepository.getIncomeByOrganizationId(organizationId);
        } else {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            if (interval.equalsIgnoreCase("year")) {
                Date intervalTime2 = new Date();
                System.out.println(currentTime.getTime() / 1000);
                System.out.println(currentTime.getTime() / 1000 - 365 * 24 * 60 * 60);
                intervalTime2.setTime(currentTime.getTime() / 1000 - 365 * 24 * 60 * 60);
                Timestamp intervalTime = new Timestamp(intervalTime2.getTime() * 1000);
                System.out.println(currentTime);
                System.out.println(intervalTime);
                System.out.println("year");
                System.out.println(productRepository.getIncomeByOrganizationIdInterval(organizationId, intervalTime, currentTime));
                return productRepository.getIncomeByOrganizationIdInterval(organizationId, intervalTime, currentTime);

            } else if (interval.equalsIgnoreCase("month")) {
                Date intervalTime2 = new Date();
                System.out.println(currentTime.getTime() / 1000);
                System.out.println(currentTime.getTime() / 1000 - 30 * 24 * 60 * 60);
                intervalTime2.setTime(currentTime.getTime() / 1000 - 30 * 24 * 60 * 60);
                Timestamp intervalTime = new Timestamp(intervalTime2.getTime() * 1000);
                System.out.println(currentTime);
                System.out.println(intervalTime);
                System.out.println("month");
                System.out.println(productRepository.getIncomeByOrganizationIdInterval(organizationId, intervalTime, currentTime));
                return productRepository.getIncomeByOrganizationIdInterval(organizationId, intervalTime, currentTime);

            } else if (interval.equalsIgnoreCase("week")) {
                Date intervalTime2 = new Date();
                System.out.println(currentTime.getTime() / 1000);
                System.out.println(currentTime.getTime() / 1000 - 7 * 24 * 60 * 60);
                intervalTime2.setTime(currentTime.getTime() / 1000 - 7 * 24 * 60 * 60);
                Timestamp intervalTime = new Timestamp(intervalTime2.getTime() * 1000);
                System.out.println(currentTime);
                System.out.println(intervalTime);
                System.out.println("week");
                System.out.println(productRepository.getIncomeByOrganizationIdInterval(organizationId, intervalTime, currentTime));
                return productRepository.getIncomeByOrganizationIdInterval(organizationId, intervalTime, currentTime);

            } else if (interval.equalsIgnoreCase("day")) {
                Date intervalTime2 = new Date();
                System.out.println(currentTime.getTime() / 1000);
                System.out.println(currentTime.getTime() / 1000 - 24 * 60 * 60);
                intervalTime2.setTime(currentTime.getTime() / 1000 - 24 * 60 * 60);
                Timestamp intervalTime = new Timestamp(intervalTime2.getTime() * 1000);
                System.out.println(currentTime);
                System.out.println(intervalTime);
                System.out.println("day");
                System.out.println(productRepository.getIncomeByOrganizationIdInterval(organizationId, intervalTime, currentTime));
                return productRepository.getIncomeByOrganizationIdInterval(organizationId, intervalTime, currentTime);

            }
            return null;
        }
    }

    @Override
    public Integer getSoldAmountByOrganizationId(Integer organizationId, String interval) {
        if (interval.equalsIgnoreCase("all")) {
            System.out.println("default");
            System.out.println(productRepository.getSoldAmountByOrganizationId(organizationId));
            return productRepository.getSoldAmountByOrganizationId(organizationId);
        } else {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            if (interval.equalsIgnoreCase("year")) {
                Date intervalTime2 = new Date();
                System.out.println(currentTime.getTime() / 1000);
                System.out.println(currentTime.getTime() / 1000 - 365 * 24 * 60 * 60);
                intervalTime2.setTime(currentTime.getTime() / 1000 - 365 * 24 * 60 * 60);
                Timestamp intervalTime = new Timestamp(intervalTime2.getTime() * 1000);
                System.out.println(currentTime);
                System.out.println(intervalTime);
                System.out.println("year");
                System.out.println(productRepository.getSoldAmountByOrganizationIdInterval(organizationId, intervalTime, currentTime));
                return productRepository.getSoldAmountByOrganizationIdInterval(organizationId, intervalTime, currentTime);

            } else if (interval.equalsIgnoreCase("month")) {
                Date intervalTime2 = new Date();
                System.out.println(currentTime.getTime() / 1000);
                System.out.println(currentTime.getTime() / 1000 - 30 * 24 * 60 * 60);
                intervalTime2.setTime(currentTime.getTime() / 1000 - 30 * 24 * 60 * 60);
                Timestamp intervalTime = new Timestamp(intervalTime2.getTime() * 1000);
                System.out.println(currentTime);
                System.out.println(intervalTime);
                System.out.println("month");
                System.out.println(productRepository.getSoldAmountByOrganizationIdInterval(organizationId, intervalTime, currentTime));
                return productRepository.getSoldAmountByOrganizationIdInterval(organizationId, intervalTime, currentTime);

            } else if (interval.equalsIgnoreCase("week")) {
                Date intervalTime2 = new Date();
                System.out.println(currentTime.getTime() / 1000);
                System.out.println(currentTime.getTime() / 1000 - 7 * 24 * 60 * 60);
                intervalTime2.setTime(currentTime.getTime() / 1000 - 7 * 24 * 60 * 60);
                Timestamp intervalTime = new Timestamp(intervalTime2.getTime() * 1000);
                System.out.println(currentTime);
                System.out.println(intervalTime);
                System.out.println("week");
                System.out.println(productRepository.getSoldAmountByOrganizationIdInterval(organizationId, intervalTime, currentTime));
                return productRepository.getSoldAmountByOrganizationIdInterval(organizationId, intervalTime, currentTime);

            } else if (interval.equalsIgnoreCase("day")) {
                Date intervalTime2 = new Date();
                System.out.println(currentTime.getTime() / 1000);
                System.out.println(currentTime.getTime() / 1000 - 24 * 60 * 60);
                intervalTime2.setTime(currentTime.getTime() / 1000 - 24 * 60 * 60);
                Timestamp intervalTime = new Timestamp(intervalTime2.getTime() * 1000);
                System.out.println(currentTime);
                System.out.println(intervalTime);
                System.out.println("day");
                System.out.println(productRepository.getSoldAmountByOrganizationIdInterval(organizationId, intervalTime, currentTime));
                return productRepository.getSoldAmountByOrganizationIdInterval(organizationId, intervalTime, currentTime);

            }
            return null;
        }
    }

    @Override
    public Page<ProductDTOWithImageCount> searchStateCategoryPageable(String search, Integer categoryId, Integer state, Pageable pageable) {
        Page<Product> productsPageable = productRepository.getAllBySearchCategoryState(search, categoryId, state, pageable);

        Page<ProductDTOWithImageCount> productDTO2s = productsPageable.map(new Function<Product, ProductDTOWithImageCount>() {
            @Override
            public ProductDTOWithImageCount apply(Product product) {

                ProductDTOWithImageCount productDTOWithImageCount = new ProductDTOWithImageCount(product);
                //TODO get from Minio Service -> countAllByProductId
//                productDTOWithImageCount.setImageCount(imageRepository.countAllByProductId(product.getId()));

                return productDTOWithImageCount;
            }
        });
        return productDTO2s;
    }

    @Override
    public Page<ProductDTOWithImageCount> findAllPageable(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);

        Page<ProductDTOWithImageCount> productDTO2s = products.map(new Function<Product, ProductDTOWithImageCount>() {
            @Override
            public ProductDTOWithImageCount apply(Product product) {
                ProductDTOWithImageCount productDTOWithImageCount = new ProductDTOWithImageCount(product);
                //TODO get from Minio Service-> countAllByProductId
//                productDTOWithImageCount.setImageCount(imageRepository.countAllByProductId(product.getId()));
                return productDTOWithImageCount;
            }
        });

        return productDTO2s;
    }

    @Override
    public Page<ProductDTOWithImageCount> searchPageable(String search, Pageable pageable) {
        Page<Product> products = productRepository.searchPageable(search, pageable);

        Page<ProductDTOWithImageCount> productDTO2s = products.map(new Function<Product, ProductDTOWithImageCount>() {
            @Override
            public ProductDTOWithImageCount apply(Product product) {
                ProductDTOWithImageCount productDTOWithImageCount = new ProductDTOWithImageCount(product);
                //TODO get from Minio Service -> countAllByProductId
//                productDTOWithImageCount.setImageCount(imageRepository.countAllByProductId(product.getId()));

                return productDTOWithImageCount;
            }
        });
        return productDTO2s;
    }

    @Override
    public Page<ProductDTOWithImageCount> statePageable(Integer state, Pageable pageable) {
        Page<Product> products = productRepository.statePageable(state, pageable);

        Page<ProductDTOWithImageCount> productDTO2s = products.map(new Function<Product, ProductDTOWithImageCount>() {
            @Override
            public ProductDTOWithImageCount apply(Product product) {
                ProductDTOWithImageCount productDTOWithImageCount = new ProductDTOWithImageCount(product);
                //TODO get from Minio Service -> countAllByProductId
//                productDTOWithImageCount.setImageCount(imageRepository.countAllByProductId(product.getId()));

                return productDTOWithImageCount;
            }
        });
        return productDTO2s;
    }

    @Override
    public Page<ProductDTOWithImageCount> organizationPageable(Integer orgId, Pageable pageable) {
        Page<Product> products = productRepository.organizationPageable(orgId, pageable);

        Page<ProductDTOWithImageCount> productDTO2s = products.map(new Function<Product, ProductDTOWithImageCount>() {
            @Override
            public ProductDTOWithImageCount apply(Product product) {
                ProductDTOWithImageCount productDTOWithImageCount = new ProductDTOWithImageCount(product);
                //TODO get from Minio Service -> countAllByProductId
//                productDTOWithImageCount.setImageCount(imageRepository.countAllByProductId(product.getId()));

                return productDTOWithImageCount;
            }
        });
        return productDTO2s;
    }

    @Override
    public Page<ProductDTOWithImageCount> organizationStatePageable(Integer orgId, Integer state, Pageable pageable) {
        Page<Product> products = productRepository.organizationStatePageable(orgId, state, pageable);

        Page<ProductDTOWithImageCount> productDTO2s = products.map(new Function<Product, ProductDTOWithImageCount>() {
            @Override
            public ProductDTOWithImageCount apply(Product product) {
                ProductDTOWithImageCount productDTOWithImageCount = new ProductDTOWithImageCount(product);
                //TODO get from Minio Service -> countAllByProductId
//                productDTOWithImageCount.setImageCount(imageRepository.countAllByProductId(product.getId()));

                return productDTOWithImageCount;
            }
        });
        return productDTO2s;
    }

    @Override
    public Page<ProductDTOWithImageCount> searchStringAndOrganizationPageable(String searchString, Integer orgId, Pageable pageable) {
        Page<Product> products = productRepository.searchStringAndOrganizationPageable(searchString, orgId, pageable);

        Page<ProductDTOWithImageCount> productDTO2s = products.map(new Function<Product, ProductDTOWithImageCount>() {
            @Override
            public ProductDTOWithImageCount apply(Product product) {
                ProductDTOWithImageCount productDTOWithImageCount = new ProductDTOWithImageCount(product);
                //TODO get from Minio Service with Kafka -> countAllByProductId
//                productDTOWithImageCount.setImageCount(imageRepository.countAllByProductId(product.getId()));

                return productDTOWithImageCount;
            }
        });
        return productDTO2s;
    }

    @Override
    public Page<ProductDTOWithImageCount> searchStatePageable(String searchString, Integer state, Pageable pageable) {
        Page<Product> products = productRepository.searchStatePageable(searchString, state, pageable);

        Page<ProductDTOWithImageCount> productDTO2s = products.map(new Function<Product, ProductDTOWithImageCount>() {
            @Override
            public ProductDTOWithImageCount apply(Product product) {
                ProductDTOWithImageCount productDTOWithImageCount = new ProductDTOWithImageCount(product);
                //TODO get from Minio Service with Kafka -> countAllByProductId
//                productDTOWithImageCount.setImageCount(imageRepository.countAllByProductId(product.getId()));

                return productDTOWithImageCount;
            }
        });
        return productDTO2s;
    }
}
