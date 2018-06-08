package com.example.harun.getjob.AddJobAdvert;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harun.getjob.JobSearch.JobUtils.JobAdvertModel2;
import com.example.harun.getjob.R;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by mayne on 1.05.2018.
 */

public class AddJobAdvert extends AppCompatActivity implements View.OnClickListener, StepperLayout.StepperListener {
    private static final String TAG = "AddJobAdvert";
    StepperLayout stepperLayout;
    ImageView job_back;
    TextView companyName, previewTv;
    private JobAdvertModel2 advertModel2;
    Bundle passModelObject;
    // public static NestedScrollView nestedScroll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.add_jobadvert);
        gatherViews();

    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        Log.d(TAG, "onBackPressed: GERİ TUSUNA BASILDI ");
    }

    private void gatherViews() {
        Log.d(TAG, "gatherViews: ");
        stepperLayout = findViewById(R.id.stepperLayout);
        stepperLayout.setOffscreenPageLimit(4);
        job_back = findViewById(R.id.job_back);
        companyName = findViewById(R.id.companyName);
        previewTv = findViewById(R.id.previewTv);
        //nestedScroll = findViewById(R.id.nestedScroll);

        init();


    }

    private void init() {

        Log.d(TAG, "init: ");
        passModelObject = new Bundle();
        advertModel2 = new JobAdvertModel2();
        setJobDetails();
        passModelObject.putParcelable("passModelObject", advertModel2);
        stepperLayout.setAdapter(new FragmentStepAdapter(getSupportFragmentManager(), this, advertModel2));
        job_back.setOnClickListener(this);
        previewTv.setOnClickListener(this);

    }

    private void setJobDetails() {
        Log.d(TAG, "setJobDetails: ");
        advertModel2.setPublishDate(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime()));
        advertModel2.setCompanyLogoUrl("");
        advertModel2.setCompanyName("MD Bilişim Sistemleri");
        advertModel2.setCountApply(0);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.job_back:

                advertModel2 = null;
                finish();
                break;
            case R.id.previewTv:
                Log.d(TAG, "onClick: ÖNİZLEME ");
                FragmentManager fragmentManager = getFragmentManager();
                PreviewDialogFragment previewDialogFragment = new PreviewDialogFragment();
                previewDialogFragment.setArguments(passModelObject);
                previewDialogFragment.show(fragmentManager, "Önizleme");

                break;


        }
    }

    @Override
    public void onCompleted(View completeButton) {
        Log.d(TAG, "onCompleted: ");
    }

    @Override
    public void onError(VerificationError verificationError) {
        Log.d(TAG, "onError: ");
    }

    @Override
    public void onStepSelected(int newStepPosition) {
        Log.d(TAG, "onStepSelected: ");
    }

    @Override
    public void onReturn() {

        Log.d(TAG, "onReturn: ");
    }

   /* public static void smoothScroll() {
        Log.d(TAG, "smoothScroll: ");
        nestedScroll.setFocusable(true);
        nestedScroll.smoothScrollBy(0, nestedScroll.getBottom());
        nestedScroll.fullScroll(View.FOCUS_DOWN);


    }
*/

}
