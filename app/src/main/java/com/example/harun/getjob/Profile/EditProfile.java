package com.example.harun.getjob.Profile;

import android.app.FragmentManager;
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

import java.util.ArrayList;

public class EditProfile extends AppCompatActivity implements contentFragment {

    ArrayList<deneyimModel> denemeList;
    public TextView editAboutContent;
    private static final String TAG = "EditProfile";
    RecyclerView recyclerView;

    ImageView editDeneyim_image,editEgitim_image;
    deneyimListAdapter mdeneyimListAdapter;

    RecyclerView recyclerViewEgitim;
    ArrayList<egitimListModel> egitimListe;
    egitimListAdapter megitimListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        gatherViews(); // Tanimlamalar bu fonksiyonda toplanacak

        Log.d(TAG,"OnCreate Calıstı");




    }


    public void gatherViews() {

        Log.d(TAG,"gatherViews");
        editAboutContent = findViewById(R.id.editAbout_content);
        recyclerView = findViewById(R.id.deneyimList);
        recyclerViewEgitim = findViewById(R.id.egitimList);
        editDeneyim_image = findViewById(R.id.addDeneyim);
        editEgitim_image = findViewById(R.id.editEgitim_id);

        denemeList = new ArrayList<deneyimModel>();
        egitimListe=new ArrayList<egitimListModel>();
    }


    public void editAbout(View view) {
        //Buraya Tıklandıgında bir tane Diaglog Fragment Olusturup icerisine edit about xml yükleyecem
        Log.d(TAG,"editAboutClick");
        FragmentManager fragment = getFragmentManager();
        editAboutFragment aboutDialog = new editAboutFragment();
        aboutDialog.show(fragment, "EditAboutFragment");


    }

    public void addDeneyim(View view) {
        //Buraya Tıklandıgında bir tane Diaglog Fragment Olusturup icerisine edit deneyim xml yükleyecem
        Log.d(TAG,"addDeneyim");
        FragmentManager fragment2 = getFragmentManager();
        editExperienceFragment expDialog = new editExperienceFragment();
        expDialog.show(fragment2, "EditDeneyimContent");



    }
    public void editEgitim(View view) {
        //Buraya Tıklandıgında bir tane Diaglog Fragment Olusturup icerisine edit deneyim xml yükleyecem
        Log.d(TAG,"editEgitimOnClikc");
        FragmentManager fragment3 = getFragmentManager();
        editEgitimFragment egtmDialog = new editEgitimFragment();
        egtmDialog.show(fragment3, "EditEgitimFragment");


    }


    @Override
    public void getExperienceContent(String pz, String loc, String ay, String krm) {
        //mdeneyimModel=new deneyimModel();
//        mdeneyimModel.setPozisyon(pz);
//        mdeneyimModel.setLokasyon(loc);
//        mdeneyimModel.setAy(ay);
//        mdeneyimModel.setKurum(krm);

        denemeList.add(new deneyimModel(pz,krm,loc,ay));
        Log.d(TAG, "interfaceden gelen Deneyim : " + pz+loc);
        mdeneyimListAdapter = new deneyimListAdapter(this,denemeList);
        recyclerView.setAdapter(mdeneyimListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

    }

    @Override
    public void sendAboutContent(String input) {
        Log.d(TAG, "interfaceden gelen about : " + input);

        editAboutContent.setText(input);

    }

    @Override
    public void getEgitimContent(String okul, String bolum, String ogrenimTuru, String bsYılı, String btsYılı) {

        Log.d(TAG, "interfaceden gelen egitimList : " + okul+bolum+ogrenimTuru+bsYılı+btsYılı);
        egitimListe.add(new egitimListModel(okul,bolum,ogrenimTuru,bsYılı,btsYılı));
        megitimListAdapter=new egitimListAdapter(this,egitimListe);

        recyclerViewEgitim.setAdapter(megitimListAdapter);
        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewEgitim.setLayoutManager(linearLayoutManager1);
        recyclerViewEgitim.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEgitim.setHasFixedSize(true);

    }
}
