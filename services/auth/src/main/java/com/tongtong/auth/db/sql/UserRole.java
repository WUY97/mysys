package com.tongtong.auth.db.sql;

public class UserRole {
    private String id;
    private String role;

    public String getId() {
        return id;
    }

    public void setId(String userId) {
        this.id = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "{" +
                "\"userId\":" + (id == null ? "null" : "\"" + id + "\"") + ", " +
                "\"role\":" + (role == null ? "null" : "\"" + role + "\"") +
                "}";
    }
}
