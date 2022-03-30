package com.codegym.controller;

import com.codegym.model.Category;
import com.codegym.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    ICategoryService categoryService;

    @GetMapping
    public ModelAndView showAllCategories(@PageableDefault(value = 10) Pageable pageable) {
        Page<Category> categories = this.categoryService.findAll(pageable);
        ModelAndView modelAndView = new ModelAndView("/category/list");
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        Category category = new Category();
        ModelAndView modelAndView = new ModelAndView("/category/create");
        modelAndView.addObject("category", category);
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createCategory(@Valid @ModelAttribute Category category, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            ModelAndView modelAndView = new ModelAndView("category/create");
            modelAndView.addObject("category", category);
            return modelAndView;
        }
        Category newCategory = new Category(category.getName());
        this.categoryService.save(newCategory);
        ModelAndView modelAndView = new ModelAndView("redirect:/categories");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditFrom(@PathVariable Long id) {
        Optional<Category> category = this.categoryService.findById(id);
        if (!category.isPresent()) {
            return new ModelAndView("/product/error-404");
        }
        ModelAndView modelAndView = new ModelAndView("/category/edit");
        modelAndView.addObject("category", category);
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public ModelAndView editCategory(@PathVariable Long id, @Valid @ModelAttribute Category category, BindingResult bindingResult) {
        Optional<Category> oldCategory = this.categoryService.findById(id);
        if (bindingResult.hasFieldErrors()) {
            ModelAndView modelAndView = new ModelAndView("/category/edit");
            modelAndView.addObject("category", oldCategory.get());
            return modelAndView;
        }
        Category newCategory = new Category(category.getId(), category.getName());
        this.categoryService.save(newCategory);
        ModelAndView modelAndView = new ModelAndView("redirect:/categories");
        return modelAndView;
    }
    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteFrom(@PathVariable Long id) {
        Optional<Category> category = this.categoryService.findById(id);
        if (!category.isPresent()) {
            return new ModelAndView("/product/error-404");
        }
        ModelAndView modelAndView = new ModelAndView("/category/delete");
        modelAndView.addObject("category", category);
        return modelAndView;
    }
    @PostMapping("/delete/{id}")
    public ModelAndView deleteCategory(@PathVariable Long id){
        Optional<Category> category = this.categoryService.findById(id);
        if (!category.isPresent()) {
            return new ModelAndView("/product/error-404");
        }
        this.categoryService.deleteCategory(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/categories");
        return modelAndView;
    }
    @GetMapping("/view/{id}")
    public ModelAndView showCategoryDetails(@PathVariable Long id) {
        Optional<Category> category = this.categoryService.findById(id);
        if (!category.isPresent()) {
            return new ModelAndView("/product/error-404");
        }
        ModelAndView modelAndView = new ModelAndView("/category/view");
        modelAndView.addObject("category", category);
        return modelAndView;
    }
}
