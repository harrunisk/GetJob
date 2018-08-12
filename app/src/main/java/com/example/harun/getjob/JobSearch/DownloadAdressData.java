package com.example.harun.getjob.JobSearch;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.harun.getjob.R;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mayne on 5.04.2018.
 */


/**
 * Arka Planda Adres Bilgisini Çeker
 * Asıl adres Geocoder'in getFromLocation methodu ile gerçekleştirilecek bu methodda bir hata olur ise
 * google map api ye get isteği yapıcak olan getAdressUsingMapApi çalışacak burada Json Parse işlemi gerçekleştirilerek elde edilen
 * adres bilgisi adressCallback interface  yardımı ile jobsearch activity'e bildirilecek..
 * JobSearch activity içerisinde bu interface implement edilecek ve gerekli işlemler oradan devam edeccek ...
 */


public class DownloadAdressData extends AsyncTask<LatLng, Void, String> {
    private JSONObject jsonObject;
    private String jsonData;
    private String adressFromJson;
    private adressCallback madressCallback;
    private OkHttpClient okHttpClient = new OkHttpClient();
    //  public AVLoadingIndicatorView progres;
    private WeakReference<Activity> jobSearchWeakReference;
    //Context nesnesi yerine bunu kullanıyorum memory leak olmasın diye Bu metodu çağıran aktivitye bağlıyorum.
    private static final String TAG = "DownloadAdressData";
    private int mode;

    public interface adressCallback {
        void adressCallbackResult(String result);
    }

    public DownloadAdressData(Activity activity, adressCallback _adresCallback, int _mode) {

        //  super();
        this.jobSearchWeakReference = new WeakReference<Activity>(activity);
        this.madressCallback = _adresCallback;
        this.mode = _mode;
        //  this.progres = progress;

    }

    /**
     * Eger Geocoderde Bir hata meydana gelir ise Googlemap geocode apiye bir get İsteği yapıcaz..
     */
    private String getAdressUsingGoogleMapApi(LatLng myItemLocation) {

        // DownloadJsonData downloadJsonData = new DownloadJsonData();
        String googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                + myItemLocation.latitude + "," + myItemLocation.longitude
                + "&key=AIzaSyDEf8QNnqFNFsCz_uHOacvTVuCFNwk03sU";

        try {
            Log.d(TAG, "getAdressUsingGoogleMap: Methodu Çalışıyor");

            String local = "", sublocal = "", mahalle = "", sokakNo = "";

            jsonData = run(googleMapUrl); //---->URL Yİ OKKhttpye gönderiyorum.. ORdan bana json string olarak gelecek.
            try {

                jsonObject = new JSONObject(jsonData);
                // Log.d(TAG, "doInBackground: " + jsonObject);

                JSONArray results = (JSONArray) jsonObject.get("results");

                JSONObject adresJson = results.getJSONObject(0);
                //  Log.d(TAG, "doInBackground: RESULT"+results);

                JSONArray addressComponents = adresJson.getJSONArray("address_components");

                //  Log.d(TAG, "doInBackground: ADRESSCOMPONENET"+addressComponents);
                if (addressComponents.length() > 0) {
                    //  Log.d(TAG, "doInBackground: " + addressComponents);
                    for (int i = 0; i < addressComponents.length(); i++) {

                        JSONObject addressComponent = addressComponents.getJSONObject(i);

                        //Objenin her elemanı için kontroller yapılıp Adress olarak atanıyor .

                        if (addressComponent.has("types")) {


                            JSONArray types = addressComponent.getJSONArray("types");
                            // Log.d(TAG, "doInBackground:TYPE " + types);
                            if ("street_number".equals(types.getString(0))) {

                                ///   Log.d(TAG, "doInBackground: STREET NUMBER" + adresFromJson);
                                sokakNo = addressComponent.getString("long_name");
                            }
                            if ("route".equals(types.getString(0))) {


                                mahalle = addressComponent.getString("long_name");
                            }
                            if ("administrative_area_level_2".equals(types.getString(0))) {


                                sublocal = addressComponent.getString("long_name");
                            }
                            if ("administrative_area_level_1".equals(types.getString(0))) {

                                local = addressComponent.getString("long_name");

                            }


                        }

                    }


                    return local + " " + sublocal + " " + mahalle + " " + sokakNo;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {


        super.onPreExecute();

        if (jobSearchWeakReference.get().isFinishing() || jobSearchWeakReference.get() == null) {
            return;
        }
        visibilityProgress(true);

    }

    @Override
    protected String doInBackground(LatLng... latLngs) {
        Log.d(TAG, "getAdressDoinBackground: ÇALIŞIYOR.");
        //Burada tıklanan bölgenin adres bilgisini yazdıracagım

        String adress = "";
        if (Geocoder.isPresent()) {

            Geocoder geocoder = new Geocoder(jobSearchWeakReference.get(), Locale.getDefault());


            try {
                List<Address> addressList = geocoder.getFromLocation(latLngs[0].latitude,
                        latLngs[0].longitude, 1);

                Log.d(TAG, "getADRESS: " + addressList);
                if (addressList != null && addressList.size() > 0) {

                    adress += addressList.get(0).getAddressLine(0);


                   /* if (addressList.get(0).getThoroughfare() != null) {

                        adress += addressList.get(0).getAdminArea() + "  " + addressList.get(0).getSubAdminArea() + " ";
                        adress += addressList.get(0).getThoroughfare() + " ";
                        if (addressList.get(0).getSubThoroughfare() != null) {
                            adress += addressList.get(0).getSubThoroughfare();

                        }
                    } else {
                        adress = "Adres Bilgisi Bulunamadi !";
                    }*/

                } else {
                    adress = "Adres Bilgisi Bulunamadi !";
                }


            } catch (Exception e) {
                e.getLocalizedMessage();
                //  e.printStackTrace();
            } finally { ///-->sonunda Bir kontrol yapılacak...

                if (adress.length() <= 0) {

                    Log.d(TAG, "Finally:Adres Bilgisi Json olarak Alınacak Diğeri Çalışmadığı için adress.length" + adress.length());
                    adress += getAdressUsingGoogleMapApi(latLngs[0]);
                    //   return adress;
                } else {
                    Log.d(TAG, "Finally Adres bilgisi GEOCODER İLE SAĞLANDI");

                }

            }

            return adress;

        } else //Geocoder failed..

        {
            Log.d(TAG, "getAdress: GEOLOCATİON NOT PRESENT");
            return getAdressUsingGoogleMapApi(latLngs[0]);

        }


    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected void onPostExecute(String s) {
        //super.onPostExecute(s);

        Log.d(TAG, "onPostExecute:ADRES BİLGİSİ " + s);
        if (s != null && !s.isEmpty()) {

            madressCallback.adressCallbackResult(s);
            visibilityProgress(false);

        } else {
            s = "Adres Bilgisi Bulunamadi!";

            madressCallback.adressCallbackResult(s);
            visibilityProgress(false);

            //  setAdressFromJson("Adres Bilgisi Bulunamadi!");
        }

    }

        /*OkHttp Methodu */

    private String run(String url) throws IOException {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {

            Toast.makeText(jobSearchWeakReference.get(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return e.getMessage();
        }
    }

    private void visibilityProgress(boolean visibility) {

        if (mode == 1) {

            if (visibility) {
                jobSearchWeakReference.get().findViewById(R.id.adresLoading).setVisibility(View.VISIBLE);
            } else {
                jobSearchWeakReference.get().findViewById(R.id.adresLoading).setVisibility(View.GONE);
            }
        } else return;

    }

}

