package com.example.harun.getjob;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.harun.getjob.Profile.profilPage;

/**
 * Created by Harun on 10-Jan-18.
 */

public class UserIntro extends AppCompatActivity {

    // Button profile;
    RelativeLayout profileLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        gatherView();

    }


    public void gatherView() {


        //profile = (Button) findViewById(R.id.profileButon);
profileLayout=findViewById(R.id.profileLayout);
    }

    public void profilPage(View view) {

        Intent intent = new Intent(getApplicationContext(), profilPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }
}
