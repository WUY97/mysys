package com.tongtong.admin.api;

import com.tongtong.common.config.ServiceID;

public class GatewayApiClient extends ApiClient {

    @Override
    protected ServiceID getServiceID() {
        return ServiceID.GatewaySvc;
    }

    @Override
    protected String getServiceURI() {
        throw new UnsupportedOperationException();
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
