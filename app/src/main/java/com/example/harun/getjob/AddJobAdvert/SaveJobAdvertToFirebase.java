package com.example.harun.getjob.AddJobAdvert;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.harun.getjob.JobSearch.JobUtils.JobAdvertModel2;
import com.example.harun.getjob.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by mayne on 5.06.2018.
 */

public class SaveJobAdvertToFirebase extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "SaveJobAdvertToFirebase";
    private JobAdvertModel2 mJobAdvertModel2;

    //FİREBASE
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef, myref2;
    private FirebaseAuth mAuth;
    private String userId; //Burası Company Id olaacak Daha sonra şimdilik userID
    private StorageReference mStorageRef;
    private String databaseName = "jobAdvert"; //SaveJobAdvertToFirebase Database name
    private String newKey;
    private boolean isPublished;
//    HashMap<String, List<String>> denemeHash = new HashMap<>();
//    List<String> denemeList = new ArrayList<>();


    public SaveJobAdvertToFirebase(boolean isPublished, JobAdvertModel2 mJobAdvertModel2) {
        this.isPublished = isPublished;
        this.mJobAdvertModel2 = mJobAdvertModel2;
        Log.d(TAG, "SaveJobAdvertToFirebase: " + mJobAdvertModel2);
        if (MainActivity.userID != null) {

            userId = MainActivity.userID;
            Log.d(TAG, "FirebaseMethod: @@@@@@@" + userId);
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

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
                                    .setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "onComplete:ÉKLEME İŞLEMİ BASARILI  ");
                                    if (task.isSuccessful()) {

                                        myref2.child("notPublishedAdverts").child(dataSnapshot.getKey()).removeValue(
//
//                                                new DatabaseReference.CompletionListener() {
//                                            @Override
//                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                                                Log.d(TAG, "onComplete: SİLME İŞLMEİ BASARILI ");
//                                                Log.d(TAG, "onComplete: "+databaseReference);
//                                              //  databaseReference.removeValue()
//                                            }
//                                        }
//
                                        );

                                     /*   myref2.child("Sector").child(mJobAdvertModel2.getJobSector()).child(mJobAdvertModel2.getJobID()).addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                Log.d(TAG, "onChildAdded: " + dataSnapshot);
                                            }

                                            @Override
                                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                            }

                                            @Override
                                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                                            }

                                            @Override
                                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
*/
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

    private void publishedAdverts() {

    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute: ");
        if (!isPublished) {
            newKey = myref2.push().getKey();
            mJobAdvertModel2.setJobID(newKey);
        }
//        denemeList.add(mJobAdvertModel2.getEducationLevel());
//        denemeList.add(mJobAdvertModel2.getExpLevel());
//        denemeList.add(mJobAdvertModel2.getEmployeeHour());
//        denemeList.add(mJobAdvertModel2.getMilitary());
//        denemeList.add(mJobAdvertModel2.getGender());
//        denemeHash.put("pozisyon", denemeList);
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
