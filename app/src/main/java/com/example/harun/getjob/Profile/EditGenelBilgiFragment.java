package com.example.harun.getjob.Profile;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.harun.getjob.R;
import com.example.harun.getjob.profileModel.genelBilgiModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by mayne on 25.02.2018.
 */

public class EditGenelBilgiFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = "EditGenelBilgiFragment";
    Button genelCancel, genelSave;
    EditText editTel, editMail, editDogumTarih;
    Spinner spinAskerlik, spinEhliyet;
    Calendar calendar;
    Calendar calendar2;
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog mdatepicker;
    String secilenEhliyet, secilenAskerlik;
    contentFragment minterface;
    genelBilgiModel genelBilgiModel;
    String ehliyetText;
    String askerlikText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View v = inflater.inflate(R.layout.editgenelbilgiler, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        genelCancel = v.findViewById(R.id.genelCancel);
        genelSave = v.findViewById(R.id.genelbilgiSave);

        editTel = v.findViewById(R.id.editTel);
        editMail = v.findViewById(R.id.editMail);
        editDogumTarih = v.findViewById(R.id.editDogumTarih);

        spinAskerlik = v.findViewById(R.id.spinAskerlik);
        spinEhliyet = v.findViewById(R.id.spinEhliyet);


        ///***Spiinner doldurma işlemi
        List<String> spinListAskerlik = new ArrayList<>(Arrays.asList(v.getContext().getResources().getStringArray(R.array.askerlik)));
        ArrayAdapter<String> spinAskerlikAdapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_dropdown_item, spinListAskerlik);
        spinAskerlik.setAdapter(spinAskerlikAdapter);

        List<String> spinListEhliyet = new ArrayList<>(Arrays.asList(v.getContext().getResources().getStringArray(R.array.Ehliyet)));
        ArrayAdapter<String> spinEhliyetAdapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_dropdown_item, spinListEhliyet);
        spinEhliyet.setAdapter(spinEhliyetAdapter);


        /*OnClick handler **/
        spinEhliyet.setOnItemSelectedListener(this);
        spinAskerlik.setOnItemSelectedListener(this);


        //Buradada İlklemeler yapılıyor Dialog açılıdınga editprofildeki değerler erişip ilkliyorum
        editTel.setText(((EditProfile) getActivity()).tvTel.getText());
        editMail.setText(((EditProfile) getActivity()).tvMail.getText());
        editDogumTarih.setText(((EditProfile) getActivity()).tvDogumTarih.getText());
        ehliyetText = ((EditProfile) getActivity()).tvEhliyet.getText().toString();
        askerlikText = ((EditProfile) getActivity()).tvAskerlik.getText().toString();


        if (ehliyetText != null) {

            spinEhliyet.setSelection(spinEhliyetAdapter.getPosition(ehliyetText));

        }

        if (askerlikText != null) {

            spinAskerlik.setSelection(spinAskerlikAdapter.getPosition(askerlikText));

        }


//        spinEhliyet.setHovered(true);
//        spinAskerlik.setHovered(true);


        calendar = Calendar.getInstance();
        calendar2 = Calendar.getInstance();
        genelSave.setOnClickListener(this);
        genelCancel.setOnClickListener(this);
        editDogumTarih.setOnClickListener(this);

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Log.d(TAG, "OnDateSet");
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);


                calendar2.set(Calendar.YEAR, 1994);
                calendar2.set(Calendar.MONTH, 1);
                calendar2.set(Calendar.DAY_OF_MONTH, 1);


                //  calendar2.set(1994, 1, 1);
                //datePicker.updateDate(1980,1,1);
                //datePicker.init(1980,1,1,this);
                String format = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                editDogumTarih.setText(sdf.format(calendar.getTime()));
            }
        };
        return v;
    }

    //button Click İşlemleri
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.genelCancel:
                Log.d(TAG, "Spinner oncLick genelCancel ");

                getDialog().dismiss();


                break;

            case R.id.genelbilgiSave:
                Log.d(TAG, "Spinner oncLick genelbilgiSave ");

                minterface.getGenelContent(
                        editTel.getText().toString()
                        , editMail.getText().toString()
                        , editDogumTarih.getText().toString()
                        , secilenEhliyet, secilenAskerlik);

                getDialog().dismiss();

                break;

            case R.id.editDogumTarih:
                //Edittex tıklanıldıgında datePicker gösterilecek
                Log.d(TAG, "ediittext oncLick DatePickerDialog ");
                mdatepicker = new DatePickerDialog(getContext()
                        , date
                        , calendar2.get(Calendar.YEAR)
                        , calendar2.get(Calendar.MONTH)
                        , calendar2.get(Calendar.DAY_OF_MONTH));

                mdatepicker.show();

                break;
        }


    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        try {
            minterface = (contentFragment) getActivity();

        } catch (Exception e) {
            Log.d(TAG, "onAttach İnterface" + e.getLocalizedMessage());

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        switch (adapterView.getId()) {
            case R.id.spinAskerlik:
                Log.d(TAG, "spinner Onclik spinAskerlik");


                secilenAskerlik = adapterView.getItemAtPosition(position).toString();

                break;
            case R.id.spinEhliyet:
                Log.d(TAG, "spinner Onclik spinEhliyet");


                secilenEhliyet = adapterView.getItemAtPosition(position).toString();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
