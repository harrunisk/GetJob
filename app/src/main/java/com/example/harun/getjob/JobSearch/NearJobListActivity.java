package com.example.harun.getjob.JobSearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.harun.getjob.R;
import com.example.harun.getjob.profileModel.myFragmentPagerAdapter;

/**
 * Created by mayne on 30.03.2018.
 */

public class NearJobListActivity extends AppCompatActivity {
    private static final String TAG = "NearJobListActivity";
    AppBarLayout bottom_sheet_appbar;
    android.support.v7.widget.Toolbar bottom_sheet_toolbar;
    TabLayout bottom_sheet_tabs;
    ViewPager bottom_sheet_viewpager;
    myFragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.near_job_activity);

        gatherViews();
        setupViewPager();

    }

    private void setupViewPager() {
        Log.d(TAG, "setupViewPager: ");
        setSupportActionBar(bottom_sheet_toolbar);
        pagerAdapter = new myFragmentPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new NearJobFragment(), "Yakınımdakiler");
        pagerAdapter.addFragment(new NearJobFragment(), "Tümü");

        bottom_sheet_viewpager.setAdapter(pagerAdapter);
        bottom_sheet_viewpager.setOffscreenPageLimit(1);
        bottom_sheet_tabs.setupWithViewPager(bottom_sheet_viewpager);
        bottom_sheet_tabs.addOnTabSelectedListener(onTabSelectedListener(bottom_sheet_viewpager));
        onTabSelectedListener(bottom_sheet_viewpager);

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
        bottom_sheet_appbar = findViewById(R.id.bottom_sheet_appbar);
        bottom_sheet_toolbar = findViewById(R.id.bottom_sheet_toolbar);
        bottom_sheet_tabs = findViewById(R.id.bottom_sheet_tabs);
        bottom_sheet_viewpager = findViewById(R.id.bottom_sheet_viewpager);


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

    }

    public int getCurrentTabNumber() {

        return bottom_sheet_viewpager.getCurrentItem();
    }


}
