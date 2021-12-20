package kz.reself.limma.catalog.service.impl;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import kz.reself.limma.catalog.model.Category;
import kz.reself.limma.catalog.model.CategoryDTO;
import kz.reself.limma.catalog.model.State;
import kz.reself.limma.catalog.repository.CategoryRepository;
import kz.reself.limma.catalog.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Page<Category> findAllCategoriesPageable(Pageable pageable) {
        return this.categoryRepository.findAll(pageable);
    }

    @Override
    public Page<Category> findAllCategoriesPageableSearchString(Pageable pageable, String searchString) throws InternalException {
        return this.categoryRepository.findAllSearchString(searchString, pageable);
    }

    @Override
    public Page<Category> findAllActiveCategoriesPageable(Pageable pageable) throws InternalException {
        return categoryRepository.findAllByState(State.ACTIVE, pageable);
    }

    @Override
    public List<Category> findAllCategoriesIterable() {
        return this.categoryRepository.findAll();
    }

    @Override
    public List<Category> findAllGrandparentsIterable() throws InternalException {
        return categoryRepository.getGrandparents();
    }

    @Override
    public List<Category> findAllParentsIterable(Integer id) throws InternalException {
        return categoryRepository.getParents(id);
    }

    @Override
    public List<Category> findAllChildrenIterable(Integer id) throws InternalException {
        return categoryRepository.getChildren(id);
    }

    @Override
    public List<Category> findAllCategoriesActive() {
        return categoryRepository.getAllByState(State.ACTIVE);
    }

    @Override
    public Category findCategoryById(Integer id) {
        return this.categoryRepository.getById(id);
    }

    @Override
    public Category createCategory(Category category) {
        category.setParentCategory(findCategoryById(category.getParentCategoryId()));
        category.setState(State.ACTIVE);
        return this.categoryRepository.saveAndFlush(category);
    }

    @Override
    public void deleteCategoryById(Integer id) throws InternalException {
        Category category = categoryRepository.getById(id);
        category.setState(State.DEACTIVE);
        categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) throws InternalError {
        if (category.getId() != category.getParentCategoryId()) {
            category.setState(categoryRepository.getById(category.getId()).getState());
            return categoryRepository.save(category);
        }
        return null;
    }

//    @Override
//    public List<Category> findParentCategoriesById(Integer id) throws InternalError {
//        List<Category> categories = new ArrayList<>();
//        Category category  =  categoryRepository.getById(id);
//        categories.add(category);
//
//        do {
//            category = categoryRepository.getById(category.getParentCategoryId());
//            categories.add(category);
//        }while (category.getParentCategoryId() != null);
//        System.out.println(categories);
//        return categories;
//    }

    @Override
    public List<CategoryDTO> findParentCategoriesById(Integer id) throws InternalError {
        return getByParent(id);
    }

    private List<CategoryDTO> getByParent(Integer id) {
        List<CategoryDTO> categories = new ArrayList<>();
        CategoryDTO category = from(categoryRepository.getById(id));
        categories.add(category);
        if (category.getParentId() != null) categories.addAll(getByParent(category.getParentId()));
        return categories;
    }

    public CategoryDTO from(Category obj) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setParentId(obj.getParentCategoryId());
        categoryDTO.setId(obj.getId());
        categoryDTO.setName(obj.getName());
        return categoryDTO;
    }

}
