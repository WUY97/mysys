package com.tongtong.common.config;

public class AppConfig {
    public static final String STATUS_PATH = "/status";
    public static final String SERVICE_STATUS_PATH = "/status/services";
    public static final String DB_STATUS_PATH = "/status/databases";
    public static final String SYS_PROPERTY_PATH = "/property/{name}";

    public static final String ADMIN_RESOURCE_PATH = "/admin";

    public static final String AUTHORIZATION_RESOURCE_PATH = "/auth";
    public static final String AUTH_TOKEN_PATH = "/token";
    public static final String AUTH_TOKEN_USER_PATH = "/token/user";
    public static final String USERS_AUTH_PATH = "/users";
    public static final String USER_AUTH_PATH = "/users/{id}";

    public static final String PRODUCTS_RESOURCE_PATH = "/products";
    public static final String PRODUCT_PATH = "/{id}";

    public static final String CARTS_RESOURCE_PATH = "/carts";
    public static final String CART_PATH = "/{id}";

    public static final String ORDERS_RESOURCE_PATH = "/orders";
    public static final String ORDER_PATH = "/{id}";
    public static final String ORDER_CART_PATH = "/order/carts/{cartId}";

    public static final String USERS_PROFILE_RESOURCE_PATH = "/users-profile";
    public static final String USER_PROFILE_PATH = "/{id}";

    public static final String INVENTORY_RESOURCE_PATH = "/inventory";
    public static final String INVENTORY_PATH = "/{productId}";
    public static final String INVENTORY_RESERVATION_PATH = "/reservation";
}
