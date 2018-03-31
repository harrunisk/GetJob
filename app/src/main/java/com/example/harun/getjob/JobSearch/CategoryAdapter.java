package com.example.harun.getjob.JobSearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.harun.getjob.R;

import java.util.ArrayList;

/**
 * Created by mayne on 22.03.2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private static final String TAG = "CategoryAdapter";

    LayoutInflater layoutInflater;
    CategoryModel mcategoryModel;
    Context mContext;
    private ArrayList<CategoryModel> categoryModelArrayList;

    public CategoryAdapter(Context mContext, ArrayList<CategoryModel> categoryModelArrayList) {
        layoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.categoryModelArrayList = categoryModelArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.category_list_row, null, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        mcategoryModel = categoryModelArrayList.get(position);


        holder.categoryName.setText(mcategoryModel.getCategoryName());
        holder.categoryImage.setImageResource(mcategoryModel.getRes());

        //Seçilen kategoriye animasyon atıyorum .
        holder.categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   if (focus) {

                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_in);
                holder.categoryLayout.startAnimation(anim);
                anim.setFillAfter(true);
             //   holder.categoryLayout.requestLayout();
               /* } else {

                    Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_out);
                    holder.categoryLayout.startAnimation(anim);
                    anim.setFillAfter(true);
                }*/
            }

        });

    }

    @Override
    public int getItemCount() {
        return categoryModelArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;
        ImageView categoryImage;
        RelativeLayout categoryLayout;

        public ViewHolder(final View itemView) {
            super(itemView);

            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryLayout = itemView.findViewById(R.id.categoryLayout);
            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean focus) {

                    if (focus) {

                        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_in);
                        itemView.startAnimation(anim);
                        anim.setFillAfter(true);

                    } else {

                        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_out);
                        itemView.startAnimation(anim);
                        anim.setFillAfter(true);
                    }

                }
            });

        }
    }
}
