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


/**
 * Created by mayne on 15.02.2018.
 */

public class EditAboutFragment extends DialogFragment implements View.OnClickListener {

    private final static String TAG = "EditAboutFragment";


    EditText aboutText;
    Button cancelButon;
    Button saveButon;
    EditProfile editProfileInstance;

    public contentFragment aboutinterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_about, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
         getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        aboutText = view.findViewById(R.id.about_text);
        cancelButon = view.findViewById(R.id.aboutCancel);
        saveButon = view.findViewById(R.id.aboutSave);

        saveButon.setOnClickListener(this);
        cancelButon.setOnClickListener(this);

        aboutText.setText(((EditProfile) getActivity()).editAboutContent.getText());
        return view;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.aboutCancel:
                Log.d(TAG, "onClick: closing dialog");

                getDialog().dismiss();

                break;

            case R.id.aboutSave:


                String sendText = aboutText.getText().toString();

                Log.d(TAG, "onClick: AboutTextSave   " + sendText);
                //editProfileInstance.editAboutContent.setText(sendText);
                getDialog().dismiss();

                //((EditProfile) getActivity()).editAboutContent.setText(sendText);

                aboutinterface.sendAboutContent(sendText);

                break;

            default:
                Log.d(TAG, "onClick: Default");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            aboutinterface= (contentFragment) getActivity();
        } catch (Exception e) {
            Log.e(TAG, "onAttach: interfaceExcp: " + e.getMessage());
        }
    }
}
