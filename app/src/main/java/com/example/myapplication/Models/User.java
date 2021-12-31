package com.example.myapplication.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
    private String firstName;
    private String lastName;
    private String email;
    private boolean isProf = false;
    private String description;
    private String profession;
    private String seniority;
    private String phone;

    public String getLastName() {
        return lastName;
    }

    public boolean isProf() {
        return isProf;
    }

    public String getPhone() {
        return phone;
    }

    public int getSumRating() {
        return sumRating;
    }

    public int getCountRating() {
        return countRating;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProf(boolean prof) {
        isProf = prof;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSumRating(int sumRating) {
        this.sumRating = sumRating;
    }

    public void setCountRating(int countRating) {
        this.countRating = countRating;
    }

    private int sumRating;
    private int countRating;

    public User(){
    }

    public User(String firstName, String lastName, String email, boolean isProf){
        this.isProf = isProf;
        this.email=email;
        this.firstName = firstName;
        this.lastName = lastName;

        if(isProf){
            this.description = "Not entered";
            this.profession = "Not entered";
            this.seniority = "Not entered";
            this.phone="Not entered";
            this.sumRating=0;
            this.countRating=0;
        }
    }

    protected User(Parcel in) {
        email = in.readString();
        firstName = in.readString();
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

    public String getFirstName(){
        return firstName;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setFirstNameName(String firstName){
        this.firstName = firstName;
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
        dest.writeString(firstName);
        dest.writeString(description);
        dest.writeString(profession);
        dest.writeString(seniority);
    }
}
