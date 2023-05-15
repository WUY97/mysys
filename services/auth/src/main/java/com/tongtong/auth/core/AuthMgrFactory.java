package com.tongtong.auth.core;

public interface AuthMgrFactory {
    AuthMgr getAuthMgr(String authMgrType);
}