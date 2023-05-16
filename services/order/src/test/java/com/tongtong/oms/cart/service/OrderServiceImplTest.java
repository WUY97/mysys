package com.tongtong.oms.cart.service;

import com.tongtong.oms.cart.config.TestConfig;
import com.tongtong.oms.cart.dao.CartMockDao;
import com.tongtong.oms.cart.dao.OrderMockDao;
import com.tongtong.oms.cart.entity.Cart;
import com.tongtong.oms.order.dao.OrderDao;
import com.tongtong.oms.order.entity.Order;
import com.tongtong.oms.order.processor.OrderPostProcessor;
import com.tongtong.oms.order.processor.OrderPreProcessor;
import com.tongtong.oms.order.service.OrderServiceImpl;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Assert;

import java.io.IOException;

public class OrderServiceImplTest extends TestCase {

    private OrderServiceImpl orderService;

    public OrderServiceImplTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(OrderServiceImplTest.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        orderService = new OrderServiceImpl();
        CartServiceImpl cartService = new CartServiceImpl();
        cartService.setCartDaoBean(new CartMockDao());
        orderService.setCartServiceBean(cartService);
        OrderPreProcessor orderPreProcessor = new OrderPreProcessor();
        OrderPostProcessor orderPostProcessor = new OrderPostProcessor();
        orderPostProcessor.setInventoryClientBean(new InventoryMockClient());
        orderService.setOrderPreProcessor(orderPreProcessor);
        orderService.setOrderPostProcessor(orderPostProcessor);
        OrderDao orderDao = new OrderMockDao();
        orderService.setOrderDaoBean(orderDao);
        orderService.getOrderPostProcessor().setOrderDaoBean(orderDao);
    }

    public void testDummy() {
        Assert.assertTrue(true);
    }

    public void testCreateOrder() {
        Order order = TestConfig.createOrder(TestConfig.TEST_ORDER_ID_1);
        boolean success = orderService.saveOrder(order);
        Assert.assertTrue(success);
    }

    public void testFetchOrder() {
        // Fetch order
        Order order = orderService.fetchOrder(TestConfig.TEST_USER_ID, TestConfig.TEST_ORDER_ID_1);
        Assert.assertNotNull(order);
        Assert.assertEquals(TestConfig.TEST_ORDER_ID_1, order.getId());
        Assert.assertEquals(order.getOrderLines().size(), order.getOrderLines().size());
        Assert.assertEquals(order.getOrderLines().get(0).getProductId(),
                order.getOrderLines().get(0).getProductId());
//        Assert.assertEquals(order.getOrderLines().get(1).getProductId(),
//                order.getOrderLines().get(1).getProductId());
//        Assert.assertEquals(order.getOrderLines().get(0).getQuantity(),
//                order.getOrderLines().get(0).getQuantity());
//        Assert.assertEquals(order.getOrderLines().get(1).getQuantity(),
//                order.getOrderLines().get(1).getQuantity());

        // Fetch order
        Order order1 = orderService.fetchOrder(TestConfig.TEST_USER_ID, "Non-Existent-Id");
        Assert.assertNull(order1);
        //Assert.assertEquals(0, order1.getOrderLines().size());
    }

    public void testRemoveOrder() {
        // Remove order
        boolean success = orderService.removeOrder(TestConfig.TEST_USER_ID, TestConfig.TEST_ORDER_ID_1);
        Assert.assertTrue(success);
    }

    public void testCreateOrderFromCart() throws IOException {
        Cart cart = TestConfig.createCart(TestConfig.TEST_USER_ID);
        Order order = orderService.createOrder(TestConfig.TEST_USER_ID, "null");
        Assert.assertNotNull(order);
        Assert.assertNotNull(order.getId());
        Assert.assertEquals(TestConfig.TEST_USER_ID, order.getUserId());
        Assert.assertEquals(cart.getCartLines().size(), order.getOrderLines().size());
        // Remove order
        boolean success = orderService.removeOrder(TestConfig.TEST_USER_ID, TestConfig.TEST_ORDER_ID_1);
        Assert.assertTrue(success);
    }


}
