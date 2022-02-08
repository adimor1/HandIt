package com.example.myapplication.Models;

public class LoginUser {
    public static String loginUserEmail;
    public static String loginUserAddress;

    public static void setLoginEmail(String email) {
        loginUserEmail = email;
    }

    public static String getLoginEmail() {
        return loginUserEmail;
    }

    public static void setLoginUserAddress(String location) {
        loginUserAddress = location;
    }

    public static String getLoginUserAddress() {
        return loginUserAddress;
    }

}
