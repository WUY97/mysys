package com.tongtong.auth.mock;

import com.tongtong.auth.config.TestConfig;
import com.tongtong.common.entity.UserAuth;
import com.tongtong.common.security.JwtUtility;

public class MockJwtUtility implements JwtUtility {

    @Override
    public UserAuth parseToken(String token) {
        if (token.equals(TestConfig.DUMMY_TOKEN)) {
            return TestConfig.Test_Admin_Auth;
        }
        return null;
    }

    @Override
    public String generateToken(UserAuth userAuth) {
        return TestConfig.DUMMY_TOKEN;
    }

}
