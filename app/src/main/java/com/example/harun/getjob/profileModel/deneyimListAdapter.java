package com.example.harun.getjob.profileModel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harun.getjob.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mayne on 16.02.2018.
 */

public class deneyimListAdapter extends RecyclerView.Adapter<deneyimListAdapter.MyViewHolder> {

    LayoutInflater inflater;
    ArrayList<deneyimModel> mList;
    private static final String TAG = "deneyimAdapter";
    private HashMap<String, ArrayList<deneyimModel>> deneyimHash = new HashMap<String, ArrayList<deneyimModel>>();
    Context mContext;
    private ArrayList<deneyimModel> deneyimHashList = new ArrayList<>();
    deneyimModel model;
    private boolean visibilityCheck;

    public deneyimListAdapter(Context context, ArrayList<deneyimModel> mList, boolean visibility) {
        Log.d(TAG, "Kurucu Metod");
        inflater = LayoutInflater.from(context);
        this.mList = mList;
        this.visibilityCheck = visibility;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.deneyimlist_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        Log.d(TAG, "Satır xml olusturuldu CREATE HOLDER");

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        model = mList.get(position);
        //Eklemeleri burdada yapabilirdim ama bu sekilde yapıyorum.

        holder.setData(model, position);
        Log.d(TAG, "Veriler xml bağlandı BİNDHOLDER");

        if (visibilityCheck) {

            holder.editDeneyimListRow.setVisibility(View.GONE);
        } else {
            holder.editDeneyimListRow.setVisibility(View.VISIBLE);
        }


    }

    public HashMap<String, ArrayList<deneyimModel>> getDeneyimHash() {

        return this.deneyimHash;
    }

    @Override
    public int getItemCount() {


        return mList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView pozisyon, lokasyon, kurum, ayText;
        ImageView editDeneyimListRow;

        MyViewHolder(View itemView) {
            super(itemView);

            pozisyon = itemView.findViewById(R.id.tvPozisyon);
            lokasyon = itemView.findViewById(R.id.tvLokasyon);
            kurum = itemView.findViewById(R.id.tvKurum);
            ayText = itemView.findViewById(R.id.ayText);
            editDeneyimListRow = itemView.findViewById(R.id.editDeneyimListRow);
        }

        public void setData(deneyimModel model, int position) {
            Log.d(TAG, "Veriler implement edildi SET DATA");
            this.pozisyon.setText(model.getPozisyon());
            this.lokasyon.setText(model.getLokasyon());
            this.ayText.setText(model.getAy() + "Ay");
            this.kurum.setText(model.getKurum());

            deneyimHashList.add(model);
            deneyimHash.put(String.valueOf(position), deneyimHashList);

        }
    }
}
