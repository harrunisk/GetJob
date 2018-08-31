package com.example.harun.getjob;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

import custom_font.MyTextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView facebook;
    ImageView twitter;
    ImageView google;
    ImageView mail, phone;
    MyTextView create;

    private static final String TAG = "MainActivityLogin";
    private static final int RC_SIGN_IN = 0;
    private FirebaseAuth auth;

    //Bu ıd bana heryerde lazım olacak oyuzden static yaptım .
    public static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // auth.getCurrentUser().getUid();

        /**
         * Kullanıcı Tanımlanırsa Giriş Yapma İşlemi basarılı ve USerIntro ya Yönlendir
         * Kullanıcı Tanımlanamaz ise Giriş Yapma Ekranına Yönlendir.
         */
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Log.d(TAG, "Kullanici ADİ @@ \t" + auth.getCurrentUser().getEmail());

            userId = auth.getCurrentUser().getUid();
            Log.i(TAG, "onCreate: KULLANICI GİRİŞİ TANIMLANDI ");
            Log.d(TAG, "onCreate: USERID @@\t" + userId);

            Intent intent = new Intent(getApplicationContext(), UserIntro.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


        } else {


            setContentView(R.layout.login);
            facebook = (ImageView) findViewById(R.id.facebook);
            // twitter=(ImageView) findViewById(R.id.twitter);
            google = (ImageView) findViewById(R.id.google);
            //mail=(ImageView) findViewById(R.id.mail);
            phone = (ImageView) findViewById(R.id.phone);
            create = (MyTextView) findViewById(R.id.create);


            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SignUp.class);
                    startActivity(intent);


                }
            });


            facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(

                                    new AuthUI.IdpConfig.FacebookBuilder().build()

                            )).build(), RC_SIGN_IN);

                }
            });

             /*
            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(

                                    new AuthUI.IdpConfig.TwitterBuilder().build()

                            )).build(), RC_SIGN_IN);

                }
            });


            */

            google.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(

                                    new AuthUI.IdpConfig.GoogleBuilder().build()

                            )).build(), RC_SIGN_IN);

                }
            });

           /*
            mail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(

                                    new AuthUI.IdpConfig.EmailBuilder().build()

                            )).build(), RC_SIGN_IN);

                }
            });


            */
            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(

                                    new AuthUI.IdpConfig.PhoneBuilder().build()

                            )).build(), RC_SIGN_IN);

                }
            });


          /*  startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.TwitterBuilder().build(),
                            new AuthUI.IdpConfig.FacebookBuilder().build(),
                            new AuthUI.IdpConfig.PhoneBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build())).build(), RC_SIGN_IN);
            */
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                //user logged in


                Log.d("AUTH", auth.getCurrentUser().getEmail());
                Log.d(TAG, "onActivityResult:" + auth.getCurrentUser().getPhoneNumber());

                userId = auth.getCurrentUser().getUid();

                Intent intent = new Intent(getApplicationContext(), UserIntro.class);
                startActivity(intent);
                finish();
                // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


            } else {
                //User not authenticated
                //Log.d("AUTH","NOT AUTHENTICATED");

            }


        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.log_out_button) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("AUTH", "USER LOGGED OUT!");
                        }
                    });


        }


    }
}
