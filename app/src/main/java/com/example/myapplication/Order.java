package com.example.myapplication;

public class Order {
    private String clientEmail;
    private String proEmail;
    private int status = 0;
    private String time;
    private String date;

    public Order(String emailPro, String emailCl, String time, String date) {
        this.clientEmail = emailCl;
        this.proEmail = emailPro ;
        this.time = time;
        this.date = date;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public String getProEmail() {
        return proEmail;
    }

    public int getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public void setProEmail(String proEmail) {
        this.proEmail = proEmail;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
