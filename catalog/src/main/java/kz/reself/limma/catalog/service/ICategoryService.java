package kz.reself.limma.catalog.service;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import kz.reself.limma.catalog.model.Category;
import kz.reself.limma.catalog.model.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService {
    Page<Category> findAllCategoriesPageable(Pageable pageable) throws InternalException;
    Page<Category> findAllActiveCategoriesPageable(Pageable pageable) throws InternalException;
    List<Category> findAllCategoriesIterable() throws InternalException;
    List<Category> findAllGrandparentsIterable() throws InternalException;
    List<Category> findAllParentsIterable(Integer id) throws InternalException;
    List<Category> findAllChildrenIterable(Integer id) throws InternalException;
    List<Category> findAllCategoriesActive() throws InternalException;
    Category findCategoryById(Integer id) throws InternalException;
    Category createCategory(Category category) throws InternalException;
    void   deleteCategoryById(Integer id) throws InternalException;
    Category updateCategory(Category category) throws InternalError;
    List<CategoryDTO> findParentCategoriesById(Integer id) throws InternalError;
    Page<Category> findAllCategoriesPageableSearchString(Pageable pageable,String searchString) throws InternalException;
}
