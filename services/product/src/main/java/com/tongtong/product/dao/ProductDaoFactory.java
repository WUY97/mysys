package com.tongtong.product.dao;

import com.tongtong.product.dao.ProductDao;

public interface ProductDaoFactory {
    ProductDao getProductDao(String dbType);
}