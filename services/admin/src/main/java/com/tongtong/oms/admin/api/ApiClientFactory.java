package com.tongtong.oms.admin.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tongtong.common.config.ServiceID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ApiClientFactory {

//    private final WebClient.Builder loadBalancedWebClientBuilder;

    @Value("${GatewaySvc.client.cp.size:100}")
    private int httpClientPoolSize;

    @Autowired
    private LoadBalancerClient loadBalancer;

    @Autowired
    private WebClient.Builder webClientBuilder;

//    public ApiClientFactory(WebClient.Builder webClientBuilder) {
//        this.loadBalancedWebClientBuilder = webClientBuilder;
//    }

    public ApiClient createApiClient(ServiceID serviceID,
                                     String authHeader) {
        ApiClient apiClient = null;
        switch (serviceID) {
            case AuthSvc: {
                apiClient = new UserAuthApiClient();
                break;
            }
            case UserProfileSvc: {
                apiClient = new UserProfileApiClient();
                break;
            }
            case ProductSvc: {
                apiClient = new ProductApiClient();
                break;
            }
            case InventorySvc: {
                apiClient = new InventoryApiClient();
                break;
            }
            case CartSvc: {
                apiClient = new CartApiClient();
                break;
            }
            case OrderSvc: {
                apiClient = new OrderApiClient();
                break;
            }
            case GatewaySvc: {
                apiClient = new GatewayApiClient();
                break;
            }
            default: {
                return null;
            }
        }
        apiClient.setWebClientBuilder(webClientBuilder);
        apiClient.setLoadBalancerClient(loadBalancer);
        apiClient.setAuthHeader(authHeader);
        apiClient.setMapper(new ObjectMapper());
        return apiClient;
    }

}
