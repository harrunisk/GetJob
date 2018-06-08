package com.example.harun.getjob.AddJobAdvert;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.harun.getjob.JobSearch.JobUtils.JobAdvertModel2;
import com.example.harun.getjob.JobSearch.JobUtils.NearJobAdvertModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.maps.android.clustering.ClusterManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by mayne on 5.06.2018.
 */

public class RetainJobAdvertFromFirebase extends AsyncTask<DataSnapshot, ArrayList<JobAdvertModel2>, ArrayList<JobAdvertModel2>> {
    private static final String TAG = "RetainJobAdvertFromFire";
    private Context context;
    //FİREBASE
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef, myref2;
    private FirebaseAuth mAuth;
    private String userId; //Burası Company Id olaacak Daha sonra şimdilik userID
    private StorageReference mStorageRef;
    private String databaseName = "jobAdvert"; //SaveJobAdvertToFirebase Database name
    ClusterManager<NearJobAdvertModel> myItemClusterManager;
    private JobAdvertFromFirebase fromFirebase;
    WeakReference<Activity> mWeakReference;
    ArrayList<JobAdvertModel2> jobAdverts;

    public interface JobAdvertFromFirebase {
        void JobAdvertFromFirebaseCallback(ArrayList<JobAdvertModel2> result);
    }

    public RetainJobAdvertFromFirebase(Activity activity, JobAdvertFromFirebase _jobAdvertFromFirebase) {
        Log.d(TAG, "RetainJobAdvertFromFirebase: ");
        //  this.context = context;
        this.fromFirebase = _jobAdvertFromFirebase;
        this.mWeakReference = new WeakReference<Activity>(activity);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child(databaseName); //
        mStorageRef = FirebaseStorage.getInstance().getReference();


    }
    @Override
    protected ArrayList<JobAdvertModel2> doInBackground(DataSnapshot... dataSnapshots) {
        Log.d(TAG, "doInBackground: ");
        jobAdverts = new ArrayList<JobAdvertModel2>();
        try {
            for (DataSnapshot child : dataSnapshots[0].getChildren()) {
                Log.d(TAG, "onDataChange: CHİLD : KeY" + child.getKey());
                Log.d(TAG, "onDataChange: CHİLD : VALUE " + child.getValue());
                jobAdverts.add(child.getValue(JobAdvertModel2.class));
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

            Log.d(TAG, "onPostExecute: ");
            fromFirebase.JobAdvertFromFirebaseCallback(completeResult);
          //  mWeakReference.get().findViewById(R.id.mapProgress).setVisibility(View.GONE);
            Log.d(TAG, "onPostExecute: BOŞŞŞ ");
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute: ");
        super.onPreExecute();
        if (mWeakReference.get().isFinishing() || mWeakReference.get() == null) {
            Log.d(TAG, "onPreExecute: WeakReference.get().isFinishing() || mWeakReference.get() == null ");
            return;
        }
      //  mWeakReference.get().findViewById(R.id.mapProgress).setVisibility(View.VISIBLE);

    }

}
