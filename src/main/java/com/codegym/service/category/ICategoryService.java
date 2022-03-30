package com.codegym.service.category;

import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoryService extends IGeneralService<Category> {

    Page<Category> findByNameContaining(String name, Pageable pageable);

    void deleteCategory(Long categoryId);

    Iterable<Category> findAll();
}
