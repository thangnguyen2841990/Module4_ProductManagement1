package com.codegym.controller;

import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.service.category.ICategoryService;
import com.codegym.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/shop")
public class ProductCustomerController {

    @Autowired
    IProductService productService;
    @Autowired
    ICategoryService categoryService;
    @ModelAttribute("categories")
    Iterable<Category> categories() {
        return categoryService.findAll();
    }

    @GetMapping
    public ModelAndView showAllProducts(Optional<String> name, @PageableDefault(value = 10) Pageable pageable) {
        Page<Product> products;
        if (name.isPresent()) {
            products = this.productService.findByNameContaining(name.get(), pageable);
            ModelAndView modelAndView = new ModelAndView("/product_customer/shop");
            modelAndView.addObject("products", products);
            modelAndView.addObject("name", name.get());
            return modelAndView;

        } else {
            products = this.productService.findAll(pageable);
            ModelAndView modelAndView = new ModelAndView("/product_customer/shop");
            modelAndView.addObject("products", products);
            return modelAndView;
        }
    }
}
