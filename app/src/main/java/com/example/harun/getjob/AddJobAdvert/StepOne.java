package com.example.harun.getjob.AddJobAdvert;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.harun.getjob.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

/**
 * Created by mayne on 1.05.2018.
 */

public class StepOne extends Fragment implements Step, View.OnClickListener, View.OnTouchListener {

    private static final String TAG = "StepOne";
    AutoCompleteTextView sektorText;
    ArrayAdapter<String> sektorListAdapter;
    ImageView sektorImage;
    EditText jobDescprictiontext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View v = inflater.inflate(R.layout.add_jobadvert_step1, container, false);
        gatherViews(v);


        return v;
    }

    private void gatherViews(View v) {
        Log.d(TAG, "gatherViews: ");
        sektorText = v.findViewById(R.id.sektorText);
        sektorImage = v.findViewById(R.id.sektorImage);
        jobDescprictiontext = v.findViewById(R.id.jobDescprictiontext);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        sektorListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, getActivity().getResources().getStringArray(R.array.sektorListe));
        sektorText.setAdapter(sektorListAdapter);
        sektorText.setThreshold(0);
        sektorImage.setOnClickListener(this);
      //  jobDescprictiontext.setOnTouchListener(this);

    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        Log.d(TAG, "verifyStep: ");
        return null;
    }

    @Override
    public void onSelected() {
        Log.d(TAG, "onSelected: ");
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Log.d(TAG, "onError: ");
    }





    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sektorImage:

                Log.d(TAG, "onClick: ");
                sektorText.showDropDown();
                break;


        }
    }

    /**
     * Edittext Scroll Event ..
     * @param view
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        view.getParent().requestDisallowInterceptTouchEvent(true);
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){

            case MotionEvent.ACTION_UP:
                view.getParent().requestDisallowInterceptTouchEvent(false);
                break;

        }
        return false;
    }
}
