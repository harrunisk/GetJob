package com.example.harun.getjob.Profile;

/**
 * Created by mayne on 16.02.2018.
 */

public interface contentFragment {
    
    void getExperienceContent(String pz, String loc, String ay, String krm);

    void sendAboutContent(String input);

    void getEgitimContent(String okul, String bolum, String ogrenimTuru, String bsYılı, String btsYılı);

    void getYetenekContent(String name, int rate);

    void getGenelContent(String tel, String mail,String tarih,String ehliyet,String askerlik);

}
