package com.example.harun.getjob.profileModel;

/**
 * Created by mayne on 23.02.2018.
 */

public class yetenekModel {

    private static final String TAG = "yetenekModel";
    private String yetenekName;
    private int rate;


//    public HashMap<String, ArrayList<yetenekModel>> yetenekHash = new HashMap<>();
//    public ArrayList<yetenekModel> hashyetenek = new ArrayList<>();


    public yetenekModel() {

    }

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




//    public HashMap<String, ArrayList<yetenekModel>> hashmapping(String position, yetenekModel model) {
//        hashyetenek.add(model);
//        yetenekHash.put(position, hashyetenek);
//
//   /*     for (yetenekModel mode : hashyetenek) {
//
//            Log.d("FOR ", "hashmapping: " + mode.getYetenekName() + "\t" + mode.getRate() + "\t" + model.getYetenekName());
//
//        }
//        Log.d("HASHMAPPİNGG ", "hashmapping: " + yetenekHash.entrySet());*/
//
//
//        //yetenekHash1 = yetenekHash;
//
//
//        return yetenekHash;
//    }

}
