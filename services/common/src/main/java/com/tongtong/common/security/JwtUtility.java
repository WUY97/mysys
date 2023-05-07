package com.tongtong.common.security;

import com.tongtong.common.entity.UserAuth;

/**
 * JwtUtility provides helper interface over JWT Token libraries
 */
public interface JwtUtility {

    public UserAuth parseToken(String token);

    public String generateToken(UserAuth u);
}