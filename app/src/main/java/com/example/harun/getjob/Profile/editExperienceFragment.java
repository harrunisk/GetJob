package com.example.harun.getjob.Profile;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.harun.getjob.R;

/**
 * Created by mayne on 16.02.2018.
 */

public class editExperienceFragment extends DialogFragment implements View.OnClickListener {

    private final static String TAG = "Deneyim Dialog Fragment";
    EditText deneyimPoz, deneyimKurum, deneyimAy, deneyimLokasyon;
    Button editSave, editCancel;
    String deneyim, kurum, ay, lokasyon;
    contentFragment mcontentFragment;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.expSave:
              editItemData();
              getDialog().dismiss();
              Log.d(TAG,"Deneyim Dialog Verileri Gönderildi");
              break;
            case R.id.expCancel:
                getDialog().dismiss();
                Log.d(TAG,"Deneyim Dialog Kapandı");

        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_deneyim, container);
        deneyimPoz = v.findViewById(R.id.deneyimPozisyon);
        deneyimLokasyon = v.findViewById(R.id.deneyimLokasyon);
        deneyimAy = v.findViewById(R.id.deneyimAy);
        deneyimKurum = v.findViewById(R.id.deneyimKurum);
        editSave = v.findViewById(R.id.expSave);
        editCancel = v.findViewById(R.id.expCancel);

        editSave.setOnClickListener(this);
        editCancel.setOnClickListener(this);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mcontentFragment = (contentFragment) getActivity();


        } catch (Exception e) {
            Log.e(TAG, "onAttach: interfaceExcp: " + e.getMessage());
        }


    }

    public void editItemData() {

        deneyim=deneyimPoz.getText().toString();
        kurum=deneyimKurum.getText().toString();
        lokasyon=deneyimLokasyon.getText().toString();
        ay=deneyimAy.getText().toString();

        mcontentFragment.getExperienceContent(deneyim,lokasyon,ay,kurum);///Burdan verileere istedğin yerden erişebilrsin.

    }
}
