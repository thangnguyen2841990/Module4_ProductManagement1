package com.codegym.controller;

import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.model.ProductForm;
import com.codegym.service.category.ICategoryService;
import com.codegym.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    IProductService productService;
    @Autowired
    ICategoryService categoryService;
    @Value("C:/Users/nguye/OneDrive/Desktop/image/")
    String fileUpload;

    @ModelAttribute("categories")
    Iterable<Category> categories() {
        return categoryService.findAll();
    }

    @GetMapping
    public ModelAndView showAllProducts(Optional<String> name, @PageableDefault(value = 10) Pageable pageable) {
        Page<Product> products;
        if (name.isPresent()) {
            products = this.productService.findByNameContaining(name.get(), pageable);
            ModelAndView modelAndView = new ModelAndView("/product/list");
            modelAndView.addObject("products", products);
            modelAndView.addObject("name", name.get());
            return modelAndView;

        } else {
            products = this.productService.findAll(pageable);
            ModelAndView modelAndView = new ModelAndView("/product/list");
            modelAndView.addObject("products", products);
            return modelAndView;
        }
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ProductForm productForm = new ProductForm();
        ModelAndView modelAndView = new ModelAndView("/product/create");
        modelAndView.addObject("productForm", productForm);
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createProduct(@Valid @ModelAttribute ProductForm productForm, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            ModelAndView modelAndView = new ModelAndView("/product/create");
            modelAndView.addObject("productForm", productForm);
            return modelAndView;
        }
        MultipartFile imageFile = productForm.getImage();
        String fileName = imageFile.getOriginalFilename();
        long currentTime = System.currentTimeMillis();
        fileName = currentTime + fileName;
        try {
            FileCopyUtils.copy(imageFile.getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Product newProduct = new Product(productForm.getName(), productForm.getPrice(), productForm.getQuantity(),
                productForm.getDescription(), fileName, productForm.getCategory());
        this.productService.save(newProduct);
        ModelAndView modelAndView = new ModelAndView("redirect:/products");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editProduct(@PathVariable Long id) {
        Optional<Product> product = this.productService.findById(id);
        if (!product.isPresent()) {
            return new ModelAndView("/product/error-404");
        }
        Product oldProduct = product.get();
        ProductForm productForm = new ProductForm(oldProduct.getId(), oldProduct.getName(), oldProduct.getPrice(),
                oldProduct.getQuantity(), oldProduct.getDescription(), null, oldProduct.getCategory());
        ModelAndView modelAndView = new ModelAndView("/product/edit");
        modelAndView.addObject("productForm", productForm);
        modelAndView.addObject("product", oldProduct);
        return modelAndView;
    }
    @PostMapping("/edit/{id}")
    public ModelAndView editProduct(@Valid @ModelAttribute ProductForm productForm, BindingResult bindingResult, @PathVariable Long id){
        Optional<Product> product = this.productService.findById(id);
        if (!product.isPresent()) {
            return new ModelAndView("/product/error-404");
        }
        if(bindingResult.hasFieldErrors()){
            ModelAndView modelAndView = new ModelAndView("/product/edit");
            modelAndView.addObject("product", product.get());
            return modelAndView;
        }
        Product oldProduct = product.get();
        String image;
        MultipartFile imageFile = productForm.getImage();
        if (imageFile.getSize() == 0){
            image = oldProduct.getImage();
        }else{
            String fileName = imageFile.getOriginalFilename();
            long currentTime = System.currentTimeMillis();
            fileName = currentTime + fileName;
            image = fileName;
            try {
                FileCopyUtils.copy(imageFile.getBytes(), new File(fileUpload + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Product newProduct = new Product(productForm.getId(), productForm.getName(), productForm.getPrice(), productForm.getQuantity(),
                productForm.getDescription(), image, productForm.getCategory());
        this.productService.save(newProduct);
        ModelAndView modelAndView = new ModelAndView("redirect:/products");
        return modelAndView;
    }
    @GetMapping("/delete/{id}")
    public ModelAndView showFormDelete(@PathVariable Long id) {
        Optional<Product> product = this.productService.findById(id);
        if (!product.isPresent()) {
            return new ModelAndView("/product/error-404");
        }
        Product oldProduct = product.get();
        ModelAndView modelAndView = new ModelAndView("/product/edit");
        modelAndView.addObject("product", oldProduct);
        return modelAndView;
    }
    @PostMapping("/delete/{id}")
    public ModelAndView deleteProduct(@PathVariable Long id){
        Optional<Product> product = this.productService.findById(id);
        if (!product.isPresent()) {
            return new ModelAndView("/product/error-404");
        }
        File file = new File(fileUpload + product.get().getImage());
        if (file.exists()){
            file.delete();
        }
        this.productService.deleteById(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/products");
        return modelAndView;
    }
    @GetMapping("/view/{id}")
    public ModelAndView showProductDetails(@PathVariable Long id) {
        Optional<Product> product = this.productService.findById(id);
        if (!product.isPresent()) {
            return new ModelAndView("/product/error-404");
        }
        Product oldProduct = product.get();
        ModelAndView modelAndView = new ModelAndView("/product/view");
        modelAndView.addObject("product", oldProduct);
        return modelAndView;
    }
    @GetMapping("/search")
    public ModelAndView findByCategory(Long categoryId, @PageableDefault(value = 10) Pageable pageable){
        Page<Product> products = this.productService.findByCategory(categoryId,pageable);
        ModelAndView modelAndView = new ModelAndView("/product/listSearch1");
        modelAndView.addObject("products", products);
        modelAndView.addObject("categoryId",categoryId);
        return modelAndView;
    }

}
