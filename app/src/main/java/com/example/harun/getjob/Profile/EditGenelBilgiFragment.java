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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.harun.getjob.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog mdatepicker;
    String secilenEhliyet, secilenAskerlik;
    contentFragment minterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

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

        spinEhliyet.setOnItemSelectedListener(this);
        spinAskerlik.setOnItemSelectedListener(this);

        calendar = Calendar.getInstance();

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

                minterface.getGenelContent(editTel.getText().toString(),editMail.getText().toString(),editDogumTarih.getText().toString(),secilenEhliyet,secilenAskerlik);

                getDialog().dismiss();

                break;

            case R.id.editDogumTarih:
                //Edittex tıklanıldıgında datePicker gösterilecek
                Log.d(TAG, "ediittext oncLick DatePickerDialog ");
                mdatepicker = new DatePickerDialog(getContext(), date
                        , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                mdatepicker.show();

                break;
        }


    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        try {
            minterface= (contentFragment) getActivity();

        }catch (Exception e ) {
            Log.d(TAG,"onAttach İnterface"+e.getLocalizedMessage());

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
