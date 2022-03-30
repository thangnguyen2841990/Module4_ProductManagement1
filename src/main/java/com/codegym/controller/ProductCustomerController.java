package com.codegym.controller;

import com.codegym.model.Cart;
import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.service.category.ICategoryService;
import com.codegym.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/shop")
@SessionAttributes("cart")
public class ProductCustomerController {
    @ModelAttribute("cart")
    public Cart setupCart(){
        return  new Cart();
    }
    @Autowired
    IProductService productService;
    @Autowired
    ICategoryService categoryService;
    @ModelAttribute("categories")
    Iterable<Category> categories() {
        return categoryService.findAll();
    }

    @GetMapping
    public ModelAndView showAllProducts(Optional<String> name, @PageableDefault(value = 12) Pageable pageable) {
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
    @GetMapping("/add/{id}")
    public ModelAndView addToCart(@PathVariable Long id, @ModelAttribute Cart cart, @RequestParam("action") String action){
        Optional<Product> productOptional =productService.findById(id);
        if (!productOptional.isPresent()){
            return new ModelAndView("/product/error-404");
        }
        if (action.equals("show")){
            cart.addProduct(productOptional.get());
            return new ModelAndView("redirect:/shopping-cart");
        }
        if (action.equals("show1")){
            cart.addProduct1(productOptional.get());
            return new ModelAndView("redirect:/shopping-cart");
        }
        if (action.equals("delete")){
            cart.delete(productOptional.get());
            return new ModelAndView("redirect:/shopping-cart");
        }
        cart.addProduct(productOptional.get());
        return new ModelAndView("redirect:/shop");
    }
    @GetMapping("/search/{id}")
    public ModelAndView findByCategory(@PathVariable Long id, @PageableDefault(value = 10) Pageable pageable){
        Page<Product> products = this.productService.findByCategory(id,pageable);
        ModelAndView modelAndView = new ModelAndView("/product_customer/shop1");
        modelAndView.addObject("products", products);
        modelAndView.addObject("categoryId",id);
        return modelAndView;

    }


    }
