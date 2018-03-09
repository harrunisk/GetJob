package com.example.harun.getjob.Profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.harun.getjob.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by mayne on 26.02.2018.
 */

public class cameraFragment extends Fragment {
    public static final String TAG = "cameraFragment";
    public static final int CAMERA_REQUEST_CODE = 3;
    public static final int CurrentTab = 1;// Kameranın tab numarası 1
    Uri uri;
    Bitmap bitmap;
    Bitmap cropped;
    String newimagepath;
    FilePaths filePaths;
    Uri outputUri;
    CropImage.ActivityResult croppingImageresult;

    /**
     * BU method onCreate den önce calışacak ve Tab Sekmesi görünür oldugunda açılacak
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint: ÇALIŞIYOR");

        if (isVisibleToUser && isResumed()) {
            loadData();

        }

    }

    private void loadData() {
        /**
         * Tüm Bu işlemler Kullanıcı Camera TAbına Geçtiği Zaman Yapılmalı
         */
        if (((PhotoActivity) getActivity()).getCurrentTabNumber() == CurrentTab) {
            Log.d(TAG, "loadDATA: CAMERA TAB ");
        /*Buradan true donmesi durumunda işlemler  yapılmalı*/
            if (Permissions.checkPermision(getActivity(), Permissions.CAMERA_PERMISSION[0])) {
                Log.d(TAG, "LOAD DATA : İzinler Alınmış Sıkıntı Tın  ");
                init();

            } else {
                //Yoksa İzinler İstenmeli Öyle Devam Edilmeli
                // Permissions.requestPermission(getActivity(), Permissions.CAMERA_PERMISSION);
                Intent i = new Intent(getActivity(), PhotoActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // startActivityForResult(i, 3, null);
                startActivity(i);
                Toast.makeText(getActivity(), "Kamera İzini Verilmemiş", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera, container, false);
        Log.d(TAG, "onCreateView: ÇALIŞIYOR  ");
        if (getUserVisibleHint()) { // fragment is visible
            loadData();
        }


        return v;
    }

    private void init() {
        filePaths = new FilePaths();
        Log.d(TAG, "init: STARTİNG CAMERA ");
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, CAMERA_REQUEST_CODE);


    }

    private void cropImageActivity() {
        CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.OFF)
                .setActivityTitle("Fotoğrafı Düzenle")
                .setCropShape(CropImageView.CropShape.OVAL)
                .setCropMenuCropButtonTitle("Tamam")
                // .setGuidelinesColor(Color.BLUE)
                .setInitialCropWindowPaddingRatio(0)
                .setAutoZoomEnabled(true)
                // .setOutputUri(outputUri)
                .setRequestedSize(500, 500)
                // .setCropMenuCropButtonIcon(R.drawable.arrow)
                .start(getContext(), this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            Log.d(TAG, "startActivityForResult: FOTOGRAF ALMA TAMAMLANMIS ");

            //paremetreden gelen data değeri bitmap olarak aldım
            bitmap = (Bitmap) data.getExtras().get("data");
            //Uri outputFileUri = Uri.fromFile(new File(context.getExternalCacheDir().getPath(), "getjob.jpeg"));
          //  uri = filePaths.getImageFile(getActivity(), bitmap);
            cropImageActivity();


        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                croppingImageresult = CropImage.getActivityResult(data);
                Log.d(TAG, "onActivityResult:CAMERA CROPactivty" + croppingImageresult.getUri());


                //Crop Edilen Fotoğrafı bitmap olarak alıp kayıt etmek için
                cropped=filePaths.getCroppedBitmap(croppingImageresult.getUri());//Crop edilen image urisi gönderiliyor.
                outputUri = filePaths.getImageFile(getActivity(), cropped); //Kesilen fotograf burada kaydedilip kayıt edilen yerin uri'si return edilir



                //getCroppingImageUri(croppingImageresult.getUri());


                try {

                    Log.d(TAG, "onActivityResult: Cameradan Gelen Bitmap" + cropped);
                    Intent i = new Intent(getActivity(), EditProfile.class);
                    i.putExtra("imageFromCam", outputUri);
                    //Resimin pathini çekildikten sonra kayıt edildiği pathi gönderiyorum edit profile universal image ile yüklencek
                    startActivity(i);
                    getActivity().finish();


                } catch (NullPointerException e) {
                    Log.d(TAG, "onActivityResult: HATA " + e.getMessage());
                }


            }


        }


    }

   /* private void getCroppingImageUri(Uri uri) {

        Log.d(TAG, "getCroppingImageUri: "+uri);
        File resultFile=new File(uri.getPath());

        BitmapFactory.Options bitmapOpts = new BitmapFactory.Options();
        bitmapOpts.inMutable = true;
        Bitmap cropped = BitmapFactory.decodeFile(resultFile.getPath(), bitmapOpts);
        outputUri = filePaths.getImageFile(getActivity(), cropped);
        Log.d(TAG, "getCroppingImageUri:CAMERA  OUTPUT URİ"+outputUri);

    }
*/

}
