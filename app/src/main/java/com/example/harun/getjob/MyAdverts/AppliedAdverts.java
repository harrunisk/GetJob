package com.example.harun.getjob.MyAdverts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;

import com.example.harun.getjob.AddJobAdvert.ApplyAdvertModel;
import com.example.harun.getjob.AddJobAdvert.MyAppliedAdvert;
import com.example.harun.getjob.JobSearch.JobUtils.JobAdvertModel2;
import com.example.harun.getjob.JobSearch.JobUtils.MapHelperMethods;
import com.example.harun.getjob.JobSearch.JobUtils.NearJobAdvertAdapter;
import com.example.harun.getjob.JobSearch.JobUtils.NearJobAdvertModel;
import com.example.harun.getjob.JobSearch.JobUtils.UserLocationInfo;
import com.example.harun.getjob.R;
import com.example.harun.getjob.UserIntro;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by mayne on 8.07.2018.
 */

public class AppliedAdverts extends Fragment implements MyAppliedAdvert.AppliedAdvertCallback {
    private static final String TAG = "AppliedAdverts";
    private ViewSwitcher nearJobViewSwitch;
    private RecyclerView myAdvertsRecycler;
    private NearJobAdvertAdapter nearJobAdvertAdapter;
    private AVLoadingIndicatorView fetchDataProgress;
    private MyAppliedAdvert myAppliedAdvert;
    private ArrayList<NearJobAdvertModel> myAdvertList;
    private ApplyAdvertModel applyAdvertModel;
    private int appliedAdvertCounter = 0;
    private int callbackCounter = 0;

    public AppliedAdverts() {
        Log.d(TAG, "AppliedAdverts: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View v = inflater.inflate(R.layout.myappliedadverts_fragment_content, null, false);
        gatherViews(v);
        return v;
    }

    private void gatherViews(View v) {
        Log.d(TAG, "gatherViews: ");
        nearJobViewSwitch = v.findViewById(R.id.nearJobViewSwitch);
        myAdvertsRecycler = v.findViewById(R.id.myAdvertsRecycler);
        fetchDataProgress = v.findViewById(R.id.fetchDataProgress);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");

        myAdvertList = new ArrayList<>();
        nearJobViewSwitch.setDisplayedChild(0);
        fetchData();
    }

    private void fetchData() {
        Log.d(TAG, "fetchData: ");
        myAppliedAdvert = new MyAppliedAdvert(this);
        FirebaseDatabase.getInstance().getReference().child("users_data").child(UserIntro.userID).child("applyAdvert").orderByChild("apply").equalTo(Boolean.TRUE).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "getMyAppliedAdvert->onDataChange: " + dataSnapshot);
                        myAppliedAdvert.execute(dataSnapshot);
                        appliedAdvertCounter = (int) dataSnapshot.getChildrenCount();
                        Log.d(TAG, "onDataChange:appliedAdvertCounter " + appliedAdvertCounter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }

    @Override
    public void getApliedAdvert(ArrayList<ApplyAdvertModel> result) {

        Log.d(TAG, "getApliedAdvert: " + result);
        if (!result.isEmpty()) {
            for (ApplyAdvertModel advertModel : result) {
                Log.d(TAG, "getApliedAdvert: " + advertModel);
                getAdvertDetails(advertModel);
                applyAdvertModel = advertModel;
            }
        } else {
            Log.d(TAG, "getApliedAdvert: LİSTE BOS ");
            fillRecyclerView();
        }


    }

    private void fillRecyclerView() {

        Log.d(TAG, "fillRecyclerView: ");
        if (appliedAdvertCounter == callbackCounter) {


            if (myAdvertList != null && myAdvertList.size() > 0) {
                // nearJobViewSwitch.setDisplayedChild(0);
                Log.d(TAG, "fillRecyclerView: İF");
                fetchDataProgress.smoothToHide();
                nearJobAdvertAdapter = new NearJobAdvertAdapter(getActivity(), myAdvertList, 1);
                myAdvertsRecycler.setAdapter(nearJobAdvertAdapter);
                LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
                linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
                myAdvertsRecycler.setNestedScrollingEnabled(false);
                myAdvertsRecycler.setLayoutManager(linearLayoutManager2);
                myAdvertsRecycler.setHasFixedSize(true);

            } else {
                Log.d(TAG, "fillRecyclerView: ELSE");
                nearJobViewSwitch.setDisplayedChild(1);
                fetchDataProgress.smoothToHide();

            }


        }
    }

    private void getAdvertDetails(final ApplyAdvertModel applyAdvertModel) {
        Log.d(TAG, "getAdvertDetails: ");
        //  final RetainJobAdvertFromFirebase retainJobAdvertFromFirebase = new RetainJobAdvertFromFirebase(getActivity(), this);
        FirebaseDatabase.getInstance().getReference().child("jobAdvert").child("publishedAdverts").child(applyAdvertModel.getJobID()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.d(TAG, "onDataChange: " + dataSnapshot);

                        for (DataSnapshot advert : dataSnapshot.getChildren()) {


                            if (advert.getKey().equals("jobInfo")) {
                                Log.d(TAG, "onDataChange: ");
                                double distance;
                                JobAdvertModel2 model2 = advert.getValue(JobAdvertModel2.class);
                                distance = MapHelperMethods.getDistanceParce(
                                        MapHelperMethods.toRadiusMeters(
                                                UserLocationInfo.getInstance().getMyLocation(), model2.getmPosition()));
                                myAdvertList.add(new NearJobAdvertModel(model2.getJobID(),
                                        model2.getCompanyName(),
                                        model2.getCompanyJob(),
                                        model2.getJobSector(),
                                        model2.getJobDescpriction(),
                                        model2.getCompanyLogoUrl(),
                                        model2.getCompanyAdress(),
                                        model2.getEducationLevel(),
                                        model2.getExpLevel(),
                                        model2.getEmployeeHour(),
                                        model2.getDrivingLicence(),
                                        model2.getMilitary(),
                                        model2.getGender(),
                                        model2.getPublishDate(),
                                        model2.getCountApply(),
                                        model2.getJobPossibility(),
                                        model2.getmPosition(),
                                        applyAdvertModel.getSave(), applyAdvertModel.getApply(),
                                        MapHelperMethods.getApplyMarkerDrawable((getActivity())),
                                        "", String.format(Locale.getDefault(), "%.2f", distance)
                                ));
                                callbackCounter++;

                            }


                        }
                        fillRecyclerView();

                        //retainJobAdvertFromFirebase.execute(dataSnapshot);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

/*
    @Override
    public void JobAdvertFromFirebaseCallback(ArrayList<JobAdvertModel2> result) {
        Log.d(TAG, "JobAdvertFromFirebaseCallback: " + result);
        for (JobAdvertModel2 model2 : result) {
            Log.d(TAG, "JobAdvertFromFirebaseCallback: " + model2);

            callbackCounter++;
        }


    }*/
}
