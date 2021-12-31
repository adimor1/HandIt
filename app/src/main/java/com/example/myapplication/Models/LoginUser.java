package com.example.myapplication.Models;

public class LoginUser {
    public static String loginUserEmail;

    public static void setLoginEmail(String email) {
        loginUserEmail = email;
    }

    public static String getLoginEmail() {
        return loginUserEmail;
    }
}
