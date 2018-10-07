package com.example.harun.getjob.JobSearch.JobUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.harun.getjob.AddJobAdvert.ApplicantUserModel;
import com.example.harun.getjob.AddJobAdvert.ApplyAdvertModel;
import com.example.harun.getjob.AddJobAdvert.HelperStaticMethods;
import com.example.harun.getjob.R;
import com.example.harun.getjob.UserIntro;
import com.example.harun.getjob.TagLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.altervista.andrearosa.statebutton.StateButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by mayne on 17.04.2018.
 */


/**
 * Job Search te Tıklanan her marker için Bottom Sliding Panelde
 * Yakınımdakileri Listele Butonuna basıldıgında listelenen Liste detaylarında Önerilen İlanlar Detaylarında Kısaca Tüm İlanların Detay Kısmında
 * burası yüklenecek
 */
public class AdvertDetails {
    private static final String TAG = "AdvertDetails";

    public interface ChangeMarkerCluster {

        void changeMarker(NearJobAdvertModel item, boolean isBasvuruOrSave);

    }

    /**
     * İlan detayları sayfası ile ilgili hersey bu sınıfta ..
     */
    public static class ViewHolder implements View.OnClickListener, OnMapReadyCallback, CompoundButton.OnCheckedChangeListener {
        TextView tanimTv, jobadresTv, distance3,
                egitimSeviyesiTv, tecrubeTV, calismaSekliTv, ehliyetTv, cinsiyetTv, askerlikTv;
        TagLayout imkanlarcontent;
        StateButton btnBasvur;
        public MapView mapview;
        public GoogleMap mMap;
        ImageView save_this_advert;
        Context mContext;
        View tagView;
        private EditText preInfoTag;
        private ViewSwitcher pos_viewSwitch;
        boolean kontrol;
        private NearJobAdvertModel mJobAdvertModel;
        ChangeMarkerCluster markerCluster;
        private int mode;
        private ArrayAdapter<String> savedPreInfoAdapter;
        private HashMap<String, String> mySavedPreInfoHashMap;
        private ArrayList<String> mySavedPreInfoTagList;
        private Spinner savedPreInfoSpinner;
        private Switch saveSwitch;
        // deneme markerCluster;

        public ViewHolder(View itemView, Context mContext, @Nullable ChangeMarkerCluster changeMarkerCluster, int mode) {
            Log.d(TAG, "ViewHolder: ");
            this.mContext = mContext;
            markerCluster = changeMarkerCluster;
            btnBasvur = itemView.findViewById(R.id.basvurbtn);
            save_this_advert = itemView.findViewById(R.id.save_this_advert);
            tanimTv = itemView.findViewById(R.id.tanimTv);
            jobadresTv = itemView.findViewById(R.id.jobadresTv);
            distance3 = itemView.findViewById(R.id.distance3);
            mapview = itemView.findViewById(R.id.mapView);
            egitimSeviyesiTv = itemView.findViewById(R.id.egitimSeviyesiTv);
            tecrubeTV = itemView.findViewById(R.id.tecrubeTV);
            calismaSekliTv = itemView.findViewById(R.id.calismaSekliTv);
            ehliyetTv = itemView.findViewById(R.id.ehliyetTv);
            cinsiyetTv = itemView.findViewById(R.id.cinsiyetTv);
            askerlikTv = itemView.findViewById(R.id.askerlikTv);
            imkanlarcontent = itemView.findViewById(R.id.imkanlarcontent);
            pos_viewSwitch = itemView.findViewById(R.id.pos_viewSwitch);
            this.mode = mode;
            //bu mode değişkeni ile changeMarkerCluster İnterface inin ne zaman tetiklenip tetiklenmiyecegini ayarlıyorum
            //Çünkü bu sınıf 3 4 yerde kullanılıyor.Bazı yerlerde markerin durumunun değişmesi gerekmediğinden dolayı . mode 0 markerdurumu değiştir   mode 1 markerdurumu değiştirme
            init();


        }

        private void init() {

            if (mapview != null) {

                mapview.onCreate(null);
                mapview.getMapAsync(this);
            }
            save_this_advert.setOnClickListener(this);
            btnBasvur.setOnClickListener(this);


        }

        /**
         * NearJObAdvertAdapter den gelen itemler.
         *
         * @param fromNearJobAdapter
         */

        public void bindMapLocation(NearJobAdvertModel fromNearJobAdapter) {
            //mJobAdvertModel = fromNearJobAdapter;
            //  Log.d(TAG, "bindMapLocation: "+mjobAdvertModel);
            //   mapview.setTag(mJobAdvertModel);
            Log.d(TAG, "bindMapLocation: " + fromNearJobAdapter);
            setData(fromNearJobAdapter);
        }


