package com.tongtong.oms.cart.config;

import com.tongtong.oms.cart.entity.Cart;
import com.tongtong.oms.cart.entity.CartLine;
import com.tongtong.oms.order.entity.Order;
import com.tongtong.oms.order.entity.OrderLine;

import java.util.LinkedList;
import java.util.List;

public class TestConfig {

    public static Cart createCart(String id) {
        Cart cart = new Cart(id);
        List<CartLine> cartLines = new LinkedList<>();
        cartLines.add(new CartLine(1, TEST_PRODUCT_ID_1, 1));
        cartLines.add(new CartLine(2, TEST_PRODUCT_ID_2, 1));
        cart.setCartLines(cartLines);
        return cart;
    }

    public static final String TEST_ORDER_ID_1 = "Test-Order-Id-1";
    public static final String TEST_PRODUCT_ID_1 = "Test-Product-Id-1";
    public static final String TEST_PRODUCT_ID_2 = "Test-Product-Id-2";
    public static final String TEST_PRODUCT_ID_3 = "Test-Product-Id-3";

    public static final String TEST_USER_ID = "anurag";

    public static Order createOrder(String id) {
        Order order = new Order(id, TEST_USER_ID);
        List<OrderLine> orderLines = new LinkedList<>();
        orderLines.add(new OrderLine(1, TEST_PRODUCT_ID_1, 1));
        orderLines.add(new OrderLine(2, TEST_PRODUCT_ID_2, 1));
        order.setOrderLines(orderLines);
        return order;
    }

}
