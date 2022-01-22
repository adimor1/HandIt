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
    private String location;
    private String uriImage;
    private double longitude;
    private double latitude;

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getUriImage() {
        return uriImage;
    }

    public void setUriImage(String uriImage) {
        this.uriImage = uriImage;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isProf() {
        return isProf;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return phone;
    }

    public void setLocation(String location) {
        this.location = location;
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
            this.location= "Not entered";
            this.latitude=0;
            this.longitude=0;
            this.uriImage="https://firebasestorage.googleapis.com/v0/b/myapplication-67a01.appspot.com/o/empyavatar.png?alt=media&token=1ce9fd8f-d17a-4128-81ef-64d06d3189b9";
        }
    }

    protected User(Parcel in) {
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        description = in.readString();
        profession = in.readString();
        seniority = in.readString();
        phone =  in.readString();
        location = in.readString();
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
        dest.writeString(lastName);
        dest.writeString(description);
        dest.writeString(profession);
        dest.writeString(seniority);
        dest.writeString(phone);
        dest.writeString(location);
    }
}
