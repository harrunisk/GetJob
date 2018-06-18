package com.example.harun.getjob.AddJobAdvert;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.harun.getjob.JobSearch.JobUtils.JobAdvertModel2;
import com.example.harun.getjob.MyCustomToast;
import com.example.harun.getjob.R;

/**
 * Created by mayne on 4.06.2018.
 */

public class SuccessDialogFragment extends DialogFragment implements View.OnClickListener, DialogInterface.OnKeyListener {
    private static final String TAG = "SuccessDialogFragment";
    FloatingActionButton closeDialog2;
    Button publishBtn;
    private JobAdvertModel2 model2;
    private boolean isPublished = false;
    SaveJobAdvertToFirebase mSaveJobAdvertToFirebase;

    public SuccessDialogFragment() {
        super();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "SuccessDialogFragment: ");
        View v = inflater.inflate(R.layout.success_dialog_jobadvert, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnCancelListener(this);
        getDialog().setOnKeyListener(this);
        gatherViews(v);
        return v;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // super.onCancel(dialog);
        Log.d(TAG, "onCancel: ");


    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        //super.onDismiss(dialog);
        Log.d(TAG, "onDismiss: ");
        //   if (getDialog().isShowing()

    }

    private void gatherViews(View v) {

        closeDialog2 = v.findViewById(R.id.closeDialog2);
        publishBtn = v.findViewById(R.id.publishBtn);

        init();
    }

    private void init() {

        closeDialog2.setOnClickListener(this);
        publishBtn.setOnClickListener(this);

        if (getArguments() != null) {
            Log.d(TAG, "init: getArguments() != null");
            model2 = getArguments().getParcelable("jobAdvertModel");
            new SaveJobAdvertToFirebase(false, model2).execute();

        }

        // mSaveJobAdvertToFirebase.execute();


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.closeDialog2:
                Log.d(TAG, "onClick: ");
                getDialog().dismiss();
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;

            case R.id.publishBtn:
                Log.d(TAG, "onClick: ");
                if (!isPublished) {
                    //İlan yayınlanıyorr ....
                    new SaveJobAdvertToFirebase(true, model2).execute();
                    //   MyCustomToast.showCustomToast(getActivity(), "Yayınlanacak olan ilanlara eklenecek ! .");
                    isPublished = true;
                    publishBtn.setClickable(false);
                } else {

                    MyCustomToast.showCustomToast(getActivity(), "Yayınlandı ..");

                }

                break;
        }


    }


    @Override
    public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "GERİ TUSUNA BASILDI .: ");
        }
        return true;


    }
}
