package com.example.harun.getjob.profileModel;

/**
 * Created by mayne on 6.03.2018.
 */

public class genelBilgiModel {

   private String phone,e_mail,birthday,ehliyet,askerlik;

    public genelBilgiModel(String phone, String e_mail, String birthday, String ehliyet, String askerlik) {
        this.phone = phone;
        this.e_mail = e_mail;
        this.birthday = birthday;
        this.ehliyet = ehliyet;
        this.askerlik = askerlik;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEhliyet() {
        return ehliyet;
    }

    public void setEhliyet(String ehliyet) {
        this.ehliyet = ehliyet;
    }

    public String getAskerlik() {
        return askerlik;
    }

    public void setAskerlik(String askerlik) {
        this.askerlik = askerlik;
    }

    @Override
    public String toString() {
        return "genelBilgiModel{" +
                "phone='" + phone + '\'' +
                ", e_mail='" + e_mail + '\'' +
                ", birthday='" + birthday + '\'' +
                ", ehliyet='" + ehliyet + '\'' +
                ", askerlik='" + askerlik + '\'' +
                '}';
    }
}
