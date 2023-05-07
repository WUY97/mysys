package com.tongtong.common.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import java.util.LinkedList;
import java.util.List;

public enum Role implements GrantedAuthority {

    ADMIN("Admin"),
    USER("User");

    private String role;

    private static final Logger logger = LoggerFactory.getLogger(Role.class);

    Role(String role) {
        this.role = role;
    }

    public static Role getRole(String roleStr) {
        if (roleStr.equals(ADMIN.toString())) {
            return ADMIN;
        } else if (roleStr.equals(USER.toString())) {
            return USER;
        }
        return null;
    }

    public String toString() {
        return role;
    }

    public String getAuthority() {
        return role;
    }

    public static List<Role> getRoles(List<String> userRoleIds) {
        List<Role> roles = new LinkedList<>();
        for (String roleId : userRoleIds) {
            Role role = Role.getRole(roleId);
            if (role == null) {
                logger.error("Unidentified role " + roleId);
                continue;
            }
            roles.add(role);
        }
        return roles;
    }

}
