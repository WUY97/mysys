package com.tongtong.admin.api;

import com.tongtong.common.config.AppConfig;
import com.tongtong.common.config.ServiceID;
import com.tongtong.admin.entity.OperationStatus;
import com.tongtong.admin.entity.UserAuth;
import org.springframework.util.StringUtils;

public class UserAuthApiClient extends ApiClient {

    public OperationStatus insertBootstrapData() {
        OperationStatus operationStatus = insertData("admin", createAdminUserAuth("admin"));
        if (!operationStatus.isSuccess())
            return operationStatus;
        operationStatus = insertData("john", createUserAuth("john"));
        if (!operationStatus.isSuccess())
            return operationStatus;
        operationStatus.setMessage("Inserted User Auth bootstrap data");
        return operationStatus;
    }

    @Override
    protected ServiceID getServiceID() {
        return ServiceID.AuthSvc;
    }

    @Override
    protected String getServiceURI() {
        return AppConfig.AUTHORIZATION_RESOURCE_PATH + AppConfig.USERS_AUTH_PATH;
    }

    @Override
    protected Object createObject(String id) {
        return createUserAuth(id);
    }

    @Override
    protected String getObjectId(int index) {
        return getUserId(index);
    }

    private static UserAuth createAdminUserAuth(String userId) {
        UserAuth userAuth = new UserAuth();
        userAuth.setId(userId);
        userAuth.setPassword("password");
        userAuth.setName(StringUtils.capitalize(userId));
        userAuth.getRoles().add("Admin");
        userAuth.getRoles().add("User");
        userAuth.setEmailId("user." + userId + "@test.com");
        return userAuth;
    }

    private static UserAuth createUserAuth(String userId) {
        UserAuth userAuth = new UserAuth();
        userAuth.setId(userId);
        userAuth.setPassword("password");
        userAuth.setName(getNameFromId(userId));
        userAuth.getRoles().add("User");
        userAuth.setEmailId("user." + userId + "@test.com");
        return userAuth;
    }

}
