package com.example.harun.getjob.Profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.harun.getjob.R;

public class profilPage extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton editProfileFab;

    public void init(){
        collapsingToolbarLayout=findViewById(R.id.collapsing_toolbar);
        editProfileFab=findViewById(R.id.editProfileFab);

        // collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.personal_collapsed_title);
        // collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.personal_expanded_title);

        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        // toolbar.setTitle("P R O F İ L E");
        // toolbar.setNavigationIcon(R.drawable.arrow);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profil_frag);
        this.init();


    }

    public void editProfile(View view) {

        Intent intent=new Intent(getApplicationContext(),EditProfile.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);



    }
}
