package com.mustofa27.banksampah.model.entity;

import com.google.gson.annotations.SerializedName;

public class UserToken {
    public static String table = "user_token";
    @SerializedName("access_token")
    private String api_token;
    private String token_type;
    private int expires_in;

    public UserToken() {
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}
