package com.example.harun.getjob.AddJobAdvert;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

/**
 * Created by mayne on 21.06.2018.
 */

public class MyAppliedAdvert extends AsyncTask<DataSnapshot, ArrayList<ApplyAdvertModel>, ArrayList<ApplyAdvertModel>> {
    private static final String TAG = "MyAppliedAdvert";
    private AppliedAdvertCallback appliedAdvertCallback;
    private ArrayList<ApplyAdvertModel> applyAdvertModelArrayList;


    public interface AppliedAdvertCallback {

        void getApliedAdvert(ArrayList<ApplyAdvertModel> result);


    }

    public MyAppliedAdvert(AppliedAdvertCallback appliedAdvertCallback) {
        this.appliedAdvertCallback = appliedAdvertCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<ApplyAdvertModel> doInBackground(DataSnapshot... dataSnapshots) {
        applyAdvertModelArrayList = new ArrayList<>();
        for (DataSnapshot appliedSnap : dataSnapshots[0].getChildren()) { //->ApplyAdvert

            if (appliedSnap.exists() && appliedSnap.hasChildren()) {

                Log.d(TAG, "BASVURDUGUM İLANLAR");
                applyAdvertModelArrayList.add(appliedSnap.getValue(ApplyAdvertModel.class));
                Log.d(TAG, "doInBackground: " + applyAdvertModelArrayList);

            } else {
                Log.d(TAG, "BASVURDUGUM İLAN  YOK DOLAYISIYLA İLAN ID DE  YOK  ");

            }


        }


        return applyAdvertModelArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<ApplyAdvertModel> applyAdvertModels) {
        super.onPostExecute(applyAdvertModels);
        Log.d(TAG, "onPostExecute: ");
        appliedAdvertCallback.getApliedAdvert(applyAdvertModels);

    }
}
