package com.example.harun.getjob.profileModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mayne on 6.03.2018.
 */

public class genelBilgiModel implements Parcelable {

    private String phone, e_mail, birthday, ehliyet, askerlik;

    public genelBilgiModel(String phone, String e_mail, String birthday, String ehliyet, String askerlik) {
        this.phone = phone;
        this.e_mail = e_mail;
        this.birthday = birthday;
        this.ehliyet = ehliyet;
        this.askerlik = askerlik;
    }

    public genelBilgiModel() {
    }

    ;

    protected genelBilgiModel(Parcel in) {
        phone = in.readString();
        e_mail = in.readString();
        birthday = in.readString();
        ehliyet = in.readString();
        askerlik = in.readString();
    }

    public static final Creator<genelBilgiModel> CREATOR = new Creator<genelBilgiModel>() {
        @Override
        public genelBilgiModel createFromParcel(Parcel in) {
            return new genelBilgiModel(in);
        }

        @Override
        public genelBilgiModel[] newArray(int size) {
            return new genelBilgiModel[size];
        }
    };

    public String getPhone() {

      //  if (this.phone == null) return "Belirtilmemiş";
      //  else
            return phone;

    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getE_mail() {

      //  if (this.e_mail == null) return "Belirtilmemiş";
       // else
            return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getBirthday() {

       // if (this.birthday == null) return "Belirtilmemiş";
        //else
            return birthday;


    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEhliyet() {
       // if (this.ehliyet == null) return "Belirtilmemiş";
     //   else
            return ehliyet;


    }

    public void setEhliyet(String ehliyet) {
        this.ehliyet = ehliyet;
    }

    public String getAskerlik() {


      //  if (this.askerlik == null) return "Belirtilmemiş";
       // else
            return askerlik;

    }

    public void setAskerlik(String askerlik) {
        this.askerlik = askerlik;
    }

    @Override
    public String toString() {
        return "genelBilgiModel{" +
                "phone='" + phone + '\'' +
                ", e_mail='" + e_mail + '\'' +
                ", birthday='" + birthday + '\'' +
                ", ehliyet='" + ehliyet + '\'' +
                ", askerlik='" + askerlik + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(phone);
        parcel.writeString(e_mail);
        parcel.writeString(birthday);
        parcel.writeString(ehliyet);
        parcel.writeString(askerlik);
    }
}
