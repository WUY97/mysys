package com.tongtong.oms.cart.dao;

public interface CartDaoFactory {
    CartDao getCartDao(String dbType);
}
