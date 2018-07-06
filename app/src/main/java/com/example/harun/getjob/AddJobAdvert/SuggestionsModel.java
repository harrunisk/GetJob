package com.example.harun.getjob.AddJobAdvert;

/**
 * Created by mayne on 28.06.2018.
 */

public class SuggestionsModel {

    //private  String sectorInfo;
    private String jobInfo;
    private boolean type;

    /* public SuggestionsModel(String sectorInfo, String jobInfo) {
         this.sectorInfo = sectorInfo;
         this.jobInfo = jobInfo;
     }
 */
    public SuggestionsModel(String jobInfo, boolean type) {
        this.jobInfo = jobInfo;
        this.type = type;
    }

    public String getType() {
/*switch (type){

    case 0:
        break;
    case 1:
    return "Basvurduğum ilanlara göre ";
    break;

}*/
        if (type) {

            return "Kayıtlı ilanlarıma göre ";

        } else {
            return "Basvurduğum ilanlara göre ";

        }

    }

    public void setType(boolean type) {
        this.type = type;
    }

    /* public String getSectorInfo() {
             return sectorInfo;
         }
     */
  /*  public void setSectorInfo(String sectorInfo) {
        this.sectorInfo = sectorInfo;
    }
*/
    public String getJobInfo() {

        return jobInfo;

    }

    public void setJobInfo(String jobInfo) {
        this.jobInfo = jobInfo;
    }


    @Override
    public String toString() {
        return "SuggestionsModel{" +
                "jobInfo='" + jobInfo + '\'' +
                ", type=" + type +
                '}';
    }
}
