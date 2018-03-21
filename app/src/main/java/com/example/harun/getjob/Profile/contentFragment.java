package com.example.harun.getjob.Profile;

import com.example.harun.getjob.profileModel.deneyimModel;
import com.example.harun.getjob.profileModel.egitimListModel;
import com.example.harun.getjob.profileModel.yetenekModel;

/**
 * Created by mayne on 16.02.2018.
 */

public interface contentFragment {


    void sendAboutContent(String input);

    void getEgitimContent(String okul, String bolum, String ogrenimTuru, String bsYılı, String btsYılı,boolean checkUpdate);
    void getExperienceContent(String pz, String loc, String ay, String krm,boolean checkUpdate);
    void getYetenekContent(String name, int rate,boolean checkUpdate);


    void getGenelContent(String tel, String mail, String tarih, String ehliyet, String askerlik);

    void UpdateDeneyimListItem(deneyimModel model, int position);

    void updateYetenekListItem(yetenekModel model, int position);
    void updateEgitimListItem(egitimListModel model, int position);


}
