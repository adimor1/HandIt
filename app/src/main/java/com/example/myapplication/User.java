package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
    private String email;
    private String name;
    private String description;
    private  String profession;
    private String seniority;

    public User(){
    }

    public User(String email, String name){
        this.email=email;
        this.name = name;
        this.description = "טרם הוספת תיאור";
        this.profession = "לא נקבע";
        this.seniority = "לא נקבע";
    }

    protected User(Parcel in) {
        email = in.readString();
        name = in.readString();
        description = in.readString();
        profession = in.readString();
        seniority = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getSeniority() {
        return seniority;
    }

    public void setSeniority(String seniority) {
        this.seniority = seniority;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(profession);
        dest.writeString(seniority);
    }
}
