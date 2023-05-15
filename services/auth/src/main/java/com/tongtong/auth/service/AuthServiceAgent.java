package com.tongtong.auth.service;

import com.tongtong.common.config.AppConfig;
import com.tongtong.common.config.ServiceID;
import com.tongtong.common.status.ServiceAgent;
import com.tongtong.common.status.ServiceStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthServiceAgent extends ServiceAgent {

    @GetMapping(path = AppConfig.STATUS_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceStatus> getServiceStatus() {
        ServiceStatus status = getServiceStatus(ServiceID.AuthSvc);
        return ResponseEntity.ok(status);
    }

}
