package com.example.harun.getjob;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by mayne on 18.05.2018.
 */

public class MyCustomToast extends Toast {


    public MyCustomToast(Context context) {
        super(context);
    }

    public static void showCustomToast(Context context, String message) {


       // LayoutInflater layoutInflater = ((AppCompatActivity) context).getLayoutInflater();
        View layout = LayoutInflater.from(context).inflate(R.layout.custom_toast,null);
        TextView msgTv = layout.findViewById(R.id.toastText);
        msgTv.setText(message);
        Toast toast=new Toast(context);
        toast.setGravity(Gravity.CENTER|Gravity.BOTTOM,0,100);
        toast.setDuration(LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }
}
