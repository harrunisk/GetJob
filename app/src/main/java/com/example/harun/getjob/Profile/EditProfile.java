package com.example.harun.getjob.Profile;

import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harun.getjob.R;
import com.example.harun.getjob.profileModel.deneyimListAdapter;
import com.example.harun.getjob.profileModel.deneyimModel;
import com.example.harun.getjob.profileModel.egitimListAdapter;
import com.example.harun.getjob.profileModel.egitimListModel;
import com.example.harun.getjob.profileModel.yetenekListAdapter;
import com.example.harun.getjob.profileModel.yetenekModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class EditProfile extends AppCompatActivity implements contentFragment, View.OnClickListener {

    ArrayList<deneyimModel> denemeList;
    public TextView editAboutContent;
    private static final String TAG = "EditProfile";
    RecyclerView recyclerView;

    ImageView userProfile_image, saveAll;
    deneyimListAdapter mdeneyimListAdapter;

    RecyclerView recyclerViewEgitim;
    ArrayList<egitimListModel> egitimListe;
    egitimListAdapter megitimListAdapter;

    RecyclerView recyclerViewYetenek;
    ArrayList<yetenekModel> recyclerYetenekList;
    yetenekListAdapter myetenekListAdapter;

    TextView tvTel, tvMail, tvDogumTarih, tvEhliyet, tvAskerlik, changePhoto, empty_message, empty_message1, empty_message2;


    // String denemeUrl = "www.shareicon.net/data/128x128/2016/09/15/829473_man_512x512.png";
    String newPhotoUrl;
    Intent intent;
    Uri newPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate Calıstı");
        setContentView(R.layout.edit_profile);

        //   if (Permissions.checkPermissionArray(this,Permissions.PERMISSIONS)) {

        gatherViews();      // Tanimlamalar bu fonksiyonda toplanacak
        initImageLoader(); //GaleryFragment ve PhotoFragment tan Gelen fotoğrafların handle işlemleri

        //OnclickHandler..
        changePhoto.setOnClickListener(this);
        saveAll.setOnClickListener(this);


        /*else if (Permissions.requestPermission(this,Permissions.PERMISSIONS)){

            gatherViews(); // Tanimlamalar bu fonksiyonda toplanacak
            initImageLoader();
            UniversalImageLoader.setImage(denemeUrl, userProfile_image, null, "https://");
            //append parametresi http daha sonra file filan kullanılacak o yüzden
            changePhoto.setOnClickListener(this);
        }*/

    }


    public void initImageLoader() {
        Log.d(TAG, "initImageLoader: Image Yükleniyor..");
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(getApplicationContext());
        ImageLoader.getInstance().init(universalImageLoader.configuration());
        intent = getIntent();

        if (intent.hasExtra("imageFromCam")) {
            newPhotoUri = intent.getParcelableExtra("imageFromCam");
            Log.d(TAG, "initImageLoader: IMAGEFROMCAM" + newPhotoUri);
            UniversalImageLoader.setImage(newPhotoUri.toString(), userProfile_image, null, "");
        } else if (intent.hasExtra("newUserImagefromGalery")) {

            newPhotoUrl = intent.getStringExtra("newUserImagefromGalery");
            //newPhotoUri = Uri.fromFile(new File(newPhotoUrl));//Burada fotografın dosya yolunuda alıyorum
            Log.d(TAG, "initImageLoader: newUserImagefromGalery" + "\t" + newPhotoUrl);
            UniversalImageLoader.setImage(newPhotoUrl, userProfile_image, null, "file:/");

        } else if (intent.hasExtra("newUserImagefromGaleryUri")) {

            newPhotoUri = intent.getParcelableExtra("newUserImagefromGaleryUri");
            Log.d(TAG, "initImageLoader: newUserImagefromGaleryUri" + newPhotoUri);
            UniversalImageLoader.setImage(newPhotoUri.toString(), userProfile_image, null, "");

        }


    }

    public void gatherViews() {

        Log.d(TAG, "gatherViews widget tanımlamaları yapılıyor.");

        userProfile_image = findViewById(R.id.userProfile_image);
        editAboutContent = findViewById(R.id.editAbout_content);
        recyclerView = findViewById(R.id.deneyimList);
        recyclerViewEgitim = findViewById(R.id.egitimList);
        recyclerViewYetenek = findViewById(R.id.yetenekList);
        denemeList = new ArrayList<deneyimModel>();
        egitimListe = new ArrayList<egitimListModel>();
        recyclerYetenekList = new ArrayList<yetenekModel>();

        tvTel = findViewById(R.id.tvTel);
        tvMail = findViewById(R.id.tvMail);
        tvDogumTarih = findViewById(R.id.tvDogumTarih);
        tvEhliyet = findViewById(R.id.tvEhliyet);
        tvAskerlik = findViewById(R.id.tvAskerlik);
        changePhoto = findViewById(R.id.changePhoto);
        empty_message = findViewById(R.id.empty_message);
        empty_message1 = findViewById(R.id.empty_message1);
        empty_message2 = findViewById(R.id.empty_message2);
        saveAll = findViewById(R.id.saveAll);

    }

    /**
     * HakkımdaDüzenleme tıklandıgında
     *
     * @param view
     */
    public void editAbout(View view) {
        //Buraya Tıklandıgında bir tane Diaglog Fragment Olusturup icerisine edit about xml yükleyecem
        Log.d(TAG, "editAboutClick");
        FragmentManager fragment = getFragmentManager();
        editAboutFragment aboutDialog = new editAboutFragment();
        aboutDialog.show(fragment, "EditAboutFragment");


    }

    /**
     * Deneyim Ekleme tıklandıgında
     *
     * @param view
     */
    public void addDeneyim(View view) {
        //Buraya Tıklandıgında bir tane Diaglog Fragment Olusturup icerisine edit deneyim xml yükleyecem
        Log.d(TAG, "addDeneyim");
        FragmentManager fragment2 = getFragmentManager();
        editExperienceFragment expDialog = new editExperienceFragment();
        expDialog.show(fragment2, "EditDeneyimContent");


    }

    /**
     * Eğitim Ekleme Tıklandıgıında
     *
     * @param view
     */
    public void editEgitim(View view) {
        //Buraya Tıklandıgında bir tane Diaglog Fragment Olusturup icerisine edit deneyim xml yükleyecem
        Log.d(TAG, "editEgitimOnClikc");
        FragmentManager fragment3 = getFragmentManager();
        editEgitimFragment egtmDialog = new editEgitimFragment();
        egtmDialog.show(fragment3, "EditEgitimFragment");


    }

    /**
     * Yeteneke Ekleme Tıklandıgında
     *
     * @param view-->addYetenek
     */
    public void addYetenek(View view) {

        Log.d(TAG, "addYetenek Dialog Open");

        FragmentManager fragment3 = getFragmentManager();
        addYetenekFragment maddYetenekdialog = new addYetenekFragment();
        //maddYetenekdialog.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        maddYetenekdialog.show(fragment3, "AddYetenekFragment");


    }

    /**
     * Genel Bilgiler Düzenleme
     *
     * @param view XML View
     */
    public void addGenel(View view) {
        Log.d(TAG, "addGEnel Dialog Open");

        FragmentManager fragment4 = getFragmentManager();
        editGenelBilgiFragment editGenelBilgiFragment = new editGenelBilgiFragment();
        editGenelBilgiFragment.show(fragment4, "EditGenelbilgiFrag");


    }

    /**
     * DialogFragmentten Gelen bilgiler listeye dolduruluyor
     *
     * @param pz
     * @param loc
     * @param ay
     * @param krm
     */
    @Override
    public void getExperienceContent(String pz, String loc, String ay, String krm) {
        //mdeneyimModel=new deneyimModel();
//        mdeneyimModel.setPozisyon(pz);
//        mdeneyimModel.setLokasyon(loc);
//        mdeneyimModel.setAy(ay);
//        mdeneyimModel.setKurum(krm);

        denemeList.add(new deneyimModel(pz, krm, loc, ay));
        Log.d(TAG, "interfaceden gelen Deneyim : " + pz + loc);
        mdeneyimListAdapter = new deneyimListAdapter(this, denemeList);

        recyclerView.setAdapter(mdeneyimListAdapter);
        if (mdeneyimListAdapter.getItemCount() == 0) {

            recyclerView.setVisibility(View.GONE);
            empty_message.setVisibility(View.VISIBLE);

        }
        empty_message.setVisibility(View.INVISIBLE);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);


    }

    /**
     * Hakkımda Fragmentten gelen bilgiler
     *
     * @param input
     */
    @Override
    public void sendAboutContent(String input) {
        Log.d(TAG, "interfaceden gelen about : " + input);

        editAboutContent.setText(input);

    }


    @Override
    public void getEgitimContent(String okul, String bolum, String ogrenimTuru, String bsYılı, String btsYılı) {


        //Egitim List Dolduruluyor
        Log.d(TAG, "interfaceden gelen egitimList : " + okul + bolum + ogrenimTuru + bsYılı + btsYılı);
        egitimListe.add(new egitimListModel(okul, bolum, ogrenimTuru, bsYılı, btsYılı));
        megitimListAdapter = new egitimListAdapter(this, egitimListe);
        recyclerViewEgitim.setAdapter(megitimListAdapter);

        if (megitimListAdapter.getItemCount() == 0) {

            recyclerViewEgitim.setVisibility(View.GONE);
            empty_message1.setVisibility(View.VISIBLE);

        }
        empty_message1.setVisibility(View.INVISIBLE);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewEgitim.setLayoutManager(linearLayoutManager1);
        recyclerViewEgitim.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEgitim.setHasFixedSize(true);

    }

    @Override
    public void getYetenekContent(String name, int rate) {
        //Yetenek List Doldurma Fragment tan alınan değerler listeye dolduruluyor.
        //
        Log.d(TAG, "interfaceden gelen yetenekList : " + name + "  " + rate);

        recyclerYetenekList.add(new yetenekModel(name, rate));
        myetenekListAdapter = new yetenekListAdapter(this, recyclerYetenekList);

        recyclerViewYetenek.setAdapter(myetenekListAdapter);

        if (myetenekListAdapter.getItemCount() == 0) {

            recyclerViewYetenek.setVisibility(View.GONE);
            empty_message2.setVisibility(View.VISIBLE);

        }
        empty_message2.setVisibility(View.INVISIBLE);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewYetenek.setLayoutManager(linearLayoutManager2);
        recyclerViewYetenek.setItemAnimator(new DefaultItemAnimator());
        recyclerViewYetenek.setHasFixedSize(true);


    }

    @Override
    public void getGenelContent(String tel, String mail, String tarih, String ehliyet, String askerlik) {
        Log.d(TAG, "getGenel Content İnterfaceden Gelen ");
        tvTel.setText(tel);
        tvMail.setText(mail);
        tvDogumTarih.setText(tarih);
        tvEhliyet.setText(ehliyet);
        tvAskerlik.setText(askerlik);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.changePhoto:

                Intent intent = new Intent(this, PhotoActivity.class);
                startActivity(intent);
                break;

            case R.id.saveAll:

                saveAllchangesProfile();

        }


    }

    private void saveAllchangesProfile() {
        Log.d(TAG, "saveAllchangesProfile: @@@@DEĞİŞİKLER KAYIT EDİLİYOR ");




    }


}
