package com.example.harun.getjob;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    TextView signup;
    EditText username;
    EditText pasword;
    Button login;
    FirebaseAuth auth;
    FirebaseDatabase database;
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor;
    private static final int REQUEST_SIGNUP = 0;
    private static int userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.input_email);
        pasword = (EditText) findViewById(R.id.input_password);
        login = (Button) findViewById(R.id.btn_login);
        auth = FirebaseAuth.getInstance();

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit(); //
        pasword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);



pasword.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        final int DRAWABLE_LEFT = 0;
        final int DRAWABLE_TOP = 1;
        final int DRAWABLE_RIGHT = 2;
        final int DRAWABLE_BOTTOM = 3;
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (motionEvent.getRawX() >= (pasword.getRight() - pasword.getCompoundDrawables()[DRAWABLE_RIGHT].
                    getBounds().width())) {

                if(pasword.getInputType()==(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)){

                    pasword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pasword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.unlocked, 0);
                    pasword.setSelection(pasword.getText().length());

                }
                    else {
                    pasword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    pasword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.padlock, 0);
                    pasword.setSelection(pasword.getText().length());
                }


            return true;
            }



        }





        return false;
    }
});

        String mail = preferences.getString("email", "");
        username.setText(mail);
        String sifre = preferences.getString("sifre", "");
        pasword.setText(sifre);


       /* if(preferences.getBoolean("login", false)){
            Intent i = new Intent(getApplicationContext(),MainActivity2.class);
            startActivity(i);
            finish();
        }*/

        signup = (TextView) findViewById(R.id.link_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign_in();


            }
        });


    }

    public void sign_in() {





        final String inputEmail = username.getText().toString().trim();

        final String inputPassword = pasword.getText().toString().trim();






        if (TextUtils.isEmpty(inputEmail)) {
            username.setError("email girin");
            Toast.makeText(getApplicationContext(), "Email Adresinizi girin", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(inputPassword)) {
            pasword.setError("Sifre Girin");
            Toast.makeText(getApplicationContext(), "Şifrenizi Girin", Toast.LENGTH_SHORT).show();
        } else if (inputPassword.length() < 6) {
            Toast.makeText(getApplicationContext(), "Şifreniz 6 karakter olmali!", Toast.LENGTH_SHORT).show();
        } else if(InternetKontrol()) {

            final ProgressDialog progressDialog = new ProgressDialog(Login.this,
                    R.style.My_AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Giris Yapiliyor");
            progressDialog.show();

            auth.signInWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                        if(user.isEmailVerified()){



                            //mail girilen kullanıcını role değerini almak içi
                            DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference();
                            DatabaseReference ref=dbRef.child("Kullanicilar");

                            Query confirmedMail=ref.orderByChild("email").equalTo(inputEmail);
                            confirmedMail.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot singleSnapshot :dataSnapshot.getChildren()){

                                        final UserModel user=singleSnapshot.getValue(UserModel.class);
                                        if (Integer.parseInt(user.getRol())==0) userRole=0;
                                        else userRole=1;

                                        //role 0 means user
                                        if(userRole==0){
                                            Toast.makeText(getApplicationContext(),"Kullanıcı Mail onaylandı1",Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                            editor.putBoolean("login", true);
                                            Intent i = new Intent(Login.this, UserIntro.class);
                                            i.putExtra("username", inputEmail);
                                            finish();
                                            startActivity(i);
                                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);



                                            editor.putString("email", inputEmail);//email değeri
                                            editor.putString("sifre", inputPassword);//şifre değeri
                                            editor.putBoolean("login", true);
                                            editor.apply();

                                            Toast.makeText(getApplicationContext(), "Şirket", Toast.LENGTH_SHORT).show();


                                        }

                                        //role 1 means company
                                        else if(userRole==1){
                                            Toast.makeText(getApplicationContext(),"Şirket Mail onaylandı",Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                            editor.putBoolean("login", true);

                                            Intent i = new Intent(Login.this, MapsActivityCurrentPlace.class);
                                            i.putExtra("username", inputEmail);
                                            finish();
                                            startActivity(i);
                                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);



                                            editor.putString("email", inputEmail);//email değeri
                                            editor.putString("sifre", inputPassword);//şifre değeri
                                            editor.putBoolean("login", true);
                                            editor.apply();

                                            Toast.makeText(getApplicationContext(), "Çalişan", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });









                        }


                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Kullanici Bulunamadi yada Mail Onaylanmamış.", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else Toast.makeText(getApplicationContext(),"INTERNET BAĞLI DEĞİL ", Toast.LENGTH_LONG).show();


    }

    public boolean InternetKontrol() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isAvailable() && manager.getActiveNetworkInfo().isConnected();
    }
}


