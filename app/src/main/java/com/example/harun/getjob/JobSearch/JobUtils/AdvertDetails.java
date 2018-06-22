package com.example.harun.getjob.JobSearch.JobUtils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.harun.getjob.AddJobAdvert.ApplicantUserModel;
import com.example.harun.getjob.AddJobAdvert.ApplyAdvertModel;
import com.example.harun.getjob.AddJobAdvert.HelperStaticMethods;
import com.example.harun.getjob.MainActivity;
import com.example.harun.getjob.R;
import com.example.harun.getjob.viewpagercards.TagLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import org.altervista.andrearosa.statebutton.StateButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by mayne on 17.04.2018.
 */


/**
 * Job Search te Tıklanan her marker için Bottom Sliding Panelde
 */
public class AdvertDetails {
    private static final String TAG = "AdvertDetails";

    public interface ChangeMarkerCluster {

        void changeMarker(NearJobAdvertModel item, boolean isBasvuruOrSave);

    }

    /**
     * İlan detayları sayfası ile ilgili hersey bu sınıfta ..
     */
    public static class ViewHolder implements View.OnClickListener, OnMapReadyCallback {
        TextView tanimTv, jobadresTv, distance3,
                egitimSeviyesiTv, tecrubeTV, calismaSekliTv, ehliyetTv, cinsiyetTv, askerlikTv;
        TagLayout imkanlarcontent;
        StateButton btnBasvur;
        public MapView mapview;
        public GoogleMap mMap;
        ImageView save_this_advert;
        Context mContext;
        View tagView;
        private ViewSwitcher pos_viewSwitch;
        boolean kontrol;
        private NearJobAdvertModel mJobAdvertModel;
        ChangeMarkerCluster markerCluster;
        // deneme markerCluster;

        public ViewHolder(View itemView, Context mContext, ChangeMarkerCluster changeMarkerCluster) {
            Log.d(TAG, "ViewHolder: ");
            markerCluster = changeMarkerCluster;
            this.mContext = mContext;
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
                    HelperStaticMethods.setMargins(tagText, 5, 5, 5, 5);
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
                        markerCluster.changeMarker(mJobAdvertModel, false);
                        addFirebaseAppliedAdvert();
                        //Log.d(TAG, "SETSAVE:FALSE " + mJobAdvertModel);
                    } else {
                        save_this_advert.setActivated(true);
                        mJobAdvertModel.setSave(true);
                        //  Log.d(TAG, "SETSAVE:TRUE " + mJobAdvertModel);
                        markerCluster.changeMarker(mJobAdvertModel, false);
                        addFirebaseAppliedAdvert();

                    }
                    break;


                case R.id.basvurbtn:
                    Log.d(TAG, "onClick: ");

                    if (mJobAdvertModel.getBasvuruDurumu().equals(StateButton.BUTTON_STATES.ENABLED)) { //Basvuru yapılmamıs buton kullanılabilir durumda ise yap
                        Log.d(TAG, "onClick:btnBasvur.getState().getValue() == 0 ");
                        btnBasvur.setState(StateButton.BUTTON_STATES.DISABLED);
                        mJobAdvertModel.setBasvuruDurumu(true);//Bassvuru yapıldı demek...
                        markerCluster.changeMarker(mJobAdvertModel, true);


                        //////ADD FİREBASE BASVURU YAPILDI

                        addFirebaseAppliedAdvert();


                    } else {
                        Log.d(TAG, "BASVURU DAHA ÖNCEDEN YAPILMIS  İCİN  DİSABLE MODE: ");
                        //Log.d(TAG, "onClick: " + mJobAdvertModel);
                    }


            }


        }

        private void addFirebaseAppliedAdvert() {
            String date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());
            final ApplyAdvertModel applyAdvertModel = new ApplyAdvertModel(mJobAdvertModel.getJobID(), date, mJobAdvertModel.isSave(), mJobAdvertModel.getBasvuruDurumu() == StateButton.BUTTON_STATES.DISABLED);
            final ApplicantUserModel applicantUserModel = new ApplicantUserModel(MainActivity.userID, date);

            FirebaseDatabase.getInstance().getReference()
                    .child("users_data")
                    .child(MainActivity.userID)
                    .child("applyAdvert")
                    .child(applyAdvertModel.getJobID()).setValue(applyAdvertModel)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: Basvuru bilgisi kullanıcı basvurularına eklendi ");

                            FirebaseDatabase.getInstance().getReference()
                                    .child("jobAdvert")
                                    .child("publishedAdverts")
                                    .child(mJobAdvertModel.getJobID())
                                    .child("applyInfo")
                                    .child(MainActivity.userID).setValue(applicantUserModel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "onSuccess: basvuru bilgisi ilan detaylarına eklendi");
                                        }
                                    });


                        }
                    });


        }


    }

}




