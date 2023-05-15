package com.tongtong.auth.mock;

import com.tongtong.auth.config.TestConfig;
import com.tongtong.auth.core.AuthMgr;
import com.tongtong.common.entity.UserAuth;

import java.util.List;

public class MockAuth implements AuthMgr {
    @Override
    public boolean createUser(UserAuth userAuth) {
        if (userAuth.getId().equals(TestConfig.Test_Admin_Auth.getId())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean authenticate(String userId, String password) {
        if (userId.equals(TestConfig.Test_Admin_Auth.getId())
                && password.equals(TestConfig.Test_Admin_Auth.getPassword())) {
            return true;
        }
        return false;
    }

    @Override
    public UserAuth getUserAuth(String userId) {
        return TestConfig.Test_Admin_Auth;
    }

    @Override
    public List<String> getUserRole(String userId) {
        if (userId.equals(TestConfig.Test_Admin_Auth.getId())) {
            return TestConfig.Test_Admin_Auth.getRoles();
        }
        return TestConfig.Test_User_Auth.getRoles();
    }

    @Override
    public boolean deleteUsers() {
        return false;
    }

    @Override
    public List<UserAuth> getAllUserAuth() {
        return null;
    }
}
