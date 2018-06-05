package com.example.harun.getjob.AddJobAdvert;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.harun.getjob.JobSearch.JobUtils.JobAdvertModel2;
import com.example.harun.getjob.MyCustomToast;
import com.example.harun.getjob.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayne on 1.05.2018.
 */

public class StepThree extends Fragment implements Step, View.OnClickListener {

    private static final String TAG = "StepThree";
    Button btnPrim, btnSsk, btnYemek, btnYol, btnAgi, btnServis, btnMaas, btnKonaklama;
    TextView tvPrim, tvSsk, tvYemek, tvYol, tvAgi, tvServis, tvMaas, tvKonaklama;
    //RelativeLayout adding_possibility;
    HashMap<View, View> viewHashMap = new HashMap<>();
    ArrayList<String> stringArraylist = new ArrayList<>();
    ViewSwitcher viewSwitch_possiblity;
    private JobAdvertModel2 advertModel2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View v = inflater.inflate(R.layout.add_jobadvert_step3, container, false);
        gatherViews(v);
        return v;
    }

    private void gatherViews(View v) {
        Log.d(TAG, "gatherViews: ");
        btnPrim = v.findViewById(R.id.btnPrim);
        btnSsk = v.findViewById(R.id.btnSsk);
        btnYemek = v.findViewById(R.id.btnYemek);
        btnYol = v.findViewById(R.id.btnYol);
        btnAgi = v.findViewById(R.id.btnAgi);
        btnServis = v.findViewById(R.id.btnServis);
        btnMaas = v.findViewById(R.id.btnMaas);
        btnKonaklama = v.findViewById(R.id.btnKonaklama);
        tvPrim = v.findViewById(R.id.tvPrim);
        tvAgi = v.findViewById(R.id.tvAgi);
        tvMaas = v.findViewById(R.id.tvMaas);
        tvServis = v.findViewById(R.id.tvServis);
        tvSsk = v.findViewById(R.id.tvSsk);
        tvYemek = v.findViewById(R.id.tvYemek);
        tvYol = v.findViewById(R.id.tvYol);
        tvKonaklama = v.findViewById(R.id.tvKonaklama);
        viewSwitch_possiblity = v.findViewById(R.id.viewSwitch_possiblity);

        //adding_possibility = v.findViewById(R.id.adding_possibility);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");

        if (getArguments() != null) {
            Log.d(TAG, "onViewCreated: getArguments() != null ");
            advertModel2 = getArguments().getParcelable("jobAdvertModel");
        }

        btnPrim.setOnClickListener(this);
        btnSsk.setOnClickListener(this);
        btnYemek.setOnClickListener(this);
        btnYol.setOnClickListener(this);
        btnAgi.setOnClickListener(this);
        btnServis.setOnClickListener(this);
        btnKonaklama.setOnClickListener(this);
        btnMaas.setOnClickListener(this);

    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        Log.d(TAG, "verifyStep: ");

        if (validate()) {
            Log.d(TAG, "verifyStep: validate BOŞŞ ");

            return new VerificationError("En az bir olanak seçmeniz gerekiyor ");
        }
        setJobDetails();
        return null;
    }

    /**
     * Hashmap içersinde (buttonlar ile textViewler) seçilmiş olan textviewler bulundugu için textlerini alıp listeye ekleyip
     * model classıma gönderiyorum
     */
    private void setJobDetails() {
        Log.d(TAG, "setJobDetails: ");
        stringArraylist.clear();
        for (Map.Entry<View, View> simpleEntry : viewHashMap.entrySet()) {

            stringArraylist.add(((TextView) simpleEntry.getValue()).getText().toString());
            Log.d(TAG, "setJobDetails: " + ((TextView) simpleEntry.getValue()).getText().toString());


        }
        Log.d(TAG, "setJobDetails: " + stringArraylist.size());
        advertModel2.setJobPossibility(stringArraylist);//>Listeyi gönderdim .

    }

    /**
     * Kontrol En az 1 Olanak Secilmiş olması gerekiyor .Secilen her olanak hashmap içerinde saklandıgı için
     * Boş olması durumda true dönerek kulllanıcıyı bilgilendirecektir.->>>> verifyStep()
     *
     * @return
     */
    private boolean validate() {
        Log.d(TAG, "validate: ");
        if (viewHashMap.isEmpty()) {
            Log.d(TAG, "validate: Hashmap BOşş");
            return true;
        } else {
            Log.d(TAG, "validate: hash map Dolu ");
            return false;
        }

    }

    @Override
    public void onSelected() {
        Log.d(TAG, "onSelected: ");
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Log.d(TAG, "onError: ");
        MyCustomToast.showCustomToast(getContext(), "En az bir olanak seçmeniz gerekiyor");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnKonaklama:
                Log.d(TAG, "onClick: ");
                viewHashMap.put(btnKonaklama, tvKonaklama);
                buttonActiveState(btnKonaklama);
                break;
            case R.id.btnAgi:

                Log.d(TAG, "onClick: ");
                viewHashMap.put(btnAgi, tvAgi);
                buttonActiveState(btnAgi);
                break;
            case R.id.btnMaas:

                Log.d(TAG, "onClick: ");
                viewHashMap.put(btnMaas, tvMaas);
                buttonActiveState(btnMaas);
                break;
            case R.id.btnServis:

                Log.d(TAG, "onClick: ");
                viewHashMap.put(btnServis, tvServis);
                buttonActiveState(btnServis);
                break;
            case R.id.btnSsk:

                Log.d(TAG, "onClick: ");
                viewHashMap.put(btnSsk, tvSsk);
                buttonActiveState(btnSsk);
                break;
            case R.id.btnPrim:

                Log.d(TAG, "onClick: ");
                viewHashMap.put(btnPrim, tvPrim);
                buttonActiveState(btnPrim);
                break;
            case R.id.btnYol:

                Log.d(TAG, "onClick: ");
                viewHashMap.put(btnYol, tvYol);
                buttonActiveState(btnYol);
                break;
            case R.id.btnYemek:

                Log.d(TAG, "onClick: ");
                viewHashMap.put(btnYemek, tvYemek);
                buttonActiveState(btnYemek);
                break;


        }
    }

    private void buttonActiveState(Button _activeButton) {

        if (_activeButton.isActivated()) {

            _activeButton.setActivated(false);
            setTextViewVisibility(false, _activeButton);

        } else {

            _activeButton.setActivated(true);
            // addView(_activeButton.getText());
            setTextViewVisibility(true, _activeButton);
        }

    }

    private void setTextViewVisibility(boolean visible, Button whichButton) {
        if (visible) {
            checkEmptyView();

            viewHashMap.get(whichButton).setVisibility(View.VISIBLE);
            Log.d(TAG, "setTextViewVisibility: " + viewHashMap.size());
        } else {
            viewHashMap.get(whichButton).setVisibility(View.GONE);
            viewHashMap.remove(whichButton);
            checkEmptyView();
            Log.d(TAG, "setTextViewVisibility: " + viewHashMap.size());
        }

    }

    private void checkEmptyView() {
        Log.d(TAG, "checkEmptyView: ");
        if (viewHashMap.isEmpty()) {
            Log.d(TAG, "checkEmptyView:viewHashMap.isEmpty() ");
            viewSwitch_possiblity.setDisplayedChild(0);

        } else {
            Log.d(TAG, "checkEmptyView: NOT NULL ");
            viewSwitch_possiblity.setDisplayedChild(1);
        }

    }


    /*private void addView(CharSequence text) {
        LayoutInflater inflater = getLayoutInflater();

        TextView possibility = (TextView) inflater.inflate(R.layout.possibility_textview, null);
        possibility.setText(text);
        adding_possibility.addView(possibility);

    }*/
}
