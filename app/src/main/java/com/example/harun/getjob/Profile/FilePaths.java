package com.example.harun.getjob.Profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by mayne on 26.02.2018.
 */

public class FilePaths {

    private static final String TAG = "FilePaths";

    public String ROOT_DIR = Environment.getExternalStorageDirectory()
            + File.separator + "GETJOB"; //Olustuurlacak Klasor ismi

    public String FullName;


    public String mypath = "/storage/6F1B-5112";
    public String PICTURES = ROOT_DIR + "/Pictures";
    public String CAMERA = mypath + "/DCIM";

    /**
     * CEKİLEN FOTOGRAFI GALERİYE EKLEYİP EKLEDİĞİM YOLU GÖNDERİYORUM.
     *
     * @param context
     * @param bitmap
     * @return galeriye eklenen fotografın yolu
     */
    public Uri getImageUri(Context context, Bitmap bitmap) {

        Log.d(TAG, "getImageUri: " + bitmap);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String insertedPhotopath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "GETJOB", "profile");
        Log.d(TAG, "getImageUri: " + insertedPhotopath);
        //  Uri outputFileUri = Uri.fromFile(new File(context.getExternalCacheDir().getPath(), "pickImageResult.jpeg"));
        return Uri.parse(insertedPhotopath);
    }

    /**
     * Galeriden seçilen resim veya kameradan seçilen resimi getJob adında bir klasör oluşuturp içersine kayıt eder
     *
     * @param context
     * @param bm
     * @return  Kayıt edilen resimin dosya yolu uri olarak döndürülür.Bu uri path
     *
     * EditProfile putExtra ile gönderilir EditProfildeki ProfilResimi Güncellenir.
     */
    public Uri getImageFile(Context context, Bitmap bm) {
        Log.d(TAG, "getImageFile: BİTMAP" + bm);

        Calendar calendar = Calendar.getInstance();

        Random r = new Random();
        String number = String.valueOf(r.nextInt(1000) + 1);

        FullName = "GetJobProfile" + calendar.get(Calendar.MILLISECOND) + number + ".jpg";//Buda resimin ismi

        ///Klasorü olusturacak
        File directory = new File(ROOT_DIR + File.separator);
        if (!directory.exists()) {

            directory.mkdirs();//Klasorü Olustuur
            Log.d(TAG, "getImageFile: KLASÖR OLUSTURULDU" + directory);
        }
        else {
            Log.d(TAG, "getImageFile: KLASOR ZATEN VAR DEVAM EDİLİYOR ");
        }


        //Dosyayı Olusturuacak
        File imageFile = new File(directory, FullName);
        try {
            OutputStream outputStream = new FileOutputStream(imageFile);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            MediaScannerConnection.scanFile(context, new String[]{imageFile.toString()}, null, null);

            Log.d(TAG, "getImageFile: IMAGE KAYIT EDİLDİ " + imageFile);

            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "getImageFile: " + e.getMessage());
        }

      //  uri = Uri.fromFile(imageFile);
        // return imageFile;
       // Log.d(TAG, "getImageFile: URİİ"+uri);
        return Uri.fromFile(imageFile);


    }


}

