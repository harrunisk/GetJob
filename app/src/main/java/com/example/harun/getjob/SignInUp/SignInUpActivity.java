package com.example.harun.getjob.SignInUp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.harun.getjob.R;
import com.example.harun.getjob.profileModel.myFragmentPagerAdapter;

/**
 * Created by mayne on 24.07.2018.
 */

public class SignInUpActivity extends AppCompatActivity {
    private static final String TAG = "SignInUpActivity";
    private TabLayout signin_up_tab;
    private ViewPager signin_up_pager;
    private AppBarLayout signin_up_appbar;
    private myFragmentPagerAdapter myFragmentPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_up);

        gatherViews();

    }

    private void gatherViews() {

        signin_up_appbar = findViewById(R.id.signin_up_appbar);
        signin_up_tab = findViewById(R.id.signin_up_tab);
        signin_up_pager = findViewById(R.id.signin_up_pager);

        init();

    }

    private void init() {

        myFragmentPagerAdapter = new myFragmentPagerAdapter(getSupportFragmentManager());
        setupViewPager();

    }

    private void setupViewPager() {

        SignInFragment signInFragment=new SignInFragment();
        SignUpFragment signUpFragment=new SignUpFragment();
        myFragmentPagerAdapter.addFragment(signInFragment,"Giriş Yap");
        myFragmentPagerAdapter.addFragment(signUpFragment,"Kayıt Ol ");
        signin_up_pager.setOffscreenPageLimit(1);
        signin_up_pager.setAdapter(myFragmentPagerAdapter);
        signin_up_tab.setupWithViewPager(signin_up_pager);




    }
}
