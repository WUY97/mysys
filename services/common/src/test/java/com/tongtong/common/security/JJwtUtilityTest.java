package com.tongtong.common.security;

import com.tongtong.common.entity.UserAuth;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

/**
 * Unit test for simple App.
 */
public class JJwtUtilityTest {
    private UserAuth userAuth;
    private String testToken;

    @BeforeEach
    public void setUp() {
        userAuth = createUserAuth();
        testToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBbnVyYWcgWWFkYXYiLCJpZCI6ImFudXJhZyIsIn" +
                "JvbGUiOiJBZG1pbiBVc2VyICIsImVtYWlsSWQiOiJhbnVyYWcueWFkYXZAbmV3dGVjaHdheXMuY29tIn0.ZZJDeH3rRkOhnnu91k3bd0O3Qog9BHX1DTwf9Oboslr75UuhMj7hob0WvXJT3cMqRnnesv0bkUVExosrMigmPA";
    }

    @Test
    public void testGenerateToken() {
        JJwtUtility jwt = new JJwtUtility();
        String token = jwt.generateToken(userAuth);
        System.out.println(token);
        Assertions.assertEquals(testToken, token);
    }

    @Test
    public void testParseToken() {
        JJwtUtility jwt = new JJwtUtility();
        UserAuth parsedUserAuth = jwt.parseToken(testToken);
        Assertions.assertNotNull(parsedUserAuth);
        Assertions.assertEquals(parsedUserAuth.getId(), userAuth.getId());
        Assertions.assertEquals(parsedUserAuth.getName(), userAuth.getName());
        Assertions.assertEquals(parsedUserAuth.getEmailId(), userAuth.getEmailId());
        Assertions.assertEquals(parsedUserAuth.getRoles().get(0), userAuth.getRoles().get(0));
        Assertions.assertEquals(parsedUserAuth.getRoles().get(1), userAuth.getRoles().get(1));
    }

    private UserAuth createUserAuth() {
        UserAuth userAuth = new UserAuth();
        userAuth.setId("anurag");
        userAuth.setPassword("password");
        userAuth.setName("Anurag Yadav");
        userAuth.getRoles().add("Admin");
        userAuth.getRoles().add("User");
        userAuth.setEmailId("anurag.yadav@newtechways.com");
        return userAuth;
    }
}