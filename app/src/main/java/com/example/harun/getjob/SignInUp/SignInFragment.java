package com.example.harun.getjob.SignInUp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.harun.getjob.MyCustomToast;
import com.example.harun.getjob.R;
import com.example.harun.getjob.UserIntro;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.altervista.andrearosa.statebutton.StateButton;

/**
 * Created by mayne on 24.07.2018.
 */

public class SignInFragment extends Fragment implements View.OnClickListener {
    private CircularImageView userProfile_image;
    private FloatingActionButton signin_mailBtn, signin_phoneBtn, signin_AnonymouslyBtn;
    private LinearLayout signin_mailLayout, signin_phoneLayout, signin_AnonymouslyLayout;
    private TextInputLayout signin_mailInputLayout, signin_passwordInputLayout;
    public StateButton sign_inBtn;
    private RelativeLayout signinContent;
    private TextView resetPassword;
    private EditText signin_mailtext, signin_passwordText, signin_phoneText;
    private static final String TAG = "SignInFragment";

    public SignInFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.sign_in2, null, false);
        gatherView(v);
        return v;


    }

    private void gatherView(View v) {

        userProfile_image = v.findViewById(R.id.userProfile_image);
        signin_mailBtn = v.findViewById(R.id.signin_mailBtn);
        signin_phoneBtn = v.findViewById(R.id.signin_phoneBtn);
        signin_AnonymouslyBtn = v.findViewById(R.id.signin_AnonymouslyBtn);
        signin_mailLayout = v.findViewById(R.id.signin_mailLayout);
        signin_phoneLayout = v.findViewById(R.id.signin_phoneLayout);
        signin_mailInputLayout = v.findViewById(R.id.signin_mailInputLayout);
        signin_AnonymouslyLayout = v.findViewById(R.id.signin_AnonymouslyLayout);
        signin_passwordInputLayout = v.findViewById(R.id.signin_passwordInputLayout);
        sign_inBtn = v.findViewById(R.id.sign_inBtn);
        signin_mailtext = v.findViewById(R.id.signin_mailtext);
        signin_passwordText = v.findViewById(R.id.signin_passwordText);
        signin_phoneText = v.findViewById(R.id.signin_phoneText);
        signinContent = v.findViewById(R.id.signinContent);
        resetPassword = v.findViewById(R.id.resetPassword);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        signin_mailBtn.setOnClickListener(this);
        signin_phoneBtn.setOnClickListener(this);
        sign_inBtn.setOnClickListener(this);
        signin_phoneText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        resetPassword.setOnClickListener(this);
        signin_AnonymouslyBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.signin_mailBtn:
                loginMethodCheckVisibility(signin_mailLayout, signin_phoneLayout, signin_AnonymouslyLayout);
                signin_mailtext.setText("");
                signin_passwordText.setText("");
                break;
            case R.id.signin_phoneBtn:
                Log.d(TAG, "onClick: ");
                loginMethodCheckVisibility(signin_phoneLayout, signin_mailLayout, signin_AnonymouslyLayout);
                signin_phoneText.setText("");
                break;
            case R.id.signin_AnonymouslyBtn:
                Log.d(TAG, "onClick: ");
                loginMethodCheckVisibility(signin_AnonymouslyLayout, signin_mailLayout, signin_phoneLayout);
                break;
            case R.id.sign_inBtn:
                sign_inBtn.setState(StateButton.BUTTON_STATES.LOADING);

                if (checkLoginInfoInputs()) {

                    if (signin_mailLayout.isShown()) {

                        loginWithMail(signin_mailtext.getText().toString(), signin_passwordText.getText().toString());


                    }
                    if (signin_phoneLayout.isShown()) {


                        //Giriş Yapmadan Öncde Bu Telefon Sistemde Kayıtlımı Diye Kontrol ediyoruz
                        //kayıtlı ise activasyonkodu sayfasına değilse signup sayfasına yönlendircez.
                        NumberIsRegistered(signin_phoneText.getText().toString());


                    }
                    if (signin_AnonymouslyLayout.isShown()) {

                        //Anonim Olarak Giriş Eklenecek Ama Diğer Activityler Düzenlendikten Sonra Şimdi Anonim olarak Giriş
                        //Yapılırsa Uygulama çöker
                        // signInAnonymously();

                    }


                } else {


                    Log.d(TAG, "onClick: ");
                    // MyCustomToast.showCustomToast(getActivity(), "BOŞ ALANLAR VAR GİBİ ");
                    sign_inBtn.setState(StateButton.BUTTON_STATES.FAILED);


                }

                break;


            case R.id.resetPassword:


                break;

        }

    }

    private void signInAnonymously() {

        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


            }
        });


    }

    public void loginWithPhone(PhoneAuthCredential credential) {
        Log.d(TAG, "loginWithPhone: ");

        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Log.d(TAG, "Giriş İŞlemi BAşarılı ");
                    FirebaseUser user = task.getResult().getUser();


                    Log.d(TAG, "Giriş Detayları" + user.getUid());
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), UserIntro.class));
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


                } else {
                    Log.d(TAG, "onComplete: ");
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Snackbar snackbar = Snackbar
                                .make(signinContent, "Invalid OTP ! Please enter correct OTP", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }

                }


            }
        });

    }

    /**
     * Numara Sistemde Kayıtlımı  Kayıtlı İse Giriş Yapabilir Kayıtlı DEğilse  Gerekli Bilgileri
     * Doldurup (Signup) Kayıt olmalı sonrasında  Giriş Yapmalı
     *
     * @param _number
     * @return
     */
    private void NumberIsRegistered(String _number) {


        Query query = FirebaseDatabase.getInstance().getReference().child("users_account")
                .orderByChild("phoneNumber").equalTo(_number);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "NumberIsRegistered DATASNAPSHOT: " + dataSnapshot);
                //Bir DataSnaphot varsa daha önceden kayıdı yapılmıs bir telefon numarasıdır.
                if (dataSnapshot.hasChildren()) {
                    Log.d(TAG, "NumberIsRegistered:onDataChange: " + dataSnapshot.hasChildren());
                    VerificationPhoneFragment verificationPhoneFragment = new VerificationPhoneFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("phone", signin_phoneText.getText().toString());
                    verificationPhoneFragment.setArguments(bundle);
                    verificationPhoneFragment.show(getChildFragmentManager(), "PhoneVerificationSignIn");

                } else {
                    sign_inBtn.setState(StateButton.BUTTON_STATES.FAILED);
                    MyCustomToast.showCustomToast(getActivity()
                            , "Bu Telefon Numarası Üzerinde  Kayıtlı Bir Kullanıcı Bulunamadı Kayıt Olmayı Deneyin ");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void loginWithMail(String mail, String password) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {


                    if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                        sign_inBtn.setState(StateButton.BUTTON_STATES.SUCCESS);

                        eMailVerified();
                        getActivity().finish();
                        Intent loginIntent = new Intent(getActivity(), UserIntro.class);
                        startActivity(loginIntent);
                        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


                    } else {
                        sign_inBtn.setState(StateButton.BUTTON_STATES.FAILED);
                        //YENİDEN KOD GÖNDERİLMESİ İSTENCEK
                        Snackbar.make(signinContent, "Emailiniz Onaylanmamış",
                                Snackbar.LENGTH_LONG).setAction("Yeniden Gönder", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                resendVerificationMail();

                            }
                        }).show();

                    }


                } else {

                    sign_inBtn.setState(StateButton.BUTTON_STATES.FAILED);
                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    notifyErrorMessage(errorCode);


                }


            }
        });


    }

    private void resendVerificationMail() {

        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    MyCustomToast.showCustomToast(getActivity(), " AKTİVASYON KODU YENİDEN GÖNDERİLDİ ");
                    Log.d(TAG, "onComplete: AKTİVASYON KODU YENİDEN GÖNDERİLDİ ");

                } else {

                    MyCustomToast.showCustomToast(getActivity(), "" + task.getException());
                    Log.d(TAG, "onComplete: hATA oLUStu ");

                }

                //  resendBtn.setEnabled(false);
            }
        });


    }

    private void eMailVerified() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("users_account").
                child(user.getUid()).child("verificationStatus").
                setValue(user.isEmailVerified()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.d(TAG, "onSuccess: EMAİL ONAYLAMA İŞLEMİ GERÇEKLEŞTİRİLMİŞ VERİFİCATİON STATUS TRUE ");


            }
        });


    }

    /**
     * Kullanıcı Girişi  Sırasında olusabilecek Hatalar
     * @param errorCode
     */
    private void notifyErrorMessage(String errorCode) {
        switch (errorCode) {

            case "ERROR_INVALID_EMAIL":
                MyCustomToast.showCustomToast(getActivity(), "The email address is badly formatted.");
                signin_mailtext.requestFocus();
                Snackbar.make(signinContent, "Geçersiz Email Adresi", Snackbar.LENGTH_LONG).show();
                break;
            case "ERROR_WRONG_PASSWORD":
                Snackbar.make(signinContent, "Şifre veya Email Geçersiz", Snackbar.LENGTH_LONG).show();

                MyCustomToast.showCustomToast(getActivity(), "The password is invalid or the user does not have a password.");
                signin_passwordText.requestFocus();
                break;
            case "ERROR_USER_DISABLED":
                Snackbar.make(signinContent, "Hesabınız Askıya Alındı", Snackbar.LENGTH_LONG).show();

                MyCustomToast.showCustomToast(getActivity(), "The user account has been disabled by an administrator.");
                break;

            case "ERROR_USER_NOT_FOUND":
                Snackbar.make(signinContent, "Böyle Bir Hesap Bulunamadı.", Snackbar.LENGTH_LONG).show();
                MyCustomToast.showCustomToast(getActivity(), "There is no user record corresponding to this identifier. The user may have been deleted.");
                signin_passwordText.requestFocus();
                break;


        }


    }

    /**
     * Örn: Giriş Yöntemi Telefonsa 1 .parametre telefon layout olacak ve o gösterilecek 2.parametre bu durumda
     * mail layout olacak ve  gizlenecek
     *
     * @param show ->> Gösterilecek layout
     * @param hide ->>Saklanacak layout dizisi
     */
    private void loginMethodCheckVisibility(LinearLayout show, LinearLayout... hide) {
        if (show.isShown()) {
            show.setVisibility(View.GONE);
        } else {
            show.setVisibility(View.VISIBLE);
            for (int i = 0; i < hide.length; i++) {
                hide[i].setVisibility(View.GONE);
            }

        }


    }

    /**
     * Email usülüne uygunmu kontrolünü yapan method .
     *
     * @param email
     */
    public boolean validateEmail(String email) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            signin_mailInputLayout.setError("Geçersiz Mail Adresi ");
            return true;
        } else {

            signin_mailInputLayout.setErrorEnabled(false);
            return false;
        }

    }

    /**
     * Pasword 6 karakterden fazla mı değilmi kontrolünü ypan method .
     *
     * @param password
     */
    public boolean validatePassword(String password) {

        if (password.isEmpty() || password.length() < 6) {
            signin_passwordInputLayout.setError("6 karakterden az olamaz ");
            return true;

        } else {
            signin_passwordInputLayout.setErrorEnabled(false);
            return false;
        }
    }

    /**
     * @param strings
     * @return
     */
    private boolean checkInputisEmpty(String... strings) {
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].isEmpty()) {
                return true;
            }

        }

        return false;
    }


    /**
     * @return
     */
    private boolean checkLoginInfoInputs() {

        //Kullanıcı mail ile kayıt yapıyorsa mail ve pasword bos olamaz ve pasword 6 karakteden azz olamaz
        if (signin_mailLayout.isShown()) {
            return !(validateEmail(signin_mailtext.getText().toString()) || validatePassword(signin_passwordText.getText().toString()));

        }
        if (signin_phoneLayout.isShown()) {

            return !checkInputisEmpty(signin_phoneText.getText().toString());

        }

        MyCustomToast.showCustomToast(getActivity(), "Giriş Yapma Yöntemi Seç ");
        return false;

    }


}