        public void setData(NearJobAdvertModel data) {

            mJobAdvertModel = data;
            Log.d(TAG, "setData: " + mJobAdvertModel);
            jobadresTv.setText(data.getCompanyAdress());
            distance3.setText(data.getCompanyDistance());
            tanimTv.setText(data.getJobDescpriction());
            egitimSeviyesiTv.setText(data.getEducationLevel());
            tecrubeTV.setText(data.getExpLevel());
            calismaSekliTv.setText(data.getEmployeeHour());
            ehliyetTv.setText(data.getDrivingLicence());
            cinsiyetTv.setText(data.getGender());
            askerlikTv.setText(data.getMilitary());
            save_this_advert.setActivated(data.isSave()); //Daha önceden kayıtlı ise onu set ediyoruz...
            btnBasvur.setState(data.getBasvuruDurumu()); //
            Log.d(TAG, "setData:DİSABLED" + data.getBasvuruDurumu().getValue());
            addPossibility(data.getJobPossibility());
            setMapLocation();

        }

        private void addPossibility(ArrayList<String> jobPossibility) {

            Log.d(TAG, "addPossibility: ");
            pos_viewSwitch.setDisplayedChild(1);
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (String text : jobPossibility) {

                tagView = layoutInflater.inflate(R.layout.pos_textview, null, false);
                if (!text.isEmpty()) {
                    TextView tagText = tagView.findViewById(R.id.tagText);
                    tagText.setText(text);
                    HelperStaticMethods.setMargins(tagText, 5, 5, 5, 0);
                    imkanlarcontent.addView(tagView);


                }


            }

        }


        @Override
        public void onMapReady(GoogleMap googleMap) {
            Log.d(TAG, "onMapReady: ");
            MapsInitializer.initialize(mContext);
            mMap = googleMap;
            mMap.getUiSettings().setMapToolbarEnabled(true);
            setMapLocation();


        }

        private void setMapLocation() {
            Log.d(TAG, "setMapLocation: ");
            if (mJobAdvertModel == null) return;
            if (mMap == null) return;
            mMap.addMarker(new MarkerOptions().position(mJobAdvertModel.getmPosition())
                    .icon(MapHelperMethods.getNormalMarkerDrawable(mContext))
                    .title(mJobAdvertModel.getCompanyName() + "\n".concat(mJobAdvertModel.getCompanyJob())));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mJobAdvertModel.getmPosition(), 15f));
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

