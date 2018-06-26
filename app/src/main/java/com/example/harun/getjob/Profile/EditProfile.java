package com.example.harun.getjob.Profile;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.tapadoo.alerter.Alerter;

import java.io.File;
import java.util.ArrayList;

public class EditProfile extends AppCompatActivity implements contentFragment, View.OnClickListener {
    private static final String TAG = "EditProfile";
    ArrayList<deneyimModel> deneyimList;
    public TextView editAboutContent;

    RecyclerView recyclerViewDeneyim;
    RecyclerView recyclerViewEgitim;
    RecyclerView recyclerViewYetenek;
    deneyimListAdapter mdeneyimListAdapter;
    egitimListAdapter megitimListAdapter;
    yetenekListAdapter myetenekListAdapter;
    ArrayList<egitimListModel> egitimListe;
    ArrayList<yetenekModel> recyclerYetenekList;

    firebaseContent mfirebaseContent;
    FirebaseMethods mFirebaseMethods;
    genelBilgiModel mgenelBilgilerim;
    AllModelsList mAllModelsList;

    ImageView userProfile_image, saveAll, cancel_action, changePhotoImage;
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

    //  AVLoadingIndicatorView uploadProgress;
    // String denemeUrl = "www.shareicon.net/data/128x128/2016/09/15/829473_man_512x512.png";
    String newPhotoUrl;
    Intent intent;
    Uri newPhotoUri;

    boolean checkAbout = false,
            checkEgitim = false,
            checkExperience = false,
            checkPhoto = false,
            checkGenel = false,
            checkYetenek = false;

    private int getRowItemsPosition;
    Bundle getRowItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate Calıstı");
        setContentView(R.layout.edit_profile);


        //   if (Permissions.checkPermissionArray(this,Permissions.PERMISSIONS)) {

        gatherViews();       // Tanimlamalar bu fonksiyonda toplanacak
        initFromDatabaseItem();
        initImageFromDatabaseLoader();   //Firebase, fotoğraf handle işlemleri


        //OnclickHandler..
//        changePhoto.setOnClickListener(this);
        changePhotoImage.setOnClickListener(this);
        cancel_action.setOnClickListener(this);
        saveAll.setOnClickListener(this);


