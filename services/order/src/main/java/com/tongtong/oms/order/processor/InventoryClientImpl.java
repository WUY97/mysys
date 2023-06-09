package com.tongtong.oms.order.processor;

import com.google.gson.Gson;
import com.tongtong.oms.order.entity.InventoryReservation;
import com.tongtong.oms.order.service.OrderServiceImpl;
import jakarta.annotation.PostConstruct;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class InventoryClientImpl implements InventoryClient {

    // Unused with OkHttp connection
    @Value("${InventorySvc.client.cp.size:10}")
    private int httpClientPoolSize;

    //private static HttpClient client;
    private static OkHttpClient client;

//    @Autowired
//    private LoadBalancerClient loadBalancer;

    @PostConstruct
    public void postConstruct() {
        // Each client has its own connection pool. Need one instance per class.
        client = new OkHttpClient();
    }

//    public LoadBalancerClient getLoadBalancer() {
//        return loadBalancer;
//    }
//
//    public void setLoadBalancer(LoadBalancerClient loadBalancer) {
//        this.loadBalancer = loadBalancer;
//    }

    @Override
    public boolean reserveInventory(InventoryReservation inventoryReservation) throws IOException {
        String url = "http://inventory-service-url";
//        ServiceInstance instance = getLoadBalancer().choose(ServiceID.InventorySvc.toString());
//        if (instance == null) {
//            throw new IOException("Inventory service is not available");
//        }
//        StringBuilder url = new StringBuilder()
//                .append("http://").append(instance.getHost())
//                .append(":").append(instance.getPort())
//                .append(AppConfig.INVENTORY_RESOURCE_PATH)
//                .append(AppConfig.INVENTORY_RESERVATION_PATH);
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
                return true;
            }
            return false;
        } catch (IOException e) {
            throw e;
        }
    }
}