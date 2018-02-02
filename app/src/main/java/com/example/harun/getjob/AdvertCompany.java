package com.example.harun.getjob;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.harun.getjob.viewpagercards.CardFragmentPagerAdapter;
import com.example.harun.getjob.viewpagercards.CardItem;
import com.example.harun.getjob.viewpagercards.CardPagerAdapter;
import com.example.harun.getjob.viewpagercards.ShadowTransformer;


/**
 * Created by harun on 16.01.2018.
 */

public class AdvertCompany extends AppCompatActivity {

    private ViewPager mViewPager;

    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;
    private boolean mShowingFragments = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_company);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mCardAdapter = new CardPagerAdapter();
        mCardAdapter.addCardItem(new CardItem(R.string.experience, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.militaryStatus, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.levelOfEducation, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.languages, R.string.text_1));
        mFragmentCardAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(),
                dpToPixels(2, this));

        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);




    }
    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }


}
