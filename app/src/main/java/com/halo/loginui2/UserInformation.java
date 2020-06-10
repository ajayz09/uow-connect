package com.halo.loginui2;

import android.text.TextUtils;

public class UserInformation {
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String userID;
    private String userName;
    private String country;

    public UserInformation() {
    }
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getFullName(){
        String fullName = TextUtils.isEmpty(lastName) ? firstName : firstName + " " + lastName;
        return fullName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
