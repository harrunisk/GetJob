package com.example.harun.getjob.JobSearch;

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

import com.example.harun.getjob.JobSearch.JobUtils.NearJobAdvertAdapter;
import com.example.harun.getjob.JobSearch.JobUtils.NearJobAdvertModel;
import com.example.harun.getjob.R;

import java.util.ArrayList;

/**
 * Created by mayne on 30.03.2018.
 */

/**
 * Tüm İşlerin  Görüntülendiği fragment
 */
public class AllJobFragment extends Fragment {

    private static final String TAG = "AllJobFragment";

    private RecyclerView allJobRecylerList;
    private ArrayList<NearJobAdvertModel> allJobList;
    private NearJobAdvertAdapter mJobAdvertAdapter;
    private ViewSwitcher allJobViewSwitch;


    public AllJobFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nested_scroll, null);

        gatherViews(view);
        return view;


    }

    private void gatherViews(View view) {

        allJobRecylerList = view.findViewById(R.id.nearJobList);
        allJobViewSwitch = view.findViewById(R.id.nearJobViewSwitch);
        setDataList();
    }

    private void setDataList() {

        //  Log.d(TAG, "setDataList: ");
        try {
            allJobList = getArguments().getParcelableArrayList("allJobList");
            if (allJobList!=null&&allJobList.size() > 0) {
                allJobViewSwitch.setDisplayedChild(1);
            } else {
                allJobViewSwitch.setDisplayedChild(0);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "setDataList: " + allJobList);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setDataList();
        mJobAdvertAdapter = new NearJobAdvertAdapter(getContext(), allJobList);
        allJobRecylerList.setAdapter(mJobAdvertAdapter);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        allJobRecylerList.setNestedScrollingEnabled(false);
        allJobRecylerList.setLayoutManager(linearLayoutManager2);
        allJobRecylerList.setHasFixedSize(true);


    }
}
