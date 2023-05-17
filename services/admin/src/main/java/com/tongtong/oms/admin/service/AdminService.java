package com.tongtong.oms.admin.service;

import com.tongtong.common.config.AppConfig;
import com.tongtong.common.entity.Role;
import com.tongtong.common.security.Secured;
import com.tongtong.oms.admin.entity.OperationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppConfig.ADMIN_RESOURCE_PATH)
public class AdminService {

    @Autowired
    private AdminServiceImpl adminServiceBean;

    private String getUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private String getAuthHeader() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }


    @Secured({Role.ADMIN})
    @PostMapping(path = "/dataset",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createAppData(@RequestParam("userCount") Integer userCount,
                                                @RequestParam("productCount") Integer productCount) {
        OperationStatus operationStatus =
                adminServiceBean.createAppData(userCount, productCount, getAuthHeader());
        if (!operationStatus.isSuccess()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(operationStatus.getMessage());
        }
        return ResponseEntity.ok().body(operationStatus.getMessage());
    }


    @Secured({Role.ADMIN})
    @DeleteMapping(path = "/dataset", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteTestData() {
        OperationStatus operationStatus = adminServiceBean.deleteAppData(getAuthHeader());
        if (operationStatus.isSuccess())
            return ResponseEntity.ok().body(operationStatus.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(operationStatus.getMessage());
    }

}
