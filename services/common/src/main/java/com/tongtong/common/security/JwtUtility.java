package com.tongtong.common.security;

import com.tongtong.common.entity.UserAuth;

public interface JwtUtility {

    public UserAuth parseToken(String token);

    public String generateToken(UserAuth u);
}
