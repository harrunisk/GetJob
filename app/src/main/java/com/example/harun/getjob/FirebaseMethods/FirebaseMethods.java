package com.example.harun.getjob.FirebaseMethods;

/**
 * Created by mayne on 6.03.2018.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.harun.getjob.MainActivity;
import com.example.harun.getjob.Profile.FilePaths;
import com.example.harun.getjob.R;
import com.example.harun.getjob.profileModel.deneyimModel;
import com.example.harun.getjob.profileModel.egitimListModel;
import com.example.harun.getjob.profileModel.genelBilgiModel;
import com.example.harun.getjob.profileModel.yetenekModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Tüm firebase Methodları burada gerçeklenecek
 */

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    //FİREBASE
    private FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference myRef, myref2;
    private FirebaseAuth mAuth;
    public String userId;
    FilePaths mFilePaths;
    private StorageReference mStorageRef;
    private String databaseName = "users_data"; //Firebase Database name
    firebaseContent mfirebaseContent;
    private Context mContext;

    public FirebaseMethods(Context context) {
        Log.d(TAG, "FirebaseMethod: Çağırıldı");
        mContext = context;
        mfirebaseContent = new firebaseContent();
        mFilePaths = new FilePaths();
        if (MainActivity.userID != null) {

            userId = MainActivity.userID;
            Log.d(TAG, "FirebaseMethod: @@@@@@@" + userId);
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //ASıl hedef burası kayıt edilen yer
        myref2 = myRef.child(databaseName).child(userId);

    }


    public void uploadProfileImages(Uri imageUri) {

        Bitmap bitmap;
        Log.d(TAG, "uploadProfileImages: " + imageUri);

        //Fotoğrafın ekleneceği yer
        StorageReference mstorageReferences =
                mStorageRef.child(mContext.getString(R.string.users_images)).child(userId + "/profile_photo");

        //Fotoğrafı bitmape çeviriyoruz ardından sıkıstıp byte olarak alıyoruz
        bitmap = mFilePaths.getCroppedBitmap(imageUri);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = mstorageReferences.putBytes(bytes);


        //Upload task listener ..
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess: FOTOĞRAF YÜKLENDİ");
                Uri firebaseUrl = taskSnapshot.getDownloadUrl();
                //mfirebaseContent.setProfil_photo(String.valueOf(firebaseUrl));
                myref2.child("profile_photo").setValue(firebaseUrl.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: PHOTO FAİLED" + e.getLocalizedMessage());

                Toast.makeText(mContext, "Photo upload failed ", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                Log.d(TAG, "onProgress: fotoğraf Yükleniyor." + progress + "% kaldı");
                //Buraya Bir süreç işleyici koyacazi
            }
        });


    }

    /**
     * kullanıcı adı soyadı , meslek ve lokasyon gibi ana bilgileri alıp kayıt ediyor.
     *
     * @param fbContent-->bilgiler bu sınıfta saklanıyor .
     */

    public void userProfile(firebaseContent fbContent) {
        ///  if (kontrol) {

        Log.d(TAG, "userProfile: İcerikler olusturuluyor ");

        Log.d(TAG, "Kayıt Ediliyor..: " + databaseName + "\n" + userId + "\n" +
                fbContent.getJob() + "\n" + fbContent.getName() + "\n" + fbContent.getLocation());

        // myRef.child(databaseName).child(userId).setValue(fbContent);
        myref2.child("main_content").setValue(fbContent);

        //  } else {
        //   Log.d(TAG, "userProfile: DEĞİŞİKLİK ALGILANMADI");
        //  }
    }

    /**
     * @param mgenelbilgiler -->Kullanıcı Genel Bilgilerini Kayıt eder
     * @param checkChanges   -->
     */
    public void addGeneralContent(genelBilgiModel mgenelbilgiler, boolean checkChanges) {

        if (checkChanges) {
            Log.d(TAG, "general_content: İÇERİKLERİ OLUŞTURULUYOR ");


            Log.d(TAG, "Kayıt Ediliyor..: " + databaseName + "\n" + userId + "\n" +
                    mgenelbilgiler.getBirthday() + "\n" + mgenelbilgiler.getE_mail() + "\n" + mgenelbilgiler.getPhone());

            //   myRef.child(databaseName).child(userId).child("general_content").setValue(mgenelbilgiler);
            myref2.child("general_content").setValue(mgenelbilgiler);

        } else {
            Log.d(TAG, "general_content: DEĞİŞİKLİK ALGILANMADI ");
        }


    }

    /**
     * @param aboutContent -->Hakkımda bölümünün kayıt işlemini yapar
     * @param checkChanges -->Değişiklik olması durumunu granti altına alır
     */
    public void addAboutme(String aboutContent, boolean checkChanges) {

        if (checkChanges) {

            Log.d(TAG, "about_me: İCERİK OLUŞTURULUYOR");
            Log.d(TAG, "Kayıt Ediliyor..: " + databaseName + "\n" + userId + "\n" +
                    aboutContent);


            //  myRef.child(databaseName).child(userId).child("about_me").setValue(aboutContent);
            myref2.child("about_me").setValue(aboutContent);

        } else {
            Log.d(TAG, "about_me: DEĞİŞİKLİK ALGILANMADI ");
        }


    }

    /**
     * @param hashMap Yetenek Fragmentteen gelen ve yeni eklenen yetenekler hashlenip buraya geliyor buradanfa
     *                firebase kayıdı yapılıyor
     */
    public void addSkillList(HashMap<String, ArrayList<yetenekModel>> hashMap, boolean checkChanges) {
        if (checkChanges) {

            Log.d(TAG, "skill_list: İCERİK oluşturuluyor.");
            //Bunlar aslında kayıt olurken olusuturulmalı her defasında burada yeniden olusturulmasna gerek yok
            for (Map.Entry<String, ArrayList<yetenekModel>> entry : hashMap.entrySet()) {
                //  Log.d(TAG, "experience_list: entry SET list \t" + hashMap.size() + "EntrySEt" + hashMap.keySet() + entry.getValue());
                ArrayList<yetenekModel> skills = entry.getValue();
                Log.d(TAG, "skill_list: skills LİST \t" + skills);
            /*   for (yetenekModel model : skills) {

                Log.d(TAG, keyset + skills.getYetenekName() + skills.getRate());

            }*/
                myref2.child("skill_list").setValue(skills);
            }


        } else {
            Log.d(TAG, "skill_list: DEĞİŞİKLİK ALGILANMADI");
        }


    }

    /**
     * Eğitim Listesini ekler
     *
     * @param educationHash -->Eklenen liste elemanları Position değerine göre hashlenir
     * @param checkChanges  -->Değişiklik olması durumunda kayıt işlemini garanti altına alır
     */
    public void addEducationlist(HashMap<String, ArrayList<egitimListModel>> educationHash, boolean checkChanges) {

        if (checkChanges) {

            // Log.d(TAG, "educationlist: Kayıt Ediliyor....@@@@@@w" + educationHash.entrySet());
            //  myref3 = myRef.child(databaseName).child(userId).child("education_list");
            for (Map.Entry<String, ArrayList<egitimListModel>> edu_entries : educationHash.entrySet()) {

                ArrayList<egitimListModel> educationList = edu_entries.getValue();
                Log.d(TAG, "educationlistFirebase: Kayıt EDİLENLER @@@" + educationList);

                myref2.child("education_list").setValue(educationList);
            }


        } else {

            Log.d(TAG, "educationlist: DEĞİŞİKLİK ALGILANMADI");

        }


    }

    /**
     * @param deneyimHash-->Eklenen deneyim listesi pozisyon değerleri ile hash lenir
     * @param checkChanges          -->
     */

    public void addExperienceList(HashMap<String, ArrayList<deneyimModel>> deneyimHash, boolean checkChanges) {

        if (checkChanges) {
            Log.d(TAG, "deneyimListFirebase: İcerik Oluşturuluyor.");

            for (Map.Entry<String, ArrayList<deneyimModel>> deneyim_entries : deneyimHash.entrySet()) {

                ArrayList<deneyimModel> deneyimModelArrayList = deneyim_entries.getValue();


                myref2.child("experience_list").setValue(deneyimModelArrayList);

            }

        } else {
            Log.d(TAG, "deneyimListAddingFirebase: Değişiklikler Algılanamadı..");
        }

    }


}
