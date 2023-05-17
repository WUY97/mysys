package com.tongtong.common.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public enum Role implements GrantedAuthority {
//    private static final String ADMIN = "Admin";
//    private static final String USER = "User";
//
//
//    @Override
//    public String toString() {
//        return name();
//    }
//
//    @Override
//    public String getAuthority() {
//        return name();
//    }
//
//    public static Role fromString(String roleStr) {
//        if (roleStr == null) {
//            return null;
//        }
//        return ROLE_MAP.get(roleStr.toLowerCase());
//    }
//
//    public static List<Role> fromRoleIds(List<String> userRoleIds) {
//        if (userRoleIds == null) {
//            return Collections.emptyList();
//        }
//        return userRoleIds.stream()
//                .map(Role::fromString)
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//    }
//
//    private static final Map<String, Role> ROLE_MAP = new HashMap<>();
//
//    static {
//        for (Role role : values()) {
//            ROLE_MAP.put(role.toString().toLowerCase(), role);
//        }
//    }


    ADMIN("Admin"),
    USER("User");

    private String role;

    Role(String role) {
        this.role = role;
    }

    public static Role getRole(String roleStr) {
        switch (roleStr) {
            case "Admin":
                return ADMIN;
            case "User":
                return USER;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return role;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    public static List<Role> getRoles(List<String> userRoleIds) {
        return userRoleIds.stream()
                .map(Role::getRole)
                .filter(role -> role != null)
                .collect(Collectors.toList());
    }
}
