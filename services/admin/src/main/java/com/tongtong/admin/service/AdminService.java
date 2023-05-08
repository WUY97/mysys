package com.tongtong.admin.service;

import com.tongtong.common.config.AppConfig;
import com.tongtong.common.entity.Role;
import com.tongtong.common.security.Secured;
import com.tongtong.admin.entity.OperationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppConfig.ADMIN_RESOURCE_PATH)
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

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
        logger.info("Create test data with userCount={} and productCount={}", userCount, productCount);
        OperationStatus operationStatus =
                adminServiceBean.createAppData(userCount, productCount, getAuthHeader());
        if (!operationStatus.isSuccess()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(operationStatus.getMessage());
        }
        logger.info("Created test data with userCount={} and productCount={}", userCount, productCount);
        return ResponseEntity.ok().body(operationStatus.getMessage());
    }


    @Secured({Role.ADMIN})
    @DeleteMapping(path = "/dataset", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteTestData() {
        logger.info("Delete data");
        OperationStatus operationStatus = adminServiceBean.deleteAppData(getAuthHeader());
        logger.info("Deleted data with status={}", operationStatus.toString());
        if (operationStatus.isSuccess())
            return ResponseEntity.ok().body(operationStatus.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(operationStatus.getMessage());
    }

}
