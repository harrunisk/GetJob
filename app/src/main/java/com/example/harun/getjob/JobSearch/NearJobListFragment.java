package com.example.harun.getjob.JobSearch;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.harun.getjob.R;

/**
 * Created by mayne on 28.03.2018.
 */

public class NearJobListFragment extends BottomSheetDialogFragment {


    public NearJobListFragment() {
    }

    private BottomSheetBehavior.BottomSheetCallback callback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {

            if (newState == BottomSheetBehavior.STATE_HIDDEN) {

                dismiss();


            }


        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragmentjoblist, null);
        dialog.setContentView(contentView);


        ListView listView = (ListView) contentView.findViewById(R.id.JobList);
        String[] strArray = new String[30];
        for (int i = 0; i < strArray.length; i++) {
            strArray[i] = "M" + (i + 1) + "D";
        }
        listView.setAdapter(new ArrayAdapter<String>(getContext()
                , android.R.layout.simple_list_item_1, android.R.id.text1, strArray));

        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        int maxHeight = (int) (height);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();



        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) contentView.getParent());
         mBehavior.setPeekHeight(maxHeight);

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(callback);
        }
    }

/*
    public class NestedScrollableViewHelper extends ScrollableViewHelper {
        public int getScrollableViewScrollPosition(View scrollableView, boolean isSlidingUp) {
            if (mScrollableView instanceof NestedScrollView) {
                if(isSlidingUp){
                    return mScrollableView.getScrollY();
                } else {
                    NestedScrollView nsv = ((NestedScrollView) mScrollableView);
                    View child = nsv.getChildAt(0);
                    return (child.getBottom() - (nsv.getHeight() + nsv.getScrollY()));
                }
            } else {
                return 0;
            }
        }
    }



*/

  /*  @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmentjoblist, container, false);

        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        int maxHeight = (int) (height * 0.88);

        // getDialog().getWindow().setDimAmount(height);
//        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) v.getParent());
        //  mBehavior.setPeekHeight(maxHeight);

        return v;


    }*/

    /*  @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


    }*/


}
