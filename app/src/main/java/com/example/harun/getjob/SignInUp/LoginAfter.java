package com.example.harun.getjob.SignInUp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.harun.getjob.R;


/**
 * Created by harun on 07.03.2018.
 */

public class LoginAfter extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginAfter";
    TextView isAriyorumTv, elemanAriyorumTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_login2);

        gatherView();


    }

    private void gatherView() {

        isAriyorumTv = findViewById(R.id.isAriyorumTv);
        elemanAriyorumTv = findViewById(R.id.elemanAriyorumTv);
        init();

    }

    private void init() {
    
         isAriyorumTv.setOnClickListener(this);
    
    }

    @Override
    public void onClick(View view) {
        
        switch (view.getId()){
            
            case R.id.isAriyorumTv:


                Log.d(TAG, "onClick: ");
                Intent intent = new Intent(getApplicationContext(), SignInUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                break;
            
            
        }
        
        
        
    }
}
