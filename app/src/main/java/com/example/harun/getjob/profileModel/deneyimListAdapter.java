package com.example.harun.getjob.profileModel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.harun.getjob.Profile.ProfileInterfaces;
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
    ProfileInterfaces mcontentFragment;


    public deneyimListAdapter(Context context, ArrayList<deneyimModel> mList, boolean visibility) {
        Log.d(TAG, "Kurucu Metod");
        inflater = LayoutInflater.from(context);
        this.mList = mList;
        this.mContext = context;
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {

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


    /**
     * Bu method bir update butonuna tıklandıgında tıklanan liste ögesinin tüm verileri + pozisyonu ınterface yardımı ile
     * edit profile activtyden yakalanacak.
     *
     * @param model
     * @param position
     */
    public void editList(deneyimModel model, int position) {

        Log.d(TAG, "editList: " + model + position);
        this.mcontentFragment = (ProfileInterfaces) mContext;
        mcontentFragment.updateDeneyimListItem(model, position);

        // notifyItemChanged(position);

        // mList.add(position, model);
        // notifyItemInserted(position);
        // notifyItemRangeChanged(position, mList.size());


    }

    public HashMap<String, ArrayList<deneyimModel>> getDeneyimHash() {

        return this.deneyimHash;
    }

    public void removeItem(int position, ArrayList<deneyimModel> deneyimList) {

       // Log.d(TAG, "restoreItem: SİLİNMEDEN ÖNCEKİ HASH " + deneyimHash);

        deneyimHash.clear();
        // this.deneyimHash.remove(String.valueOf(position));

        this.deneyimHash.put(String.valueOf(position), deneyimList);
        notifyItemRemoved(position);

        //deneyimHash.put(String.valueOf(position), deneyimHashList);
       // Log.d(TAG, "restoreItem: " + position + "KALDIRILDI");
        //Log.d(TAG, "restoreItem: SİLİNDİKTEK SONRA DENEYİM HASH " + deneyimHash);


    }


    @Override
    public int getItemCount() {


        return mList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView pozisyon, lokasyon, kurum, ayText;
        ImageView editDeneyimListRow;
        deneyimModel model1;
        int positionItem;
        public RelativeLayout deneyimView_foreground;


        MyViewHolder(View itemView) {
            super(itemView);

            pozisyon = itemView.findViewById(R.id.tvPozisyon);
            lokasyon = itemView.findViewById(R.id.tvLokasyon);
            kurum = itemView.findViewById(R.id.tvKurum);
            ayText = itemView.findViewById(R.id.ayText);
            editDeneyimListRow = itemView.findViewById(R.id.editDeneyimListRow);
            deneyimView_foreground = itemView.findViewById(R.id.deneyimview_foreground);
            editDeneyimListRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editList(model1, positionItem);
                }
            });

        }


        public void setData(deneyimModel model, int position) {
            Log.d(TAG, "Veriler implement edildi SET DATA");
            this.pozisyon.setText(model.getPozisyon());
            this.lokasyon.setText(model.getLokasyon());
            this.ayText.setText(model.getAy().concat(" Ay"));
            this.kurum.setText(model.getKurum());

            this.model1 = model;
            this.positionItem = position;

            deneyimHashList.add(model);
            deneyimHash.put(String.valueOf(position), deneyimHashList);
            Log.d(TAG, "setData: HER EKLEMEDE " + deneyimHash.entrySet());

        }
    }
}
