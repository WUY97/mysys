package com.tongtong.common.security;

import com.tongtong.common.entity.Role;
import com.tongtong.common.entity.UserAuth;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class AppAuthentication implements Authentication {

    Principal principal;
    String credentials;
    List<Role> roles;
    boolean secure;
    String details;
    boolean isAuthenticated;
    UserAuth userAuth;

    public AppAuthentication(UserAuth userAuth, boolean secure, String authHeader) {
        this.principal = new AppPrincipal(userAuth.getId());
        this.credentials = userAuth.getPassword();
        this.roles = new LinkedList<>();
        for (String roleStr : userAuth.getRoles()) {
            this.roles.add(Role.getRole(roleStr));
        }
        this.secure = secure;
        this.details = authHeader;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        this.isAuthenticated = b;
    }

    @Override
    public String getName() {
        return principal.getName();
    }

    public boolean isSecure() {
        return secure;
    }

}