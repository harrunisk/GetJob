package com.example.harun.getjob.profileModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mayne on 23.02.2018.
 */

public class yetenekModel implements Parcelable {

    private static final String TAG = "yetenekModel";
    public String yetenekName;
    public int rate;


//    public HashMap<String, ArrayList<yetenekModel>> yetenekHash = new HashMap<>();
//    public ArrayList<yetenekModel> hashyetenek = new ArrayList<>();


    public yetenekModel() {

    }

    public yetenekModel(String yetenekName, int rate) {

        this.yetenekName = yetenekName;
        this.rate = rate;
    }


    protected yetenekModel(Parcel in) {
        yetenekName = in.readString();
        rate = in.readInt();
    }

    public static final Creator<yetenekModel> CREATOR = new Creator<yetenekModel>() {
        @Override
        public yetenekModel createFromParcel(Parcel in) {
            return new yetenekModel(in);
        }

        @Override
        public yetenekModel[] newArray(int size) {
            return new yetenekModel[size];
        }
    };

    public String getYetenekName() {
        return yetenekName;
    }

    public void setYetenekName(String yetenekName) {
        this.yetenekName = yetenekName;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "yetenekModel{" +
                "yetenekName='" + yetenekName + '\'' +
                ", rate=" + rate +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(yetenekName);
        parcel.writeInt(rate);
    }


//    public HashMap<String, ArrayList<yetenekModel>> hashmapping(String position, yetenekModel model) {
//        hashyetenek.add(model);
//        yetenekHash.put(position, hashyetenek);
//
//   /*     for (yetenekModel mode : hashyetenek) {
//
//            Log.d("FOR ", "hashmapping: " + mode.getYetenekName() + "\t" + mode.getRate() + "\t" + model.getYetenekName());
//
//        }
//        Log.d("HASHMAPPİNGG ", "hashmapping: " + yetenekHash.entrySet());*/
//
//
//        //yetenekHash1 = yetenekHash;
//
//
//        return yetenekHash;
//    }

}
