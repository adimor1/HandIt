package com.example.myapplication.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class Order implements Parcelable{
    private String clientEmail;
    private String proEmail;
    private int status = 0;
    private String time;
    private String date;
    private String id;

    public Order(String emailPro, String emailCl, String time, String date) {
        this.clientEmail = emailCl;
        this.proEmail = emailPro ;
        this.time = time;
        this.date = date;
        this.id = UUID.randomUUID().toString();
    }

    public String getid(){
        return id;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public String getProEmail() {
        return proEmail;
    }

    public Order(){
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

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(date);
        dest.writeString(id);
        dest.writeInt(status);
    }

    protected Order(Parcel in) {
        time = in.readString();
        date = in.readString();
        id = in.readString();
        status=in.readInt();
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
