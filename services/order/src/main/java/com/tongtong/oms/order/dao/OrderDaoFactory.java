package com.tongtong.oms.order.dao;

public interface OrderDaoFactory {
    OrderDao getOrderDao(String dbType);
}
