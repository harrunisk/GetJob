package com.example.harun.getjob.AddJobAdvert;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.harun.getjob.JobSearch.JobUtils.JobAdvertModel2;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

/**
 * Created by mayne on 5.06.2018.
 */

public class RetainJobAdvertFromFirebase extends AsyncTask<DataSnapshot, ArrayList<JobAdvertModel2>, ArrayList<JobAdvertModel2>> {
    private static final String TAG = "RetainJobAdvertFromFire";
    //private String databaseName = "jobAdvert"; //SaveJobAdvertToFirebase Database name
    private JobAdvertFromFirebase fromFirebase;
    private ArrayList<JobAdvertModel2> jobAdverts;
    private ArrayList<ApplicantUserModel> applicantUserModelArrayList;

   // HashMap<String, Boolean> basvuruYapılmısmı = new HashMap<>();


    public interface JobAdvertFromFirebase {
        void JobAdvertFromFirebaseCallback(ArrayList<JobAdvertModel2> result);
    }

    public RetainJobAdvertFromFirebase(Activity activity, JobAdvertFromFirebase _jobAdvertFromFirebase) {
        Log.d(TAG, "RetainJobAdvertFromFirebase: ");
        this.fromFirebase = _jobAdvertFromFirebase;

    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute: ");
        super.onPreExecute();
     /*   if (mWeakReference.get().isFinishing() || mWeakReference.get() == null) {
            Log.d(TAG, "onPreExecute: WeakReference.get().isFinishing() || mWeakReference.get() == null ");
            return;
        }*/
        //  mWeakReference.get().findViewById(R.id.mapProgress).setVisibility(View.VISIBLE);

    }

    @Override
    protected ArrayList<JobAdvertModel2> doInBackground(DataSnapshot... dataSnapshots) {
        Log.d(TAG, "doInBackground: ");
        jobAdverts = new ArrayList<JobAdvertModel2>();
        applicantUserModelArrayList = new ArrayList<ApplicantUserModel>();
        try {
            for (DataSnapshot child : dataSnapshots[0].getChildren()) {
                if (child.exists()) {
                    Log.d(TAG, "doInBackground: child.exists()");
                    // child.getChildren().iterator().next().
                    for (DataSnapshot adverts : child.getChildren()) {
                        Log.d(TAG, "child2: " + adverts);

                        if (adverts.getKey().equals("jobInfo")) {

                            Log.d(TAG, "onDataChange: CHİLD2 : KeY" + child.getKey());
                            Log.d(TAG, "onDataChange: CHİLD2 : VALUE " + child.getValue());
                            jobAdverts.add(adverts.getValue(JobAdvertModel2.class));

                        } else if (adverts.getKey().equals("applyInfo")) {
/*
                            /**
                             * Burada bu ilana basvuran kişilerin Idlerini alıyorum ..

                            Log.d(TAG, "child2.getKey().equals(applyInfo): BASVURANLARIN ID Sİ " + adverts.getChildren());
                            Log.d(TAG, "child2.getKey().equals(applyInfo): BASVURAN KİŞİ SAYISI  " + adverts.getChildrenCount());
                            for (DataSnapshot applicant : adverts.getChildren()) {
                                Log.d(TAG, "doInBackground: " + applicant);
                                Log.d(TAG, "doInBackground: " + applicantUserModelArrayList);//İşe Basvuranları userId leri var buarada
                                if (applicant.getKey().equals(MainActivity.userID)) {
                                    /*
                                    İlana basvuranlar arasında şuanki kullanıcının olması durumu

                                    Log.d(TAG, "snapshot.getKey().equals(MainActivity.userID:ŞUANKİ KULLANICI BU İLANA BASVURU YAPMIS  " + applicant.getKey());
                                    //  child.
                                    applicantUserModelArrayList.add(applicant.getValue(ApplicantUserModel.class));
                                    basvuruYapılmısmı.put(child.getKey(), true);


                                }

                            }
*/

                        }


                    }


                } else {
                    Log.d(TAG, "doInBackground: CHİLD BULUNAMADI .");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "doInBackground: " + e);
        }
        return jobAdverts;

    }

    @Override
    protected void onPostExecute(ArrayList<JobAdvertModel2> completeResult) {
        Log.d(TAG, "onPostExecute: " + completeResult);
        fromFirebase.JobAdvertFromFirebaseCallback(completeResult);
        //  mWeakReference.get().findViewById(R.id.mapProgress).setVisibility(View.GONE);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

    }


}
