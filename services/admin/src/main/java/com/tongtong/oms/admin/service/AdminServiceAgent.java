package com.tongtong.oms.admin.service;

import com.tongtong.common.config.AppConfig;
import com.tongtong.common.config.ServiceID;
import com.tongtong.common.status.DatabaseStatus;
import com.tongtong.common.status.ServiceAgent;
import com.tongtong.common.status.ServiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminServiceAgent extends ServiceAgent {

    @Autowired
    private AdminServiceImpl adminServiceBean;

    @GetMapping(path = AppConfig.STATUS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getServiceStatus() {
        ServiceStatus status = getServiceStatus(ServiceID.AdminSvc);
        return ResponseEntity.ok().body(status.toJson());
    }

    @GetMapping(path = AppConfig.ADMIN_RESOURCE_PATH + AppConfig.SERVICE_STATUS_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ServiceStatus>> getServicesStatus() {
        List<ServiceStatus> status = adminServiceBean.getServicesStatus();
        return ResponseEntity.ok().body(status);
    }

    @GetMapping(path = AppConfig.ADMIN_RESOURCE_PATH + AppConfig.DB_STATUS_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DatabaseStatus>> getDBStatus() {
        List<DatabaseStatus> status = adminServiceBean.getDBStatus();
        return ResponseEntity.ok().body(status);
    }

}
