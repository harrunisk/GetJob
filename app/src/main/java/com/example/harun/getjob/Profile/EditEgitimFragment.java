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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.harun.getjob.R;
import com.example.harun.getjob.profileModel.egitimListModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mayne on 17.02.2018.
 */

public class EditEgitimFragment extends DialogFragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    EditText egitimOkul, egitimBolum, egitimlisans;
    Spinner bs_spinner, bts_spinner;
    Button egitimSave,egitimCancel;
    contentFragment mcontentFragment;
    egitimListModel megitimListModel;
    String bsyil, btsyil;

    private final static String TAG = "EditEgitimFragment";
    String[] yillarim = new String[]{
            "1900",
            "1800",
            "1700",
            "1600",
            "1500"
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.edit_egitim, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        egitimOkul = v.findViewById(R.id.egitimOkul);
        egitimBolum = v.findViewById(R.id.egitimBolum);
        egitimlisans = v.findViewById(R.id.egitimlisans);
        bs_spinner = v.findViewById(R.id.bs_spinner);
        bts_spinner = v.findViewById(R.id.bts_spinner);
        egitimCancel = v.findViewById(R.id.egitimCancel);
        egitimSave = v.findViewById(R.id.egitimSave);

       // bs_spinner.setPrompt(String.valueOf(R.string.spinnerPrompt));
      //  bts_spinner.setPrompt(String.valueOf(R.string.spinnerPrompt));

        final List<String> yilList = new ArrayList<>(Arrays.asList(yillarim));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                v.getContext(),android.R.layout.simple_spinner_dropdown_item,yilList);

      // ArrayAdapter yillar = ArrayAdapter.createFromResource(v.getContext(),R.array.yillar,android.R.layout.simple_spinner_dropdown_item);
       // yillar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bs_spinner.setAdapter(spinnerArrayAdapter);
        bts_spinner.setAdapter(spinnerArrayAdapter);


        egitimSave.setOnClickListener(this);
        egitimCancel.setOnClickListener(this);
        bs_spinner.setOnItemSelectedListener(this);

        bts_spinner.setOnItemSelectedListener(this);
        //ArrayAdapter<String[]> arrayAdapter = new ArrayAdapter<String[]>(this, android.R.layout.simple_spinner_dropdown_item, megitimListModel.getSpinnerListe())


        return v;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.egitimCancel:
                Log.i(TAG, "egitimCance oncLick");

                getDialog().dismiss();


                break;
            case R.id.egitimSave:
                Log.d(TAG, "egitimsave oncLick");
                mcontentFragment.getEgitimContent(egitimOkul.getText().toString(), egitimBolum.getText().toString(),egitimlisans.getText().toString(),bsyil,btsyil);
                getDialog().dismiss();
                break;

            default:

                break;

        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mcontentFragment = (contentFragment) getActivity();


        } catch (Exception e) {

            Log.e(TAG, e.getLocalizedMessage());

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


        switch (adapterView.getId()) {

            case R.id.bs_spinner:
                Log.d(TAG, "Spinner oncLick Basln. yili ");
                bsyil = adapterView.getItemAtPosition(position).toString();

                break;
            case R.id.bts_spinner:
                Log.d(TAG, "Spinner oncLick : Bitis Yili");

                btsyil=adapterView.getItemAtPosition(position).toString();
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
