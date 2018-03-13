package com.example.harun.getjob.profileModel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.harun.getjob.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mayne on 24.02.2018.
 */

public class yetenekListAdapter extends RecyclerView.Adapter<yetenekListAdapter.MyViewHolder> {
    private static final String TAG = "yetenekListAdapter";

    LayoutInflater layoutInflater;
    ArrayList<yetenekModel> yetenekListe;
    // yetenekModel yetenekModel=new yetenekModel();
    private HashMap<String, ArrayList<yetenekModel>> yetenekHash = new HashMap<>();
    private ArrayList<yetenekModel> hashyetenek = new ArrayList<>();
    private boolean visibilityCheck;

    public yetenekListAdapter(Context context, ArrayList<yetenekModel> yetenekListe, boolean visibility) {
        Log.d(TAG, "yetenekListAdapter: ");
        this.layoutInflater = LayoutInflater.from(context);
        this.yetenekListe = yetenekListe;
        this.visibilityCheck = visibility;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View v = layoutInflater.inflate(R.layout.yeteneklist_row, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ÇALIŞIYOPRRR\t" + position);
        yetenekModel myetenekmodel = yetenekListe.get(position);

        //yetenekHash1=yetenekModel.hashmapping(String.valueOf(position), myetenekmodel);

        setYetenekHash(String.valueOf(position), myetenekmodel);
        //  Log.d(TAG, "onBindViewHolder: yetenekHASH \t"+yetenekHash1.size());


        holder.tvYetenek.setText(myetenekmodel.getYetenekName().toUpperCase());
        holder.rateStar.setRating(((float) myetenekmodel.getRate()));

        if (visibilityCheck) {

            holder.editYetenekrow.setVisibility(View.GONE);
        } else {
            holder.editYetenekrow.setVisibility(View.VISIBLE);
        }


    }

    private void setYetenekHash(String position, yetenekModel model) {

        Log.d(TAG, "setYetenekHash: ");
        hashyetenek.add(model);
        yetenekHash.put(position, hashyetenek);
        Log.d("HASHMAPPİNGG ", "hashmapping: " + yetenekHash.entrySet());

        for (yetenekModel mode : hashyetenek) {

            Log.d("FOR ", "hashmapping: " + mode.getYetenekName()
                    + "\t" + mode.getRate() + "\t" + model.getYetenekName());

        }

    }

    public HashMap<String, ArrayList<yetenekModel>> getYetenekHash() {
        Log.d(TAG, "getYetenekHash: +" + yetenekHash.entrySet());
        return this.yetenekHash;

    }

    @Override
    public int getItemCount() {
        return yetenekListe.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvYetenek;
        RatingBar rateStar;
        ImageView editYetenekrow;

        public MyViewHolder(View itemView) {
            super(itemView);
            //satır elemanları tanımlanacak

            tvYetenek = itemView.findViewById(R.id.yetenek);
            rateStar = itemView.findViewById(R.id.yetenekRating);
            editYetenekrow = itemView.findViewById(R.id.editYetenekrow);
        }
    }
}
