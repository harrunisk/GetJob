package com.example.harun.getjob.JobSearch;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.harun.getjob.R;

/**
 * Created by mayne on 30.03.2018.
 */

public class NearJobFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_nested_scroll,null);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //Burda List View Doldurulacak Activity Olusutugunda



        super.onViewCreated(view, savedInstanceState);
    }
}
