package com.example.harun.getjob.JobSearch.JobUtils;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.harun.getjob.R;

import java.util.ArrayList;

/**
 * Created by mayne on 22.03.2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements View.OnClickListener {

    private static final String TAG = "CategoryAdapter";

    private LayoutInflater layoutInflater;
    private CategoryModel mcategoryModel;
    private Context mContext;
    private ArrayList<CategoryModel> categoryModelArrayList;
    private int selected_position = 0;
    private RecyclerView mRecyler;
    private CategoryAdapterInterface loadAdvertSectorInfo;
    private ArrayList<View> selectedView = new ArrayList<>(1);

    public interface CategoryAdapterInterface {

        void LoadAdvertWithSectorInfo(String sectorName);


    }


    public CategoryAdapter(Context mContext, ArrayList<CategoryModel> categoryModelArrayList, CategoryAdapterInterface _loadAdvertSectorInfo) {
        layoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.categoryModelArrayList = categoryModelArrayList;
        this.loadAdvertSectorInfo = _loadAdvertSectorInfo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.jobsector_categotylist, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mcategoryModel = categoryModelArrayList.get(position);
        holder.categoryName.setText(mcategoryModel.getCategoryName());
        holder.count.setText(mcategoryModel.getAdvertCount());
        if (selected_position == position) {
            Log.d(TAG, "onBindViewHolder: " + selected_position + "\t" + position);
            holder.categoryBackLayout.setSelected(true);
           // holder.countBack.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.SandyBrown)));
            selectedView.add(holder.categoryBackLayout);
            startAnim(holder.categoryLayout);
        } else {
            holder.categoryBackLayout.setSelected(false);
          //  holder.countBack.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.Wheat)));
            Log.d(TAG, "onBindViewHolder:previousPosition ! position ");
        }

        holder.categoryBackLayout.setTag(R.id.PositionTag, position);
        holder.categoryBackLayout.setTag(R.id.CategoryTag, mcategoryModel.getCategoryName());
        holder.categoryBackLayout.setOnClickListener(this);

    }

    private void startAnim(View view) {
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_in);
        view.startAnimation(anim);
        anim.setFillAfter(true);
        anim.setFillEnabled(true);

    }

//    private void changeCountBackground(){
//
//
//
//
//
//    }


    @Override
    public void onClick(View view) {
        selected_position = (int) view.getTag(R.id.PositionTag);
        Log.d(TAG, "onClick:MEtodu " + selected_position + (String) view.getTag(R.id.CategoryTag));
        checkOtherSelected();
        if (!view.isSelected()) {
            loadAdvertSectorInfo.LoadAdvertWithSectorInfo((String) view.getTag(R.id.CategoryTag)); //-->JobSearch
            view.setSelected(true);
            startRevealAnim(view);
            startAnim((View) view.getParent());
            selectedView.add(view);
        }
    }

    private void checkOtherSelected() {
        if (selectedView.size() > 0) {
            Log.d(TAG, "checkOtherSelected:selectedView.get(0).isSelected() ");
            selectedView.get(0).setSelected(false);
            View v = (View) selectedView.get(0).getParent();
            v.clearAnimation();
            selectedView.clear();
        } else {
            Log.d(TAG, "checkOtherSelected: LİSTE BOS ");
        }
    }

    /**
     * Reveal Animasyon
     *
     * @param categoryBackLayout
     */
    private void startRevealAnim(View categoryBackLayout) {
        ViewAnimationUtils.createCircularReveal(categoryBackLayout,
                categoryBackLayout.getWidth(),
                categoryBackLayout.getHeight(),
                0,
                categoryBackLayout.getHeight() * 2).start();


    }


    @Override
    public int getItemCount() {

        return categoryModelArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName, count;
        CardView categoryLayout;
        RelativeLayout categoryBackLayout;
        View countBack;

        public ViewHolder(final View itemView) {
            super(itemView);

            // categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryLayout = itemView.findViewById(R.id.categoryLayout);
            categoryBackLayout = itemView.findViewById(R.id.categoryBackLayout);
            count = itemView.findViewById(R.id.count);
            countBack = itemView.findViewById(R.id.countBack);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick:itemView ");
                    selected_position = getAdapterPosition();
                  //  countBack.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.SandyBrown)));

                    Log.d(TAG, "onClick: " + getAdapterPosition());

                }
            });

        }
    }
}
