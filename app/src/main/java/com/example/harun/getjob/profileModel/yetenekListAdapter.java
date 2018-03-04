package com.example.harun.getjob.profileModel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.harun.getjob.R;

import java.util.ArrayList;

/**
 * Created by mayne on 24.02.2018.
 */

public class yetenekListAdapter extends RecyclerView.Adapter<yetenekListAdapter.MyViewHolder> {
    private static final String TAG = "yetenekListAdapter";

    LayoutInflater layoutInflater;
    ArrayList<yetenekModel> yetenekListe;

    public yetenekListAdapter(Context context, ArrayList<yetenekModel> yetenekListe) {

        this.layoutInflater = LayoutInflater.from(context);
        this.yetenekListe = yetenekListe;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.yeteneklist_row, parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(v);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        yetenekModel myetenekmodel = yetenekListe.get(position);

        holder.tvYetenek.setText(myetenekmodel.getYetenekName().toUpperCase());
        holder.rateStar.setRating(((float) myetenekmodel.getRate()));

    }

    @Override
    public int getItemCount() {
        return yetenekListe.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvYetenek;
        RatingBar rateStar;

        public MyViewHolder(View itemView) {
            super(itemView);
            //satır elemanları tanımlanacak

            tvYetenek = itemView.findViewById(R.id.yetenek);
            rateStar = itemView.findViewById(R.id.yetenekRating);
        }
    }
}
