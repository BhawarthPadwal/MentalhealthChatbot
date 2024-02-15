package com.bae.dialogflowbot.models;

public class Data {

    String userName, userEmail, userContact, userAge, userGender, imageUrl;

    public Data(String userName, String userEmail, String userContact, String userAge, String userGender, String imageUrl) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userContact = userContact;
        this.userAge = userAge;
        this.userGender = userGender;
        this.imageUrl = imageUrl;
    }

    public Data() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
