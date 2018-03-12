package com.example.harun.getjob.Profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harun.getjob.R;
import com.example.harun.getjob.profileModel.PhotoModel;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * Created by mayne on 26.02.2018.
 */

public class GaleryFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "GaleryFragment";
    CircularImageView user_image;
    ProgressBar image_progress;
    GridView galeryphoto;
    Spinner directorySpinner;
    ImageView closeGalery;
    TextView tvOk;
    RelativeLayout imageArea;
    ArrayList<PhotoModel> directory;
    ArrayList<PhotoModel> imagepath;
    Activity activity;
    private String mAppend = "file:/";
    MyCropImage mcropImageClass;
    String selectedImage = "";
    Bitmap cropped;
    FilePaths mfilePaths;
    Uri selectedImageuri;
    Uri outputUri;
    Bitmap bitmapCropped;
    CropImage.ActivityResult croppingImageresult;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.gallery_fragment, container, false);
        user_image = v.findViewById(R.id.userImage);
        image_progress = v.findViewById(R.id.image_progress);
        galeryphoto = v.findViewById(R.id.galeryphoto);
        directorySpinner = v.findViewById(R.id.spinnerDirectory);
        closeGalery = v.findViewById(R.id.closeGalery);
        imageArea = v.findViewById(R.id.imageArea);
        tvOk = v.findViewById(R.id.tvOk);
        directory = new ArrayList<>();
        activity = getActivity();
        closeGalery.setOnClickListener(this);
        tvOk.setOnClickListener(this);
        //cropImageView = v.findViewById(R.id.cropImageView);

        // Permissions.requestPermission(getActivity(), Permissions.READ_STORAGE_PERMISSION);

        if (Permissions.checkPermision(getActivity(), Permissions.READ_STORAGE_PERMISSION[0])) {

            init();

        } else {
            Permissions.showAlertdilaog(getActivity(), "İzinler Verilmemiş", "İzinleri verde işimize bakalım",2000);

            Intent i = new Intent(getActivity(), PhotoActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            getActivity().finish();


        }


        return v;
    }

    private void init() {

        /*Telefondaki resim içeren dosya yolllarını spinner içerisine yerleştiriyoruz
         *esecilen resim yolunda resimler grid view da gösterilecek
         */
        Log.d(TAG, "init:");

        mfilePaths = new FilePaths();

        final ArrayList<String> paths = new ArrayList<>();
        // Dönen İmage Directoryleri directory listesine esşitliyorum
        directory = FileSearch.getAllShownImagesPath(true, getActivity(), null);
        if (directory != null) {


            for (int i = 0; i < directory.size(); i++) {
                paths.add(directory.get(i).getImageBucket());
                Log.d("SPİNNER DİRECTORY ", paths.get(i));
            }


        }

        spinnerTask(paths);


    }

    private void spinnerTask(ArrayList<String> paths) {
        Log.d(TAG, "spinnerTask: Spinnner Dolduruluyor.");

        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, paths);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directorySpinner.setAdapter(spinAdapter);

        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemSelected: Spinner Directory" + directory.get(i));
                //final PhotoModel model = directory.get(i);


                setupGridView(directory.get(i).getBucketId());

                // setupGridView(directory.get(i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    /**
     * @param selectedDirectory -->Spinnerdan seçilen itemin butcketID bilgileri geliyor
     */
    private void setupGridView(final String selectedDirectory) {

        Log.d(TAG, "setupGridView: directory chosen: " + selectedDirectory);
        // Log.d(TAG, "setupGridView: directory chosen: " + selectedDirectory.getImagePath());
        final ArrayList<PhotoModel> image = FileSearch.getAllShownImagesPath(false, getActivity(), selectedDirectory);
        final ArrayList<String> imagePathString = new ArrayList<>();
        //GridView genişlikleri ayarlanıcak yani her gelen resim için

        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth / (3);
        galeryphoto.setColumnWidth(imageWidth);

        if (image != null) {


            for (int i = 0; i < image.size(); i++) {
                imagePathString.add(image.get(i).getImagePath());
                Log.d("SPİNNER DİRECTORY ", imagePathString.get(i));
            }


        }
        //GRidView Adapter sınıfı
        Log.d(TAG, "setupGridView: imajlar yükleniyor");


        GridViewAdapter gridViewAdapter = new GridViewAdapter(getActivity(), R.layout.gridview_imagelist_row,
                mAppend, imagePathString);
        galeryphoto.setAdapter(gridViewAdapter);


        //Galery activity inflate oldugu anda galerinin İlk resimini seçilen resim yapıyoruz.
        UniversalImageLoader.setImage(imagePathString.get(0), user_image, null, mAppend);

        //Secilen resimi koyuyorum

        galeryphoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Log.d(TAG, "onItemClick:GRİDVİEW SECİLEN FOTOGRAF  " + imagePathString.get(position));

                //UniversalImageLoader.setImage(imagePathString.get(position), user_image, null, mAppend);
                selectedImage = imagePathString.get(position);
                selectedImageuri = Uri.fromFile(new File(selectedImage));

                //mcropImageClass=new MyCropImage(getContext(),);
                // mcropImageClass.editPhoto(selectedImageuri);
                CropImage();

            }
        });


    }


    public void CropImage() {
        Log.d(TAG, "CropImage: URi" + selectedImageuri);

        CropImage.activity(selectedImageuri)
                .setGuidelines(CropImageView.Guidelines.OFF)
                .setActivityTitle("Fotoğrafı Düzenle")
                .setCropShape(CropImageView.CropShape.OVAL)
                .setCropMenuCropButtonTitle("Tamam")
                // .setGuidelinesColor(Color.BLUE)
                .setInitialCropWindowPaddingRatio(0)
                .setAutoZoomEnabled(true)
                // .setOutputUri(outputUri)
                .setRequestedSize(500, 500)
                // .setCropMenuCropButtonIcon(R.drawable.arrow)
                .start(getContext(), this);


    }

    //Fotograg galeriden seçilmesi durumunda editprofile bu pathi gönderecek
    private void userImagefromGalery(String imagePath, Uri uri) {

        if (uri != null) {

            Log.d(TAG, "userImage_fromGalery: Secilen Resim Uri" + "\t" + uri);
            Intent i = new Intent(getActivity(), EditProfile.class);
            i.putExtra("newUserImagefromGaleryUri", uri);
            startActivity(i);

            getActivity().finish();
        } else {
            Log.d(TAG, "userImage_fromGalery: Uri yok path " + imagePath);
            Intent i = new Intent(getActivity(), EditProfile.class);
            i.putExtra("newUserImagefromGalery", imagePath);
            startActivity(i);

            getActivity().finish();

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Activityden gelen crop image
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            croppingImageresult = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                //outputUri=mfilePaths.getImageUri(getContext(),croppingImageresult.getOriginalBitmap());
                Log.d(TAG, "onActivityResult: Croopping Image REsult " + croppingImageresult.getUri());

                cropped=mfilePaths.getCroppedBitmap(croppingImageresult.getUri());//Crop edilen image urisi gönderiliyor.Bitmap olarak alınıyor
                outputUri = mfilePaths.getImageFile(getActivity(), cropped); //Kesilen fotograf burada kaydedilip kayıt edilen yerin uri'si return edilir

                user_image.setImageURI(outputUri);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.d(TAG, "onActivityResult: Hata Oluştu");

            }
        }
    }

  /*  private void getImageUri(Uri uri) {
        File resultFile = new File(uri.getPath());
        Log.d(TAG, "getIMAGEURİ: Cropping RESULT FİLE " + resultFile.toString());
        BitmapFactory.Options bitmapOpts = new BitmapFactory.Options();
        bitmapOpts.inMutable = true;
        cropped = BitmapFactory.decodeFile(resultFile.getPath(), bitmapOpts);
        outputUri = mfilePaths.getImageFile(getActivity(), cropped); //Kesilen fotograf burada kaydedilip kayıt edilen yerin uri'si return edilir

        Log.d(TAG, "getIMAGEURİ: Croopping Image REsult BİTMAP " + cropped + "\t" + outputUri);
        // return outputUri;

    }
*/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.closeGalery:
                Log.d(TAG, "onClick: CloseActivity");
                getActivity().finish();

                break;

            case R.id.tvOk:

                if (!selectedImage.isEmpty()) {
                    Log.d(TAG, "onClick: TAMAM tıklandı ");
                    userImagefromGalery(selectedImage, outputUri);
                } else {

                    Toast.makeText(activity, "Lütfen Bir Fotoğraf Seçiniz", Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }
}
