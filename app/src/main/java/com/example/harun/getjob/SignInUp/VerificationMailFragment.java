package com.example.harun.getjob.SignInUp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harun.getjob.MyCustomToast;
import com.example.harun.getjob.R;
import com.example.harun.getjob.UserIntro;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.altervista.andrearosa.statebutton.StateButton;

/**
 * Created by mayne on 25.07.2018.
 */

public class VerificationMailFragment extends DialogFragment implements DialogInterface.OnKeyListener, View.OnClickListener {
    private static final String TAG = "VerificationMailFragmen";
    private TextView verificationText;
    private Button resendBtn, loginBtn;
    private FirebaseUser user;
    private ImageView cancelVerification;

    public VerificationMailFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.verification_mail, null, false);
        getDialog().setOnKeyListener(this);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        gatherView(v);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");


    }

    private void gatherView(View v) {

        resendBtn = v.findViewById(R.id.resendBtn);
        loginBtn = v.findViewById(R.id.loginBtn);
        verificationText = v.findViewById(R.id.verificationText);
        //cancelVerification = v.findViewById(R.id.cancelVerification);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setLayout(width, height);
        getDialog().getWindow().setWindowAnimations(
                R.style.DialogSlideAnimation);
        user = FirebaseAuth.getInstance().getCurrentUser();
        CharSequence styledText = Html.fromHtml(getString(R.string.verificationText, user.getEmail()));
        verificationText.setText(styledText);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        resendBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        //cancelVerification.setOnClickListener(this);
        verificationText.setOnClickListener(this);

    }

    @Override
    public void setStyle(int style, int theme) {

        super.setStyle(style, theme);
    }

    @Override
    public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "GERİ TUSUNA BASILDI .: ");
        }
        return true;


    }

    /**
     * Mailin onaylanmıs bir mail oldugunu kullanıcı hesap biligilerini tutugum yere bildiriyorum.
     */
    public void eMailVerified() {

        FirebaseDatabase.getInstance().getReference().child("users_account").
                child(user.getUid()).child("verificationStatus").
                setValue(user.isEmailVerified()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.d(TAG, "onSuccess: EMAİL ONAYLAMA İŞLEMİ GERÇEKLEŞTİRİLMİŞ VERİFİCATİON STATUS TRUE ");


            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.resendBtn:

                if (user.isEmailVerified()) {

                    Log.d(TAG, "onClick: EMAİL ONAYLANMIS GOZUKUYOR ");

                    eMailVerified();


                } else {


                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            MyCustomToast.showCustomToast(getActivity(), " AKTİVASYON KODU YENİDEN GÖNDERİLDİ ");
                            Log.d(TAG, "onComplete: AKTİVASYON KODU YENİDEN GÖNDERİLDİ ");
                            resendBtn.setEnabled(false);
                        }
                    });


                }

                break;
            case R.id.loginBtn:

                user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            if (user.isEmailVerified()) {
                                eMailVerified();
                                dismiss();
                                getActivity().finish();
                                Intent loginIntent = new Intent(getActivity(), UserIntro.class);
                                startActivity(loginIntent);
                                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                            } else {


                                Log.d(TAG, "onComplete:RELOAD  " + user.getEmail() + user.isEmailVerified());
                                dismiss();
                                getActivity().finish();
                                Intent loginIntent = new Intent(getActivity(), SignInUpActivity.class);
                                startActivity(loginIntent);
                                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


                            }


                        } else {


                            Log.d(TAG, "onComplete: TASK BASARISIZ");
                        }


                    }
                });


                break;


            case R.id.verificationText:

                Log.d(TAG, "onClick:cancelVerification ");

                //Kullanıcı mail activasyonun halihazırda onaylamıssa geri butonuna basınca bişey olmayacak
                //Eğer onaylamamıs ise kayıt edilen account silinecek .. //Örnegğin kullnaıcı kendisinin olmayan
                // yanlıs bir mail girdi activasyon
                //kodu kendisine gelmedi bu durumda geri gelecek
                //Eğer onaylanmıs ise zaten gerek yok
                if (!user.isEmailVerified()) {

                    FirebaseDatabase.getInstance().getReference().child("users_data").child(user.getUid()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("users_account").child(user.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Log.d(TAG, "onComplete: ACCOUNT VERİLERİ SİLİNDİ ");

                                deletePhotoIfExist();
                            }

                        }
                    });


                } else {

                    dismiss();
                    Log.d(TAG, "O MAİL ONAYLANMIS ");

                }


                break;

        }
    }


    public void deletePhotoIfExist() {
        Log.d(TAG, "deletePhotoIfExist: ");
        if (getArguments() != null && getArguments().getBoolean("photoExist")) {
            FirebaseStorage.getInstance().getReference().child(getString(R.string.users_images)).
                    child(user.getUid()+"/profile_photo").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Log.d(TAG, "YÜKLENEN RESİM SİLİNDİ ...");

                    deleteUser();
                }
            });
        } else {


            deleteUser();

        }


    }

    public void deleteUser() {
        Log.d(TAG, "deleteUser: ");
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: Kullanıcı Siliindi ");
                    if (((SignUpFragment) getParentFragment()) != null) {
                        ((SignUpFragment) getParentFragment()).continueBtn.setState(StateButton.BUTTON_STATES.ENABLED);
                        dismiss();
                        //  FirebaseAuth.getInstance().signOut();
                    }

                }


            }
        });


    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");

        super.onDestroy();
    }
}
