package com.tongtong.oms.admin.entity;

public class Contact {
    private String name;
    private String telephone;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\":" + (name == null ? "null" : "\"" + name + "\"") + ", " +
                "\"telephone\":" + (telephone == null ? "null" : "\"" + telephone + "\"") + ", " +
                "\"email\":" + (email == null ? "null" : "\"" + email + "\"") +
                "}";
    }
}
