package com.example.harun.getjob.SignInUp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.harun.getjob.DeviceLocation;
import com.example.harun.getjob.FirebaseMethods.FirebaseMethods;
import com.example.harun.getjob.FirebaseMethods.MainContent;
import com.example.harun.getjob.MyCustomToast;
import com.example.harun.getjob.Profile.Permissions;
import com.example.harun.getjob.Profile.PhotoActivity;
import com.example.harun.getjob.Profile.UniversalImageLoader;
import com.example.harun.getjob.R;
import com.example.harun.getjob.UserAccountSettings;
import com.example.harun.getjob.UserIntro;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.wang.avi.AVLoadingIndicatorView;

import org.altervista.andrearosa.statebutton.StateButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by mayne on 24.07.2018.
 */

public class SignUpFragment extends Fragment implements View.OnClickListener, DeviceLocation.DeviceLocationCallback, FirebaseMethods.UploadPhotoCallback {
    private static final String TAG = "SignUpFragment";
    private FloatingActionButton mailBtn, phoneBtn;
    private LinearLayout mailLayout, phoneLayout;
    private EditText signUpNameText, signUpJobText, signUpLocationText, mailtext, passwordText, phoneText, repeatPasswordText;
    public StateButton continueBtn;
    private MainContent mainContent;
    private FirebaseAuth mAuth;
    private ImageView signUpgetLocation, changePhotoImage;
    private TextInputLayout signUpNameLayout, signUpLocationLayout, signUpJobLayout, mailInputLayout, passwordInputLayout, repeatPasswordInputLayout;
    private HashMap<View, View> inputViewHashMap;
    private boolean mPermissionGranted;
    private Uri newPhotoUri;
    private CircularImageView userProfile_image;
    private String newPhotoUrl;
    private NestedScrollView signUpNestedScroll;
    private AVLoadingIndicatorView locationProgress;
    private String registerMethod; //true Mail false Phone

