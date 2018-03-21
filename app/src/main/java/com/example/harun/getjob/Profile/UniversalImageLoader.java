package com.example.harun.getjob.Profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.example.harun.getjob.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by mayne on 26.02.2018.
 */

public class UniversalImageLoader {

    public static final int defaultImage = R.drawable.man;
    private Context context;


    public UniversalImageLoader(Context context) {
        this.context = context;
    }

    public ImageLoaderConfiguration configuration() {

        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(defaultImage).showImageOnFail(defaultImage).cacheOnDisk(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY).displayer(new FadeInBitmapDisplayer(200)).build();


        ImageLoaderConfiguration imageLoaderconfig = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(displayImageOptions).memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();

        return imageLoaderconfig;
    }


    public static void setImage(String imageUrl, ImageView image, final AVLoadingIndicatorView mProgressBar, String append ){
        ImageLoader imageLoader= ImageLoader.getInstance();

        imageLoader.displayImage(append + imageUrl, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(mProgressBar != null){ //progressBar bazı yerlerde null gonderdiğim için kontrolu yapıldı
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if(mProgressBar != null){
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(mProgressBar != null){
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if(mProgressBar != null){
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });


    }
}
