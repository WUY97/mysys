package com.tongtong.common.security;

import java.security.Principal;

public class AppPrincipal implements Principal {

    private String name;

    public AppPrincipal(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Principal name can not be null");
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppPrincipal that = (AppPrincipal) o;

        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
