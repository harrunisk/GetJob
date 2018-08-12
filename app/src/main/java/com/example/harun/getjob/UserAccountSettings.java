package com.example.harun.getjob;

/**
 * Created by mayne on 2.12.2017.
 */

public class UserAccountSettings {

    private String eMail, userID, password, phoneNumber, signUpDate;
    private Boolean verificationStatus;

    public UserAccountSettings() {
    }

    public UserAccountSettings(String eMail, String userID, String password, String phoneNumber, String signUpDate, Boolean verificationStatus) {
        this.eMail = eMail;
        this.userID = userID;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.signUpDate = signUpDate;
        this.verificationStatus = verificationStatus;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSignUpDate() {
        return signUpDate;
    }

    public void setSignUpDate(String signUpDate) {
        this.signUpDate = signUpDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(Boolean verificationStatus) {
        this.verificationStatus = verificationStatus;
    }


    @Override
    public String toString() {
        return "UserAccountSettings{" +
                "eMail='" + eMail + '\'' +
                ", userID='" + userID + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", verificationStatus=" + verificationStatus +
                ", signUpDate=" + signUpDate +
                '}';
    }
}
