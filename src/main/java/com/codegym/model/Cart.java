package com.codegym.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<Product, Integer> products = new HashMap<>();

    public Cart(Map<Product, Integer> products) {
        this.products = products;
    }

    public Cart() {
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Product, Integer> products) {
        this.products = products;
    }

    // kiểm tra xem sản phẩm đã có trong giỏ hàng hay chưa
    private boolean checkItemInCart(Product product) {
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            if (entry.getKey().getId().equals(product.getId())) {
                return true;
            }
        }
        return false;
    }

    private Map.Entry<Product, Integer> selectItemInCart(Product product) {
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            if (entry.getKey().getId().equals(product.getId())) {
                return entry;
            }
        }
        return null;
    }

    //thêm sản phẩm vào trong giỏ hàng
    public void addProduct(Product product) {
        if (!checkItemInCart(product)) {
            products.put(product, 1);
        } else {
            Map.Entry<Product, Integer> itemEntry = selectItemInCart(product);
            Integer newQuantity = itemEntry.getValue() + 1;
            products.replace(itemEntry.getKey(), newQuantity);
        }
    }
    // giảm sản phẩm mua
    public void addProduct1(Product product) {

            Map.Entry<Product, Integer> itemEntry = selectItemInCart(product);
            if (itemEntry.getValue()>=1){
                Integer newQuantity = itemEntry.getValue() -1 ;
                products.replace(itemEntry.getKey(), newQuantity);

            }

    }
    // xóa sản phẩm giỏ hàng
    public void delete(Product product) {

    products.remove(product);

    }

    // đếm số lượng hiện có trong giỏ hàng
    public Integer countProductQuantity() {
        Integer productQuantity = 0;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            productQuantity += entry.getValue();
        }
        return productQuantity;
    }

    //đếm số lượng sản phẩm có trong giỏ hàng.
    public Integer countItemQuantity() {
        return products.size();
    }

    //dùng để tính tổng số tiền cần phải thanh toán.
    public Float countTotalPayment() {
        float payment = 0;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            payment += entry.getKey().getPrice() * (float) entry.getValue();
        }
        return payment;
    }
}
