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
    private int basvuruDurumu;
    private BitmapDescriptor markerIcon;
    private String newLocationDistance, companyDistance;
    private LatLng mCoord;

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
            int basvuruDurumu,
            BitmapDescriptor markerIcon,
            String newLocationDistance, String companyDistance) {
        //Bunlar jobADvertModel2 'e
        // Diğerleride bu sınıfın değişkeni
        super(jobID,companyName, companyJob, jobSector, jobDescpriction,
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

    public void setBasvuruDurumu(int _basvuruDurumu) { //İlana Basvuru yapılmısmı
        this.basvuruDurumu = _basvuruDurumu;
    }

    public StateButton.BUTTON_STATES getBasvuruDurumu() {

        if (basvuruDurumu == 0) {

            return StateButton.BUTTON_STATES.ENABLED;//Yani basvurulabiliri duruma gec basvuru yapılmamıs isse

        } else {
            return StateButton.BUTTON_STATES.DISABLED; //Basvuru yapılmıs ise butonu devre dısı bırak .
        }
    }


    public BitmapDescriptor getMarkerIcon() {
        return markerIcon;
    }

    public void setMarkerIcon(BitmapDescriptor markerIcon) {
        this.markerIcon = markerIcon;
    }

    public String getCompanyDistance() {
        return companyDistance + "m";
    }

    public void setCompanyDistance(String companyDistance) {
        this.companyDistance = companyDistance;
    }

    public String getNewLocationDistance() {
        if (newLocationDistance == null) {
            return "";
        } else {
            return newLocationDistance + "m";
        }
    }

    public void setNewLocationDistance(String newLocationDistance) {
        this.newLocationDistance = newLocationDistance;
    }

    protected NearJobAdvertModel(Parcel in) {
        super(in);
        isSave = in.readByte() != 0;
        basvuruDurumu = in.readInt();
        newLocationDistance = in.readString();
        companyDistance = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (isSave ? 1 : 0));
        dest.writeInt(basvuruDurumu);
        dest.writeString(newLocationDistance);
        dest.writeString(companyDistance);
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


    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
