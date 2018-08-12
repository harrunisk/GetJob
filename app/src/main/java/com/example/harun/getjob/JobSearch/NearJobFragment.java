package com.example.harun.getjob.JobSearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;

import com.example.harun.getjob.JobSearch.JobUtils.NearJobAdvertAdapter;
import com.example.harun.getjob.JobSearch.JobUtils.NearJobAdvertModel;
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
    private ArrayList<NearJobAdvertModel> nearJobList;
    private ViewSwitcher nearJobViewSwitch;
    private NearJobAdvertAdapter mJobAdvertAdapter;

    public NearJobFragment() {


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.nearjobfragment_content, null);
        nearJobListRecycler = view.findViewById(R.id.nearJobList);
        nearJobViewSwitch = view.findViewById(R.id.nearJobViewSwitch);
        // int resId = R.anim.layout_animation;
       setDataList();

        return view;
    }


    private void setDataList() {

        //  Log.d(TAG, "setDataList: ");
        try {
            nearJobList = getArguments().getParcelableArrayList("nearJobList");
            if (nearJobList != null && nearJobList.size() > 0) {

                nearJobViewSwitch.setDisplayedChild(1);

            } else {

                nearJobViewSwitch.setDisplayedChild(0);

            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "setDataList: " + nearJobList);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        mJobAdvertAdapter = new NearJobAdvertAdapter(getContext(), nearJobList,0);
        nearJobListRecycler.setAdapter(mJobAdvertAdapter);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        // SnapHelper snapHelper = new PagerSnapHelper();
        nearJobListRecycler.setNestedScrollingEnabled(false);
        nearJobListRecycler.setLayoutManager(linearLayoutManager2);
        // nearJobListRecycler.setItemAnimator(new DefaultItemAnimator());
        nearJobListRecycler.setHasFixedSize(true);
        //   nearJobListRecycler.setRecyclerListener(this);

        //  snapHelper.attachToRecyclerView(nearJobListRecycler);

    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        Log.d(TAG, "onViewRecycled: ");
        NearJobAdvertAdapter.MyViewHolder myViewHolder = (NearJobAdvertAdapter.MyViewHolder) holder;
        Log.d(TAG, "onViewRecycled: aaaa");
        if (myViewHolder != null && myViewHolder.mMap != null) {
            Log.d(TAG, "onViewRecycled: ü");
            myViewHolder.mMap.clear();
            myViewHolder.mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        }

    }
}
