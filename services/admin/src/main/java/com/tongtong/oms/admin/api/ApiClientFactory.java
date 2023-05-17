package com.tongtong.oms.admin.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tongtong.common.config.ServiceID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiClientFactory {

    @Value("${GatewaySvc.client.cp.size:100}")
    private int httpClientPoolSize;

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
        apiClient.setAuthHeader(authHeader);
        apiClient.setMapper(new ObjectMapper());
        apiClient.setClient(new HttpClient(httpClientPoolSize));
        return apiClient;
    }

}
