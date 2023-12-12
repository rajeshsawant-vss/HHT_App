package com.tatamotors.errorproofing.model;

import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("username")
    String userName;
    @SerializedName("password")
    String password;

    public LoginModel(String userName, String pass) {
        this.userName = userName;
        this.password = pass;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
