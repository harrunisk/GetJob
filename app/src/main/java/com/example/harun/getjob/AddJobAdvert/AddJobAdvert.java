package com.example.harun.getjob.AddJobAdvert;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.harun.getjob.R;
import com.stepstone.stepper.StepperLayout;

/**
 * Created by mayne on 1.05.2018.
 */

public class AddJobAdvert extends AppCompatActivity {
    private static final String TAG = "AddJobAdvert";
    StepperLayout stepperLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.add_jobadvert);
        gatherViews();

    }

    private void gatherViews() {
        Log.d(TAG, "gatherViews: ");
        stepperLayout = findViewById(R.id.stepperLayout);

        init();


    }

    private void init() {

        Log.d(TAG, "init: ");
        stepperLayout.setAdapter(new FragmentStepAdapter(getSupportFragmentManager(), this));


    }


}