        /**
         * JobSearch te tıklanan her ilan markeri için bir map olusturmak gerektiğin
         * den önce bu method calısıtırılıp sonra map koyuluyor.
         */
        public void clearMap() {
            Log.d(TAG, "clearMap: ");
            if (mMap != null) {
                Log.d(TAG, "clearMap: mMap !=null");
                mMap.clear();
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
            }
            if (pos_viewSwitch.getCurrentView() == pos_viewSwitch.getChildAt(1)) {
                Log.d(TAG, "clearMap: pos_viewSwitch.getCurrentView() == pos_viewSwitch.getChildAt(1)");
                imkanlarcontent.removeAllViews();

            }

        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.save_this_advert:
                    Log.d(TAG, "onClick: SAVE THİS ADVERT ");
                    if (save_this_advert.isActivated()) {
                        save_this_advert.setActivated(false);
                        mJobAdvertModel.setSave(false);
                        changeMarkerState(false);

                        if (mJobAdvertModel.getBasvuruDurumu().equals(StateButton.BUTTON_STATES.DISABLED)) {
                            Log.d(TAG, "onClick: BASVURU İŞLEMİ YAPILMIS KAYIDI EKLE ");
                            addFirebaseAppliedAdvert("", false);

                        } else {
                            Log.d(TAG, "onClick: NE BASVURU NE KAYIT VAR İLANI KAYDINI TUTMANIN ANLAMI YOK .");
                            removeFromFirebase();
                        }
                        //Log.d(TAG, "SETSAVE:FALSE " + mJobAdvertModel);
                    } else {
                        save_this_advert.setActivated(true);
                        mJobAdvertModel.setSave(true);
                        //  Log.d(TAG, "SETSAVE:TRUE " + mJobAdvertModel);
                        changeMarkerState(false);
                        addFirebaseAppliedAdvert("", false);


                    }
                    break;


                case R.id.basvurbtn:
                    Log.d(TAG, "onClick: ");
                    if (mJobAdvertModel.getBasvuruDurumu().equals(StateButton.BUTTON_STATES.ENABLED)) { //Basvuru yapılmamıs buton kullanılabilir durumda ise yap
                        Log.d(TAG, "onClick:btnBasvur.getState().getValue() == 0 ");
                        btnBasvur.setState(StateButton.BUTTON_STATES.LOADING);
                        mJobAdvertModel.setBasvuruDurumu(true);//Bassvuru yapıldı demek...
                        showPreInfoDıalog();
                        changeMarkerState(true);


                        // addFirebaseAppliedAdvert();
                    } else {
                        Log.d(TAG, "BASVURU DAHA ÖNCEDEN YAPILMIS  İCİN  DİSABLE MODE: ");
                        //Log.d(TAG, "onClick: " + mJobAdvertModel);
                    }


            }


        }

        /**
         * Ön Yazı Eklemek İçin Acılan Dialog ve İŞlemleri
         */
        private void showPreInfoDıalog() {

            Log.d(TAG, "showPreInfoDıalog: ");
            final Dialog dialog = new Dialog(mContext, R.style.CustomDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.preinfo_dialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {

                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        Log.d(TAG, "onKey: BACK ");
                    }


                    return true;
                }
            });
            final EditText infoText = dialog.findViewById(R.id.infoText);
            Button doneBtn = dialog.findViewById(R.id.doneBtn);
            savedPreInfoSpinner = dialog.findViewById(R.id.savedPreInfoSpinner);
            preInfoTag = dialog.findViewById(R.id.preInfoTag);
            saveSwitch = dialog.findViewById(R.id.saveSwitch);
            getMySavedPreInfo();
            saveSwitch.setOnCheckedChangeListener(this);
            savedPreInfoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if (adapterView.getSelectedItem().equals("Kayıtlı Ön Yazılarımdan Seç")) {

                        Log.d(TAG, "onItemSelected: " + adapterView.getSelectedItem());


                    } else {


                        infoText.setText(mySavedPreInfoHashMap.get(adapterView.getSelectedItem().toString()));
                        Log.d(TAG, "onItemSelected: " + adapterView.getSelectedItem());

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            doneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Önyazı Kayıt Etme acık
                    if (saveSwitch.isChecked()) {

                        if (TextUtils.isEmpty(preInfoTag.getText())) {

                            //Onyazı Baslıgı Bos ise
                            Log.d(TAG, "KAYIT ETME ACIK BASLIK BOS : ");
                            preInfoTag.setError("Bos Olamaz");

                        } else {
                            Log.d(TAG, "onClick: KAYIT ETME ACIK BASLIK DOLU  ");
                            btnBasvur.setState(StateButton.BUTTON_STATES.DISABLED);
                            addFirebaseAppliedAdvert(infoText.getText().toString(), true);
                            dialog.dismiss();
                        }

                    } else {
                        Log.d(TAG, "KAYIT ETME KAPALI ON YAZI KAYIDINA GEREK YOK ");
                        btnBasvur.setState(StateButton.BUTTON_STATES.DISABLED);
                        addFirebaseAppliedAdvert(infoText.getText().toString(), false);
                        dialog.dismiss();

                    }
                }
            });

            dialog.show();

        }

        /**
         * Kayıtlı Ön yazılarımı Firebaseden al Spinner İçerisine Yerleştir  ve Spinner İşlemlerini yap
         */
        private void getMySavedPreInfo() {

            Log.d(TAG, "getMySavedPreInfo: ");
            mySavedPreInfoHashMap = new HashMap<>();
            mySavedPreInfoTagList = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference().child("users_data").child(UserIntro.userID).
                    child("myPreInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Log.d(TAG, "onDataChange: ");

                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            mySavedPreInfoHashMap.put(snapshot.getKey(), (String) snapshot.getValue());
                            Log.d(TAG, "onDataChange:KAYIT ON YAZILAR  " + snapshot);

                        }
                        mySavedPreInfoTagList.addAll(mySavedPreInfoHashMap.keySet());
                        mySavedPreInfoTagList.add("Kayıtlı Ön Yazılarımdan Seç");
                        savedPreInfoAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mySavedPreInfoTagList) {

                            // 0 dan büyükse liste son ekleneni liste içerisnde gösterme yani "Kayıtlı Ön yazılarımdan sec yazısını "
                            @Override
                            public int getCount() {
                                return super.getCount() > 0 ? super.getCount() - 1 : super.getCount();
                            }
                        };
                        savedPreInfoSpinner.setAdapter(savedPreInfoAdapter);
                        savedPreInfoSpinner.setHovered(true);
                        savedPreInfoSpinner.setSelection(savedPreInfoAdapter.getCount());//Sonuncu yu al Spinner hint olacak

                    } else {
                        Log.d(TAG, "onDataChange: KAYITLI ONYAZI BULUNAMADI");
                        mySavedPreInfoTagList.add("Kayitli Ön Yaziniz Yok ");
                        savedPreInfoAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mySavedPreInfoTagList);
