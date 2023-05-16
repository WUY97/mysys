package com.tongtong.product.service;

import com.tongtong.common.config.AppConfig;
import com.tongtong.common.config.ServiceID;
import com.tongtong.common.status.ServiceAgent;
import com.tongtong.common.status.ServiceStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductServiceAgent extends ServiceAgent {

    @GetMapping(path = AppConfig.STATUS_PATH, produces = "application/json")
    public ResponseEntity<ServiceStatus> getServiceStatus() {
        ServiceStatus status = getServiceStatus(ServiceID.ProductSvc);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

}