        /*else if (Permissions.requestPermission(this,Permissions.PERMISSIONS)){

            gatherViews(); // Tanimlamalar bu fonksiyonda toplanacak
            initImageLoader();
            UniversalImageLoader.setImage(denemeUrl, userProfile_image, null, "https://");
            //append parametresi http daha sonra file filan kullanılacak o yüzden
            changePhoto.setOnClickListener(this);
        }*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: TELEFONUN GERİ tusuna Basıldı");
        startActivity(new Intent(this, profilPage.class));
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }

    private void initFromDatabaseItem() {

        Log.d(TAG, "initFromDatabaseItem: İLKLEMELER YAPILIYOR");

        intent = getIntent();

        Log.d(TAG, "initFromDatabaseItem: ALLITEM KULLANILIYOR ");
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
            deneyimList = mAllModelsList.getMdeneyimListModel();
            recyclerYetenekList = mAllModelsList.getMyetenekListModel();

        /*  Listeler Boş değilse adapter çağırılsın boşsa gerek yok .. */
            if (egitimListe.size() > 0) {
                setRecylerEgitim(egitimListe);
            }
            if (deneyimList.size() > 0) {
                setRecylerDeneyim(deneyimList);
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
    public void initImageFromDatabaseLoader() {
        Log.d(TAG, "initImageLoader: Image Yükleniyor..");


        //Firebaseden gelene Fotograf urlsi
        if (newPhotoUrl != null) {

            UniversalImageLoader universalImageLoader = new UniversalImageLoader(getApplicationContext());
            ImageLoader.getInstance().init(universalImageLoader.configuration());
            UniversalImageLoader.setImage(newPhotoUrl, userProfile_image, null, "");

        }


    }

    /*
        edittextlerin focuslarını kaldırma
        Kullanıcı bu edittextlere tıklayıp işlemini bitirdikten sonra focuslarını kaldırıyorum.
     */
    public void clearFocus() {
        edituserName.clearFocus();
        editJob.clearFocus();
        editLocation.clearFocus();

    }

    public void gatherViews() {

        Log.d(TAG, "gatherViews widget tanımlamaları yapılıyor.");

        edituserName = findViewById(R.id.edituserName);
        editJob = findViewById(R.id.editJob);
        editLocation = findViewById(R.id.editLocation);


        userProfile_image = findViewById(R.id.userProfile_image);
        editAboutContent = findViewById(R.id.editAbout_content);
        recyclerViewDeneyim = findViewById(R.id.deneyimList);
        recyclerViewEgitim = findViewById(R.id.egitimList);
        recyclerViewYetenek = findViewById(R.id.yetenekList);


        tvTel = findViewById(R.id.tvTel);
        tvMail = findViewById(R.id.tvMail);
        tvDogumTarih = findViewById(R.id.tvDogumTarih);
        tvEhliyet = findViewById(R.id.tvEhliyet);
        tvAskerlik = findViewById(R.id.tvAskerlik);
        //changePhoto = findViewById(R.id.changePhoto);
        empty_message = findViewById(R.id.empty_message);
        empty_message1 = findViewById(R.id.empty_message1);
        empty_message2 = findViewById(R.id.empty_message2);
        saveAll = findViewById(R.id.saveAll);
        cancel_action = findViewById(R.id.cancel_action);
        changePhotoImage = findViewById(R.id.changePhotoImage);
        // yetenekLayout = findViewById(R.id.yetenekLayout);
        //uploadProgress=findViewById(R.id.uploadProgress);
        //uploadProgress.hide();

        init();

    }

    /**
     * widget ve değişken ilklemeleri
     */
    private void init() {
        deneyimList = new ArrayList<deneyimModel>();
        egitimListe = new ArrayList<egitimListModel>();
        recyclerYetenekList = new ArrayList<yetenekModel>();
        mFirebaseMethods = new FirebaseMethods(getApplicationContext());


    }

    /**
     * HakkımdaDüzenleme tıklandıgında
     *
     * @param view
     */
    public void editAbout(View view) {
        clearFocus();
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
        clearFocus();
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
        clearFocus();
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
        clearFocus();

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
        clearFocus();
        Log.d(TAG, "addGEnel Dialog Open");

        FragmentManager fragment4 = getFragmentManager();
        EditGenelBilgiFragment editGenelBilgiFragment = new EditGenelBilgiFragment();
        editGenelBilgiFragment.show(fragment4, "EditGenelbilgiFrag");


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

        profilPage.sendData.setMgenelBilgiModel(mgenelBilgilerim);

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
        profilPage.sendData.setAbout_me(input);
        checkAbout = true;

    }

    /***
     *
     * @param okul
     * @param bolum
     * @param ogrenimTuru
     * @param bsYılı
     * @param btsYılı
     * @param checkUpdate ->>
     *      update işlemi gerçekleştiğinde true olarak gelir ve liste öğesinde set ve notifyChange işlemi yapılır
     *      normal ekleme yapıldıgında false geldiğinde add işlemi yapılır

     */
    @Override
    public void getEgitimContent(String okul, String bolum, String ogrenimTuru, String bsYılı, String btsYılı, boolean checkUpdate) {

        //Egitim List Dolduruluyor
        if (checkUpdate) {
            Log.d(TAG, "getEgitimContent: UPDATE LİST ITEM " + okul + bolum);
            egitimListe.set(getRowItemsPosition, new egitimListModel(okul, bolum, ogrenimTuru, bsYılı, btsYılı));
            megitimListAdapter.notifyItemChanged(getRowItemsPosition);
            megitimListAdapter.notifyItemRangeChanged(0, egitimListe.size());
            setRecylerEgitim(egitimListe);

            profilPage.sendData.setMegitimListModel(egitimListe);
            checkEgitim = true;

        } else {

            Log.d(TAG, "interfaceden gelen egitimList : " + okul + bolum + ogrenimTuru + bsYılı + btsYılı);
            egitimListe.add(new egitimListModel(okul, bolum, ogrenimTuru, bsYılı, btsYılı));
            setRecylerEgitim(egitimListe);
            profilPage.sendData.setMegitimListModel(egitimListe);
            checkEgitim = true;
        }


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
    public void getExperienceContent(String pz, String loc, String ay, String krm, boolean checkUpdate) {

        if (checkUpdate) { //Bir update işlemi gerçekleştirildiğinde

            Log.d(TAG, "interfaceden gelen Değişiklik yapılan deneyim itemleri: " + pz + loc + getRowItemsPosition);
            deneyimList.set(getRowItemsPosition, new deneyimModel(pz, krm, loc, ay)); //Pozisyon değerinin güncelliyoruz.
            mdeneyimListAdapter.notifyItemRangeChanged(0, deneyimList.size());
            mdeneyimListAdapter.notifyItemChanged(getRowItemsPosition);
//            Log.d(TAG, "getExperienceContent: " + denemeList.size());
            setRecylerDeneyim(deneyimList);
            checkExperience = true;//Değişiklik yapıldı
        } else {

            deneyimList.add(new deneyimModel(pz, krm, loc, ay));
            Log.d(TAG, "interfaceden gelen Deneyim : " + pz + loc);
            setRecylerDeneyim(deneyimList);
            checkExperience = true;//Değişiklik yapıldı
        }


    }


    @Override
    public void getYetenekContent(String name, int rate, boolean checkUpdate) {
        //Yetenek List Doldurma Fragment tan alınan değerler listeye dolduruluyor.
        //
        Log.d(TAG, "interfaceden gelen yetenekList : " + name + "  " + rate);
        if (checkUpdate) {
            Log.d(TAG, "getYetenekContent: UPDATE YETENEK ");
            recyclerYetenekList.set(getRowItemsPosition, new yetenekModel(name, rate));
            myetenekListAdapter.notifyItemChanged(getRowItemsPosition);
            myetenekListAdapter.notifyItemRangeChanged(0, recyclerYetenekList.size());
            setRecylerYetenek(recyclerYetenekList);
            checkYetenek = true;
        } else {
            recyclerYetenekList.add(new yetenekModel(name, rate));
            setRecylerYetenek(recyclerYetenekList);
            checkYetenek = true;

        }
    }


    /**
     * Bu fonksiyon interface aracılıgı ile implement edildi.Kullanıcı bir liste ögesini düzenlemek için tıklaıdıgında bu fonksiton
     * tetiklenecek.Burada diaalog yeniden açılarak düzeneleme  işlemini yapabilecek.
     *
     * @param model-->Tıklanan    liste ögesinin tüm elemanları->>bundle yapısı ile fragmenta aktarılacak
     * @param position-->tıklanan liste ögesinin pozisyon değeri-->pozisyon değişkeni yerel bir değişkene atanıp kullanılacak..
     */
    @Override
    public void UpdateDeneyimListItem(deneyimModel model, int position) {

        Log.d(TAG, "UpdateDeneyimListItem: " + model + "\t " + position);
        //Log.d(TAG, "UpdateDeneyimListItem: Silinmeden önce " + denemeList);

        getRowItems = new Bundle();
        getRowItems.putParcelable("deneyimSatir", model); //Fragmenta gönderilen satır itemleri
        getRowItemsPosition = position;
        //denemeList.remove(position)
        FragmentManager fragment3 = getFragmentManager();
        EditExperienceFragment expDialog1 = new EditExperienceFragment();
        expDialog1.setArguments(getRowItems);
        expDialog1.show(fragment3, "UpdateDeneyimListItem");
    }

    @Override
    public void updateYetenekListItem(yetenekModel model, int position) {
        Log.d(TAG, "updateYetenekListItem: " + model + position);

        getRowItems = new Bundle();

        getRowItems.putParcelable("yetenekSatir", model);
        getRowItemsPosition = position;

        FragmentManager fragment = getFragmentManager();
        EditYetenekFragment editYetenekFragment = new EditYetenekFragment();
        editYetenekFragment.setArguments(getRowItems);
        editYetenekFragment.show(fragment, "updateFragment");

    }

    @Override
    public void updateEgitimListItem(egitimListModel model, int position) {
        Log.d(TAG, "updateEgitimListItem: ");
        getRowItemsPosition = position;
        getRowItems = new Bundle();

        getRowItems.putParcelable("updateEgitimList", model);

        FragmentManager fragment = getFragmentManager();
        EditEgitimFragment editEgitimFragment = new EditEgitimFragment();
        editEgitimFragment.setArguments(getRowItems);
        editEgitimFragment.show(fragment, "updateEgitimFragment");


    }


    private void setRecylerYetenek(final ArrayList<yetenekModel> _recyclerYetenekList) {
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

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        // Drawable verticalDivider =
//        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.divider));
        // dividerItemDecoration.getItemOffsets(new RecyclerView.ItemDecoration(o));
        recyclerViewYetenek.setItemAnimator(new DefaultItemAnimator());
        recyclerViewYetenek.setHasFixedSize(true);
        recyclerViewYetenek.addItemDecoration(dividerItemDecoration);

        //Liste Öğesi Sağa kaydırıldıgında Delete İşlemini gerçekleştirecek.

        ItemTouchHelper.SimpleCallback swipeYetenekList = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;
            }


            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    final View yetenekForeground = ((yetenekListAdapter.MyViewHolder) viewHolder).yetenekForeground;

                    getDefaultUIUtil().onSelected(yetenekForeground);
                }
            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                final View yetenekForeground = ((yetenekListAdapter.MyViewHolder) viewHolder).yetenekForeground;

                getDefaultUIUtil().onDrawOver(c, recyclerView, yetenekForeground, dX, dY, actionState, isCurrentlyActive);

                //   super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                final View yetenekForeground = ((yetenekListAdapter.MyViewHolder) viewHolder).yetenekForeground;

                getDefaultUIUtil().onDraw(c, recyclerView, yetenekForeground, dX, dY, actionState, isCurrentlyActive);

                //  super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                final View yetenekForeground = ((yetenekListAdapter.MyViewHolder) viewHolder).yetenekForeground;

                getDefaultUIUtil().clearView(yetenekForeground);

            }

