package com.example.harun.getjob.profileModel;

/**
 * Created by mayne on 9.03.2018.
 */


import android.os.Parcel;
import android.os.Parcelable;

import com.example.harun.getjob.FirebaseMethods.MainContent;

import java.util.ArrayList;

/**
 * Tüm liste modelleri burada toplayıp tek seferde firebaseden verileri çekip  buaraya eşitleyip buradan
 * profilPage doldurma ayrıca putExtra  ile  Edit Profile gönderebilmek için parcelable yapıyorum.
 * işlemi gerçekleştirecim
 */
public class AllModelsList implements Parcelable {

    private ArrayList<egitimListModel> megitimListModel;
    private ArrayList<deneyimModel> mdeneyimListModel;
    private ArrayList<yetenekModel> myetenekListModel;
    private genelBilgiModel mgenelBilgiModel;
    private MainContent mfirebaseContent;
    private String about_me;
    private String profilePhotoUrl;


    public AllModelsList() {
    }

    public AllModelsList(ArrayList<egitimListModel> megitimListModel,
                         ArrayList<deneyimModel> mdeneyimListModel,
                         ArrayList<yetenekModel> myetenekListModel,
                         genelBilgiModel mgenelBilgiModel,
                         MainContent mfirebaseContent,
                         String about_me,
                         String profilePhotoUrl) {

        this.megitimListModel = megitimListModel;
        this.mdeneyimListModel = mdeneyimListModel;
        this.myetenekListModel = myetenekListModel;
        this.mgenelBilgiModel = mgenelBilgiModel;
        this.mfirebaseContent = mfirebaseContent;
        this.about_me = about_me;
        this.profilePhotoUrl = profilePhotoUrl;
    }


    protected AllModelsList(Parcel in) {
        megitimListModel = in.createTypedArrayList(egitimListModel.CREATOR);
        mdeneyimListModel = in.createTypedArrayList(deneyimModel.CREATOR);
        myetenekListModel = in.createTypedArrayList(yetenekModel.CREATOR);

        mgenelBilgiModel = in.readParcelable(genelBilgiModel.class.getClassLoader());
        mfirebaseContent = in.readParcelable(MainContent.class.getClassLoader());
        about_me = in.readString();
        profilePhotoUrl = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        //Bunlar parcelable  Liste
        dest.writeTypedList(megitimListModel);
        dest.writeTypedList(mdeneyimListModel);
        dest.writeTypedList(myetenekListModel);


        //Parcelable  sınıf
        dest.writeParcelable(mgenelBilgiModel, flags);
        dest.writeParcelable(mfirebaseContent, flags);

        //Parcelable string
        dest.writeString(about_me);
        dest.writeString(profilePhotoUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AllModelsList> CREATOR = new Creator<AllModelsList>() {
        @Override
        public AllModelsList createFromParcel(Parcel in) {
            return new AllModelsList(in);
        }

        @Override
        public AllModelsList[] newArray(int size) {
            return new AllModelsList[size];
        }
    };

    public ArrayList<egitimListModel> getMegitimListModel() {
        return megitimListModel;
    }

    public void setMegitimListModel(ArrayList<egitimListModel> megitimListModel) {
        this.megitimListModel = megitimListModel;
    }

    public ArrayList<deneyimModel> getMdeneyimListModel() {
        return mdeneyimListModel;
    }

    public void setMdeneyimListModel(ArrayList<deneyimModel> mdeneyimListModel) {
        this.mdeneyimListModel = mdeneyimListModel;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public ArrayList<yetenekModel> getMyetenekListModel() {
        return myetenekListModel;
    }

    public void setMyetenekListModel(ArrayList<yetenekModel> myetenekListModel) {
        this.myetenekListModel = myetenekListModel;
    }

    public genelBilgiModel getMgenelBilgiModel() {
        return mgenelBilgiModel;
    }

    public void setMgenelBilgiModel(genelBilgiModel mgenelBilgiModel) {
        this.mgenelBilgiModel = mgenelBilgiModel;
    }

    public MainContent getMfirebaseContent() {
        return mfirebaseContent;
    }

    public void setMfirebaseContent(MainContent mfirebaseContent) {
        this.mfirebaseContent = mfirebaseContent;
    }

    @Override
    public String toString() {
        return "AllModelsList{" +
                "megitimListModel=" + megitimListModel +
                ", mdeneyimListModel=" + mdeneyimListModel +
                ", myetenekListModel=" + myetenekListModel +
                ", mgenelBilgiModel=" + mgenelBilgiModel +
                ", mfirebaseContent=" + mfirebaseContent +
                ", about_me='" + about_me + '\'' +
                ", profilePhotoUrl='" + profilePhotoUrl + '\'' +
                '}';
    }


}
