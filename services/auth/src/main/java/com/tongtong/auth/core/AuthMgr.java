package com.tongtong.auth.core;

import com.tongtong.common.entity.UserAuth;

import java.util.List;

public interface AuthMgr {
    boolean createUser(UserAuth userAuth);

    boolean authenticate(String userId, String password);

    UserAuth getUserAuth(String userId);

    List<UserAuth> getAllUserAuth();

    List<String> getUserRole(String userId);

    boolean deleteUsers();
}
