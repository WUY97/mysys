package com.tongtong.product.dao;

public interface ProductDaoFactory {
    ProductDao getProductDao(String dbType);
}