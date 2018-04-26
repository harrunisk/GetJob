package com.example.harun.getjob.JobSearch.JobUtils;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.harun.getjob.JobSearch.JobSearch;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by mayne on 26.04.2018.
 */

/**
 * üzerine tıklanan itemin nasıl davranması gerektiğini Bu sınıfta belirledim .
 * Ex.-> slidingheader.setOnTouchListener(new GestureEvent(this));
 * yukarıdaki gibi bir kullanımda
 * slidingPanelin Header kısmı  İçin onRightToLeftSwipe() Yada onLefttoRightSwipe yani soldan saga veya sagdan sola
 * bir dokunma(tıklanma) işlemi oldugunda Paneli gizliyorum.
 *
 */
public class GestureEvent implements View.OnTouchListener {
    private static final String TAG = "GestureEvent";
    private float downX, downY, upX, upY;
    private static final int MIN_DISTANCE = 30;
    private static final int MIN_SWİPEDISTANCE = 70;
    private Activity mActivty;
    //Context context;
    // WeakReference<JobSearch> mWeakReference;


    public GestureEvent(Activity _mActivty) {
        //  mWeakReference = new WeakReference<JobSearch>(jobSearch);
        this.mActivty = _mActivty;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                Log.d(TAG, "onTouch: aaaaaaaaaaaaaa");
                downX = motionEvent.getX();
                downY = motionEvent.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = motionEvent.getX();
                upY = motionEvent.getY();
                float deltaX = downX - upX;
                float deltaY = downY - upY;

                //Sadece dokunma işlemi ..Yani dokundugun yeri koordinati ile eline çektiğindeki yerin koordinatı eşit ise
                if (Math.abs(deltaX) == 0) {

                    Log.d(TAG, "onTouch: DELTAX0");
                    // mWeakReference.get().mSlidingPanelLayout.setAnchorPoint(0.7f);

                    ((JobSearch) mActivty).mSlidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    //mWeakReference.get().mSlidingPanelLayout.setAnchorPoint(0.7f);
                }

                // Yatay Swipe sağdan sola veya tam tersi ..

                if (Math.abs(deltaX) > MIN_SWİPEDISTANCE) {
                    if (deltaX < 0) {
                        this.onLeftToRightSwipe();
                        return true;
                    }
                    if (deltaX > 0) {
                        this.onRightToLeftSwipe();
                        return true;
                    }
                }

                //Dikey Swipe Aşagıdan yukarıya veya yukarıdan aşagıya
                if (Math.abs(deltaY) > MIN_SWİPEDISTANCE) {

                    if (deltaY < 0) {
                        Log.d(TAG, "onTouch: TopTOBOTTOM ");
                        this.onTopToBottomSwipe();
                        return true;
                    }
                    if (deltaY > 0) {
                        this.onBottomToTopSwipe();
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void onRightToLeftSwipe() {
        Log.d(TAG, "onRightToLeftSwipe: ");

        ((JobSearch) mActivty).mSlidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);


    }

    private void onLeftToRightSwipe() {
        Log.d(TAG, "onLeftToRightSwipe: ");
        ((JobSearch) mActivty).mSlidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    private void onBottomToTopSwipe() {
        Log.d(TAG, "onBottomToTopSwipe: ");
    }

    private void onTopToBottomSwipe() {

        Log.d(TAG, "onTopToBottomSwipe: ");
        ((JobSearch) mActivty).mSlidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);


    }
}



