package com.example.harun.getjob;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    TextView login;
    EditText name,username,email,pasword,rePasword,tel;
    RadioButton roleButton,kullanici,sirket;
    RadioGroup roleGroup;


    Button signup;
    TextView kayittv;
    FirebaseAuth auth;
    FirebaseDatabase database;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
       /*
            Map<String, String> yeniUser = new HashMap<String, String>();
            yeniUser.put("ad", "Adınız");
            yeniUser.put("soyad", "Soyadınız");

            final DatabaseReference databaseReference = firebaseDatabase.getReference();
            databaseReference.child("users")
                    .child(firebaseAuth.getCurrentUser().getUid())
                    .setValue(yeniUser);
*/
        kayittv=(TextView)findViewById(R.id.kayittv);
       /* Typeface face=Typeface.createFromAsset(getAssets(),"fonts/Athletic.ttf");
        kayittv.setTypeface(face);*/

        login = (TextView) findViewById(R.id.link_login);
        name = (EditText) findViewById(R.id.input_name);
        username = (EditText) findViewById(R.id.input_username);
        email = (EditText) findViewById(R.id.input_email);
        // tel = (EditText) findViewById(R.id.input_mobile);
        pasword = (EditText) findViewById(R.id.input_password);
        rePasword = (EditText) findViewById(R.id.input_reEnterPassword);
        signup = (Button) findViewById(R.id.btn_signup);

        roleGroup=(RadioGroup) findViewById(R.id.radioGroup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });


    }

    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.My_AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String get_password = pasword.getText().toString().trim();
        final String get_username = username.getText().toString().trim();
        final String get_email2 = email.getText().toString().trim();
        final String get_name = name.getText().toString();
        //radio button value
        int selectedId=roleGroup.getCheckedRadioButtonId();
        roleButton=(RadioButton)findViewById(selectedId);
        final String get_role=roleButton.getHint().toString();

        Toast.makeText(getApplicationContext(),get_role,Toast.LENGTH_LONG).show();

        //  final String get_mobile = tel.getText().toString();

        ////***DATABASE KAYIT İSLEMİ*******/////////
        DatabaseReference dbref = database.getReference("Kullanicilar");
        String key = dbref.push().getKey();

        DatabaseReference dbrefkey = database.getReference("Kullanicilar/" + key);
        dbrefkey.setValue(new UserModel(get_email2, get_name, get_password, get_username,get_role));

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences nesnesi oluşturuluyor ve prefernces referansına bağlanıyor
        editor = preferences.edit(); //aynı şekil editor nesnesi oluşturuluyor


        ////////***************EMAİL KAYIT *****/////////////
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(get_email2, get_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                editor.putString("email", get_email2);//email değeri
                editor.putString("sifre", get_password);//şifre değeri
                editor.putBoolean("login", true);
                editor.apply();


                if (task.isSuccessful()) {
                    onSignupSuccess();
                    progressDialog.dismiss();
                    Intent intent = new Intent(SignupActivity.this, Login.class);
                    intent.putExtra("username", get_username);
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    user.sendEmailVerification();
                    Toast.makeText(getApplicationContext(),"Onay maili gönderildi",Toast.LENGTH_LONG).show();
                    startActivity(intent);
                    finish();


                } else {
                    onSignupFailed();
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        });

    }

    //////***********Doğru Giris Kontrolleri
    public boolean validate() {
        boolean valid = true;

        String get_name = name.getText().toString();
        String get_email = email.getText().toString().trim();
        //String get_mobile = tel.getText().toString();
        String get_password = pasword.getText().toString();
        String get_rePassword = rePasword.getText().toString();
        String get_username = username.getText().toString();
        //radio value
        int selectedId=roleGroup.getCheckedRadioButtonId();
        kullanici=(RadioButton)findViewById(R.id.kullanici);
        sirket=(RadioButton)findViewById(R.id.sirket);
        if(selectedId==-1)
        {

            kullanici.setError("Kullanıcı tipi seçiniz");
            sirket.setError("Kullanıcı tipi seçiniz");
            valid=false;
        }
        else{

            kullanici.setError(null);
            sirket.setError(null);
            }





        if (get_username.isEmpty()) {
            username.setError("Kullanıcı Adı Boş Bırakılamaz");
            valid = false;
        } else {
            username.setError(null);
        }

        if (get_name.isEmpty() || get_name.length() < 3) {
            name.setError("Ad Soyad 3 Karakterden Az olamaz");
            valid = false;
        } else {
            name.setError(null);
        }


        if (get_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(get_email).matches()) {
            email.setError("Geçerli Mail Adresi Giriniz");
            valid = false;
        } else {
            email.setError(null);
        }

       /* if (get_mobile.isEmpty() || get_mobile.length()!=10) {
            tel.setError("Telefon Numarası Giriniz");
            Toast.makeText(getApplicationContext(), "Telefon numarası 10 Haneli Olmalı", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            tel.setError(null);
        }*/

        if (get_password.isEmpty() || get_password.length() < 6 || get_password.length() > 10) {
            pasword.setError("Parola En az 6 karakter olmalı");
            valid = false;
        } else {
            pasword.setError(null);
        }

        if (get_rePassword.isEmpty() || get_rePassword.length() < 6 || get_rePassword.length() > 10 || !(get_rePassword.equals(get_password))) {
            rePasword.setError("Parolalar Eşleşmiyor");
            valid = false;
        } else {
            rePasword.setError(null);
        }



        return valid;
    }


    public void onSignupSuccess() { // basarılı giris
        signup.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() { // basarısız giris
        Toast.makeText(getBaseContext(), "Kayıt Yapilamadi", Toast.LENGTH_LONG).show();

        signup.setEnabled(true);
    }


}
