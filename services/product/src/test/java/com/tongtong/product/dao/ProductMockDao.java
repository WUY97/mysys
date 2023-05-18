package com.tongtong.product.dao;

import com.tongtong.product.config.TestConfig;
import com.tongtong.product.entity.Product;

import java.util.LinkedList;
import java.util.List;

public class ProductMockDao implements ProductDao {

    @Override
    public List<Product> getProducts() {
        List<Product> products = new LinkedList<Product>();
        products.add(getProduct(TestConfig.productTestId_1));
        return products;
    }

    @Override
    public Product getProduct(String id) {
        if (id.equals(TestConfig.productTestId_Bad)) {
            return null;
        }
        Product product = TestConfig.createProduct(id);
        return product;
    }

    @Override
    public boolean addProduct(Product product) {
        if (product.getId().equals(TestConfig.productTestId_Bad)) {
            return false;
        }
        return true;
    }

    @Override
    public List<Product> getProductsByIds(List<String> ids) {
        List<Product> products = new LinkedList<Product>();
        for (String id : ids) {
            products.add(getProduct(id));
        }
        return products;
    }

    @Override
    public Product modifyProduct(Product product) {
        return product;
    }

    @Override
    public boolean removeProduct(String id) {
        if (id.equals(TestConfig.productTestId_Bad)) {
            return false;
        }
        Product product = getProduct(id);
        return true;
    }

    @Override
    public boolean removeProducts() {
        return false;
    }
}