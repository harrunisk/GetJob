package com.example.harun.getjob.AddJobAdvert;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mayne on 18.06.2018.
 */

public class ApplyAdvertModel implements Parcelable {


    private static final String TAG = "ApplyAdvertModel";
    private String jobID;
    private String applyDate;
    private boolean isSave;
    private boolean isApply;

    public ApplyAdvertModel(String jobID, String applyDate, boolean isSave, boolean isApply) {
        this.jobID = jobID;
        this.applyDate = applyDate;
        this.isSave = isSave;
        this.isApply = isApply;
    }
    public ApplyAdvertModel() {
    }

    protected ApplyAdvertModel(Parcel in) {
        jobID = in.readString();
        applyDate = in.readString();
        isSave = in.readByte() != 0;
        isApply = in.readByte() != 0;
    }

    public static final Creator<ApplyAdvertModel> CREATOR = new Creator<ApplyAdvertModel>() {
        @Override
        public ApplyAdvertModel createFromParcel(Parcel in) {
            return new ApplyAdvertModel(in);
        }

        @Override
        public ApplyAdvertModel[] newArray(int size) {
            return new ApplyAdvertModel[size];
        }
    };

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }


    public boolean getSave() {
        return isSave;
    }

    public void setSave(boolean save) {
        isSave = save;
    }

    public boolean getApply() {
        return isApply;
    }

    public void setApply(boolean apply) {
        isApply = apply;
    }



    @Override
    public String toString() {
        return "ApplyAdvertModel{" +
                "jobID='" + jobID + '\'' +
                ", applyDate='" + applyDate + '\'' +
                ", isSave=" + isSave +
                ", isApply=" + isApply +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(jobID);
        parcel.writeString(applyDate);
        parcel.writeByte((byte) (isSave ? 1 : 0));
        parcel.writeByte((byte) (isApply ? 1 : 0));
    }
}