//                        {
//
//
//                            // 0 dan büyükse liste son ekleneni liste içerisnde gösterme yani "Kayıtlı Ön yazılarımdan sec yazısını "
//                            @Override
//                            public int getCount() {
//                                return super.getCount();
//                            }
//                        };
                        savedPreInfoSpinner.setAdapter(savedPreInfoAdapter);
                        savedPreInfoSpinner.setHovered(true);
                       // savedPreInfoSpinner.setSelection(savedPreInfoAdapter.getCount());//Sonuncu yu al Spinner hint olacak

                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        /**
         * Mode 0 markerin gerekli oldugu durumlarda gecerlidir.
         *
         * @param isBasvuruOrSave
         */
        private void changeMarkerState(boolean isBasvuruOrSave) {
            if (mode == 0) {
                Log.d(TAG, "changeMarkerState: MODE" + mode);
                markerCluster.changeMarker(mJobAdvertModel, isBasvuruOrSave);

            } else {


                return;
            }
        }

        private void removeFromFirebase() {

            FirebaseDatabase.getInstance().getReference()
                    .child("users_data")
                    .child(UserIntro.userID)
                    .child("applyAdvert")
                    .child(mJobAdvertModel.getJobID()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i(TAG, "onSuccess: İLAN KAYIDI GERİ ALINDI ");
                }
            });

        }

        /**
         * Kayıt veya Basvuru İşlemlerinde Firebase Kayıdı Yapıyor
         *
         * @param infoText
         * @param issavePreInfo -->Ön yazının kayıt edilip edilmeyecegini belirten parametre
         */
        private void addFirebaseAppliedAdvert(final String infoText, final boolean issavePreInfo) {

            final String date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());
            final ApplyAdvertModel applyAdvertModel = new ApplyAdvertModel(
                    mJobAdvertModel.getJobID(),
                    date, mJobAdvertModel.isSave(),
                    mJobAdvertModel.getBasvuruDurumu() == StateButton.BUTTON_STATES.DISABLED);
            FirebaseDatabase.getInstance().getReference()
                    .child("users_data")
                    .child(UserIntro.userID)
                    .child("applyAdvert")
                    .child(applyAdvertModel.getJobID()).setValue(applyAdvertModel)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i(TAG, "onSuccess: Basvuru bilgisi kullanıcı basvurularına eklendi ");

                            FirebaseDatabase.getInstance().getReference()
                                    .child("users_data")
                                    .child(UserIntro.userID)
                                    .child("suggestionKey").child(mJobAdvertModel.getJobSector()).child(mJobAdvertModel.getCompanyJob()).setValue(mJobAdvertModel.isSave()) //kayıtlara göre ise true değilse false yani basvurulara göre  sıralancak önerilecek ilanlar
                                    // .child(mJobAdvertModel.getJobSector()).child(mJobAdvertModel.getCompanyJob()).setValue(true)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i(TAG, "onSuccess: Kullanıcıya Daha sonra Önerilecek Olan  İlanlar İçin Sektör ve Meslek Bilgisi eklendi ");

                                            //Sadece basvuru işlemi gerçekleşmiş ise
                                            if (mJobAdvertModel.getBasvuruDurumu() == StateButton.BUTTON_STATES.DISABLED) {
                                                final ApplicantUserModel applicantUserModel = new ApplicantUserModel(UserIntro.userID, date, infoText);
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("jobAdvert")
                                                        .child("publishedAdverts")
                                                        .child(mJobAdvertModel.getJobID())
                                                        .child("applyInfo")
                                                        .child(UserIntro.userID).setValue(applicantUserModel)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.i(TAG, "onSuccess: basvuru bilgisi ilanın detaylarına eklendi");

                                                            }
                                                        });

                                                //Kayıt Etme acık ise
                                                if (issavePreInfo) {
                                                    FirebaseDatabase.getInstance().getReference().child("users_data")
                                                            .child(UserIntro.userID).child("myPreInfo").child(preInfoTag.getText().toString()).setValue(infoText).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.i(TAG, "onSuccess: onyazı Bilgisi KAayıt Edildi");
                                                        }
                                                    });

                                                }
                                            }


                                        }
                                    });


                        }
                    });


        }


        //Switch Btn -->Ön yazı Kaydet
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            Log.d(TAG, "onCheckedChanged: " + b);
            if (b) {

                preInfoTag.setVisibility(View.VISIBLE);

            } else {

                preInfoTag.setVisibility(View.GONE);
                preInfoTag.setText("");

            }
        }
    }

}




