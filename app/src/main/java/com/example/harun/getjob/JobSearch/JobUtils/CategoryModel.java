package com.example.harun.getjob.JobSearch.JobUtils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

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
    private sectorCallback sectorCallback;
    private ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
    private HashMap<String, ArrayList<String>> sectorListfromFirebase = new HashMap<>();
    private ArrayList<String> subSector;
    //int sectorCount=0;


    public CategoryModel(Context mContext, @Nullable doneCallback mDoneCallback, @Nullable sectorCallback sectorCallback) {
        this.mContext = mContext;
        this.mDoneCallback = mDoneCallback;
        this.sectorCallback = sectorCallback;
    }

    public interface doneCallback {

        void getCategoryCompleteCallback(ArrayList<CategoryModel> categoryModelArrayList, int size,HashMap<String, ArrayList<String>> sectorHash);

    }

    public interface sectorCallback {


        void getSectorCallback(HashMap<String, ArrayList<String>> sectorListfromFirebase);

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
    public void getSectorList() {
        FirebaseDatabase.getInstance().getReference().child("sectorList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //  Log.d(TAG, "onDataChange:dataSnapshot " + dataSnapshot);

                for (DataSnapshot sectors : dataSnapshot.getChildren()) {
                    subSector = new ArrayList<>();
                    // Log.d(TAG, "onDataChange:sectors " + sectors);
                    //  Log.d(TAG, "onDataChange:sectors.getKey " + sectors.getKey());

                    for (DataSnapshot jobs : sectors.getChildren()) {

                        //  Log.d(TAG, "onDataChange:jobs.getKey() " + jobs.getKey());
                        subSector.add(jobs.getKey());
                    }
                    sectorListfromFirebase.put(sectors.getKey(), subSector);


                }

                if (sectorCallback != null) {

                    sectorCallback.getSectorCallback(sectorListfromFirebase);

                } else {
                    //  Log.d(TAG, "onDataChange:TÜM İLANLAR İÇİN CALISTI ");
                    Query query2 = FirebaseDatabase.getInstance().getReference().child("jobAdvert").child("publishedAdverts");
                    getCategoryCount(categoryModelArrayList, "Tüm İlanlar", query2);
                    for (String strings : sectorListfromFirebase.keySet()) {
                        //  Log.d(TAG, "onDataChange:SEKTÖR LİSTESİ  " + strings);
                        Query query = FirebaseDatabase.getInstance().getReference().child("jobAdvert").child("publishedAdverts").
                                orderByChild("jobInfo/jobSector").equalTo(strings);
                        getCategoryCount(categoryModelArrayList, strings, query);


                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * Sektör Listesini  ve Bu Sektörlerde kaç adet ilan oldugunu bulan method
     */
  /*  public void getDataModel() {
        getSectorList();
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
                getCategoryCount(categoryModelArrayList, sektorList.get(i), query);
            }


        }

        Log.d(TAG, "getDataModel: " + categoryModelArrayList);
        // return categoryModelArrayList;
    }
*/

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
                //  Log.d(TAG, "onDataChange:" + String.valueOf(dataSnapshot.getChildrenCount()));
                //  Log.d(TAG, "onDataChange: " + dataSnapshot);
                count[0] = String.valueOf(dataSnapshot.getChildrenCount());
                categoryModelArrayList.add(new CategoryModel(sektor, count[0]));
                mDoneCallback.getCategoryCompleteCallback(categoryModelArrayList, sectorListfromFirebase.size(),sectorListfromFirebase);
                //  Log.d(TAG, "onDataChange: " + categoryModelArrayList);
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
