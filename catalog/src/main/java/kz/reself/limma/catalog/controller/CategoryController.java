package kz.reself.limma.catalog.controller;

import io.swagger.annotations.*;
import kz.reself.limma.catalog.constant.PageableConstant;
import kz.reself.limma.catalog.model.Category;
import kz.reself.limma.catalog.repository.CategoryRepository;
import kz.reself.limma.catalog.service.ICategoryService;
import kz.reself.limma.catalog.utils.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Api(tags = {"Category"}, description = "Управление категориями ")
public class CategoryController extends CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    public static final String PRIVATE_URL = "/private/categories";
    public static final String PUBLIC_URL = "/public/categories";

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @ApiOperation(value = "Получить список катерогий листами", tags = {"Category"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.(По умолчанию по 'id')", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "sortDirection", dataType = "string", value = "Указывает на тип сортировки.(По умолчанию по 'asc')", allowableValues = "asc,desc", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.(По умолчанию page равно на 0)", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице..(По умолчанию size равно на 5)", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что Category существуют и возвращает.")
    })
    @RequestMapping(value = PUBLIC_URL + "/read/all/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readAllPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
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

        Page<Category> categories;
        if (allRequestParams.containsKey("searchString")) {
            categories = this.categoryService.findAllCategoriesPageableSearchString(pageableRequest,allRequestParams.get("searchString"));
        } else if (allRequestParams.containsKey("state")) {
            categories = this.categoryRepository.state(Integer.parseInt(allRequestParams.get("state")), pageableRequest);
        } else {
            categories = this.categoryService.findAllCategoriesPageable(pageableRequest);
        }

        return builder(success(categories));
    }

    @ApiOperation(value = "Получить список всех категорий", tags = {"Category"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = PUBLIC_URL + "/read/all/iterable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readIterable() {
        return builder(success(categoryService.findAllCategoriesIterable()));
    }

    @ApiOperation(value = "Получить список активных категорий", tags = {"Category"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = PUBLIC_URL + "/read/all/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> allActive() {
        return builder(success(categoryService.findAllCategoriesActive()));
    }


    @ApiOperation(value = "Получить Category", tags = {"Category"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запрошенная запись найдена"),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная запись не найдена.")
    })
    @RequestMapping(value = PUBLIC_URL + "/read/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        return builder(success(categoryService.findCategoryById(id)));
    }

    @ApiOperation(value = "Создать категорию", tags = {"Category"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = PRIVATE_URL + "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@RequestBody Category category) {
        return builder(success(categoryService.createCategory(category)));
    }

    @ApiOperation(value = "Обновить категорию", tags = {"Category"})
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = PRIVATE_URL + "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> update(@RequestBody Category category) {
        return builder(success(categoryService.updateCategory(category)));
    }


    @ApiOperation(value = "Удалить категорию", tags = {"Category"})
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = PRIVATE_URL + "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteKiInformedConsent(@PathVariable(name = "id") Integer categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return builder(success("success"));
    }

    @ApiOperation(value = "Поулчить список родительских категорий определенной категории", tags = {"Category"})
    @RequestMapping(value = PUBLIC_URL + "/read/parents/category/{categoryId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getByParentID(@PathVariable(name = "categoryId") Integer categoryId) {
        return ResponseEntity.ok(categoryService.findParentCategoriesById(categoryId));
    }

    @ApiOperation(value = "Получить список всех родительских категорий", tags = {"Category"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = PUBLIC_URL + "/read/parents/iterable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readGrandParents() {
        return builder(success(categoryService.findAllGrandparentsIterable()));
    }

    @ApiOperation(value = "Получить список родительских категорий по ID", tags = {"Category"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = PUBLIC_URL + "/read/parents/{parentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readParents(@PathVariable("parentId") Integer id) {
        return builder(success(categoryService.findAllParentsIterable(id)));
    }

    @ApiOperation(value = "Получить список дочерних категорий", tags = {"Category"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = PUBLIC_URL + "/read/childrens/{childId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readChildren(@PathVariable("childId") Integer id) {
        return builder(success(categoryService.findAllChildrenIterable(id)));
    }
}

