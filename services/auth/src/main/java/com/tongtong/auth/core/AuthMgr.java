package com.tongtong.auth.core;

import java.util.List;

import com.tongtong.common.entity.UserAuth;

public interface AuthMgr {
    boolean createUser(UserAuth userAuth);

    boolean authenticate(String userId, String password);

    UserAuth getUserAuth(String userId);

    List<UserAuth> getAllUserAuth();

    List<String> getUserRole(String userId);

    boolean deleteUsers();
}