    public SignUpFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sign_up2, null, false);
        gatherView(v);
        return v;
    }

    private void gatherView(View v) {
        Log.d(TAG, "gatherView: ");
        mailBtn = v.findViewById(R.id.mailBtn);
        phoneBtn = v.findViewById(R.id.phoneBtn);
        mailLayout = v.findViewById(R.id.mailLayout);
        phoneLayout = v.findViewById(R.id.phoneLayout);
        signUpNameText = v.findViewById(R.id.signUpNameText);
        signUpJobText = v.findViewById(R.id.signUpJobText);
        signUpLocationText = v.findViewById(R.id.signUpLocationText);
        passwordText = v.findViewById(R.id.passwordText);
        phoneText = v.findViewById(R.id.phoneText);
        mailtext = v.findViewById(R.id.mailtext);
        continueBtn = v.findViewById(R.id.continueBtn);
        signUpNameLayout = v.findViewById(R.id.signUpNameLayout);
        signUpJobLayout = v.findViewById(R.id.signUpJobLayout);
        signUpLocationLayout = v.findViewById(R.id.signUpLocationLayout);
        mailInputLayout = v.findViewById(R.id.mailInputLayout);
        passwordInputLayout = v.findViewById(R.id.passwordInputLayout);
        signUpgetLocation = v.findViewById(R.id.signUpgetLocation);
        changePhotoImage = v.findViewById(R.id.changePhotoImage);
        userProfile_image = v.findViewById(R.id.userProfile_image);
        signUpNestedScroll = v.findViewById(R.id.signUpNestedScroll);
        repeatPasswordText = v.findViewById(R.id.repeatPasswordText);
        repeatPasswordInputLayout = v.findViewById(R.id.repeatPasswordInputLayout);
        locationProgress = v.findViewById(R.id.locationProgress);

    }

    private void init() {
        Log.d(TAG, "onClick: ");

        mailBtn.setOnClickListener(this);
        phoneBtn.setOnClickListener(this);
        continueBtn.setOnClickListener(this);
        signUpgetLocation.setOnClickListener(this);
        changePhotoImage.setOnClickListener(this);
        //   passwordText.addTextChangedListener(new PasswordTransformationMethod());
        phoneText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        inputViewHashMap = new HashMap<>();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        inputViewHashMap.put(signUpNameText, signUpNameLayout);
        inputViewHashMap.put(signUpJobText, signUpJobLayout);
        inputViewHashMap.put(signUpLocationText, signUpLocationLayout);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.mailBtn:
                Log.d(TAG, "onClick: ");
                registerMethod = "Mail";
                mailtext.setText("");
                passwordText.setText("");
                loginMethodCheckVisibility(mailLayout, phoneLayout);
                signUpNestedScroll.post(new Runnable() {
                    @Override
                    public void run() {
                        signUpNestedScroll.fullScroll(View.FOCUS_DOWN);
                    }
                });
                break;
            case R.id.phoneBtn:
                Log.d(TAG, "onClick: ");
                registerMethod = "Phone";
                phoneText.setText("");
                loginMethodCheckVisibility(phoneLayout, mailLayout);
                break;

            case R.id.continueBtn:
                Log.d(TAG, "Continue BTN CLİCK EVENT : ");
                //Tüm alanların dolulugunu kontrol et

                //Önce Genel Bilgilerin Kontrolu
                if (checkInputs(signUpNameText.getText().toString(),
                        signUpJobText.getText().toString(),
                        signUpLocationText.getText().toString()) && checkRegisterInfoInputs()) {

                    //Activasyon Süreci ve Kayıt İşlemine geçiş
                    if (mailLayout.isShown()) {
                        Log.d(TAG, "Continue Btn Mail Layout  : ");
                        registerUserWithEmail();
                        continueBtn.setState(StateButton.BUTTON_STATES.LOADING);


                    } else if (phoneLayout.isShown()) {

                        Log.d(TAG, "Continue Btn PhoneLayout ");
                        // sendVerificatonPhoneNumber();
                        //  continueBtn.setState(StateButton.BUTTON_STATES.LOADING);
                        //Telefon Numarası
                        //Telefon Numarasını AL ve
                        //  registerUserWithPhoneNumber();
                        if (validatePhoneNumber(phoneText.getText().toString())) {
                            continueBtn.setState(StateButton.BUTTON_STATES.DISABLED);
                            checkNumberAlreadyExist(phoneText.getText().toString());
                        }


                    } else {
                        MyCustomToast.showCustomToast(getActivity(), "Kayıt Olma Yöntemi Seçmen Lazım ");

                    }


                } else {

                    Log.d(TAG, "onClick: BOS ALANLAR VAR .");
                    MyCustomToast.showCustomToast(getActivity(), "Boş Alanlar var ");


                }

                break;

            case R.id.signUpgetLocation:
                if (!signUpgetLocation.isActivated()) {
                    //signUpgetLocation.setVisibility(View.GONE);
                    locationProgress.setVisibility(View.VISIBLE);
                    getLocation();

                }


                break;

            case R.id.changePhotoImage:

                Intent intent = new Intent(getActivity(), PhotoActivity.class);
                startActivityForResult(intent, 2);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }


    }


    public boolean validatePhoneNumber(String mPhoneNumber) {
        //  String phoneNumber = mPhoneNumberField.getText().toString();
        //   Log.d(TAG, "validatePhoneNumber: " + mPhoneNumber);
        //   Log.d(TAG, "validatePhoneNumber: " + mPhoneNumber.length());
        //  Log.d(TAG, "validatePhoneNumber: " + mPhoneNumber.trim());
        if (mPhoneNumber.isEmpty() || mPhoneNumber.length() != 12) {
            MyCustomToast.showCustomToast(getActivity(), "Geçersiz Telefon Numarası");
            return false;
        }

        return true;
    }

    private void checkNumberAlreadyExist(String _number) {
        Log.d(TAG, "checkNumberAlreadyExist: " + _number);
        Query query = FirebaseDatabase.getInstance().getReference().child("users_account")
                .orderByChild("phoneNumber").equalTo(_number);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "checkNumberAlreadyExist DATASNAPSHOT: " + dataSnapshot);
                //Bir DataSnaphot varsa daha önceden kayıdı yapılmıs bir telefon numarasıdır.
                if (dataSnapshot.hasChildren()) {
                    continueBtn.setState(StateButton.BUTTON_STATES.FAILED);

                    MyCustomToast.showCustomToast(getActivity()
                            , "Bu Telefon Numarası Üzerinde Daha Önceden Bir Kayıt Olusturulmus.Giriş Yapmayı Deneyin");
                } else {
                    VerificationPhoneFragment verificationPhoneFragment = new VerificationPhoneFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("phone", phoneText.getText().toString());
                    verificationPhoneFragment.setArguments(bundle);
                    verificationPhoneFragment.show(getChildFragmentManager(), "PhoneVerificationSignUp");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case 2:
                if (resultCode == RESULT_OK) {
                    if (data.hasExtra("imageFromCam")) {
                        newPhotoUri = data.getParcelableExtra("imageFromCam");
                        Log.d(TAG, "initImageLoader: IMAGEFROMCAM" + newPhotoUri);
                        UniversalImageLoader.setImage(newPhotoUri.toString(), userProfile_image, null, "");
                    } else if (data.hasExtra("newUserImagefromGalery")) {

                        newPhotoUrl = data.getStringExtra("newUserImagefromGalery");
                        newPhotoUri = Uri.fromFile(new File(newPhotoUrl));//Burada fotografın dosya yolunuda alıyorum
                        Log.d(TAG, "initImageLoader: newUserImagefromGalery" + "\t" + newPhotoUrl);
                        UniversalImageLoader.setImage(newPhotoUrl, userProfile_image, null, "file:/");
                    } else if (data.hasExtra("newUserImagefromGaleryUri")) {

                        newPhotoUri = data.getParcelableExtra("newUserImagefromGaleryUri");
                        Log.d(TAG, "initImageLoader: newUserImagefromGaleryUri" + newPhotoUri);
                        UniversalImageLoader.setImage(newPhotoUri.toString(), userProfile_image, null, "");
                    }


                }

                break;


        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Log.d(TAG, "onRequestPermissionsResult: ONREQUESTPERMİSSİONRESULT");
        switch (requestCode) {

            case 1:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(TAG, "onRequestPermissionsResult: İzin VERİLMİŞ " + permissions[0]);
                    mPermissionGranted = true;
                    getLocation();
                } else {
                    mPermissionGranted = false;
                    Log.d(TAG, "onRequestPermissionsResult: İzin Verilmemiş ");
                    signUpgetLocation.setEnabled(false);

                }

                break;

        }


    }

    @Override
    public void deviceLocationCallback(LatLng gps) {
        Log.d(TAG, "deviceLocationCallback: " + gps);
        if (Geocoder.isPresent() && gps != null) {

            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            try {
                List<Address> coordinats = geocoder.getFromLocation(gps.latitude, gps.longitude, 1);
                if (coordinats.size() > 0) {

                    String cityName = coordinats.get(0).getAdminArea();
                    String subAreaName = coordinats.get(0).getSubAdminArea();
                    Log.d(TAG, "deviceLocationCallback: " + coordinats.get(0));
                    signUpLocationText.setText(cityName.concat("/" + subAreaName));
                    locationProgress.smoothToHide();
                  //  signUpgetLocation.setVisibility(View.VISIBLE);
                    signUpgetLocation.setActivated(true);


                }


            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "deviceLocationCallback: " + e);
            }

        } else {
            MyCustomToast.showCustomToast(getActivity(), "Konum Bilgisi Alınırken Hata Olustu Tekrar Deneyin ");
           // signUpgetLocation.setVisibility(View.VISIBLE);
            signUpgetLocation.setActivated(false);


        }
    }

    private void getLocation() {

        if (Permissions.checkPermissionArray(getActivity(), Permissions.MapPermission)) {
            //İZİNLER VERİLMİŞ İSE DEVİCE LOCATİON CAGIR
            //LAT LNG BILGISINI AL VE ADRES BİLGİSİNDN SEHİR BİLGİSİNE ULAS
            Log.d(TAG, "getLocation: İzİNLER VERİLMİŞ ");
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                new DeviceLocation(getActivity(), this).getDeviceLocation();
            } else {

                MyCustomToast.showCustomToast(getActivity(), "Cihazının Konum Bilgisinin Açık oldugundan Emin Olun ");

            }

        } else {
            //İZİNLERİ İSTE
            Log.d(TAG, "getLocation: İZİNLER VERİLMEMİŞ İZİNLER İSTENECEK ");
            requestPermissions(Permissions.MapPermission, 1);

        }


    }

    private void loginMethodCheckVisibility(LinearLayout show, LinearLayout hide) {
        if (show.isShown()) {
            show.setVisibility(View.GONE);
        } else {
            show.setVisibility(View.VISIBLE);
            hide.setVisibility(View.GONE);

        }


    }

    public void verifyVerificationCode(PhoneAuthCredential credential) {
        Log.d(TAG, "verifyVerificationCode: " + credential.toString());
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        Log.d(TAG, "signInWithPhoneAuthCredential: ");
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Log.d(TAG, "SignInPhoneOnCompleteListener");
                    final String uid = task.getResult().getUser().getUid();
                    final UserAccountSettings userAccountSettings =
                            new UserAccountSettings(
                                    "",
                                    uid,
                                    "",
                                    phoneText.getText().toString(),
                                    new SimpleDateFormat("dd.MM.yyyy HH:mm",
                                            Locale.getDefault()).format(Calendar.getInstance().getTime()),
                                    true);
                    FirebaseDatabase.getInstance().getReference().child("users_account").child(uid).
                            setValue(userAccountSettings).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "KullanıcıUserAccountSettings  Eklendi");
                                createUserMainContent(uid);

                            }

                        }
                    });

                } else {
                    continueBtn.setState(StateButton.BUTTON_STATES.FAILED);
                    Log.d(TAG, "signInWithPhoneAuthCredential: TASK BASARISIZ OLDU ");
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
//                        Snackbar snackbar = Snackbar
//                                .make((CoordinatorLayout) findViewById(R.id.parentlayout), "Invalid OTP ! Please enter correct OTP", Snackbar.LENGTH_LONG);
//
//                        snackbar.show();
                        MyCustomToast.showCustomToast(getActivity(), task.getException().getMessage());
                    }

                }


            }
        });


    }


    private void registerUserWithEmail() {
        Log.d(TAG, "registerUserWithEmail: ");
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(mailtext.getText().toString(),
                passwordText.getText().toString()).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Log.d(TAG, "onComplete: Kayıt İşlemi BAşarılı ID-->>> " + mAuth.getUid());
                            sendVerificationEmail(mAuth.getUid());


                        } else {
                            continueBtn.setState(StateButton.BUTTON_STATES.FAILED);
                            /**

                             * Hata kodları dzenlenecek
                             */

                            try {
                                throw task.getException();
                            }
                            // Zayıf Şifre
                            catch (FirebaseAuthWeakPasswordException weakPassword) {
                                Log.d(TAG, "onComplete: weak_password");
                                MyCustomToast.showCustomToast(getActivity(), "ŞİFRE HATASI" + weakPassword);
                            }
                            // Yanlış mail Formatı
                            catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                Log.d(TAG, "onComplete: malformed_email");
                                MyCustomToast.showCustomToast(getActivity(), "GEÇERSİZ MAİL" + malformedEmail);
                                Log.d(TAG, "onComplete: KAyıt İşlemi Basasrızı" + task.getException());
                            }
                            //MAİL ZATEN MEVCUT
                            catch (FirebaseAuthUserCollisionException existEmail) {
                                Log.d(TAG, "onComplete: exist_email");
                                MyCustomToast.showCustomToast(getActivity(), "BU MAİL DAHA ÖNCEDEN ALINMIS ");
                            } catch (Exception e) {
                                Log.d(TAG, "onComplete: " + e.getMessage());
                                MyCustomToast.showCustomToast(getActivity(), "" + e);

                            }


                        }


                    }
                });


    }

    @Override
    public void onResume() {

        Log.d(TAG, "onResume: ");
        continueBtn.setState(StateButton.BUTTON_STATES.ENABLED);

        super.onResume();
    }

    private void sendVerificationEmail(final String uid) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && !user.isEmailVerified()) {
            final UserAccountSettings userAccountSettings =
                    new UserAccountSettings(
                            mailtext.getText().toString(),
                            uid,
                            passwordText.getText().toString(),
                            "",
                            new SimpleDateFormat("dd.MM.YYYY HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime()),
                            user.isEmailVerified());
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email Activasyon Kodu Gönderildi" + user.getEmail());
                        FirebaseDatabase.getInstance().getReference().child("users_account").child(uid).
                                setValue(userAccountSettings).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "KullanıcıUserAccountSettings  Eklendi");

                                    createUserMainContent(mAuth.getUid());

                                }

                            }
                        });
                    } else {
                        continueBtn.setState(StateButton.BUTTON_STATES.FAILED);
                        Log.d(TAG, "Email ACtivasyon Kodu Gönderilirken Hata Olustu" + task.getException());
                        MyCustomToast.showCustomToast(getActivity(), "Email Activasyon Kodu Gönderilirken Hata Olustu.");

                    }


                }
            });


        }


    }


    @Override
    public void uploadPhotoListener(boolean isSuccess) {
        Log.d(TAG, "uploadPhotoListener:YÜKLEME DURUMU -->> " + isSuccess);
        if (registerMethod.equals("Mail")) {
            continueBtn.setState(StateButton.BUTTON_STATES.DISABLED);
            VerificationMailFragment verificationMailFragment = new VerificationMailFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("photoExist", isSuccess);
            verificationMailFragment.setArguments(bundle);
            verificationMailFragment.show(getChildFragmentManager(), "Verification");
            Log.d(TAG, "uploadProfilePhoto: PHOTO EKLENMEMİŞ-->MAilFragment ");

        } else {
            Log.d(TAG, "uploadProfilePhoto: Foto Eklenmemiş -->UserIntro ");

            continueBtn.setState(StateButton.BUTTON_STATES.DISABLED);
            getActivity().finish();
            startActivity(new Intent(getActivity(), UserIntro.class));
            getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

        }
    }


    /**
     * Kullanıcı Fotograf Eklemiş ise önce Fotograf yüklenecek ardından mail onaylama sayfası acılacak
     * Eklenmemiş ise direk mail onaylama sayfası
     */
    public void uploadProfilePhoto() {
        if (newPhotoUri != null) {
            //Kulllanıcı Fotograf Eklemiş İse Fotografı Yükledikten Sonra VerificationMailFragment acılacak
            //Kullanıcı Fotograf Yüklemeişse newPhotoUri null olur bu durumda else tarafı calısır
            FirebaseMethods firebaseMethods = new FirebaseMethods(getActivity());
            firebaseMethods.uploadProfileImages(newPhotoUri, SignUpFragment.this);
        } else {
            //Fotograf Eklenmemiş ise Kayıt Yöntemi Mail se MailFragmentte Yönlendirilerek Aktivayon Maili Gönderilecek
            //Kayıt Yöntemi Telefon İse Aktivasyon Kodu  Onaylandıktan Sonra Buraya  Geldiği İçin Direk UserIntro
            if (registerMethod.equals("Mail")) {
                VerificationMailFragment verificationMailFragment = new VerificationMailFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("photoExist", false);
                verificationMailFragment.setArguments(bundle);
                verificationMailFragment.show(getChildFragmentManager(), "Verification");
                Log.d(TAG, "uploadProfilePhoto: PHOTO EKLENMEMİŞ-->MAilFragment ");

            } else {
                Log.d(TAG, "uploadProfilePhoto: Foto Eklenmemiş -->UserIntro ");
                continueBtn.setState(StateButton.BUTTON_STATES.SUCCESS);
                getActivity().finish();
                startActivity(new Intent(getActivity(), UserIntro.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


            }

        }
    }

    /**
     * Kullanıcının Adı syadı Mesleke ve Lokasyon Bilgisi Kayıt Ediliyor
     * Bu kayıt basarılı olursa Profil Fotografı varsa O yüklenecek
     *
     * @param uid-->User ID
     */
    private void createUserMainContent(String uid) {
        FirebaseDatabase.getInstance().getReference().
                child("users_data").
                child(uid).
                child("profile_data")
                .child("main_content").
                setValue(mainContent).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Log.d(TAG, "Kullanıcı Ana bilgileri Eklendi ");
                    uploadProfilePhoto();
                }

            }
        });


    }

    /**
     * Giriş Methodlarının kontrolü
     *
     * @return
     */
    private boolean checkRegisterInfoInputs() {

        //Kullanıcı mail ile kayıt yapıyorsa mail ve pasword bos olamaz ve pasword 6 karakteden azz olamaz
        //validateEmail veya validatePasword durumlarından herahngi birinde
        // bir hata varsa  true olarak gelir buradan ise false olarak döner hata var anlamında
        if (mailLayout.isShown()) {


            return !(validateEmail(mailtext.getText().toString()) || validatePassword(passwordText.getText().toString(), repeatPasswordText.getText().toString()));
        }

        if (phoneLayout.isShown()) {

            if (checkInputisEmpty(phoneText.getText().toString())) {
                MyCustomToast.showCustomToast(getActivity(), "Tüm Alanları Doldur");
                return false;
            }


        }
        return true;


    }

    /**
     * Email usülüne uygunmu kontrolünü yapan method .
     *
     * @param email true-->hata var
     *              false-->hata yok
     */
    public boolean validateEmail(String email) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            mailInputLayout.setError("Geçersiz Mail Adresi ");
            return true;
        } else {

            mailInputLayout.setErrorEnabled(false);
            return false;
        }

    }

    /**
     * Pasword 6 karakterden fazla mı değilmi ayrıca sifre tekrarı ile uyusup uyusmadıgının kontrolünü yapan method .
     *
     * @param password-->sifre
     * @param repeatPassword-->sifreTekrarı true-->hata var
     *                                      false-->hata yok
     */
    public boolean validatePassword(String password, String repeatPassword) {

        if (password.isEmpty() || password.length() < 6) {
            passwordInputLayout.setError("6 karakterden az olamaz ");
            return true;

        } else {
            //password boş değilse ve 6 karaktreden fazla ise
            //repeatPasword ile eşleşip eşleşmediğine bakıyoruz eşleşiyorsa false dönerek hata olmadıgını anlıyoruz
            //eger eşleme yoksa repeatPasword de hata oldugunu bıldııryoruz ve true donerek hata var dıyoruz
            passwordInputLayout.setErrorEnabled(false);
            if (!repeatPassword.equals(password)) {

                repeatPasswordInputLayout.setError("Şifre Eşleşmiyor");
                return true;


            } else {
                repeatPasswordInputLayout.setErrorEnabled(false);

                return false;
            }
        }
    }

    /**
     * Gelen Strinlerin Bos olup olmamasına göre durum döndürüyor true -> boş  false--> dolu
     *
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
     * Gelen ViewLerin (Edittext) bos olması durumunda -->setError .
     *
     * @param views
     */
    private void setErrorInputIfEmpty(View... views) {

        for (int i = 0; i < views.length; i++) {

            if (((EditText) views[i]).getText().toString().isEmpty()) {

                ((TextInputLayout) inputViewHashMap.get(views[i])).setError("Bu alan Boş olamaz");
            } else {
                ((TextInputLayout) inputViewHashMap.get(views[i])).setErrorEnabled(false);


            }


        }


    }

    /**
     * @param _signUpNameText
     * @param _signUpJobText
     * @param _signUpLocationText
     * @return
     */
    private boolean checkInputs(String _signUpNameText, String _signUpJobText, String _signUpLocationText) {

        setErrorInputIfEmpty(signUpNameText, signUpJobText, signUpLocationText);
        if (checkInputisEmpty(_signUpJobText, _signUpLocationText, _signUpNameText)) {
            return false;
        } else {
            mainContent = new MainContent(_signUpJobText,
                    _signUpLocationText,
                    _signUpNameText);
            return true;
        }

    }

    @Override
    public void onDestroy() {

        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

}
