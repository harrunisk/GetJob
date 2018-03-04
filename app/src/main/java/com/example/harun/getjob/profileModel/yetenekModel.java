package com.example.harun.getjob.profileModel;

/**
 * Created by mayne on 23.02.2018.
 */

public class yetenekModel {

    String yetenekName;
    int rate;

    public yetenekModel(String yetenekName, int rate) {
        this.yetenekName = yetenekName;
        this.rate = rate;
    }

    public String getYetenekName() {
        return yetenekName;
    }

    public void setYetenekName(String yetenekName) {
        this.yetenekName = yetenekName;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
