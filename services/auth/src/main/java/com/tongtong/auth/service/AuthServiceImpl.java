package com.tongtong.auth.service;

import com.tongtong.auth.core.AuthMgr;
import com.tongtong.auth.core.AuthMgrFactory;
import com.tongtong.auth.entity.OAuthToken;
import com.tongtong.common.entity.UserAuth;
import com.tongtong.common.security.JwtUtility;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;

@Component
public class AuthServiceImpl {

    @Autowired
    private AuthMgrFactory authMgrFactory;

    private AuthMgr authBean;

    @Autowired
    private JwtUtility jwtUtilBean;

    @Value("${database.type}")
    private String authMgrType;

    @Value("${auth.client.id:web-client}")
    private String authClientId;

    @Value("${auth.client.secret:secret}")
    private String authClientSecret;

    @PostConstruct
    public void postConstruct() {
        this.authBean = authMgrFactory.getAuthMgr(authMgrType);
    }

    public void setAuthBean(AuthMgr authBean) {
        this.authBean = authBean;
    }

    public AuthMgr getAuthBean() {
        return authBean;
    }

    public void setJwtUtilBean(JwtUtility jwtUtilBean) {
        this.jwtUtilBean = jwtUtilBean;
    }

    public JwtUtility getJwtUtilBean() {
        return jwtUtilBean;
    }

    public OAuthToken getOAuthToken(String userId, String password) {
        boolean isAuthenticated = getAuthBean().authenticate(userId, password);

        if (!isAuthenticated) {
            return null;
        }

        UserAuth userAuth = getAuthBean().getUserAuth(userId);
        List<String> userRoleIds = getAuthBean().getUserRole(userId);
        userAuth.setRoles(userRoleIds);

        String token = getJwtUtilBean().generateToken(userAuth);
        OAuthToken authToken = new OAuthToken(token);

        return authToken;
    }

    public UserAuth getUserAuth(String accessToken) {
        if (accessToken == null) {
            return null;
        }
        return getJwtUtilBean().parseToken(accessToken);
    }

    public List<UserAuth> getUserAuthList() {
        List<UserAuth> userAuthList = getAuthBean().getAllUserAuth();
        userAuthList.sort(new Comparator<UserAuth>() {
            @Override
            public int compare(UserAuth o1, UserAuth o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        return userAuthList;
    }

    public boolean createUserAuth(UserAuth userAuth) {
        return getAuthBean().createUser(userAuth);
    }

    public boolean deleteUsersAuth() {
        return getAuthBean().deleteUsers();
    }

    /**
     * @param basicAuthHeader Authorization: Basic base64(user:pass)
     * @return true if client is authenticated
     */
    boolean authenticateClient(String basicAuthHeader) {
        String base64Credentials = basicAuthHeader.substring("Basic".length()).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String userColonPass = new String(credDecoded, StandardCharsets.UTF_8);
        final String[] credentials = userColonPass.split(":", 2);
        if (credentials[0].equals(authClientId)) {
            if (credentials[1].equals(authClientSecret)) {
                return true;
            }
        }
        return false;
    }

}