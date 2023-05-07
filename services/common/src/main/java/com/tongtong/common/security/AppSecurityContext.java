package com.tongtong.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

public class AppSecurityContext implements SecurityContext {

    Authentication authentication;

    public AppSecurityContext(Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public Authentication getAuthentication() {
        return authentication;
    }

    @Override
    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }
}
