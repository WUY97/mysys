package com.tongtong.oms.admin.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tongtong.common.config.AppConfig;
import com.tongtong.common.config.ServiceID;
import com.tongtong.common.status.ServiceStatus;
import com.tongtong.oms.admin.entity.OperationStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;


public abstract class ApiClient {

    protected String authHeader;
    protected ObjectMapper mapper;
    protected HttpClient client;

    public void setAuthHeader(String authHeader) {
        this.authHeader = authHeader;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void setClient(HttpClient client) {
        this.client = client;
    }

    protected abstract ServiceID getServiceID();

    protected ServiceID getEndpointServiceID() {
        return getServiceID();
    }

    protected abstract String getServiceURI();

    protected abstract Object createObject(String id);

    protected abstract String getObjectId(int index);

    public OperationStatus insertBootstrapData() {
        throw new UnsupportedOperationException();
    }

    public OperationStatus insertData(int index) {
        String id = getObjectId(index);
        Object object = createObject(id);
        return insertData(id, object);
    }

    public OperationStatus insertData(String id, Object object) {
        String uri = getServiceURI() +
                "/" + id;
        OperationStatus operationStatus = new OperationStatus(false);
        operationStatus.setMessage("Unable to convert object to json: " + object.toString());
        String jsonObject;
        try {
            jsonObject = mapper.writeValueAsString(object);
        } catch (JsonProcessingException jpe) {
            return operationStatus;
        }
        if (jsonObject == null) {
            return operationStatus;
        }
        operationStatus = insertData(uri, jsonObject);
        return operationStatus;
    }

    protected OperationStatus insertData(String uri, String data) {
        OperationStatus operationStatus = new OperationStatus();
        operationStatus.setSuccess(false);
//        ServiceInstance instance = loadBalancer.choose(getEndpointServiceID().toString());
//        if (instance == null) {
//            operationStatus.setMessage("Unknown host for: "+getEndpointServiceID().toString());
//            return operationStatus;
//        }
//        String host = instance.getHost();
//        int port = instance.getPort();
        String host = "http://localhost";
        int port = 8080;
        try {
            HttpClientResponse response = client.sendPut(host, port, uri,
                    authHeader, MediaType.APPLICATION_JSON_VALUE, data);
            operationStatus.setSuccess(true);
            operationStatus.setMessage("Inserted data: " + data);
        } catch (Exception e) {
            operationStatus.setMessage("Error inserting data: " + e.getMessage());
        }
        return operationStatus;
    }

    public OperationStatus deleteData() {
        OperationStatus operationStatus = new OperationStatus();
        operationStatus.setSuccess(false);
//        ServiceInstance instance = loadBalancer.choose(getEndpointServiceID().toString());
//        if (instance == null) {
//            operationStatus.setMessage("Unknown host for: "+getEndpointServiceID().toString());
//            return operationStatus;
//        }
//        String host = instance.getHost();
//        int port = instance.getPort();
        String host = "http://localhost";
        int port = 8080;
        try {
            String uri = new StringBuilder().append(getServiceURI()).toString();
            HttpClientResponse response = client.sendDelete(host, port, uri, authHeader);
            operationStatus.setSuccess(true);
            operationStatus.setMessage("Deleted data");
        } catch (Exception e) {
            operationStatus.setSuccess(false);
            operationStatus.setMessage("Error deleting data: " + e.getMessage());
        }
        return operationStatus;
    }

    public ServiceStatus getStatus() {
//        ServiceInstance instance = loadBalancer.choose(getEndpointServiceID().toString());
//        ServiceStatus serviceStatus = new ServiceStatus(getEndpointServiceID().toString());
//        if (instance == null) {
//            serviceStatus.setServiceHost("Unknown");
//            serviceStatus.setServiceTime("Service not available");
//            return serviceStatus;
//        }
//        String host = instance.getHost();
//        int port = instance.getPort();
        String host = "http://localhost";
        int port = 8080;
        try {
            String uri = new StringBuilder().append(AppConfig.STATUS_PATH).toString();
            HttpClientResponse response = client.sendGet(host, port, uri, authHeader, "");
            String status = response.getBody();
            return (new Gson()).fromJson(status, ServiceStatus.class);
        } catch (Exception e) {
//            serviceStatus.setServiceHost(host+":"+port);
//            serviceStatus.setServiceTime("Not Reachable: "+e.getMessage());
//            return serviceStatus;
            return null;
        }
    }

    private static final String PRODUCT_ID_PREFIX = "test-product-";

    protected String getProductId(int index) {
        return new StringBuilder(PRODUCT_ID_PREFIX).
                append(String.format("%05d", index)).toString();
    }

    private static final String USER_ID_PREFIX = "test-user-";

    protected String getUserId(int index) {
        return new StringBuilder(USER_ID_PREFIX).
                append(String.format("%05d", index)).toString();
    }

    protected static String getNameFromId(String id) {
        StringBuilder buffer = new StringBuilder(id);
        for (int i = 0; i < buffer.length(); i++) {
            if (buffer.charAt(i) == '-') {
                buffer.setCharAt(i, ' ');
            }
        }
        return StringUtils.capitalize(buffer.toString());
    }

}
