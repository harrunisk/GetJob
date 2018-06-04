package com.example.harun.getjob.AddJobAdvert;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harun.getjob.JobSearch.JobUtils.JobAdvertModel2;
import com.example.harun.getjob.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayne on 1.05.2018.
 */

public class StepOne extends Fragment implements Step, View.OnClickListener, View.OnTouchListener {

    private static final String TAG = "StepOne";
    AutoCompleteTextView sektorText;
    ArrayAdapter<String> sektorListAdapter;
    ImageView sektorImage;
    TextView sektor, arananMeslek, jobDescprictiontv;
    EditText jobDescprictiontext, arananMeslekText;

    Map<EditText, TextView> validateHash;

    private JobAdvertModel2 mjobAdvertModel2;

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
        arananMeslekText = v.findViewById(R.id.arananMeslekText);
        sektor = v.findViewById(R.id.sektor);
        arananMeslek = v.findViewById(R.id.arananMeslek);
        jobDescprictiontv = v.findViewById(R.id.jobDescprictiontv);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        validateHash = new HashMap<EditText, TextView>();
        if (getArguments() != null) {
            Log.d(TAG, "onViewCreated: getArguments() != null ");
            mjobAdvertModel2 = getArguments().getParcelable("jobAdvertModel");
        }

        sektorListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, getActivity().getResources().getStringArray(R.array.sektorListe));
        sektorText.setAdapter(sektorListAdapter);
        sektorText.setThreshold(0);
        sektorImage.setOnClickListener(this);
        validateHash.put(jobDescprictiontext, jobDescprictiontv);
        validateHash.put(sektorText, sektor);
        validateHash.put(arananMeslekText, arananMeslek);

        //  jobDescprictiontext.setOnTouchListener(this);

    }

    @Nullable
    @Override
    public VerificationError verifyStep() {

        /**
         * Burada Edittextler ile TextView leri hashten cıkartıp dolumu bosmu kontrolu yapıyorum
         *
         */
        setJobDetails();
        for (Map.Entry<EditText, TextView> simpleEntry : validateHash.entrySet()) {
            Log.d(TAG, "validating:" + "\t" + simpleEntry.getKey() + "\t" + simpleEntry.getValue());
            if (TextUtils.isEmpty(((EditText) simpleEntry.getKey()).getText().toString())) {
                Log.d(TAG, "validate:  BOŞ ");
                ((TextView) simpleEntry.getValue()).setError("BOŞ OLAMAZ ");
                return new VerificationError("Tüm alanları doldurunuz.");
            } else {
                Log.d(TAG, "validating: DOLU ");

                ((TextView) simpleEntry.getValue()).setError(null);
            }
        }


        return null;
    }

    private void setJobDetails() {

        Log.d(TAG, "setJobDetails: ");
        mjobAdvertModel2.setJobDescpriction(jobDescprictiontext.getText().toString());
        mjobAdvertModel2.setJobSector(sektorText.getText().toString());
        mjobAdvertModel2.setCompanyJob(arananMeslekText.getText().toString());

    }


   /* private boolean validate() {

        if (jobDescprictiontext.getText().toString().trim().isEmpty()) {
            Log.d(TAG, "validate: jobDescprictiontext BOŞ ");
            jobDescprictiontv.setError("BOŞ OLAMAZ ");
            return true;

        } else if (TextUtils.isEmpty(sektorText.getText().toString())) {
            Log.d(TAG, "validate: sektorText BOŞ ");
            sektor.setError("BOŞ OLAMAZ ");
            return true;

        } else if (TextUtils.isEmpty(arananMeslekText.getText().toString())) {
            Log.d(TAG, "validate:arananMeslekText BOŞ ");
            arananMeslek.setError("Boş olamaz.");

            return true;

        }
        return false;

    }*/

  /*  private void validating(Map.Entry simpleEntry) {

     /*   Log.d(TAG, "validating: "+simpleEntry.toString()+"\t");
        if (TextUtils.isEmpty(((EditText) simpleEntry.getKey()).getText().toString())) {
            Log.d(TAG, "validate:  BOŞ ");
            ((TextView) simpleEntry.getValue()).setError("BOŞ OLAMAZ ");
        } else {
            Log.d(TAG, "validating: DOLU ");
            ((TextView) simpleEntry.getValue()).setError(null);

        }


            }*/


    @Override
    public void onSelected() {
        Log.d(TAG, "onSelected:STEP1 ");
        jobDescprictiontv.setError(null);
        sektor.setError(null);
        arananMeslek.setError(null);

    }

    @Override
    public void onError(@NonNull VerificationError error) {


        Log.d(TAG, "onError:STEP1 ");

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
     *
     * @param view
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        view.getParent().requestDisallowInterceptTouchEvent(true);
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_UP:
                view.getParent().requestDisallowInterceptTouchEvent(false);
                break;

        }
        return false;
    }


}
