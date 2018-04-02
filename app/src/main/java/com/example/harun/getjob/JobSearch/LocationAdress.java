package com.example.harun.getjob.JobSearch;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mayne on 2.04.2018.
 */


/**
 *
 * Bu sınıfta her halükarda bir String dönecek..
 * Asıl method getAdress Methodu dışardan erişime açık olan method
 * Bu methodda Geocoder Çalışmaz ise https://maps.googleapis.com/maps/api/geocode/json?latlng "" google apisine get isteği yapacak olan
 * getUsingMap methodu çalışacak.Arka planda adres çekilerek geriye Adres stringini gönderecek.
 * Sonuç olarak bu sınıfta bir adres Stringi geri dönecek.
 *
 *
 */

public class LocationAdress {

    private static final String TAG = "LocationAdress";
    Context mContext;


    public LocationAdress(Context mContext) {
        this.mContext = mContext;
    }

    public String getAdress(LatLng myItemLocation) {

        Log.d(TAG, "getAdress: ÇALIŞIYOR.");

        //Burada tıklanan bölgenin adres bilgisini yazdıracagım


        String adress = "";
        if (Geocoder.isPresent()) {

            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());


            try {
                List<Address> addressList = geocoder.getFromLocation(myItemLocation.latitude,
                        myItemLocation.longitude, 1);

                Log.d(TAG, "getADRESS: " + addressList);
                if (addressList != null && addressList.size() > 0) {

                    if (addressList.get(0).getThoroughfare() != null) {

                        adress += addressList.get(0).getAdminArea() + "  " + addressList.get(0).getSubAdminArea() + " ";
                        adress += addressList.get(0).getThoroughfare() + " ";
                        if (addressList.get(0).getSubThoroughfare() != null) {
                            adress += addressList.get(0).getSubThoroughfare();

                        }
                    } else {
                        adress = "Adres Bilgisi Bulunamadi !";
                    }

                } else {
                    adress = "Adres Bilgisi Bulunamadi !";
                }


            } catch (Exception e) {

                e.printStackTrace();

            }
            return adress;
        } else //Geocoder failed..

        {
            Log.d(TAG, "getAdress: GEOLOCATİON NOT PRESENT");
            return getAdressUsingGoogleMap(myItemLocation);

        }

        // return "Adres Bilgisi Yok .";
    }

    /**
     * Eger Geocoderde Bir hata meydana gelir ise Googlemap geocode apiye bir get İsteği yapıcaz..
     */
    private String getAdressUsingGoogleMap(LatLng myItemLocation) {

        DownloadJsonData downloadJsonData = new DownloadJsonData();
        String googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                + myItemLocation.latitude + "," + myItemLocation.longitude
                + "&key=AIzaSyDEf8QNnqFNFsCz_uHOacvTVuCFNwk03sU";
        try {

            Log.d(TAG, "getAdressUsingGoogleMap:Çalışıyor. ");

            downloadJsonData.execute(googleMapUrl);
            //new DownloadJsonData().execute(googleMapUrl);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return downloadJsonData.getAdressFromJson();
    }


    public class DownloadJsonData extends AsyncTask<String, Void, String> {
        private JSONObject jsonObject;
        private String jsonData;
        private String adressFromJson;
        private OkHttpClient okHttpClient = new OkHttpClient();

        @Override
        protected String doInBackground(String... strings) {
            try {
                Log.d(TAG, "doInBackground: ");

                String local = "", sublocal = "", mahalle = "", sokakNo = "";

                jsonData = run(strings[0]); //---->URL Yİ OKKhttpye gönderiyorum.. ORdan bana json string olarak gelecek.
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
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);

            Log.d(TAG, "onPostExecute: " + s);

            if (s != null) {
                setAdressFromJson(s);
            } else {

                setAdressFromJson("Adres Bilgisi Bulunamadi!");
            }

        }


        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
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

                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                return e.getMessage();
            }
        }

        public void setAdressFromJson(String adressFromJson) {
            this.adressFromJson = adressFromJson;
        }

        public String getAdressFromJson() {
            return adressFromJson;
        }
    }

}
