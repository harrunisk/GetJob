package com.example.harun.getjob.Profile;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.harun.getjob.R;
import com.tapadoo.alerter.Alerter;


//Kullanılacak Bütün izinleri bu sınıftan alınacak
public class Permissions {
    public static final String TAG = "Permission";

    public static final String[] PERMISSIONS = {

            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    public static final String[] CAMERA_PERMISSION = {
            Manifest.permission.CAMERA
    };

    public static final String[] WRITE_STORAGE_PERMISSION = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final String[] READ_STORAGE_PERMISSION = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };


    ////HER SINIFTAN KULLANABİLMEK icin burada izin sınfı kullanıyorum . static olarak erişim saglanacak

    /**
     * İzinler İstenecek
     *
     * @param permission
     */
    public static boolean myrequestPermission(Activity activity, String[] permission) {

        Log.d(TAG, "requestPermission:İzinler İsteniyor ");
        ActivityCompat.requestPermissions(activity, permission, 1);

        return true;
    }


    /**
     * Permission dizisindeki her eleamnı checkPermission metoduna gönderecek bir nevi diziyi ayrıştırıp tek tek
     * kontrole gönderme
     *
     * @param permission
     * @return true false
     */
    public static boolean checkPermissionArray(Activity activity, String[] permission) {
        Log.d(TAG, "checkPermissionArray: ");
        for (int i = 0; i < permission.length; i++) {
            String check = permission[i];

            //İzinlerden 1 Tanesi bile verilmemiş ise false döner
            if (!checkPermision(activity, check)) {
                Log.d(TAG, "checkPermissionArray: İzin verilmemiş" + check);
                return false;
            }

        }
        return true;

    }


    public static Alerter showAlertdilaog(Activity activity, String permission, String aciklama, long duration) {

        Log.d(TAG, "showAlertdilaog: CREATED");
        Alerter a;
        a = Alerter.create(activity).setTitle(permission).setText(aciklama)
                .setIcon(R.drawable.ic_notifications_active_black_24dp)
                .enableSwipeToDismiss()
                .enableProgress(true).
                        setDuration(duration).
//                enableInfiniteDuration(true).
                //    disableOutsideTouch().
                        setProgressColorRes(R.color.Tomato).
                        setBackgroundColorRes(R.color.SlateBlue);


        return a;
    }


    /**
     * Her izin için Tek tek kontrol yapacak izin daha önceden verilmişmi verilmemişmi  ve true false dönecek
     *
     * @param permission
     * @return
     */
    public static boolean checkPermision(Activity activity, String permission) {

        Log.d(TAG, "checkPermision: İzinler kontrol ediliyor ");

        int permissionRequest = ActivityCompat.checkSelfPermission(activity, permission);
        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            // Toast.makeText(activity, "İzin Verilmemiş" + permission, Toast.LENGTH_SHORT).show();
            /*if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {

                Log.d(TAG, "checkPermision:shouldShowRequestPermissionRationale " + permission);
                showAlertdilaog(activity, permission);
                return false;
            }*/
            /*else{

                myrequestPermission(activity,PERMISSIONS);

                return true;
            }*/

            return false;

        } else {
            Log.d(TAG, "checkPermision: izin verilmiş " + permission);
            return true;
        }

    }


}