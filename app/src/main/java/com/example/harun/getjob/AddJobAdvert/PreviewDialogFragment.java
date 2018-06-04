package com.example.harun.getjob.AddJobAdvert;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.harun.getjob.JobSearch.JobUtils.JobAdvertModel2;
import com.example.harun.getjob.R;
import com.example.harun.getjob.viewpagercards.TagLayout;

/**
 * Created by mayne on 23.05.2018.
 */

public class PreviewDialogFragment extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "PreviewDialogFragment";
    TextView sektor1, meslek, preDialog_pbDate, istanimTv;
    JobAdvertModel2 model2;
    TextView egitimSeviyesiTv, tecrubeTV, calismaSekliTv, ehliyetTv, cinsiyetTv, askerlikTv, jobadresTv;
    TagLayout imkanlarcontent;
    ScrollView scrollViewDialog;
    ViewSwitcher pos_viewSwitch;
    FloatingActionButton closeDialog1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.preview_dialog, container);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        gatherViews(view);

        return view;
    }

    private void gatherViews(View view) {
        Log.d(TAG, "gatherViews: ");
        sektor1 = view.findViewById(R.id.sektor1);
        meslek = view.findViewById(R.id.meslek);
        istanimTv = view.findViewById(R.id.istanimTv);
        egitimSeviyesiTv = view.findViewById(R.id.egitimSeviyesiTv);
        tecrubeTV = view.findViewById(R.id.tecrubeTV);
        calismaSekliTv = view.findViewById(R.id.calismaSekliTv);
        ehliyetTv = view.findViewById(R.id.ehliyetTv);
        cinsiyetTv = view.findViewById(R.id.cinsiyetTv);
        askerlikTv = view.findViewById(R.id.askerlikTv);
        scrollViewDialog = view.findViewById(R.id.scrollViewDialog);
        imkanlarcontent = view.findViewById(R.id.imkanlarcontent);
        pos_viewSwitch = view.findViewById(R.id.pos_viewSwitch);
        jobadresTv = view.findViewById(R.id.jobadresTv);
        closeDialog1 = view.findViewById(R.id.closeDialog1);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        model2 = getArguments().getParcelable("passModelObject");
        closeDialog1.setOnClickListener(this);
        init();


    }

    private void init() {
        Log.d(TAG, "init: ");

        sektor1.setText(model2.getJobSector());
        meslek.setText(model2.getCompanyJob());
        istanimTv.setText(model2.getJobDescpriction());
        egitimSeviyesiTv.setText(model2.getEducationLevel());
        tecrubeTV.setText(model2.getExpLevel());
        calismaSekliTv.setText(model2.getEmployeeHour());
        ehliyetTv.setText(model2.getDrivingLicence());
        cinsiyetTv.setText(model2.getGender());
        askerlikTv.setText(model2.getMilitary());
        jobadresTv.setText(model2.getCompanyAdress());
        addView();

    }

    private void addView() {
        if (model2.getJobPossibility() != null) {
            pos_viewSwitch.setDisplayedChild(1);
            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            for (String text : model2.getJobPossibility()) {
                Log.d(TAG, "addView: " + text);
                if (!text.isEmpty()) {
                    View tagView = layoutInflater.inflate(R.layout.pos_textview, null, false);
                    TextView tagTextView = tagView.findViewById(R.id.tagText);
                    tagTextView.setText(text);
                    setMargins(tagTextView, 5, 5, 5, 5);
                    imkanlarcontent.addView(tagView);

                  /*  TextView possibility = new TextView(getContext());
                    possibility.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    possibility.setText(text);
                    possibility.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    possibility.setTextColor(Color.BLACK);
                    possibility.setTextSize(12);
                    possibility.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_all_black_10dp, 0, 0, 0);
                    possibility.setCompoundDrawablePadding(5);
                    // possibility.setCompoundDrawables(getActivity().getResources().getDrawable(R.drawable.ic_done_all_black_10dp), null, null, null);
                    possibility.setBackground(getActivity().getDrawable(R.drawable.edittext_back));
                    possibility.setPadding(10, 10, 10, 10);*/

                } else {
                    Log.d(TAG, "addView: textBOŞ ");
                    pos_viewSwitch.setDisplayedChild(0);

                }
            }


        } else {
            Log.d(TAG, "addView:model2.getJobPossibility() == null ");
            pos_viewSwitch.setDisplayedChild(0);
        }
    }

    public void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            Log.d(TAG, "setMargins: ");
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.setLayoutParams(p);
            v.requestLayout();

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.closeDialog1:
                // Log.d(TAG, "onClick: ");
                getDialog().dismiss();
                break;
        }
    }
}

