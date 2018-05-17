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

import com.example.harun.getjob.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.HashMap;

/**
 * Created by mayne on 1.05.2018.
 */

public class StepThree extends Fragment implements Step, View.OnClickListener {

    private static final String TAG = "StepThree";
    Button btnPrim, btnSsk, btnYemek, btnYol, btnAgi, btnServis, btnMaas, btnKonaklama;
    TextView tvPrim, tvSsk, tvYemek, tvYol, tvAgi, tvServis, tvMaas, tvKonaklama;
    //RelativeLayout adding_possibility;
    HashMap<View, View> viewHashMap = new HashMap<>();

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

        //adding_possibility = v.findViewById(R.id.adding_possibility);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
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

            viewHashMap.get(whichButton).setVisibility(View.VISIBLE);

        } else {
            viewHashMap.get(whichButton).setVisibility(View.GONE);
        }

    }

    /*private void addView(CharSequence text) {
        LayoutInflater inflater = getLayoutInflater();

        TextView possibility = (TextView) inflater.inflate(R.layout.possibility_textview, null);
        possibility.setText(text);
        adding_possibility.addView(possibility);

    }*/
}
