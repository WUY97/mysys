package com.tongtong.oms.order.processor;

import com.google.gson.Gson;
import com.tongtong.common.config.AppConfig;
import com.tongtong.common.config.ServiceID;
import com.tongtong.oms.order.entity.InventoryReservation;
import com.tongtong.oms.order.service.OrderServiceImpl;
import jakarta.annotation.PostConstruct;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;


import java.io.IOException;


@Component
public class InventoryClientImpl implements InventoryClient {

    // Unused with OkHttp connection
    @Value("${InventorySvc.client.cp.size:10}")
    private int httpClientPoolSize;

    //private static HttpClient client;
    private static OkHttpClient client;

    @Autowired
    private LoadBalancerClient loadBalancer;

    @PostConstruct
    public void postConstruct() {
        // Each client has its own connection pool. Need one instance per class.
        client = new OkHttpClient();
    }

    public LoadBalancerClient getLoadBalancer() {
        return loadBalancer;
    }

    @Override
    public boolean reserveInventory(InventoryReservation inventoryReservation) throws IOException {
        ServiceInstance instance = getLoadBalancer().choose(ServiceID.InventorySvc.toString());
        if (instance == null) {
        }
        StringBuilder url = new StringBuilder()
                .append("http://").append(instance.getHost())
                .append(":").append(instance.getPort())
                .append(AppConfig.INVENTORY_RESOURCE_PATH)
                .append(AppConfig.INVENTORY_RESERVATION_PATH);
        String invResJson = (new Gson()).toJson(inventoryReservation);
        RequestBody body = RequestBody.create(invResJson, okhttp3.MediaType.parse("application/json; charset=utf-8"));
        Request.Builder requestBuilder = new Request.Builder()
                .url(url.toString())
                .post(body);
        String authHeader = OrderServiceImpl.getThreadLocal().get();
        requestBuilder.addHeader("Authorization", authHeader);
        Request request = requestBuilder.build();
        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 200) {
            }
            return false;
        } catch (IOException e) {
            throw e;
        }
    }
}