package com.example.harun.getjob.JobSearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.harun.getjob.JobSearch.JobUtils.JobAdvertModel;
import com.example.harun.getjob.JobSearch.JobUtils.NearJobAdvertAdapter;
import com.example.harun.getjob.R;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

/**
 * Created by mayne on 30.03.2018.
 */

public class NearJobFragment extends Fragment implements RecyclerView.RecyclerListener {
    Bundle extras;
    private static final String TAG = "NearJobFragment";
    private RecyclerView nearJobListRecycler;
    ArrayList<JobAdvertModel> nearJobList;
    private NearJobAdvertAdapter mJobAdvertAdapter;


    public NearJobFragment() {


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_nested_scroll, null);
        nearJobListRecycler = view.findViewById(R.id.nearJobList);
        setDataList();

        return view;
    }

    private void setDataList() {

        //  Log.d(TAG, "setDataList: ");
        try {
            nearJobList = getArguments().getParcelableArrayList("yetenekSatir");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "setDataList: " + nearJobList);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        //Burda List View Doldurulacak Activity Olusutugunda

        mJobAdvertAdapter = new NearJobAdvertAdapter(getContext(), nearJobList);
        nearJobListRecycler.setAdapter(mJobAdvertAdapter);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        // SnapHelper snapHelper = new PagerSnapHelper();
        nearJobListRecycler.setNestedScrollingEnabled(false);
        nearJobListRecycler.setLayoutManager(linearLayoutManager2);
        nearJobListRecycler.setItemAnimator(new DefaultItemAnimator());
        nearJobListRecycler.setHasFixedSize(true);
        nearJobListRecycler.setRecyclerListener(this);
        //  snapHelper.attachToRecyclerView(nearJobListRecycler);


    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {

        NearJobAdvertAdapter. MyViewHolder myViewHolder = (NearJobAdvertAdapter.MyViewHolder) holder;
        Log.d(TAG, "onViewRecycled: aaaa");
        if (myViewHolder != null && myViewHolder.mMap != null) {
            Log.d(TAG, "onViewRecycled: ü");
            myViewHolder.mMap.clear();
            myViewHolder.mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        }

    }
}
