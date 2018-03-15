package com.example.harun.getjob.profileModel;

import android.util.Log;

/**
 * Created by mayne on 28.02.2018.
 */

public class PhotoModel {

    private String bucketId,
            imageBucket,
            imagePath,
            imageName;


    public PhotoModel(String bucketId, String imageBucket, String imagePath, String imageName) {
        this.bucketId = bucketId;
        this.imageBucket = imageBucket;
        this.imageName = imageName;
        this.imagePath = imagePath;
        Log.d("PHOTOMODEL ", "LİSTE ELEMMANLARI DOLDURULUYOR ");
    }

    public String getBucketId() {
        return bucketId;
    }

    public String getImageBucket() {
        return imageBucket;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImagePath() {
        return imagePath;
    }


}
