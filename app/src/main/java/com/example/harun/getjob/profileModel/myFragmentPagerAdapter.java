package com.example.harun.getjob.profileModel;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayne on 26.02.2018.
 */

public class myFragmentPagerAdapter extends FragmentPagerAdapter {

    public static final String TAG = "myFragmentPAgerAdapter";
    private List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public myFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: " + position);

//        Fragment fm2;
//        switch (position) {
//            case 0:
//                fm2 = new GaleryFragment();
//             //   addFragment(fm2);
//
//                break;
//            case 1:
//                fm2 = new cameraFragment();
//               // addFragment(fm2);
//                break;
//            default:
//                fm2 = null;
//                break;
//
//        }
//
//        return fm2;
        return fragmentList.get(position);
    }


    @Override
    public int getCount() {

        //return 2;
        return fragmentList.size();
    }

    public void addFragment(Fragment fm, String title) {

        fragmentList.add(fm);
        mFragmentTitleList.add(title);
    }
  @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }


}
