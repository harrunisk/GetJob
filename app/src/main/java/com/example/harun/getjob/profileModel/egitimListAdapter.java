package com.example.harun.getjob.profileModel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.harun.getjob.R;

import java.util.ArrayList;

/**
 * Created by mayne on 17.02.2018.
 */

public class egitimListAdapter extends RecyclerView.Adapter<egitimListAdapter.ViewHolder> {

    ArrayList<egitimListModel> egitimList;
    private final static String TAG = "egitimListAdapter";
    LayoutInflater inflater;
    egitimListModel egitimListModel;

    public egitimListAdapter(Context context, ArrayList<egitimListModel> egitimList) {
        this.egitimList = egitimList;
        this.inflater = LayoutInflater.from(context);




    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.egitimlist_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        Log.d(TAG,"EgitimListOnCreateViewHolder");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        egitimListModel=egitimList.get(position);

        holder.bsYil.setText(egitimListModel.getBsYılı());
        holder.btsYil.setText(egitimListModel.getBtsYılı());
        holder.nameOkul.setText(egitimListModel.getOkul());
        holder.tvBolum.setText(egitimListModel.getBolum());
        holder.tvTur.setText(egitimListModel.getOgrenimTuru());



    }

    @Override
    public int getItemCount() {
        return egitimList.size();
    }


    // Bir satırda bulunan elemanları tanımlayacagımız sınıf
    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView bsYil,btsYil,nameOkul,tvBolum,tvTur;

        public ViewHolder(View itemView) {
            super(itemView);
            bsYil=itemView.findViewById(R.id.bsYil);
            btsYil=itemView.findViewById(R.id.btsYil);
            nameOkul=itemView.findViewById(R.id.nameOkul);
            tvBolum=itemView.findViewById(R.id.tvBolum);
            tvTur=itemView.findViewById(R.id.tvTur);



        }
    }
}
