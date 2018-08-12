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
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.harun.getjob.R;
import com.example.harun.getjob.profileModel.yetenekModel;

/**
 * Created by mayne on 23.02.2018.
 */

public class EditYetenekFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "EditYetenekFragment";

    //LayoutInflater layoutInflater;
    ImageView closeDialog;
    RatingBar mRating;
    EditText yetenekName;
    Button saveContent;
    private yetenekModel myetenekModel;
    ProfileInterfaces mcontentFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_yetenek, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        yetenekName = view.findViewById(R.id.yetenek_content);
        mRating = view.findViewById(R.id.yetenekRatingBar);
        closeDialog = view.findViewById(R.id.closeDialog);
        saveContent = view.findViewById(R.id.yetenekSave);

        closeDialog.setOnClickListener(this);
        saveContent.setOnClickListener(this);

        if (getTag().equals("updateFragment")) {

            myetenekModel = getArguments().getParcelable("yetenekSatir");

            yetenekName.setText(myetenekModel.getYetenekName()); //Null olabilir kullanıcı yetenek eklerken kontrolü yapılacak
            mRating.setRating(myetenekModel.getRate());


        }


        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.closeDialog:

                Log.d(TAG, "CloseDailog onClick");

                getDialog().dismiss();
                break;

            case R.id.yetenekSave:
                Log.d(TAG, "SaveDialog onClick" + yetenekName.getText().toString() + mRating.getRating());

                /*
                editProfileActivitye gönderildi
                 */
                if (getTag().equals("updateFragment")) {
                    mcontentFragment.getYetenekContent(yetenekName.getText().toString(), (int) mRating.getRating(), true);
                    getDialog().dismiss();
                } else {
                    mcontentFragment.getYetenekContent(yetenekName.getText().toString(), (int) mRating.getRating(), false);
                    getDialog().dismiss();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mcontentFragment = (ProfileInterfaces) getActivity();
        } catch (Exception e) {
            Log.d(TAG, " OnAttach Error : " + e.getLocalizedMessage());


        }

    }
}
