package com.example.harun.getjob.Profile;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.harun.getjob.profileModel.PhotoModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayne on 26.02.2018.
 */

public class FileSearch {
    public static final String TAG = "FileSearch";


    /**
     * BU METOD İNDEX YOLUNU GÖNDERECEK BURAYA GEÇİLEN PARAMETRE ÖRNEĞİN sdcard İÇERİSİNDEKİ DİZİNLERİ GETİRİ
     * KISACA DİZİNLERİ GETİRECEK.
     *
     * @param directory public String mypath="/storage/sdcard";
     * @return
     */

    public static ArrayList<String> getDirectoryPath(String directory) {
        Log.d(TAG, "getDirectoryPath: " + directory);
        ArrayList<String> pathArray = new ArrayList<>();

        File file = new File(directory);
        File[] fileList = file.listFiles();


        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {

                pathArray.add(fileList[i].getAbsolutePath());

            }


        }


        return pathArray;
    }

    /**
     * BİR DİZİNDEKİ İTEMLERİ BULAN METOD YANİ DCIM İÇERİSİNDEKİ RESİMLER
     *
     * @param directory
     * @return
     */

    public static ArrayList<String> getFilePath(String directory) {
        Log.d(TAG, "getFilePath: " + directory);
        ArrayList<String> filePathArray = new ArrayList<>();

        File file = new File(directory);
        File[] filePath = file.listFiles();
        try {
            for (int i = 0; i < filePath.length; i++) {
                if (filePath[i].isFile()) {
                    filePathArray.add(filePath[i].getAbsolutePath());
                    Log.d(TAG, "getFilePath: FİLE NAME İS " + filePath[i]);
                } else Log.d(TAG, "getFilePath: NOT FİLEEEE " + filePath[i]);
            }
        } catch (Exception e) {
            Log.d(TAG, "getFilePath: ERROOOOOOORRRR" + e.getLocalizedMessage());

        }

        return filePathArray;

    }

    /**
     * BU METOD İYİLEŞTİRİLMELİ ARTIK KUSACAM .
     * @param kontrol Bu parametre true ise spinner dolduruluyor  false ise gridview dolduruluyor
     * @param activity
     * @param spinnerdenGelenItem spinnerdaki tıklanan dosyanın Bilgileri geliyor
     * @return
     */
    public static ArrayList<PhotoModel> getAllShownImagesPath(boolean kontrol, Activity activity, @Nullable String spinnerdenGelenItem) {
        Uri uri;
        Cursor cursor;
        String imageName,imageBucket,bucketId,imagePath;

      //  ArrayList<String> imageDirectory = new ArrayList<>();
       // String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

       // int column_index_data, column_index_folder_name, column_index_folder_name2, column_index_folder_name3;
       // ArrayList<String> listOfAllImages = new ArrayList<String>();
      //  String absolutePathOfImage = null;

        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.DISPLAY_NAME
        };

        //Spinner Listesi için Cursor
        if (kontrol) {

            cursor = activity.getContentResolver().query(
                    uri,
                    projection,
                    null,
                    null,
                    null);
        }
        //GridView imageleri için cursor Burada spinnerdan itemin bucketıd si alınarak sadece o dizine ait resimleri alıyoruz
        else {
            //String photoModel = spinnerdenGelenItem.getBucketId();
            cursor = activity.getContentResolver().query(
                    uri,
                    projection,
                    projection[3] + " =\"" + spinnerdenGelenItem+ "\"",
                    null, null);
        }

//        //Tüm resimlerin full pathleri geliyor

//        //column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//
////Tüm REsimlerin isimleri geldi

//        column_index_folder_name = cursor
//                .getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
//
//        column_index_folder_name3 = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media._ID);
//
//
//        //Her REsim icin bulundugu dizin ismi geliyor

//        column_index_folder_name2 = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        ArrayList<PhotoModel> photos = new ArrayList<>();//Bu listede İmagelerin tüm bilgileri var
        List<String> bucketIds = new ArrayList<>();
        List<String> imagePaths = new ArrayList<>();

        while (cursor.moveToNext()) {

            // absolutePathOfImage =cursor.getString(column_index_folder_name2); // Dizin İsimleri geliyor
            //absolutePathOfImage = ROOT_DIR + "/" + cursor.getString(column_index_folder_name2); // Dizin İsimleri geliyor
             imageName = cursor.getString(cursor.getColumnIndex(projection[4]));
             imageBucket = cursor.getString(cursor.getColumnIndex(projection[2]));
             bucketId = cursor.getString(cursor.getColumnIndex(projection[3]));
             imagePath = cursor.getString(cursor.getColumnIndex(projection[1]));

            final PhotoModel model;

            //BURASI SPİNNER DOLDURMA İÇİN liste Dolduruluyor
            if (kontrol) {
                model = new PhotoModel(bucketId, imageBucket, imagePath, null);
                if (!bucketIds.contains(bucketId)) {
                    photos.add(model);
                    bucketIds.add(bucketId);
                }
            } else {
                model = new PhotoModel(bucketId, imageBucket, imagePath, imageName);
                if (!imagePaths.contains(imagePath)) {
                    photos.add(model);
                    imagePaths.add(imagePath);
                }
            }

        }
        cursor.close();

        return photos;
    }


}
