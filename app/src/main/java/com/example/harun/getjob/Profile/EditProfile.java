package com.example.harun.getjob.Profile;

import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harun.getjob.FirebaseMethods.FirebaseMethods;
import com.example.harun.getjob.FirebaseMethods.firebaseContent;
import com.example.harun.getjob.R;
import com.example.harun.getjob.profileModel.AllModelsList;
import com.example.harun.getjob.profileModel.deneyimListAdapter;
import com.example.harun.getjob.profileModel.deneyimModel;
import com.example.harun.getjob.profileModel.egitimListAdapter;
import com.example.harun.getjob.profileModel.egitimListModel;
import com.example.harun.getjob.profileModel.genelBilgiModel;
import com.example.harun.getjob.profileModel.yetenekListAdapter;
import com.example.harun.getjob.profileModel.yetenekModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
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

    AllModelsList mAllModelsList;
    contentFragment mcontentFragment;
    ImageView changePhotoImage;
    public TextView tvTel,
            tvMail,
            tvDogumTarih,
            tvEhliyet,
            tvAskerlik,
            changePhoto,
            empty_message,
            empty_message1,
            empty_message2;


    //isim,job,lokasyon

    EditText edituserName, editJob, editLocation;

    yetenekModel myetenekModel;

    // String denemeUrl = "www.shareicon.net/data/128x128/2016/09/15/829473_man_512x512.png";
    String newPhotoUrl;
    Intent intent;
    Uri newPhotoUri;

    firebaseContent mfirebaseContent;
    FirebaseMethods mFirebaseMethods;
    genelBilgiModel mgenelBilgilerim;

    boolean checkAbout = false,
            checkEgitim = false,
            checkExperience = false,
            checkPhoto = false,
            checkGenel = false,
            checkYetenek = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate Calıstı");
        setContentView(R.layout.edit_profile);

        //   if (Permissions.checkPermissionArray(this,Permissions.PERMISSIONS)) {

        gatherViews();       // Tanimlamalar bu fonksiyonda toplanacak
        initFromDatabaseItem();
        initImageLoader();   //GaleryFragment ve PhotoFragment tan Gelen fotoğrafların handle işlemleri


        //OnclickHandler..
        changePhoto.setOnClickListener(this);
        changePhotoImage.setOnClickListener(this);
        saveAll.setOnClickListener(this);


        /*else if (Permissions.requestPermission(this,Permissions.PERMISSIONS)){

            gatherViews(); // Tanimlamalar bu fonksiyonda toplanacak
            initImageLoader();
            UniversalImageLoader.setImage(denemeUrl, userProfile_image, null, "https://");
            //append parametresi http daha sonra file filan kullanılacak o yüzden
            changePhoto.setOnClickListener(this);
        }*/

    }

    private void initFromDatabaseItem() {

        Log.d(TAG, "initFromDatabaseItem: İLKLEMELER YAPILIYOR");
        intent = getIntent();

        mAllModelsList = intent.getParcelableExtra("AllItems");

        if (mAllModelsList != null) {


            Log.d(TAG, "initFromDatabaseItem:\t " + mAllModelsList.getAbout_me() + "\t" + mAllModelsList.getMfirebaseContent().getName());
            editAboutContent.setText(mAllModelsList.getAbout_me());
            edituserName.setText(mAllModelsList.getMfirebaseContent().getName());
            editJob.setText(mAllModelsList.getMfirebaseContent().getJob());
            editLocation.setText(mAllModelsList.getMfirebaseContent().getLocation());
            tvTel.setText(mAllModelsList.getMgenelBilgiModel().getPhone());
            tvMail.setText(mAllModelsList.getMgenelBilgiModel().getE_mail());
            tvDogumTarih.setText(mAllModelsList.getMgenelBilgiModel().getBirthday());
            tvEhliyet.setText(mAllModelsList.getMgenelBilgiModel().getEhliyet());
            tvAskerlik.setText(mAllModelsList.getMgenelBilgiModel().getAskerlik());
            newPhotoUrl = mAllModelsList.getProfilePhotoUrl();

            egitimListe = mAllModelsList.getMegitimListModel();
            denemeList = mAllModelsList.getMdeneyimListModel();
            recyclerYetenekList = mAllModelsList.getMyetenekListModel();

        /*Listeler Boş değilse adapter çağırılsın boşsa gerek yok .. */
            if (egitimListe.size() > 0) {
                setRecylerEgitim(egitimListe);
            }
            if (denemeList.size() > 0) {
                setRecylerDeneyim(denemeList);
            }
            if (recyclerYetenekList.size() > 0) {

                setRecylerYetenek(recyclerYetenekList);
            }

        } else
            Log.d(TAG, "initFromDatabaseItem: ALLMODELLİST NULL");
    }

    /**
     * GALERi Veya Cameradan Gelen Resim userProfilImage  olarak ayarlanıyor.
     */
    public void initImageLoader() {
        //Burada Databaseden gelen profil resmi önce konacak ..

        Log.d(TAG, "initImageLoader: Image Yükleniyor..");
        if (newPhotoUrl != null) {

            UniversalImageLoader universalImageLoader = new UniversalImageLoader(getApplicationContext());
            ImageLoader.getInstance().init(universalImageLoader.configuration());
            UniversalImageLoader.setImage(newPhotoUrl, userProfile_image, null, "");

        }
        intent = getIntent();

        if (intent.hasExtra("imageFromCam")) {
            newPhotoUri = intent.getParcelableExtra("imageFromCam");
            Log.d(TAG, "initImageLoader: IMAGEFROMCAM" + newPhotoUri);
            UniversalImageLoader.setImage(newPhotoUri.toString(), userProfile_image, null, "");
            checkPhoto = true;
        } else if (intent.hasExtra("newUserImagefromGalery")) {

            newPhotoUrl = intent.getStringExtra("newUserImagefromGalery");
            newPhotoUri = Uri.fromFile(new File(newPhotoUrl));//Burada fotografın dosya yolunuda alıyorum
            Log.d(TAG, "initImageLoader: newUserImagefromGalery" + "\t" + newPhotoUrl);
            UniversalImageLoader.setImage(newPhotoUrl, userProfile_image, null, "file:/");
            checkPhoto = true;
        } else if (intent.hasExtra("newUserImagefromGaleryUri")) {

            newPhotoUri = intent.getParcelableExtra("newUserImagefromGaleryUri");
            Log.d(TAG, "initImageLoader: newUserImagefromGaleryUri" + newPhotoUri);
            UniversalImageLoader.setImage(newPhotoUri.toString(), userProfile_image, null, "");
            checkPhoto = true;
        }


    }


    public void gatherViews() {

        Log.d(TAG, "gatherViews widget tanımlamaları yapılıyor.");

        edituserName = findViewById(R.id.edituserName);
        editJob = findViewById(R.id.editJob);
        editLocation = findViewById(R.id.editLocation);


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
        changePhotoImage = findViewById(R.id.changePhotoImage);
        mFirebaseMethods = new FirebaseMethods(getApplicationContext());


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
        EditAboutFragment aboutDialog = new EditAboutFragment();
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
        EditExperienceFragment expDialog = new EditExperienceFragment();
        expDialog.show(fragment2, "EditDeneyimContent");


    }

    /**
     * Eğitim Ekleme Tıklandıgıında
     *
     * @param view ->>  XMl deki + Butonu ..
     */
    public void editEgitim(View view) {
        //Buraya Tıklandıgında bir tane Diaglog Fragment Olusturup icerisine edit deneyim xml yükleyecem
        Log.d(TAG, "editEgitimOnClikc");
        FragmentManager fragment3 = getFragmentManager();
        EditEgitimFragment egtmDialog = new EditEgitimFragment();
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
        EditYetenekFragment maddYetenekdialog = new EditYetenekFragment();
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
        EditGenelBilgiFragment editGenelBilgiFragment = new EditGenelBilgiFragment();
        editGenelBilgiFragment.show(fragment4, "EditGenelbilgiFrag");


    }

    /**
     * DialogFragmentten Gelen bilgiler listeye dolduruluyor
     *
     * @param pz-->pozisyon
     * @param loc-->lokasyon
     * @param ay-->kaç       ay çalışıldı
     * @param krm-->nerede   çalışıldı
     */
    @Override
    public void getExperienceContent(String pz, String loc, String ay, String krm) {

        denemeList.add(new deneyimModel(pz, krm, loc, ay));
        Log.d(TAG, "interfaceden gelen Deneyim : " + pz + loc);
        setRecylerDeneyim(denemeList);
        checkExperience = true;//Değişiklik yapıldı


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
        checkAbout = true;

    }


    @Override
    public void getEgitimContent(String okul, String bolum, String ogrenimTuru, String bsYılı, String btsYılı) {

        //Egitim List Dolduruluyor
        Log.d(TAG, "interfaceden gelen egitimList : " + okul + bolum + ogrenimTuru + bsYılı + btsYılı);
        egitimListe.add(new egitimListModel(okul, bolum, ogrenimTuru, bsYılı, btsYılı));
        setRecylerEgitim(egitimListe);

        checkEgitim = true;

    }

    @Override
    public void getYetenekContent(String name, int rate) {
        //Yetenek List Doldurma Fragment tan alınan değerler listeye dolduruluyor.
        //
        Log.d(TAG, "interfaceden gelen yetenekList : " + name + "  " + rate);

        recyclerYetenekList.add(new yetenekModel(name, rate));
        setRecylerYetenek(recyclerYetenekList);
        checkYetenek = true;


    }

    @Override
    public void getGenelContent(String tel, String mail, String tarih, String ehliyet, String askerlik) {
        Log.d(TAG, "getGenel Content İnterfaceden Gelen ");


        tvTel.setText(tel);
        tvMail.setText(mail);
        tvDogumTarih.setText(tarih);
        tvEhliyet.setText(ehliyet);
        tvAskerlik.setText(askerlik);
        checkGenel = true;

        mgenelBilgilerim = new genelBilgiModel(

                tvTel.getText().toString()
                , tvMail.getText().toString()
                , tvDogumTarih.getText().toString()
                , tvEhliyet.getText().toString()
                , tvAskerlik.getText().toString()

        );

    }

    private void setRecylerYetenek(ArrayList<yetenekModel> _recyclerYetenekList) {
        Log.d(TAG, "setRecylerYetenek: ");
        myetenekListAdapter = new yetenekListAdapter(this, _recyclerYetenekList, false);

        recyclerViewYetenek.setAdapter(myetenekListAdapter);


        if (myetenekListAdapter.getItemCount() == 0) {

            Log.d(TAG, "setRecylerYetenek:getItemCount() " + myetenekListAdapter.getItemCount());
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

    private void setRecylerDeneyim(ArrayList<deneyimModel> _deneyimList) {
        Log.d(TAG, "setRecylerDeneyim: ");

        mdeneyimListAdapter = new deneyimListAdapter(this, _deneyimList, false);


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

    private void setRecylerEgitim(ArrayList<egitimListModel> _egitimListe) {
        Log.d(TAG, "setRecylerEgitim: DOlduruluyor" + _egitimListe);

        megitimListAdapter = new egitimListAdapter(this, _egitimListe, false);
        recyclerViewEgitim.setAdapter(megitimListAdapter);

        if (megitimListAdapter.getItemCount() == 0) {

            Log.d(TAG, "setRecylerEgitim: LİSTEBOŞ ");
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
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.changePhoto:

                Intent intent = new Intent(this, PhotoActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.saveAll:

                if (itemEmptyCheck()) {

                    SaveAllChanges allChanges = new SaveAllChanges();
                    allChanges.execute();
                    //saveAllchangesProfile();
                    break;
                }else{


                    Toast.makeText(this, "İsim Meslek Lokasyon Boş Olamaz", Toast.LENGTH_SHORT).show();
                    break;
                }


            case R.id.changePhotoImage:

                Intent intent2 = new Intent(this, PhotoActivity.class);
                startActivity(intent2);
                finish();
                break;


        }


    }

    /**
     * Önemli bilgilerin boş olmamamıs gerekli isim meslek lokasyon
     *
     * @return -->true Boş değil  false-->değişiklikleri kontrol ediniz
     */

    private boolean itemEmptyCheck() {

        //Boş olup olmadıgını kontrol ediyorum Gerekte yok ilerde değiştircem.
        if (!TextUtils.isEmpty(edituserName.getText().toString())
                && !TextUtils.isEmpty(editJob.getText().toString())
                && !TextUtils.isEmpty(editLocation.getText().toString())) {

            return true;
        }
        return false;
    }

    /**
     * Bu Class Arka planda işlemlerini Kayıt işlemlerini gerçekleştirmektedir.
     * profil üzerinde yapılmıs olaran değişikliler kayıt edilir
     * Değişikler check parametresi ile kontrol edilir
     * check Parametresi kullanıcı bir kayıt girdiğinde true olmaktadır.
     * Arka Plana almamın sebebi bazen Fotografı filan yükleme uzun sürüyor en azından  bi 10 sn filan
     * böyle yapınca kullanıcı işlemine devam edebilir.Buradaki sorun ise Güncelleme işleminin hemen sonrasında profilPage'in
     * yeni güncellemeyi almaması Çünkü direk profilPage açılıyor bu işlemler arka planda yapılırken...
     */

    private class SaveAllChanges extends AsyncTask<Void, Void, Void> {

        private static final String TAG = "SaveAllChanges";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* Permissions.showAlertdilaog(EditProfile.this, "Profiliniz Güncelleniyor ",
                    "Değişiklikleriniz kayıt ediliyor", 3000);*/

            Log.d(TAG, "onPreExecute:Çalışıyor ");

        }

        @Override
        protected Void doInBackground(Void... voids) {

            Log.d(TAG, "saveAllchangesProfile: @@@@DEĞİŞİKLER KAYIT EDİLİYOR ");

            mfirebaseContent = new firebaseContent(
                    editJob.getText().toString()
                    , editLocation.getText().toString()
                    , edituserName.getText().toString()
                    //   , "yok"

            );

            mFirebaseMethods.userProfile(mfirebaseContent);

            if (checkGenel) {
                mFirebaseMethods.addGeneralContent(mgenelBilgilerim, checkGenel);
                checkGenel = false;
            }

            if (checkYetenek) {
                mFirebaseMethods.addSkillList(myetenekListAdapter.getYetenekHash(), checkYetenek);
                checkYetenek = false;

            }
            if (checkAbout) {
                mFirebaseMethods.addAboutme(editAboutContent.getText().toString(), checkAbout);
                checkAbout = false;
            }
            if (checkEgitim) {
                mFirebaseMethods.addEducationlist(megitimListAdapter.getEgitimHashMap(), checkEgitim);
                checkEgitim = false;
            }
            if (checkExperience) {
                mFirebaseMethods.addExperienceList(mdeneyimListAdapter.getDeneyimHash(), checkExperience);
                checkExperience = false;
            }
            if (checkPhoto) {
                mFirebaseMethods.uploadProfileImages(newPhotoUri);
                checkPhoto = false;
            }

            publishProgress();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "onPostExecute: bitti activyty açılıyor.");
            Intent i = new Intent(EditProfile.this, profilPage.class);
            startActivity(i);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            finish();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }
    }
}
