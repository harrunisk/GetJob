package com.example.harun.getjob.MyAdverts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

import com.example.harun.getjob.R;
import com.example.harun.getjob.profileModel.myFragmentPagerAdapter;

/**
 * Created by mayne on 8.07.2018.
 */

public class MyAdvertsActivty extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MyAdvertsActivty";
    private TabLayout myAdvertsTab;
    private ViewPager myAdverts_viewPager;
    private myFragmentPagerAdapter myFragmentPagerAdapter;
    private RecyclerView myAdvertsRecycler;
    private TabHost tabHost;
    private ImageView backBtn;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myadvert_sactivity);
        gatherViews();


    }

    private void gatherViews() {
        myAdverts_viewPager = findViewById(R.id.myAdverts_viewPager);
        myAdvertsTab = findViewById(R.id.myAdvertsTab);
        myAdvertsRecycler = findViewById(R.id.myAdvertsRecycler);
        backBtn = findViewById(R.id.backBtn);
        init();
    }

    private void init() {

        myFragmentPagerAdapter = new myFragmentPagerAdapter(getSupportFragmentManager());
        setupViewPager();
        backBtn.setOnClickListener(this);
    }


    private void setupViewPager() {

        final AppliedAdverts myAppliedAdvert = new AppliedAdverts();
        SavedAdverts savedAdverts = new SavedAdverts();

        myFragmentPagerAdapter.addFragment(myAppliedAdvert, "Başvurularım");
        myFragmentPagerAdapter.addFragment(savedAdverts, "Kayıt Ettiklerim");

        myAdverts_viewPager.setOffscreenPageLimit(1);
        myAdverts_viewPager.setAdapter(myFragmentPagerAdapter);


//        myAdverts_viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
//            @Override
//            public void transformPage(@NonNull View page, float position) {
//                myAdverts_viewPager.setRotationY(position * -20);
//            }
//        });
        myAdvertsTab.setupWithViewPager(myAdverts_viewPager);
        // myAdvertsTab.getTabAt(0).setCustomView(getLayoutInflater().inflate(R.layout.tabcustom_view, null));
        //  myAdvertsTab.addTab(myAdvertsTab.newTab().setCustomView(R.layout.tabcustom_view).setText("Basvurularım"),0);
        // myAdvertsTab.addTab(myAdvertsTab.newTab().setCustomView(R.layout.tabcustom_view).setText("Kayıt"),1);
        myAdverts_viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.d(TAG, "onPageScrolled: ");
            }

            @Override
            public void onPageSelected(int position) {
                //  Log.d(TAG, "onPageSelected: " + position);
                //    myAdvertsTab.getTabAt(position).setCustomView(R.layout.);
//                if (position == 0) {
//                    Log.d(TAG, "onPageSelected: 0" + position);
//                    myAdvertsTab.getTabAt(position).setCustomView(getLayoutInflater().inflate(R.layout.tabcustom_view, null));
//
//
//                } else {
//                    Log.d(TAG, "onPageSelected: 1 " + position);
//                    myAdvertsTab.getTabAt(position).setCustomView(getLayoutInflater().inflate(R.layout.tabcustom_view, null));
//
//
//                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //    Log.d(TAG, "onPageScrollStateChanged: ");
            }
        });


        myAdvertsTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabSelected: ");
                //tabHost.getTa
                myAdverts_viewPager.setCurrentItem(tab.getPosition());
                //  tab.setCustomView(getLayoutInflater().inflate(R.layout.tabcustom_view, null));
               /* if (!(tab.getPosition() == 0)) {
                    Log.d(TAG, "onPageSelected: 0" + tab.getPosition());
                    myAdvertsTab.setBackground(getDrawable(R.drawable.right_rounded));


                }*/
//                } else {
//                    Log.d(TAG, "onPageSelected: 1 " + tab.getPosition());
//                    tab.setCustomView(getLayoutInflater().inflate(R.layout.tabcustom_view, null));
//
//
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //  Log.d(TAG, "onTabUnselected: ");

                // tab.setCustomView(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //     Log.d(TAG, "onTabReselected: ");
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.backBtn:

                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;

        }


    }
}


