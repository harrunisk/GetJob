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

import java.util.ArrayList;

public class EditProfile extends AppCompatActivity implements contentFragment {

    ArrayList<deneyimModel> denemeList;
    public TextView editAboutContent;
    private static final String TAG = "EditProfile";
    RecyclerView recyclerView;
    ImageView editDeneyim_image;
    deneyimListAdapter mdeneyimListAdapter;
    deneyimModel mdeneyimModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        gatherViews(); // Tanimlamalar bu fonksiyonda toplanacak

        mdeneyimListAdapter = new deneyimListAdapter(this,denemeList);
        recyclerView.setAdapter(mdeneyimListAdapter);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

    }


    public void gatherViews() {

        editAboutContent = findViewById(R.id.editAbout_content);
        recyclerView = findViewById(R.id.deneyimList);
        editDeneyim_image = findViewById(R.id.addDeneyim);
        denemeList = new ArrayList<deneyimModel>();

    }


    public void editAbout(View view) {
        //Buraya Tıklandıgında bir tane Diaglog Fragment Olusturup icerisine edit about xml yükleyecem

        FragmentManager fragment = getFragmentManager();
        editAboutFragment aboutDialog = new editAboutFragment();
        aboutDialog.show(fragment, "EditAboutFragment");


    }

    public void addDeneyim(View view) {
        //Buraya Tıklandıgında bir tane Diaglog Fragment Olusturup icerisine edit about xml yükleyecem

        FragmentManager fragment2 = getFragmentManager();
        editExperienceFragment expDialog = new editExperienceFragment();
        expDialog.show(fragment2, "EditDeneyimContent");


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

    }

    @Override
    public void sendAboutContent(String input) {
        Log.d(TAG, "interfaceden gelen about : " + input);

        editAboutContent.setText(input);

    }
}
