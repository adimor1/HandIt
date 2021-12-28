package com.example.myapplication;

public class User {
    private String email;
    private String name;
    private String description;

    public User(){
    }

    public User(String email, String name){
        this.email=email;
        this.name = name;
        this.description = "טרם הוספת תיאור";
    }

    public String getEmail(){
        return email;
    }

    public String getName(){
        return name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
