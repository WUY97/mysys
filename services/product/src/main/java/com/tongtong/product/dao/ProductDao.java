package com.tongtong.product.dao;

import com.tongtong.product.entity.Product;

import java.util.List;

public interface ProductDao {

    public List<Product> getProducts();

    public Product getProduct(String id);

    public boolean addProduct(Product product);

    Product modifyProduct(Product product);

    public boolean removeProduct(String id);

    public boolean removeProducts();
}