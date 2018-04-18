package com.example.harun.getjob.JobSearch.JobUtils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harun.getjob.Profile.UniversalImageLoader;
import com.example.harun.getjob.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by mayne on 20.03.2018.
 */


/**
 * Sizin için öneriliyor Listesinin Adapteri ..
 */
public class JobAdvertAdapter extends RecyclerView.Adapter<JobAdvertAdapter.MyViewHolder> {

    private static final String TAG = "JobAdvertAdapter";
    private ArrayList<JobAdvertModel> jobAdvertModelArrayList;
    private LayoutInflater inflater;
    private Context mContext;
    JobAdvertModel mAdvertModel;

    public JobAdvertAdapter(Context context, ArrayList<JobAdvertModel> jobAdvertModelArrayList) {
        Log.d(TAG, "JobAdvertAdapter: Constructor ");
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.jobAdvertModelArrayList = jobAdvertModelArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.job_advert, null, false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        mAdvertModel = jobAdvertModelArrayList.get(position);

        holder.companyName.setText(mAdvertModel.getCompanyName());
        holder.companyJob.setText(mAdvertModel.getCompanyJob());
        holder.jobDescpriction.setText(mAdvertModel.getJobDescpriction());
        holder.companyLocation.setText(mAdvertModel.getCompanyLocation());
        holder.companyDistance.setText(mAdvertModel.getCompanyDistance());


        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.configuration());
        UniversalImageLoader.setImage(mAdvertModel.getCompanyLogoUrl(), holder.companyLogo, null, "");


    }

    @Override
    public int getItemCount() {
        return jobAdvertModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView companyName, companyJob, jobDescpriction, companyLocation, companyDistance;
        ImageView companyLogo,saveAdvert;
       // FloatingActionButton saveAdvert;

        public MyViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "MyViewHolder: ");
            companyName = itemView.findViewById(R.id.companyName);
            companyJob = itemView.findViewById(R.id.companyJob);
            jobDescpriction = itemView.findViewById(R.id.jobDescpriction);
            companyLocation = itemView.findViewById(R.id.companyLocation);
            companyDistance = itemView.findViewById(R.id.companyDistance);
            companyLogo = itemView.findViewById(R.id.companyLogo);
            saveAdvert = itemView.findViewById(R.id.saveAdvert);

            saveAdvert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "İlan Kayıt Edildi( Eklenecek. )", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
}