            @Override
            public int convertToAbsoluteDirection(int flags, int layoutDirection) {
                return super.convertToAbsoluteDirection(flags, layoutDirection);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d(TAG, "onSwiped: ");


                int yetenekListPosition = viewHolder.getAdapterPosition();

                _recyclerYetenekList.remove(yetenekListPosition);

                myetenekListAdapter.removeItem(yetenekListPosition, _recyclerYetenekList);


                Alerter alertDialog = Permissions.showAlertdilaog(EditProfile.this, "Yetenek Silindi",
                        "", 1000);


                alertDialog.show();
                profilPage.sendData.setMyetenekListModel(_recyclerYetenekList);


                checkYetenek = true;
                //  yetenekLayout.forceLayout();

            }


        };

        ItemTouchHelper swipeYetenek = new ItemTouchHelper(swipeYetenekList);
        swipeYetenek.attachToRecyclerView(recyclerViewYetenek);


    }


    private void setRecylerDeneyim(final ArrayList<deneyimModel> _deneyimList) {
        Log.d(TAG, "setRecylerDeneyim: ");

        mdeneyimListAdapter = new deneyimListAdapter(this, _deneyimList, false);


        recyclerViewDeneyim.setAdapter(mdeneyimListAdapter);
        //Liste Boş ise empty Mesaj Devreye girecek.
        if (mdeneyimListAdapter.getItemCount() == 0) {

            recyclerViewDeneyim.setVisibility(View.GONE);
            empty_message.setVisibility(View.VISIBLE);

        }
        empty_message.setVisibility(View.INVISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewDeneyim.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerViewDeneyim.addItemDecoration(dividerItemDecoration);
        recyclerViewDeneyim.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDeneyim.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback swipeDelete = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;


            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                //super.onSelectedChanged(viewHolder, actionState);

                if (viewHolder != null) {
                    final View deneyimView_foreground = ((deneyimListAdapter.MyViewHolder) viewHolder).deneyimView_foreground;

                    getDefaultUIUtil().onSelected(deneyimView_foreground);
                }

            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                        int actionState, boolean isCurrentlyActive) {
                final View deneyimView_foreground = ((deneyimListAdapter.MyViewHolder) viewHolder).deneyimView_foreground;

                getDefaultUIUtil().onDrawOver(c, recyclerView, deneyimView_foreground, dX, dY, actionState, isCurrentlyActive);

            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final View deneyimView_foreground = ((deneyimListAdapter.MyViewHolder) viewHolder).deneyimView_foreground;

                getDefaultUIUtil().clearView(deneyimView_foreground);

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                //super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                final View deleteView = ((deneyimListAdapter.MyViewHolder) viewHolder).deneyimView_foreground;

                getDefaultUIUtil().onDraw(c, recyclerView, deleteView, dX, dY, actionState, isCurrentlyActive);


            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d(TAG, "onSwiped: ");
                int deneyimListPosition = viewHolder.getAdapterPosition();
                //Log.d(TAG, "onSwiped: SİLİNMEDEN ÖNCE " + _deneyimList + "\t" + "SİLİNECEK POZİYON " + position);
                if (viewHolder instanceof deneyimListAdapter.MyViewHolder) {

                    //Kullanıcı Sağa kaydırdığında
                    /**
                     * 1 ->Pozisyondaki itemi listeden Silmen lazım
                     * 2->Hashmapi temizlemen lazım Ardından burda yeni oluşan listeyi gönderip hashlenecek
                     * 3->deneyimListAdaptere itemin kaldırıldığını söylemen laızm notifyItemRemoveds
                     *
                     *
                     *
                     */

                    _deneyimList.remove(deneyimListPosition);
                    // Log.d(TAG, "onSwiped: POZİYON SİLİNDİ");
                    mdeneyimListAdapter.removeItem(deneyimListPosition, _deneyimList);
                    // Log.d(TAG, "onSwiped:SİLİNDİKTEN SONRA  " + _deneyimList + position);
//                    Toast.makeText(EditProfile.this, "İtem Silindi " + denemeList.get(position), Toast.LENGTH_LONG).show();
                    // mdeneyimListAdapter.notifyItemRemoved(position);
                    // mdeneyimListAdapter.notifyItemRangeChanged(0, _deneyimList.size());
                    Alerter alertDialog = Permissions.showAlertdilaog(EditProfile.this, "Deneyim Silindi",
                            "", 1000);


                    alertDialog.show();
                    profilPage.sendData.setMdeneyimListModel(_deneyimList); //Direk güncellemesi için buna eşitliyorum.
                    checkExperience = true;
                    //  Log.d(TAG, "onSwiped: NOTİFYITEMRANGECHANGE" + _deneyimList + "\t" + _deneyimList.size());
                }


            }

            @Override
            public int convertToAbsoluteDirection(int flags, int layoutDirection) {
                return super.convertToAbsoluteDirection(flags, layoutDirection);
            }


        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeDelete);
        itemTouchHelper.attachToRecyclerView(recyclerViewDeneyim);
    }

    private void setRecylerEgitim(final ArrayList<egitimListModel> _egitimListe) {
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
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerViewEgitim.addItemDecoration(dividerItemDecoration);
        recyclerViewEgitim.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEgitim.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback swipeEgitimList = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    final View egitim_foreground = ((egitimListAdapter.ViewHolder) viewHolder).egitim_foreground;

                    getDefaultUIUtil().onSelected(egitim_foreground);
                }
            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                final View egitim_foreground = ((egitimListAdapter.ViewHolder) viewHolder).egitim_foreground;

                getDefaultUIUtil().onDrawOver(c, recyclerView, egitim_foreground, dX, dY, actionState, isCurrentlyActive);

                //   super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                final View egitim_foreground = ((egitimListAdapter.ViewHolder) viewHolder).egitim_foreground;

                getDefaultUIUtil().onDraw(c, recyclerView, egitim_foreground, dX, dY, actionState, isCurrentlyActive);

                //  super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                final View egitim_foreground = ((egitimListAdapter.ViewHolder) viewHolder).egitim_foreground;

                getDefaultUIUtil().clearView(egitim_foreground);

            }

            @Override
            public int convertToAbsoluteDirection(int flags, int layoutDirection) {
                return super.convertToAbsoluteDirection(flags, layoutDirection);
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                Log.d(TAG, "onSwiped: ");
                int egitimListposition = viewHolder.getAdapterPosition();
                _egitimListe.remove(egitimListposition);

                megitimListAdapter.removeItem(egitimListposition, _egitimListe);

                Alerter alertDialog = Permissions.showAlertdilaog(EditProfile.this, "Eğitim Bilgisi Silindi.",
                        "", 1000);


                alertDialog.show();
                profilPage.sendData.setMegitimListModel(_egitimListe);
                checkEgitim = true; // Listede Değişiklik Yapıldı


            }
        };
        ItemTouchHelper egitimTouchHelper = new ItemTouchHelper(swipeEgitimList);
        egitimTouchHelper.attachToRecyclerView(recyclerViewEgitim);


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
           /* case R.id.changePhoto:

                //startActivityForResult
                Intent intent = new Intent(this, PhotoActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;*/

            case R.id.saveAll:

                if (itemEmptyCheck()) {
                    //uploadProgress.show();
                    SaveAllChanges allChanges = new SaveAllChanges();
                    allChanges.execute();
                    //saveAllchangesProfile();
                    break;
                } else {


                    Toast.makeText(this, "İsim Meslek Lokasyon Boş Olamaz", Toast.LENGTH_SHORT).show();
                    break;
                }


            case R.id.changePhotoImage:

                Intent intent2 = new Intent(this, PhotoActivity.class);
                startActivityForResult(intent2, 1);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;

            case R.id.cancel_action:

                Intent intent3 = new Intent(this, profilPage.class);
                startActivity(intent3);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
                break;

        }


    }

    /**
     * Photo Activityden gelen dataların yakalanması işlemi için oluşturuldu.
     *
     * @param requestCode-->1->PhotoActivty
     * @param resultCode->Resultok-ResultCancel
     * @param data->photo                       uri
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //  super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: RESULT OK" + data);

                //data = getIntent();
                //GaleryFragment ve PhotoFragment tan Gelen Kullanıcının  seçtiği fotoğraf

                if (data.hasExtra("imageFromCam")) {
                    newPhotoUri = data.getParcelableExtra("imageFromCam");
                    Log.d(TAG, "initImageLoader: IMAGEFROMCAM" + newPhotoUri);
                    UniversalImageLoader.setImage(newPhotoUri.toString(), userProfile_image, null, "");
                    checkPhoto = true;
                } else if (data.hasExtra("newUserImagefromGalery")) {

                    newPhotoUrl = data.getStringExtra("newUserImagefromGalery");
                    newPhotoUri = Uri.fromFile(new File(newPhotoUrl));//Burada fotografın dosya yolunuda alıyorum
                    Log.d(TAG, "initImageLoader: newUserImagefromGalery" + "\t" + newPhotoUrl);
                    UniversalImageLoader.setImage(newPhotoUrl, userProfile_image, null, "file:/");
                    checkPhoto = true;
                } else if (data.hasExtra("newUserImagefromGaleryUri")) {

                    newPhotoUri = data.getParcelableExtra("newUserImagefromGaleryUri");
                    Log.d(TAG, "initImageLoader: newUserImagefromGaleryUri" + newPhotoUri);
                    UniversalImageLoader.setImage(newPhotoUri.toString(), userProfile_image, null, "");
                    checkPhoto = true;
                }


            }
            if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "onActivityResult:RESULTCANCELED ");

            }


        }

    }

    /**
     * Önemli bilgilerin boş olmaması gerekli isim meslek lokasyon
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
     * Bu Class Arka planda  Kayıt işlemlerini gerçekleştirmektedir.
     * profil üzerinde yapılmıs olaran değişikliler kayıt edilir
     * Değişikler check parametresi ile kontrol edilird
     * check Parametresi kullanıcı bir kayıt girdiğinde true olmaktadır.
     * Arka Plana almamın sebebi bazen Fotografı filan yükleme uzun sürüyor en azından  bi 10 sn filan
     * böyle yapınca kullanıcı işlemine devam edebilir.
     */
////CHECK MEMORY LEAK ARKA PLANDA ÇALIŞIYOR AMA HAFIZADAN SİLİNMİYOR .. ..
    private class SaveAllChanges extends AsyncTask<Void, Void, Void> {

        private static final String TAG = "SaveAllChanges";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


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
            //   uploadProgress.hide();
            Log.d(TAG, "onPostExecute: bitti activyty açılıyor.");
            Intent i = new Intent(EditProfile.this, profilPage.class);
         //   ImageLoader.getInstance().destroy();
            startActivity(i);


            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            // ((profilPage)getApplicationContext()).profilimageProgress.show();
            finish();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }
    }
}
