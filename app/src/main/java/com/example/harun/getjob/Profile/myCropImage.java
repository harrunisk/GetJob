package com.example.harun.getjob.Profile;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

/**
 * Created by mayne on 3.03.2018.
 */

public class myCropImage {

    private static final String TAG = "CropImage";
    Context context;
    Fragment fm;

    public myCropImage(Context context,Fragment fm) {
        this.context = context;
        this.fm=fm;
    }

    public void editPhoto(Uri selectedImageuri) {
        Log.d(TAG, "CropImage: URi" + selectedImageuri);

             CropImage.activity(selectedImageuri)
                .setGuidelines(CropImageView.Guidelines.OFF)
                .setActivityTitle("Fotoğrafı Düzenle")
                .setCropShape(CropImageView.CropShape.OVAL)
                .setCropMenuCropButtonTitle("Tamam")
                // .setGuidelinesColor(Color.BLUE)
                .setInitialCropWindowPaddingRatio(0)
                .setAutoZoomEnabled(true)
                // .setOutputUri(outputUri)
                .setRequestedSize(120, 120)
                // .setCropMenuCropButtonIcon(R.drawable.arrow)
                .start(context,fm);


    }
}
