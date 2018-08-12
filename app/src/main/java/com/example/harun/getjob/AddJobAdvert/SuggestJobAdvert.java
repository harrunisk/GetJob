package com.example.harun.getjob.AddJobAdvert;

import android.os.AsyncTask;
import android.util.Log;

import com.example.harun.getjob.JobSearch.JobUtils.JobAdvertModel2;
import com.example.harun.getjob.JobSearch.JobUtils.MapHelperMethods;
import com.example.harun.getjob.JobSearch.JobUtils.NearJobAdvertModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by mayne on 24.06.2018.
 */

public class SuggestJobAdvert extends AsyncTask<DataSnapshot, ArrayList<NearJobAdvertModel>, ArrayList<NearJobAdvertModel>> {

    private static final String TAG = "SuggestJobAdvert";
    private SuggestJobAdvertInterface suggestJobAdvertInterface;
    private ArrayList<NearJobAdvertModel> suggestionList;
    private LatLng gps;
    private String type;
    private double distance = 0;
    private boolean sector;
    private ArrayList<String> myAppliedAdvertJobIdLList;

    public interface SuggestJobAdvertInterface {

        void SuggestJobAdvertCallback(ArrayList<NearJobAdvertModel> callbackResultList, boolean sektor);

    }

    public SuggestJobAdvert(SuggestJobAdvertInterface suggestJobAdvertInterface, LatLng gps, String type, boolean _sector, ArrayList<String> myAppliedAdvertJobIdLList) {

        this.suggestJobAdvertInterface = suggestJobAdvertInterface;
        this.gps = gps;
        this.type = type;
        this.sector = _sector;
        this.myAppliedAdvertJobIdLList = myAppliedAdvertJobIdLList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //   getDeviceLocation();

    }

    @Override
    protected ArrayList<NearJobAdvertModel> doInBackground(DataSnapshot... dataSnapshots) {

        suggestionList = new ArrayList<>();

        for (DataSnapshot dataSnapshot : dataSnapshots[0].getChildren()) {

            Log.d(TAG, "doInBackground: " + dataSnapshot);

            if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {

                for (DataSnapshot adverts : dataSnapshot.getChildren()) {
                    if (adverts.getKey().equals("jobInfo")) {

                        Log.d(TAG, "doInBackground:getKey " + adverts.getKey());
                        Log.d(TAG, "doInBackground:getChildren " + adverts.getValue(JobAdvertModel2.class));

                        //Daha önceden Basvurulmus veya kayıt edilmiş ilanlar burada ayrıstırılıyor.
                        if (!myAppliedAdvertJobIdLList.contains(adverts.getValue(JobAdvertModel2.class).getJobID())) {


                            distance = MapHelperMethods.getDistanceParce(MapHelperMethods.toRadiusMeters(gps, adverts.getValue(JobAdvertModel2.class).getmPosition()));
                            //  Log.d(TAG, "doInBackground:distance " + distance);
                            if (distance <= 1000000) {
                                Log.d(TAG, "doInBackground:distance <= 100 " + distance);

                                /**
                                 * 10000KM YAPTIM HEPSİ GELSİN DİYE
                                 Şimdi burada gelecek olan ilanların içinden bana yakın  30km içinde olanları listeye alıyorum .
                                 Bu 30km yi daha sonra kullanıcı belirlyeecek şimdilik 30 km
                                 Burada önemli olan bana gelecek olan ilanların neye göre geleceği yani
                                 bu doInBack methoduna gelen datasnapshot ın sektöre göre mi  meslege göre mi firebaseden çekilip buraya geleceği
                                 Kullanıcı daha önceden bir ilana başvuru veya kayıt işlemi yaptı ise o ilanın sektör bilgisi meslegi ve lokasyonu
                                 kayıt altına alınacak her kullanıcı için özel olan suggestAdverts tablosunda kayıt edilecek.buradada suggestAdverts tablosundan gelen bilgilerle
                                 eşleşen ilanlar çekilecek ve kullanıcının belirleyeceği bir km aralıgında veya Lokasyona göre buradan süzülüp listeye alınıcak
                                 eğer Hiç kayıt veya basvuru işlemi yapmammıs ise Bulundugu konuma yakın herhangi sektör veya meslekten işler burada doldurulacak.

                                 */

                                suggestionList.add(new NearJobAdvertModel(adverts.getValue(JobAdvertModel2.class), String.format(Locale.getDefault(), "%.2f", distance), type));

                            }
                        }

                    }
                }


            }


        }


        return suggestionList;
    }


    @Override
    protected void onPostExecute(ArrayList<NearJobAdvertModel> jobAdvertModel2s) {
        super.onPostExecute(jobAdvertModel2s);
        Log.d(TAG, "onPostExecute: " + jobAdvertModel2s);
        suggestJobAdvertInterface.SuggestJobAdvertCallback(jobAdvertModel2s, sector);
    }
}
