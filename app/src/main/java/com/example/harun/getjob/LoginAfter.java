package com.example.harun.getjob;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.dd.morphingbutton.MorphingButton;

/**
 * Created by harun on 07.03.2018.
 */

public class LoginAfter extends AppCompatActivity {


    SearchView searchView;
    Button advertButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_after_2);

        searchView = (SearchView)findViewById(R.id.searchView1);
        advertButton=(Button)findViewById(R.id.button2);
        searchView.setIconified(false);

        searchView.setQueryHint("İş Ara");

advertButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(v.getContext(),MainActivity.class);
        startActivity(intent);


    }
});

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);

            }
        });




    }
}
