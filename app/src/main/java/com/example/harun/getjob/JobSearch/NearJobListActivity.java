package com.example.harun.getjob.JobSearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.harun.getjob.JobSearch.JobUtils.UserLocationInfo;
import com.example.harun.getjob.R;
import com.example.harun.getjob.profileModel.myFragmentPagerAdapter;

/**
 * Created by mayne on 30.03.2018.
 */

public class NearJobListActivity extends AppCompatActivity {
    private static final String TAG = "NearJobListActivity";
    AppBarLayout mAppBar;
    android.support.v7.widget.Toolbar mToolbar;
    TabLayout mTabs;
    ViewPager mViewPager;
    TextView userAdress;
    TextView userArea;
    myFragmentPagerAdapter pagerAdapter;
    int joblistSize = 0;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.near_job_activity);

        gatherViews();
        setupViewPager();


    }


    /**
     * JobSearch den gelen liste bundle iççine alınarak NearjobFragmentt gönderilecek.
     *
     * @return
     */
    public Bundle getDataFromJobSearchActivity() {


        // Intent intent = getIntent();

        // intent.getParcelableArrayListExtra("nearList");

        Bundle extras = getIntent().getExtras(); //jobSearchden gelen bundle al
        joblistSize = extras.getInt("nearListSize"); //liste// size
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("yetenekSatir", extras.getParcelableArrayList("nearList"));


        return bundle;
    }

    private void setupViewPager() {
        Log.d(TAG, "setupViewPager: ");
        setSupportActionBar(mToolbar);
        pagerAdapter = new myFragmentPagerAdapter(getSupportFragmentManager());
        NearJobFragment nearJobFragment = new NearJobFragment();
        nearJobFragment.setArguments(getDataFromJobSearchActivity());
        pagerAdapter.addFragment(nearJobFragment, "Yakınımdakiler");
        pagerAdapter.addFragment(new AllJobFragment(), "Tümü");
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mTabs.setupWithViewPager(mViewPager);
        mTabs.addOnTabSelectedListener(onTabSelectedListener(mViewPager));
        onTabSelectedListener(mViewPager);

        //Head Kısmı için adres ve kapsama alanı ve bulunan iş sayısı texti dolduruluyor .
        userAdress.setText(UserLocationInfo.getInstance().getMyLocationAdress());
        userArea.setText(getString(R.string.userArea, UserLocationInfo.getInstance().getCircleArea(), joblistSize));


    }

    private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager bottom_sheet_viewpager) {

        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabSelected: " + tab.getPosition());
                bottom_sheet_viewpager.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };


    }

    private void gatherViews() {

        Log.d(TAG, "gatherViews: ");
        mAppBar = findViewById(R.id.nearJobappbar);
        mToolbar = findViewById(R.id.nearjobToolbar);
        mTabs = findViewById(R.id.nearJobTab);
        mViewPager = findViewById(R.id.nearJobPager);
        userAdress = findViewById(R.id.userAdress);
        userArea = findViewById(R.id.userArea);

    }

    @Override
    public void supportNavigateUpTo(@NonNull Intent upIntent) {
        super.supportNavigateUpTo(upIntent);
    }

    @Override
    public void onBackPressed() {
        //moveTaskToBack(true);
        finish();
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);


    }

    public int getCurrentTabNumber() {

        return mViewPager.getCurrentItem();
    }



}
