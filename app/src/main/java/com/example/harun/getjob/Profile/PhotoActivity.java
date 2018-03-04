package com.example.harun.getjob.Profile;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.harun.getjob.R;
import com.example.harun.getjob.profileModel.myFragmentPagerAdapter;

public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "PhotoActivity";
    myFragmentPagerAdapter pagerAdapter;

    ViewPager mviewPager;
    TabLayout mtabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photochange);
        Log.d(TAG, "Oncreate Start");

        //Permissions.requestPermission(this,Permissions.PERMISSIONS);

        //İzinler kontrol ediliyor İzin verilmemiş ise izin istenip.
        if (Permissions.checkPermissionArray(this, Permissions.PERMISSIONS)) {

            Log.d(TAG, "onCreate: TÜM İZİNLER VERİLMİŞ DEVAM EDİLİYOR ");
            gatherViews();
            setupViewPager();

        } else {

            Log.d(TAG, "onCreate: izinler yeniden isteniyor ");
            Permissions.myrequestPermission(this, Permissions.PERMISSIONS);


        }


        //Permissions.showAlertdilaog(this, "İzinler Verilmemiş");




           /* */
        //İzinleri iste
          /*  if(!Permissions.requestPermission(this,Permissions.PERMISSIONS))
            {


            }
        }*/


        /*else if(Permissions.(this,Permissions.PERMISSIONS)) {
            //İzinler istendikten sonra true gelirse


        }*/


    }

    /**
     * 0 galery
     * 1 camera
     *
     * @return
     */
    public int getCurrentTabNumber() {

        return mviewPager.getCurrentItem();
    }


    private void setupViewPager() {
        Log.d(TAG, "setupViewPager: ÇALIŞIYOR ");
        pagerAdapter = new myFragmentPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new GaleryFragment(), "GALERİ");
        pagerAdapter.addFragment(new cameraFragment(), "Kamera");

        mviewPager.setAdapter(pagerAdapter);

        mtabLayout.setupWithViewPager(mviewPager);
        //mviewPager.setCurrentItem(getCurrentTabNumber());


        // mtabLayout.getTabAt(0).setText("Galeri");
        //mtabLayout.getTabAt(1).setText("Kamera");

        mtabLayout.addOnTabSelectedListener(onTabSelectedListener(mviewPager));
        onTabSelectedListener(mviewPager);


        // mViewPager = (ViewPager) findViewById(R.id.viewpager_container);

        // mviewPager.addOnPageChangeListener(new TabLayoutOnPageChangeListener(mtabLayout));


        //mviewPager.setOffscreenPageLimit(1);
        /*mviewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: " + position);
                if(position==0){
                    mviewPager.setCurrentItem(position);

                    pagerAdapter.addFragment(pagerAdapter.getItem(position));

                }else{
                    mviewPager.setCurrentItem(position);

                    Log.d(TAG, "onPageSelected: ELSE " + position);
                    pagerAdapter.addFragment(pagerAdapter.getItem(position));

                }


                //   pagerAdapter.addFragment(pagerAdapter.getItem(1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
*/


    }


    private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager mviewPager2) {

        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabSelected: " + tab.getPosition());
                mviewPager2.setCurrentItem(tab.getPosition());


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
        Log.d(TAG, "Oncreate GatherViews");
        mviewPager = findViewById(R.id.viewPagerContainer);
        mtabLayout = findViewById(R.id.tabsBottom);

    }

    @Override
    public void onClick(View view) {


    }
}
