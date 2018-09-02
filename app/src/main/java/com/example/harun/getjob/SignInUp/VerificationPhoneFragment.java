package com.example.harun.getjob.SignInUp;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.harun.getjob.MyCustomToast;
import com.example.harun.getjob.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.altervista.andrearosa.statebutton.StateButton;

import java.util.concurrent.TimeUnit;

/**
 * Created by mayne on 1.08.2018.
 */

public class VerificationPhoneFragment extends DialogFragment implements View.OnClickListener, DialogInterface.OnKeyListener {
    private static final String TAG = "VerificationPhone";
    private Button phoneContinueBtn;
    private EditText phoneVerificationText;
    private String phoneNumber;
    private String verificationID;
    private String code;
    private TextView resendCodeBtn, phoneInfoText, verificationTimeCounter;
    private RelativeLayout parentLayout;
    private ImageView cancelVerification;
    private CountDownTimer countDownTimer;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private long remainingTime;

    public VerificationPhoneFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.verification_phonelayout, null, false);
        getDialog().setOnKeyListener(this);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        phoneContinueBtn = view.findViewById(R.id.phoneContinueBtn);
        phoneVerificationText = view.findViewById(R.id.phoneVerificationText);
        parentLayout = view.findViewById(R.id.parentLayout);
        resendCodeBtn = view.findViewById(R.id.resendCodeBtn);
        phoneInfoText = view.findViewById(R.id.phoneInfoText);
        cancelVerification = view.findViewById(R.id.cancelVerification);
        verificationTimeCounter = view.findViewById(R.id.verificationTimeCounter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setLayout(width, height);

        getDialog().getWindow().setWindowAnimations(
                R.style.DialogSlideAnimation);
        sendCodetoPhoneNumber();

        phoneContinueBtn.setOnClickListener(this);
        resendCodeBtn.setOnClickListener(this);
        cancelVerification.setOnClickListener(this);

    }

    private void sendCodetoPhoneNumber() {

        if (getArguments() != null) {
            phoneNumber = getArguments().getString("phone");
            phoneInfoText.setText(getResources().getString(R.string.verifcationPhoneCode, "+90" + phoneNumber));
            PhoneAuthProvider.getInstance().verifyPhoneNumber("+90" + phoneNumber, 60, TimeUnit.SECONDS,
                    getActivity(), mCallback);
            verificationTimeCounter.setText("60");
            startTimer();


        }


    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken mResendToken) {
        Log.d(TAG, "resendVerificationCode: ");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+90" + phoneNumber, 60, TimeUnit.SECONDS, getActivity(), mCallback, mResendToken);
        verificationTimeCounter.setText("60");
        startTimer();

    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    //Kodu Alıyoruz
                    code = phoneAuthCredential.getSmsCode();
                    Log.d(TAG, "onVerificationCompleted: " + code);
                    if (code != null) {
                        phoneContinueBtn.setActivated(true);
                        phoneVerificationText.setText(code);
                        Log.d(TAG, "onVerificationCompleted: " + code);
                    } else {

                        MyCustomToast.showCustomToast(getActivity(),
                                "Uppps!Kod Gönderilemedi.Neden Bilmiyorum ");

                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Log.d(TAG, "onVerificationFailed: " + e);
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        Snackbar snackbar = Snackbar
                                .make(parentLayout, "Doğrulama Başarısız! Lütfen Size Gönderilen Kodu Kontrol Edin ", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        Snackbar snackbar = Snackbar
                                .make(parentLayout, "Doğrulama Başarısız! Çok Fazla Kod İsteği Gönderdiniz.Daha Sonra Tekrar Deneyin ", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }

                }

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    Log.d(TAG, "onCodeSent: " + s);
                    verificationID = s;
                    mResendToken = forceResendingToken;
                }

                @Override
                public void onCodeAutoRetrievalTimeOut(String s) {
                    super.onCodeAutoRetrievalTimeOut(s);
                    Snackbar snackbar = Snackbar
                            .make(parentLayout, "Süre Doldu! Kodu Girmediniz.Kod Gelmedi İse Yeniden Gönder Butonuna Basabilirsiniz. ", Snackbar.LENGTH_LONG);

                    snackbar.show();
                    Log.d(TAG, "onCodeAutoRetrievalTimeOut: " + s);
                }
            };

    private void startTimer() {
        Log.d(TAG, "startTimer: ");
        resendCodeBtn.setEnabled(false);
        countDownTimer = new CountDownTimer(60000, 1000) {


            @Override
            public void onTick(long l) {
                Log.d(TAG, "onTick: " + l / 1000);
                remainingTime = l / 1000;
                verificationTimeCounter.setText(String.valueOf(l / 1000));


            }

            @Override
            public void onFinish() {
                Log.d(TAG, "onFinish: ");
                verificationTimeCounter.setText("0");
                resendCodeBtn.setEnabled(true);
                checkSmsCodeAndReturn();
            }

        }.start();

    }


    public void checkSmsCodeAndReturn() {

        //DEvam butonuna basınca eger kod onaylanmıs ise
        //SignupFragmente geri dönülecek ve account bilgileri varsa fotograf kayıt edılıp hesap olusturulacak
        if (!phoneVerificationText.getText().toString().isEmpty()) {
            if ((getParentFragment()) != null) {

                try {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
                    if (getTag().equals("PhoneVerificationSignUp")) {

                        ((SignUpFragment) getParentFragment()).continueBtn.setState(StateButton.BUTTON_STATES.LOADING);
                        getDialog().dismiss();
                        ((SignUpFragment) getParentFragment()).verifyVerificationCode(credential);


                    }
                    if (getTag().equals("PhoneVerificationSignIn")) {

                        ((SignInFragment) getParentFragment()).sign_inBtn.setState(StateButton.BUTTON_STATES.LOADING);
                        getDialog().dismiss();
                        ((SignInFragment) getParentFragment()).loginWithPhone(credential);


                    }
                } catch (Exception e) {

                    MyCustomToast.showCustomToast(getActivity(), e.getMessage());


                }


            }
        } else {

            Log.d(TAG, "checkSmsCodeAndReturn: ");
            MyCustomToast.showCustomToast(getActivity(), "Kod Girmediniz Kod Gelmedi İse Yeniden Gönder Butonuna Basabilirsiniz ");

        }


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.phoneContinueBtn:

                Log.d(TAG, "DEVAM BUTONU BASILDI  ");
                checkSmsCodeAndReturn();
                break;


            case R.id.resendCodeBtn:
                //if (remainingTime)
                stopTimer();
                resendVerificationCode(phoneNumber, mResendToken);
                MyCustomToast.showCustomToast(getActivity(), "Yeniden Kod Gönderdik");
                break;
            case R.id.cancelVerification:
                stopTimer();
                if (getTag().equals("PhoneVerificationSignUp")) {
                    ((SignUpFragment) getParentFragment()).continueBtn.setState(StateButton.BUTTON_STATES.FAILED);
                }
                if (getTag().equals("PhoneVerificationSignIn")) {
                    ((SignInFragment) getParentFragment()).sign_inBtn.setState(StateButton.BUTTON_STATES.FAILED);
                }
                getDialog().dismiss();
                break;

        }

    }

    public void stopTimer() {
        if (countDownTimer != null) {
            Log.d(TAG, "countDownTimer != null ");
            countDownTimer.cancel();
        }


    }

    @Override
    public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "GERİ TUSUNA BASILDI .: ");
        }
        return true;
    }
}
