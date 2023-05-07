package com.tongtong.common.config;

public enum ServiceID {

    AuthSvc("AuthSvc"), ProductSvc("ProductSvc"), UserProfileSvc("UserProfileSvc"),
    CartSvc("CartSvc"), OrderSvc("OrderSvc"), InventorySvc("InventorySvc"),
    GatewaySvc("GatewaySvc"), AdminSvc("AdminSvc");

    private String serviceId;

    ServiceID(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        return serviceId;
    }
}
