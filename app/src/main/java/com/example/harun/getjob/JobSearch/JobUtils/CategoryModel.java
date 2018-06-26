package com.example.harun.getjob.JobSearch.JobUtils;

import android.content.Context;
import android.util.Log;

import com.example.harun.getjob.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mayne on 22.03.2018.
 */

public class CategoryModel {
    private static final String TAG = "CategoryModel";
//    int[] res1 = {R.drawable.scientist,
//            R.drawable.chef, R.drawable.businessman, R.drawable.businessman, R.drawable.businessman, R.drawable.doctor, R.drawable.manager, R.drawable.worker, R.drawable.workerman,
//            R.drawable.scientist, R.drawable.scientist, R.drawable.scientist, R.drawable.scientist, R.drawable.scientist, R.drawable.scientist, R.drawable.scientist, R.drawable.scientist, R.drawable.scientist, R.drawable.scientist, R.drawable.scientist,
//            R.drawable.scientist, R.drawable.scientist, R.drawable.scientist, R.drawable.scientist};

    private String categoryName;
    private String advertCount;
    private ArrayList<String> sektorList = new ArrayList<>();
    private String[] sektor;
    private Context mContext;
    private final String[] count = new String[1];
    private doneCallback mDoneCallback;
    private ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();


    public CategoryModel(Context mContext, doneCallback mDoneCallback) {
        this.mContext = mContext;
        this.mDoneCallback = mDoneCallback;
    }

    public interface doneCallback {

        void getCategoryCompleteCallback(ArrayList<CategoryModel> categoryModelArrayList);

    }

    public String getAdvertCount() {
        Log.d(TAG, "getAdvertCount: " + advertCount);
        return advertCount;
    }

    public void setAdvertCount(String advertCount) {
        this.advertCount = advertCount;
    }

    public CategoryModel(String categoryName, String count) {
        this.categoryName = categoryName;
        this.advertCount = count;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    /**
     * Sektör Listesini  ve Bu Sektörlerde kaç adet ilan oldugunu bulan method
     */
    public void getDataModel() {

        sektor = mContext.getResources().getStringArray(R.array.sektorListe);
        sektorList.add(0, "Tüm İlanlar");
        sektorList.addAll(1, Arrays.asList(sektor));

        for (int i = 0; i < sektor.length + 1; i++) {

            if (i == 0) {

                Query query = FirebaseDatabase.getInstance().getReference().child("jobAdvert").child("publishedAdverts");
                getCategoryCount(categoryModelArrayList, sektorList.get(i), query);

            } else {
                Query query = FirebaseDatabase.getInstance().getReference().child("jobAdvert").child("publishedAdverts").
                      orderByChild("jobInfo/jobSector").equalTo(sektorList.get(i));
                //equalTo("jobInfo").
                // orderByKey().equalTo(sektorList.get(i));
                //equalTo(sektorList.get(i));
                getCategoryCount(categoryModelArrayList, sektorList.get(i), query);
            }


        }

        Log.d(TAG, "getDataModel: " + categoryModelArrayList);
        // return categoryModelArrayList;
    }

    /**
     * Sektör de kaç adet yayınlanmıs ilan oldugunu getiren method
     *
     * @param categoryModelArrayList --> Sektör Listesi
     * @param sektor                 -->Sektör adı
     * @param query                  --> İlan sayısını bulmak için gönderilen firebase query -> her sektör için 1 query calısıyor .
     */
    public void getCategoryCount(final ArrayList<CategoryModel> categoryModelArrayList, final String sektor, Query query) {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange:" + String.valueOf(dataSnapshot.getChildrenCount()));
                Log.d(TAG, "onDataChange: " + dataSnapshot);
                // if (isAll) {
                count[0] = String.valueOf(dataSnapshot.getChildrenCount());
                categoryModelArrayList.add(new CategoryModel(sektor, count[0]));
                mDoneCallback.getCategoryCompleteCallback(categoryModelArrayList);
                Log.d(TAG, "onDataChange: " + categoryModelArrayList);
                //}

//
//                else {
//
//                    if (dataSnapshot.getChildren().iterator().next().child("jobInfo").exists()) {
//                        Log.d(TAG, "onDataChange: dataSnapshot.getChildren().iterator().next().child(jobInfo).exists()");
//                        if (dataSnapshot.getChildren().iterator().next().child("jobInfo").child("jobSector").getValue() == sektor) {
//
//
//                            Log.d(TAG, "onDataChange: sektör" + sektor);
//                            count[0] = String.valueOf(dataSnapshot.getChildrenCount());
//                            categoryModelArrayList.add(new CategoryModel(sektor, count[0]));
//                            mDoneCallback.getCategoryCompleteCallback(categoryModelArrayList);
//                            Log.d(TAG, "onDataChange: " + categoryModelArrayList);
//
//
//                        }
//
//
//                    }
//
//
//                }

                //setCategoryModelArrayList(categoryModelArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ");
            }


        });
    }


    @Override
    public String toString() {
        return "CategoryModel{" +
                "categoryName='" + categoryName + '\'' +
                ", advertCount='" + advertCount + '\'' +
                '}';
    }


}
