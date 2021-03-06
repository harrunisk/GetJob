package com.example.harun.getjob.AddJobAdvert;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.example.harun.getjob.JobSearch.JobUtils.JobAdvertModel2;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

/**
 * Created by mayne on 1.05.2018.
 */

public class FragmentStepAdapter extends AbstractFragmentStepAdapter {
    JobAdvertModel2 advertModel2;

    @Override
    public Parcelable saveState() {
        Log.d(TAG, "saveState: ");
        return super.saveState();
    }


    private static final String TAG = "FragmentStepAdapter";

    public FragmentStepAdapter(@NonNull FragmentManager fm, @NonNull Context context, JobAdvertModel2 jobAdvertModel2) {
        super(fm, context);
        this.advertModel2 = jobAdvertModel2;
    }

    @Override
    public Step createStep(int position) {
        Log.d(TAG, "createStep: ");
        if (position == 0) {
            Log.d(TAG, "createStep: 0");
            StepOne step = new StepOne();
            Bundle b = new Bundle();
            b.putParcelable("jobAdvertModel", advertModel2);
            step.setArguments(b);
            return step;

        }
        if (position == 1) {
            Log.d(TAG, "createStep: 1");
            final StepTwo step2 = new StepTwo();
            Bundle b = new Bundle();
            b.putParcelable("jobAdvertModel", advertModel2);
            step2.setArguments(b);
            return step2;

        }
        if (position == 2) {
            Log.d(TAG, "createStep: 2");
            final StepThree step2 = new StepThree();
            Bundle b = new Bundle();
            b.putParcelable("jobAdvertModel", advertModel2);
            step2.setArguments(b);
            return step2;

        }
        if (position == 3) {
            Log.d(TAG, "createStep: 3");
            final StepFour step4 = new StepFour();
            Bundle b = new Bundle();
            b.putParcelable("jobAdvertModel", advertModel2);
            step4.setArguments(b);
            return step4;

        } else {
            Log.d(TAG, "createStep: 1 2 3 4 5 ");
            final StepOne step3 = new StepOne();
            Bundle b1 = new Bundle();
            b1.putInt("CurrentKey", position);
            step3.setArguments(b1);
            return step3;
        }
    }


    @NonNull
    @Override
    public StepViewModel getViewModel(int position) {
        Log.d(TAG, "getViewModel: d");
        StepViewModel.Builder stepViewModel = new StepViewModel.Builder(context);
        switch (position) {
            case 0:
                stepViewModel.setTitle("İş tanımı");
                stepViewModel.setSubtitle("Aranan Nitelikler");
                break;
            case 1:
                stepViewModel.setTitle("Pozisyon");
                stepViewModel.setSubtitle("Bilgileri");
                break;
            case 2:
                stepViewModel.setTitle("Sağlanacak");
                stepViewModel.setSubtitle("İmkanlar");

                break;
            case 3:
                stepViewModel.setTitle("Konum-Adres");
                stepViewModel.setSubtitle("Bilgisi");
                break;
        }

        return stepViewModel.create();
    }

    @Override
    public int getCount() {
        return 4;
    }
}
