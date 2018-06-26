package com.example.harun.getjob.AddJobAdvert;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.harun.getjob.JobSearch.JobUtils.JobAdvertModel2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by mayne on 5.06.2018.
 */

public class SaveJobAdvertToFirebase extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "SaveJobAdvertToFirebase";
    private JobAdvertModel2 mJobAdvertModel2;

    //FİREBASE
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef, myref2;
    private String databaseName = "jobAdvert"; //SaveJobAdvertToFirebase Database name
    private String newKey;
    private boolean isPublished;

    public SaveJobAdvertToFirebase(boolean isPublished, JobAdvertModel2 mJobAdvertModel2) {
        this.isPublished = isPublished;
        this.mJobAdvertModel2 = mJobAdvertModel2;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        //ASıl hedef burası kayıt edilen yer
        myref2 = myRef.child(databaseName);

    }


    @Override
    protected Void doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: ");

        if (!isPublished) {

            myref2.child("notPublishedAdverts").child(newKey).setValue(mJobAdvertModel2).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Log.d(TAG, "isPublishedonSuccess: ");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "isPublishedonFailure: ");
                }
            });

        } else {
            Log.d(TAG, "doInBackground:ELSE  ");

            myref2.child("notPublishedAdverts").child(mJobAdvertModel2.getJobID()).
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {

                            Log.d(TAG, "onDataChange:removeEventListener " + dataSnapshot);
                            Log.d(TAG, "onDataChange: " + dataSnapshot.getKey());

                            myref2.child("publishedAdverts").child(dataSnapshot.getKey())
                                    .child("jobInfo")
                                    .setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "onComplete:ÉKLEME İŞLEMİ BASARILI  ");
                                    if (task.isSuccessful()) {

                                        myref2.child("notPublishedAdverts").child(dataSnapshot.getKey()).removeValue();

                                    }
                                }
                            });
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d(TAG, "onCancelled: ");
                        }
                    });


            //myref2.child("publishedAdverts").child(newKey).setValue(mJobAdvertModel2);//publish edince buraya kayıt ediyorum sadece

        }

        return null;
    }


    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute: ");
        if (!isPublished) {
            newKey = myref2.push().getKey();
            mJobAdvertModel2.setJobID(newKey);
        }
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d(TAG, "onPostExecute: ");
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onCancelled(Void aVoid) {
        Log.d(TAG, "onCancelled: ");
        super.onCancelled(aVoid);
    }


}
