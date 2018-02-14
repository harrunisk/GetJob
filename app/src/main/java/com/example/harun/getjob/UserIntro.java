package com.example.harun.getjob;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.harun.getjob.Profile.profilPage;

/**
 * Created by Harun on 10-Jan-18.
 */

public class UserIntro extends AppCompatActivity{

    Button profile;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        gatherView();

    }



    public void gatherView(){


        profile=(Button)findViewById(R.id.profileButon);

    }

    public void profilPage(View view) {

        Intent intent=new Intent(getApplicationContext(),profilPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }
}
