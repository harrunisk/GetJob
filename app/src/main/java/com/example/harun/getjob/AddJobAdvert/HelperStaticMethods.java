package com.example.harun.getjob.AddJobAdvert;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mayne on 7.06.2018.
 */

public class HelperStaticMethods {
    private static final String TAG = "HelperStaticMethods";

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            Log.d(TAG, "setMargins: ");
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.setLayoutParams(p);
            v.requestLayout();

        }
    }


    /**
     * İlanın yayınlanma tarihi ile şuanki tarih arasındaki gün farkıbı bulan method
     *
     * @return
     */
    public static String getDateDifference(String publishDate) {
        Log.d(TAG, "getDateDifference:");
        String difference = "";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        Date today = c.getTime();
        sdf.format(today);
        Date timestamp;
        try {
            timestamp = sdf.parse(publishDate);
            Log.d(TAG, "getDateDifference: " + timestamp + "\t" + today);
            difference = String.valueOf(Math.round(((today.getTime() - timestamp.getTime()) / 1000 / 60 / 60 / 24)));
            Log.d(TAG, "getDateDifference: " + difference);
        } catch (ParseException e) {
            Log.e(TAG, "getDateDifference: ParseException: " + e.getMessage());
            difference = "0";
        }
        Log.d(TAG, "getDateDifference: " + difference);
        return difference.concat("+ gündür yayinda");
    }

}
