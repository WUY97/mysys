package com.tongtong.oms.admin.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tongtong.common.config.AppConfig;
import com.tongtong.common.config.ServiceID;
import com.tongtong.common.status.ServiceStatus;
import com.tongtong.oms.admin.entity.OperationStatus;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public abstract class ApiClient {

    protected String authHeader;
    protected WebClient.Builder webClientBuilder;
    protected LoadBalancerClient loadBalancerClient;
    protected ObjectMapper mapper;

    public void setAuthHeader(String authHeader) {
        this.authHeader = authHeader;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void setWebClientBuilder(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public void setLoadBalancerClient(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
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
        WebClient client = webClientBuilder.build();
        ServiceInstance instance = loadBalancerClient.choose(getEndpointServiceID().toString());

        OperationStatus operationStatus = new OperationStatus();
        operationStatus.setSuccess(false);
        operationStatus.setMessage("Unknown host for: " + getEndpointServiceID().toString());

        if (instance == null) {
            return operationStatus;
        }

        String baseUrl = instance.getUri().toString();
        String url = baseUrl + uri;

        try {
            client.put()
                    .uri(url)
                    .header("Authorization", authHeader)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(data)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();  // block here to make it synchronous

            operationStatus.setSuccess(true);
            operationStatus.setMessage("Inserted data: " + data);
            return operationStatus;
        } catch (Exception e) {
            operationStatus.setSuccess(false);
            operationStatus.setMessage("Error inserting data: " + e.getMessage());
        }

        return operationStatus;
    }

    public OperationStatus deleteData() {
        OperationStatus operationStatus = new OperationStatus();
        operationStatus.setSuccess(false);
        WebClient client = webClientBuilder.build();
        ServiceInstance instance = loadBalancerClient.choose(getEndpointServiceID().toString());

        if (instance == null) {
            operationStatus.setMessage("Unknown host for: " + getEndpointServiceID().toString());
            return operationStatus;
        }

        String baseUrl = instance.getUri().toString();
        String url = baseUrl + getServiceURI();
        try {
            client.delete()
                    .uri(url)
                    .header("Authorization", authHeader)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            operationStatus.setSuccess(true);
            operationStatus.setMessage("Deleted data");
        } catch (Exception e) {
            operationStatus.setSuccess(false);
            operationStatus.setMessage("Error deleting data: " + e.getMessage());
        }
        return operationStatus;
    }

    public ServiceStatus getStatus() {
        WebClient client = webClientBuilder.build();
        ServiceInstance instance = loadBalancerClient.choose(getEndpointServiceID().toString());
        ServiceStatus serviceStatus = new ServiceStatus(getEndpointServiceID().toString());
        if (instance == null) {
            serviceStatus.setServiceHost("Unknown");
            serviceStatus.setServiceTime("Service not available");
            return serviceStatus;
        }

        String baseUrl = instance.getUri().toString();
        String url = baseUrl + AppConfig.STATUS_PATH;

        try {
            String response = client.get()
                    .uri(url)
                    .header("Authorization", authHeader)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return new ObjectMapper().readValue(response, ServiceStatus.class);
        } catch (Exception e) {
            serviceStatus.setServiceHost(baseUrl);
            serviceStatus.setServiceTime("Not Reachable: " + e.getMessage());
            return serviceStatus;
        }
    }

    private static final String PRODUCT_ID_PREFIX = "test-product-";

    protected String getProductId(int index) {
        return PRODUCT_ID_PREFIX +
                String.format("%05d", index);
    }

    private static final String USER_ID_PREFIX = "test-user-";

    protected String getUserId(int index) {
        return USER_ID_PREFIX +
                String.format("%05d", index);
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
