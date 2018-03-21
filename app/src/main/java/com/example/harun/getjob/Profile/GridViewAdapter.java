package com.example.harun.getjob.Profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.harun.getjob.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

/**
 * Created by mayne on 26.02.2018.
 */

public class GridViewAdapter extends ArrayAdapter<String> {
    public static final String TAG = "GridViewAdapter";
    public Context context;
    private LayoutInflater mlayoutinf;
    private int layoutResource;
    private String append;
    private ArrayList<String> imageUrl;


    public GridViewAdapter(Context context, int layoutResource, String append, ArrayList<String> imageUrl) {
        super(context, layoutResource, imageUrl);
       // Log.d(TAG, "GridViewAdapter: "+append+" " +imageUrl.get(0));
        this.context = context;
        this.mlayoutinf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutResource = layoutResource;
        this.append = append;
        this.imageUrl = imageUrl;
    }

    private static class ViewHolder {
        CustomImageView gridimageView;
        AVLoadingIndicatorView gridProgressBar;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder mholder;
        Log.d(TAG, "getView: ");
        if (convertView == null) {

            convertView = mlayoutinf.inflate(layoutResource, parent, false);
            mholder = new ViewHolder();
            mholder.gridimageView = convertView.findViewById(R.id.gridView_image);
            mholder.gridProgressBar = convertView.findViewById(R.id.grid_imageProgressBar);

            convertView.setTag(mholder);

        } else {
            mholder = (ViewHolder) convertView.getTag();


        }
        String imgUrl = getItem(position);
        ImageLoader image = ImageLoader.getInstance();
        image.displayImage(append + imgUrl, mholder.gridimageView, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if (mholder.gridProgressBar != null) {
                    Log.d(TAG, "onLoadingStarted: "+imageUri);
                    mholder.gridProgressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (mholder.gridProgressBar != null) {
                    Log.d(TAG, "onLoadingFailed: "+failReason);
                    mholder.gridProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (mholder.gridProgressBar != null) {
                    mholder.gridProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if (mholder.gridProgressBar != null) {
                    mholder.gridProgressBar.setVisibility(View.GONE);
                }
            }
        });

        return convertView;
    }
}
