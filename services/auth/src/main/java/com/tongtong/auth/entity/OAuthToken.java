package com.tongtong.auth.entity;

public class OAuthToken {
    private String access_token;
    private String token_type;
    private Integer expires_in;

    public OAuthToken() {
    }

    public OAuthToken(String access_token) {
        this.access_token = access_token;
        this.token_type = "Bearer";
        expires_in = 36000;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    @Override
    public String toString() {
        return "{" +
                "\"access_token\":" + (access_token == null ? "null" : "\"" + access_token + "\"") + ", " +
                "\"token_type\":" + (token_type == null ? "null" : "\"" + token_type + "\"") + ", " +
                "\"expires_in\":" + (expires_in == null ? "null" : "\"" + expires_in + "\"") +
                "}";
    }
}
