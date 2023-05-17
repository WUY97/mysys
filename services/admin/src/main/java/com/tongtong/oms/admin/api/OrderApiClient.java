package com.tongtong.oms.admin.api;

import com.tongtong.common.config.AppConfig;
import com.tongtong.common.config.ServiceID;

public class OrderApiClient extends ApiClient {

    @Override
    protected ServiceID getServiceID() {
        return ServiceID.OrderSvc;
    }

    @Override
    protected String getServiceURI() {
        return AppConfig.ORDERS_RESOURCE_PATH;
    }

    @Override
    protected Object createObject(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getObjectId(int index) {
        throw new UnsupportedOperationException();
    }

}
