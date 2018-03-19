package com.example.harun.getjob.Profile;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.harun.getjob.R;
import com.example.harun.getjob.profileModel.deneyimModel;

/**
 * Created by mayne on 16.02.2018.
 */

public class EditExperienceFragment extends DialogFragment implements View.OnClickListener {

    private final static String TAG = "Deneyim Dialog Fragment";
    private EditText deneyimPoz, deneyimKurum, deneyimAy, deneyimLokasyon;
    private Button editSave, editCancel;
    String deneyim, kurum, ay, lokasyon;
    private contentFragment mcontentFragment;
    private deneyimModel mdeneyimModel;


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.expSave:
                editItemData();
                getDialog().dismiss();
                Log.d(TAG, "Deneyim Dialog Verileri Gönderildi");
                break;
            case R.id.expCancel:
                getDialog().dismiss();
                Log.d(TAG, "Deneyim Dialog Kapandı");

        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_deneyim, container);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        deneyimPoz = v.findViewById(R.id.deneyimPozisyon);
        deneyimLokasyon = v.findViewById(R.id.deneyimLokasyon);
        deneyimAy = v.findViewById(R.id.deneyimAy);
        deneyimKurum = v.findViewById(R.id.deneyimKurum);
        editSave = v.findViewById(R.id.expSave);
        editCancel = v.findViewById(R.id.expCancel);

        editSave.setOnClickListener(this);
        editCancel.setOnClickListener(this);


        /*Liste itemi düzenlemek için açılan fragment

         *tıklanan satırın öğeleri yükleniyor
         */
        if (getTag().equals("UpdateDeneyimListItem")) {

            Log.d(TAG, "onCreateView: UpdateDeneyimListItem");

            //mdeneyimModel = ((EditProfile) getActivity()).getRowItems();

            mdeneyimModel = getArguments().getParcelable("deneyimSatir");
            Log.d(TAG, "onCreateView: EditDENEYİMListITem" + mdeneyimModel.getPozisyon());

            deneyimPoz.setText(mdeneyimModel.getPozisyon());
            deneyimKurum.setText(mdeneyimModel.getKurum());
            deneyimLokasyon.setText(mdeneyimModel.getLokasyon());
            deneyimAy.setText(mdeneyimModel.getAy());


        }


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

    /**
     * Fragment üzerindeki verileri interface ile edit profile activitye bildiriyor.
     */
    public void editItemData() {

        deneyim = deneyimPoz.getText().toString();
        kurum = deneyimKurum.getText().toString();
        lokasyon = deneyimLokasyon.getText().toString();
        ay = deneyimAy.getText().toString();

        if(getTag().equals("UpdateDeneyimListItem")){
            mcontentFragment.getExperienceContent(deneyim, lokasyon, ay, kurum,true);///Burdan verileere istedğin yerden erişebilrsin.

        }else{
            mcontentFragment.getExperienceContent(deneyim, lokasyon, ay, kurum,false);///Burdan verileere istedğin yerden erişebilrsin.

        }

    }
}
