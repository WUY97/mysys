package com.tongtong.product.config;

import com.tongtong.product.entity.Product;

public class TestConfig {
    public static String productTestId_1 = "junit-test-prod-1";
    public static String productTestName_1 = "junit-test-prod-name-1";
    public static Float productTestPrice_1 = 10.0F;
    public static Float productTestPrice_2 = 100.0F;

    public static String productTestId_Bad = "junit-test-prod-2";

    public static Product createProduct(String id) {
        Product product = new Product();
        product.setId(id);
        product.setName(TestConfig.productTestName_1);
        product.setPrice(TestConfig.productTestPrice_1);
        product.setImageUrl("http://placehold.it/350x150");
        return product;
    }

}