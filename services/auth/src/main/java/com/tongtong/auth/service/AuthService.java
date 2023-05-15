package com.tongtong.auth.service;

import com.tongtong.auth.entity.OAuthToken;
import com.tongtong.common.config.AppConfig;
import com.tongtong.common.entity.UserAuth;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(AppConfig.AUTHORIZATION_RESOURCE_PATH)
public class AuthService {

    @Autowired
    private AuthServiceImpl authServiceBean;

    public AuthServiceImpl getAuthServiceBean() {
        return authServiceBean;
    }

    /**
     * This method is used for generating auth token by posting user cred as json
     * This method is open to all roles
     *
     * @param userAuth json representation of UserAuth object
     * @return auth token in http response body
     */
    @PostMapping(path = AppConfig.AUTH_TOKEN_PATH, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity postUserCred(HttpServletRequest httpServletRequest,
                                       @RequestBody UserAuth userAuth) {
        ResponseEntity result;

        if (!authenticateClient(httpServletRequest)) {
            result = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Client Credentials");
        } else {
            String userId = userAuth.getId();
            String password = userAuth.getPassword();
            OAuthToken authToken = getAuthServiceBean().getOAuthToken(userId, password);
            result = createTokenResponse(userAuth.getId(), authToken);
        }

        return result;
    }

    /**
     * This method is used for generating auth token by posting user cred as form parameters
     * This method is open to all roles
     *
     * @param userId   user id as form parameter
     * @param password user password as form parameter
     * @return access token as http response
     */
    @PostMapping(path = AppConfig.AUTH_TOKEN_PATH, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity postUserAuth(HttpServletRequest httpServletRequest,
                                       @RequestParam("username") String userId,
                                       @RequestParam("password") String password) {
        ResponseEntity result;

        if (!authenticateClient(httpServletRequest)) {
            result = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Client Credentials");
        } else {
            OAuthToken authToken = getAuthServiceBean().getOAuthToken(userId, password);
            result = createTokenResponse(userId, authToken);
        }

        return result;
    }

    /**
     * This method decrypts and returns user auth information provided in user auth token
     * This method is open to all roles
     *
     * @param accessToken user access token
     * @return user auth object with user authentication details
     */
    @GetMapping(path = AppConfig.AUTH_TOKEN_USER_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserAuth> getUserCred(@RequestParam("access_token") String accessToken) {
        ResponseEntity<UserAuth> result;
        if (accessToken == null || accessToken.equals("")) {
            result = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            UserAuth userAuth = getAuthServiceBean().getUserAuth(accessToken);
            result = ResponseEntity.ok().
                    header("Pragma", "no-cache").
                    body(userAuth);
        }

        return result;
    }

    /**
     * This method can only be called by admin to show all user auth records.
     *
     * @return list of user auth records
     */
    @Secured({"Admin"})
    @GetMapping(path = AppConfig.USERS_AUTH_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserAuth>> getUserAuthList() {
        ResponseEntity<List<UserAuth>> result;
        List<UserAuth> userAuthList = getAuthServiceBean().getUserAuthList();
        if (userAuthList == null) {
            result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } else {
            result = ResponseEntity.ok().body(userAuthList);
        }
        return result;
    }


    /**
     * This method can only be called by admin to insert a new user auth record
     *
     * @param userAuth user id as form parameter
     * @return user auth object with user authentication details
     */
    @Secured({"Admin"})
    @PutMapping(path = AppConfig.USER_AUTH_PATH, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserAuth> createUserAuth(@PathVariable("id") String id,
                                                   @RequestBody UserAuth userAuth) {
        ResponseEntity<UserAuth> result;
        if (!id.equals(userAuth.getId())) {
            result = ResponseEntity.badRequest().build();
        } else if (getAuthServiceBean().createUserAuth(userAuth)) {
            result = ResponseEntity.ok().header("Pragma", "no-cache").body(userAuth);
        } else {
            result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return result;
    }


    /**
     * Delete all users. Usage is limited to admin role.
     *
     * @return
     */
    @Secured({"Admin"})
    @DeleteMapping(path = AppConfig.USERS_AUTH_PATH, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteUsersAuth() {
        ResponseEntity<String> result;
        if (getAuthServiceBean().deleteUsersAuth()) {
            result = ResponseEntity.ok("USERS AUTH DELETED");
        } else {
            result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("USERS AUTH NOT DELETED");
        }
        return result;
    }

    private ResponseEntity<OAuthToken> createTokenResponse(String userId, OAuthToken authToken) {
        ResponseEntity<OAuthToken> result;
        if (authToken == null) {
            result = new ResponseEntity(null, HttpStatus.FORBIDDEN);
        } else {
            CacheControl cc = CacheControl.maxAge(3600, TimeUnit.SECONDS);
            result = ResponseEntity.ok().cacheControl(cc).header("Pragma", "no-cache").body(authToken);
        }

        return result;
    }

    private boolean authenticateClient(HttpServletRequest httpServletRequest) {
        boolean result = false;
        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.toLowerCase().startsWith("basic")) {
            result = getAuthServiceBean().authenticateClient(authHeader);
        }
        return result;
    }

}