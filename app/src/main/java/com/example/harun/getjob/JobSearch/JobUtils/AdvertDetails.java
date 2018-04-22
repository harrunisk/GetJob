package com.example.harun.getjob.JobSearch.JobUtils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harun.getjob.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

import org.altervista.andrearosa.statebutton.StateButton;

/**
 * Created by mayne on 17.04.2018.
 */


/**
 * Job Search te Tıklanan her marker için Bottom Sliding Panelde
 */
public class AdvertDetails extends JobAdvertModel {
    private static final String TAG = "AdvertDetails";

    public interface ChangeMarkerCluster {

        void changeMarker(JobAdvertModel item, boolean isBasvuruOrSave);

    }

    /**
     * İlan detayları sayfası ile ilgili hersey bu sınıfta ..
     */
    public static class ViewHolder implements View.OnClickListener, OnMapReadyCallback {
        TextView tanimTv;
        StateButton btnBasvur;
        public MapView mapview;
        public GoogleMap mMap;
        ImageView save_this_advert;
        Context mContext;
        boolean kontrol;
        private JobAdvertModel mJobAdvertModel;
        ChangeMarkerCluster markerCluster;
        // deneme markerCluster;

        public ViewHolder(View itemView, Context mContext, ChangeMarkerCluster changeMarkerCluster) {
            markerCluster = changeMarkerCluster;
            this.mContext = mContext;
            Log.d(TAG, "ViewHolder: ");
            btnBasvur = itemView.findViewById(R.id.basvurbtn);
            save_this_advert = itemView.findViewById(R.id.save_this_advert);
            tanimTv = itemView.findViewById(R.id.tanimTv);
            mapview = itemView.findViewById(R.id.mapView);
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

        public void bindMapLocation(JobAdvertModel fromNearJobAdapter) {
            //mJobAdvertModel = fromNearJobAdapter;
            //  Log.d(TAG, "bindMapLocation: "+mjobAdvertModel);
            //   mapview.setTag(mJobAdvertModel);
            Log.d(TAG, "bindMapLocation: " + fromNearJobAdapter);
            setData(fromNearJobAdapter);
        }


        public void setData(JobAdvertModel data) {

            mJobAdvertModel = data;
            Log.d(TAG, "setData: " + mJobAdvertModel);
            save_this_advert.setActivated(data.isSave()); //Daha önceden kayıtlı ise onu set ediyoruz...
            // tanimTv.setText(data.getCompanyDistance());
            btnBasvur.setState(data.getBasvuruDurumu()); //
            Log.d(TAG, "setData:DİSABLED" + data.getBasvuruDurumu().getValue());

            setMapLocation();

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
            // Log.d(TAG, "setMapLocation: " + mjobAdvertModel.getPosition());
            if (mMap == null) return;
            mMap.addMarker(new MarkerOptions().position(mJobAdvertModel.getPosition())
                    .icon(MapHelperMethods.getNormalMarkerDrawable(mContext))
                    .title(mJobAdvertModel.getCompanyName() + "\n".concat(mJobAdvertModel.getCompanyJob())));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mJobAdvertModel.getPosition(), 15f));
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

        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.save_this_advert:
                    Log.d(TAG, "onClick: SAVE THİS ADVERT ");
                    if (save_this_advert.isActivated()) {
                        save_this_advert.setActivated(false);
                        mJobAdvertModel.setSave(false);
                        markerCluster.changeMarker(mJobAdvertModel,false);
                        //Log.d(TAG, "SETSAVE:FALSE " + mJobAdvertModel);
                    } else {
                        save_this_advert.setActivated(true);
                        mJobAdvertModel.setSave(true);
                        //  Log.d(TAG, "SETSAVE:TRUE " + mJobAdvertModel);
                        markerCluster.changeMarker(mJobAdvertModel,false);
                    }
                    break;


                case R.id.basvurbtn:
                    Log.d(TAG, "onClick: ");

                    if (mJobAdvertModel.getBasvuruDurumu().equals(StateButton.BUTTON_STATES.ENABLED)) { //Basvuru yapılmamıs buton kullanılabilir durumda ise yap
                        Log.d(TAG, "onClick:btnBasvur.getState().getValue() == 0 ");
                        btnBasvur.setState(StateButton.BUTTON_STATES.DISABLED);
                        mJobAdvertModel.setBasvuruDurumu(1);//Bassvuru yapıldı demek...
                        markerCluster.changeMarker(mJobAdvertModel,true);

                    } else {
                        Log.d(TAG, "BASVURU DAHA ÖNCEDEN YAPILMIS  İCİN  DİSABLE MODE: ");
                        //Log.d(TAG, "onClick: " + mJobAdvertModel);
                    }


            }


        }


    }






    /*
    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        View myView = view;

        if (myView == null) {
            Log.d(TAG, "getView: ");
            myView = layoutInflater.inflate(R.layout.jobadvertdetails, null, false);
            viewHolder = new ViewHolder(myView);
            myView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) myView.getTag();
        }


        viewHolder.setData((JobAdvertModel) getItem(position));
        return myView;
    }
*/
}




