package com.example.harun.getjob.JobSearch.JobUtils;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import org.altervista.andrearosa.statebutton.StateButton;

import java.util.ArrayList;

/**
 * Created by mayne on 24.05.2018.
 */

public class NearJobAdvertModel extends JobAdvertModel2 implements ClusterItem, Parcelable {
    private static final String TAG = "NearJobAdvertModel";

    private boolean isSave;
    private boolean basvuruDurumu;
    private BitmapDescriptor markerIcon;
    private String newLocationDistance, companyDistance;
    private LatLng mCoord;
    private JobAdvertModel2 jobAdvertModel2;
    private String suggestType;


    public NearJobAdvertModel(
            String jobID,
            String companyName,
            String companyJob,
            String jobSector,
            String jobDescpriction,
            String companyLogoUrl,
            String companyAdress,
            String educationLevel,
            String expLevel,
            String employeeHour,
            String drivingLicence,
            String military,
            String gender,
            String publishDate,
            int countApply,
            ArrayList<String> jobPossibility,
            LatLng mPosition,
            boolean isSave,
            boolean basvuruDurumu,
            BitmapDescriptor markerIcon,
            String newLocationDistance, String companyDistance) {
        //Bunlar jobADvertModel2 'e
        // Diğerleride bu sınıfın değişkeni
        super(jobID, companyName, companyJob, jobSector, jobDescpriction,
                companyLogoUrl, companyAdress, educationLevel,
                expLevel, employeeHour, drivingLicence, military,
                gender, publishDate, countApply, jobPossibility,
                mPosition

        );

        this.isSave = isSave;
        this.basvuruDurumu = basvuruDurumu;
        this.markerIcon = markerIcon;
        this.newLocationDistance = newLocationDistance;
        this.companyDistance = companyDistance;
        setPosition(mPosition);
    }

    public NearJobAdvertModel(JobAdvertModel2 model2, String companyDistance, String suggestType) {
        super(model2.getJobID(), model2.getCompanyName(), model2.getCompanyJob(), model2.getJobSector(), model2.getJobDescpriction(),
                model2.getCompanyLogoUrl(), model2.getCompanyAdress(), model2.getEducationLevel(),
                model2.getExpLevel(), model2.getEmployeeHour(), model2.getDrivingLicence(), model2.getMilitary(),
                model2.getGender(), model2.getPublishDate(), model2.getCountApply(), model2.getJobPossibility(),
                model2.getmPosition());
        this.jobAdvertModel2 = model2;
        this.companyDistance = companyDistance;
        this.suggestType = suggestType;

    }

    protected NearJobAdvertModel(Parcel in) {
        super(in);
        isSave = in.readByte() != 0;
        basvuruDurumu = in.readByte() != 0;
        newLocationDistance = in.readString();
        companyDistance = in.readString();
        mCoord = in.readParcelable(LatLng.class.getClassLoader());
        jobAdvertModel2 = in.readParcelable(JobAdvertModel2.class.getClassLoader());
        suggestType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (isSave ? 1 : 0));
        dest.writeByte((byte) (basvuruDurumu ? 1 : 0));
        dest.writeString(newLocationDistance);
        dest.writeString(companyDistance);
        dest.writeParcelable(mCoord, flags);
        dest.writeParcelable(jobAdvertModel2, flags);
        dest.writeString(suggestType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NearJobAdvertModel> CREATOR = new Creator<NearJobAdvertModel>() {
        @Override
        public NearJobAdvertModel createFromParcel(Parcel in) {
            return new NearJobAdvertModel(in);
        }

        @Override
        public NearJobAdvertModel[] newArray(int size) {
            return new NearJobAdvertModel[size];
        }
    };

    public JobAdvertModel2 getJobAdvertModel2() {
        return jobAdvertModel2;
    }

    public void setJobAdvertModel2(JobAdvertModel2 jobAdvertModel2) {
        this.jobAdvertModel2 = jobAdvertModel2;
    }

    public String getSuggestType() {
        return suggestType;
    }

    public void setSuggestType(String suggestType) {

        this.suggestType = suggestType;
    }

    @Override
    public LatLng getPosition() {
        // Log.d(TAG, "getPosition: " + mCoord);
        return mCoord;
    }


    public void setPosition(LatLng mPosition) {
        // Log.d(TAG, "setPosition: " + mPosition);
        mCoord = mPosition;
    }

    public boolean isSave() {
        return isSave;
    }

    public void setSave(boolean save) {
        isSave = save;
    }

    public void setBasvuruDurumu(boolean _basvuruDurumu) { //İlana Basvuru yapılmısmı
        this.basvuruDurumu = _basvuruDurumu;
    }

    public StateButton.BUTTON_STATES getBasvuruDurumu() {

        if (!basvuruDurumu) {

            return StateButton.BUTTON_STATES.ENABLED;//Yani basvurulabiliri duruma gec basvuru yapılmamıs ise

        } else {
            return StateButton.BUTTON_STATES.DISABLED; //Basvuru yapılmıs ise butonu devre dısı bırak .
        }
    }

    public boolean isBasvuruDurumu() {
        return basvuruDurumu;
    }

    public BitmapDescriptor getMarkerIcon() {
        return markerIcon;
    }

    public void setMarkerIcon(BitmapDescriptor markerIcon) {
        this.markerIcon = markerIcon;
    }

    public String getCompanyDistance() {
        return companyDistance + " km";
    }

    public void setCompanyDistance(String companyDistance) {
        this.companyDistance = companyDistance;
    }

    public String getNewLocationDistance() {
        if (newLocationDistance == null) {
            return "";
        } else {
            return newLocationDistance + "km";
        }
    }

    public void setNewLocationDistance(String newLocationDistance) {
        this.newLocationDistance = newLocationDistance;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    @Override
    public String toString() {
        return "NearJobAdvertModel{" +
                "isSave=" + isSave +
                ", basvuruDurumu=" + basvuruDurumu +
                ", markerIcon=" + markerIcon +
                ", newLocationDistance='" + newLocationDistance + '\'' +
                ", companyDistance='" + companyDistance + '\'' +
                ", mCoord=" + mCoord +
                ", jobAdvertModel2=" + jobAdvertModel2 +
                ", suggestType='" + suggestType + '\'' +
                '}';
    }
}
