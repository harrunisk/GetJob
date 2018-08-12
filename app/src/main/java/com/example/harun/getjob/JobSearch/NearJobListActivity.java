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
import android.view.View;
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
    TextView userAdress, toolBartitle;
    TextView userArea;
    myFragmentPagerAdapter pagerAdapter;
    private int nearJoblistSize = 0;
    private int allJoblistSize = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.near_job_activity);

        gatherViews();


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");

    }

    /**
     * JobSearch den gelen liste bundle iççine alınarak NearjobFragmentt gönderilecek.
     *
     * @return
     */
    public Bundle getDataFromJobSearchActivity(int mode) {

        Bundle extras = getIntent().getExtras(); //jobSearchden gelen bundle al
        nearJoblistSize = extras.getInt("nearListSize"); //liste// size
        allJoblistSize = extras.getInt("allJobListSize"); //liste// size
        Bundle bundle = new Bundle();
        if (mode == 1) {
            bundle.putParcelableArrayList("nearJobList", extras.getParcelableArrayList("nearList"));

        } else {

            bundle.putParcelableArrayList("allJobList", extras.getParcelableArrayList("allJobList"));

        }


        return bundle;
    }

    private void setupViewPager() {
        Log.d(TAG, "setupViewPager: ");
        setSupportActionBar(mToolbar);
        pagerAdapter = new myFragmentPagerAdapter(getSupportFragmentManager());

        NearJobFragment nearJobFragment = new NearJobFragment();
        nearJobFragment.setArguments(getDataFromJobSearchActivity(1));

        AllJobFragment allJobFragment = new AllJobFragment();
        allJobFragment.setArguments(getDataFromJobSearchActivity(2));

        pagerAdapter.addFragment(nearJobFragment, "Yakınımdakiler");
        pagerAdapter.addFragment(allJobFragment, "Tümü");
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                page.setRotationY(position * -30);
                /*
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalizedposition / 2 + 0.5f);
                page.setScaleY(normalizedposition / 2 + 0.5f);
                 */
            }
        });
        mViewPager.setOffscreenPageLimit(1);
        mTabs.setupWithViewPager(mViewPager);
        mTabs.addOnTabSelectedListener(onTabSelectedListener(mViewPager));
        onTabSelectedListener(mViewPager);

        //Head Kısmı için adres ve kapsama alanı ve bulunan iş sayısı texti dolduruluyor .Burada yeni konum seçildiğinde bazı işlemler
        //yapılacak..

        userAdress.setText(getIntent().getExtras().getInt("whichLocation") == 1
                ? UserLocationInfo.getInstance().getMyLocationAdress() : UserLocationInfo.getInstance().getNewLocationAdress());
        userArea.setText(getString(R.string.userArea, UserLocationInfo.getInstance().getCircleArea(), nearJoblistSize));
        toolBartitle.setText(getIntent().getExtras().getString("Category"));

    }

    private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager _mViewPager) {

        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabSelected: " + tab.getPosition());
                _mViewPager.setCurrentItem(tab.getPosition());

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
        toolBartitle = findViewById(R.id.toolbartitle);
        setupViewPager();

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
