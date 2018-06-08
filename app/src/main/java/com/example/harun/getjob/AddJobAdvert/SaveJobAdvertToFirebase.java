package com.example.harun.getjob.AddJobAdvert;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.harun.getjob.JobSearch.JobUtils.JobAdvertModel2;
import com.example.harun.getjob.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by mayne on 5.06.2018.
 */

public class SaveJobAdvertToFirebase extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "SaveJobAdvertToFirebase";
    private Context mContext;
    private JobAdvertModel2 mJobAdvertModel2;

    //FİREBASE
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef, myref2;
    private FirebaseAuth mAuth;
    private String userId; //Burası Company Id olaacak Daha sonra şimdilik userID
    private StorageReference mStorageRef;
    private String databaseName = "jobAdvert"; //SaveJobAdvertToFirebase Database name

//    HashMap<String, List<String>> denemeHash = new HashMap<>();
//    List<String> denemeList = new ArrayList<>();


    public SaveJobAdvertToFirebase(Context mContext, JobAdvertModel2 mJobAdvertModel2) {
        this.mContext = mContext;
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

        myref2.push().setValue(mJobAdvertModel2);
        return null;
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute: ");
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
