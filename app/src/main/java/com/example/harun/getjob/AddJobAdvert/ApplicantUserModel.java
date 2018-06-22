package com.example.harun.getjob.AddJobAdvert;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mayne on 18.06.2018.
 */

public class ApplicantUserModel implements Parcelable {


    private static final String TAG = "ApplyAdvertModel";
    private String userID;
    private String applyDate;

    public ApplicantUserModel() {
    }

    public ApplicantUserModel(String jobID, String applyDate) {
        this.userID = jobID;
        this.applyDate = applyDate;
    }

    protected ApplicantUserModel(Parcel in) {
        userID = in.readString();
        applyDate = in.readString();
    }


    public static final Creator<ApplicantUserModel> CREATOR = new Creator<ApplicantUserModel>() {
        @Override
        public ApplicantUserModel createFromParcel(Parcel in) {
            return new ApplicantUserModel(in);
        }

        @Override
        public ApplicantUserModel[] newArray(int size) {
            return new ApplicantUserModel[size];
        }
    };

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userID);
        parcel.writeString(applyDate);
    }

    @Override
    public String toString() {
        return "ApplyAdvertModel{" +
                "userID='" + userID + '\'' +
                ", applyDate='" + applyDate + '\'' +
                '}';
    }
}
