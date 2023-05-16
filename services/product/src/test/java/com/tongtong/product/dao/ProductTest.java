package com.tongtong.product.dao;

import com.tongtong.product.config.TestConfig;
import com.tongtong.product.entity.Product;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Assert;

public class ProductTest extends TestCase {

    private ProductDao productDaoBean;

    public ProductTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(ProductTest.class);
    }

    public void setUp() {
        productDaoBean = new ProductMockDao();
    }

    public void testDummy() {
        Assert.assertEquals(true, true);
    }

    public void testAddProduct() {
        Product product1 = TestConfig.createProduct(TestConfig.productTestId_1);
        boolean success = productDaoBean.addProduct(product1);
        Assert.assertTrue(success);
    }

    public void testGetProduct() {
        Product product1 = TestConfig.createProduct(TestConfig.productTestId_1);
        Product fetchedProduct = productDaoBean.getProduct(product1.getId());

        Assert.assertNotNull(fetchedProduct);
        Assert.assertEquals(product1.getId(), fetchedProduct.getId());
        Assert.assertEquals(product1.getName(), fetchedProduct.getName());
        Assert.assertEquals(product1.getPrice(), fetchedProduct.getPrice());
        Assert.assertEquals(product1.getImageUrl(), fetchedProduct.getImageUrl());

        Product fetchedProductBad = productDaoBean.getProduct(TestConfig.productTestId_Bad);
        Assert.assertNull(fetchedProductBad);
    }

    public void testModifyProduct() {
        Product product1 = TestConfig.createProduct(TestConfig.productTestId_1);
        product1.setPrice(TestConfig.productTestPrice_2);
        Product retProduct = productDaoBean.modifyProduct(product1);
        Assert.assertNotNull(retProduct);
        Assert.assertEquals(product1.getId(), retProduct.getId());
        Assert.assertEquals(product1.getPrice(), retProduct.getPrice());
    }

    public void testRemoveProduct() {
        Product product1 = TestConfig.createProduct(TestConfig.productTestId_1);
        boolean result = productDaoBean.removeProduct(product1.getId());
        Assert.assertTrue(result);
    }

}