package com.example.harun.getjob;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.harun.getjob.JobSearch.JobAdvertAdapter;
import com.example.harun.getjob.JobSearch.JobAdvertModel;
import com.example.harun.getjob.JobSearch.JobSearch;
import com.example.harun.getjob.Profile.profilPage;

import java.util.ArrayList;

/**
 * Created by Harun on 10-Jan-18.
 */

public class UserIntro extends AppCompatActivity {
    private static final String TAG = "UserIntro";


    RelativeLayout profileLayout;
    private RecyclerView recyclerViewJobAdvert;
    ArrayList<JobAdvertModel> jobAdvertList;
    JobAdvertAdapter mJobAdvertAdapter;

    String denemeurl = "http://logosolusa.com/wp-content/uploads/parser/MD-Logo-1.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        gatherView();
        init();
        setRecyclerAdvertView();

    }

    private void init() {

        jobAdvertList = new ArrayList<JobAdvertModel>();
    }

    private void setRecyclerAdvertView() {

        Log.d(TAG, "setRecyclerAdvertView: ");
        for (int i = 0; i < 10; i++) {

            jobAdvertList.add(new JobAdvertModel("MD BİLİŞİM SİSTEMLERİ" + i, "Bilgisayar Mühendisi" + i, "Android Developer" + i, "İstanbul" + i, "5km", denemeurl));

        }
        mJobAdvertAdapter = new JobAdvertAdapter(this, jobAdvertList);
        recyclerViewJobAdvert.setAdapter(mJobAdvertAdapter);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerViewJobAdvert.setLayoutManager(linearLayoutManager2);
        recyclerViewJobAdvert.setItemAnimator(new DefaultItemAnimator());
        recyclerViewJobAdvert.setHasFixedSize(true);
        snapHelper.attachToRecyclerView(recyclerViewJobAdvert);


    }

    public void gatherView() {

        //profile = (Button) findViewById(R.id.profileButon);
        profileLayout = findViewById(R.id.profileLayout);
        recyclerViewJobAdvert = findViewById(R.id.recommendedList);

    }

    public void profilPage(View view) {

        Intent intent = new Intent(getApplicationContext(), profilPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }

    public void jobSearch(View view) {


        Intent intent=new Intent(this, JobSearch.class);
        startActivity(intent);

        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


    }
}
