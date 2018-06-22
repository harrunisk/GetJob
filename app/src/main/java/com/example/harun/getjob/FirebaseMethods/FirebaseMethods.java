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
import com.example.harun.getjob.profileModel.AllModelsList;
import com.example.harun.getjob.profileModel.deneyimModel;
import com.example.harun.getjob.profileModel.egitimListModel;
import com.example.harun.getjob.profileModel.genelBilgiModel;
import com.example.harun.getjob.profileModel.yetenekModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
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
    private String databaseName = "users_data"; //SaveJobAdvertToFirebase Database name
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
        myref2 = myRef.child(databaseName).child(userId).child("profile_data");

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
     * @param checkChanges          -->Değişiklik yapılmasında durumunda Hashleme işlemi yapılmalı ve öyle eklenmelidir.
     */

    public void addExperienceList(HashMap<String, ArrayList<deneyimModel>> deneyimHash, boolean checkChanges) {

        if (checkChanges) {
            Log.d(TAG, "deneyimListFirebase: İcerik Oluşturuluyor.");

            for (Map.Entry<String, ArrayList<deneyimModel>> exp_entries : deneyimHash.entrySet()) {

                ArrayList<deneyimModel> experienceList = exp_entries.getValue();


                myref2.child("experience_list").setValue(experienceList);

            }

        } else {
            Log.d(TAG, "deneyimListAddingFirebase: Değişiklikler Algılanamadı..");
        }

    }


    /**
     * profilPage->firebaseinit() methodu ile buraya bir datasnapshot aktarılır .
     * Databaseden tüm verileri getirir.
     * Tüm kontroller burada yapılır Child varmı Child içinde item varmı
     * Şayet kullanıcı bazı bölümlerin kayıtlarını yapmamış ise  null olarak kayıt eder Profil page'de null gelenler ayrıştırılır.
     * AllModelList classı burada indirilen tüm itemleri Model classları ile bir arada tutan Parcelable class.Bunları daha sonra isteğin şekilde parse ederek kullanabilrsin
     * Örnek ->> mAllModelList.getEgitimListModel().getOkul();
     *
     * @param dataSnapshot -
     * @return -->profilPage ->> setProfileItems()
     */
    public AllModelsList getDataFromFirebase(DataSnapshot dataSnapshot) {
        Log.d(TAG, "onDataChange: getDAtaFromDatabase");
        Log.d(TAG, "getDataFromFirebase: " + dataSnapshot);
        final ArrayList<egitimListModel> egitimList = new ArrayList<>();
//Firebase den coollection cekmek icin gerekli olan generictypeindicator
        final GenericTypeIndicator<ArrayList<egitimListModel>> genericTypeIndicator =
                new GenericTypeIndicator<ArrayList<egitimListModel>>() {
                };

        final ArrayList<yetenekModel> yetenekList = new ArrayList<>();
        GenericTypeIndicator<ArrayList<yetenekModel>> genericTypeIndicator1 = new GenericTypeIndicator<ArrayList<yetenekModel>>() {
        };

        final ArrayList<deneyimModel> deneyimList = new ArrayList<>();
        GenericTypeIndicator<ArrayList<deneyimModel>> genericTypeIndicator2 = new GenericTypeIndicator<ArrayList<deneyimModel>>() {
        };
        genelBilgiModel bilgiModel = new genelBilgiModel();
        firebaseContent content = new firebaseContent();
        String photoUrl = new String();
        String about_me = new String();

        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//
//            if (dataSnapshot1.getKey().equals(userId).) {
            Log.d(TAG, "getDataFromFirebase: " + dataSnapshot1);

            //Log.d(TAG, "onDataChange: dataSnapshot1 TÜM ELEMANLAR" + dataSnapshot1.child(userId).getValue());

            if (dataSnapshot1.getKey().equals("about_me")) {

                about_me = String.valueOf(dataSnapshot1.getValue());

            } else if (dataSnapshot1.getKey().equals("education_list")) {
                Log.d(TAG, "getDataFromFirebase:dataSnapshot1.getKey().equals(education_list) ");

                ArrayList<egitimListModel> egitimListModelArrayList =
                        dataSnapshot1.getValue(genericTypeIndicator);
                Log.d(TAG, "getDataFromFirebase:-->egitimListModelArrayList" + egitimListModelArrayList);
                if (egitimListModelArrayList.size() > 0) {
                    for (egitimListModel _egitimListModel : egitimListModelArrayList) {

                        egitimList.add(new egitimListModel(_egitimListModel.okul, _egitimListModel.bolum, _egitimListModel.ogrenimTuru, _egitimListModel.bsYılı, _egitimListModel.btsYılı));

                        Log.d(TAG, "onDataChange: EGİTİMLİSTMODEL" + egitimList);

                        //Log.d(TAG, "onDataChange: " + egitimListModel.okul + "\t" + egitimListModel.bolum + "\t" + egitimListModel.btsYılı);

                    }
                } else {

                    Log.d(TAG, "getDataFromFirebase: egitimListBOŞŞŞŞŞŞŞŞ");
                }


            } else if (dataSnapshot1.getKey().equals("experience_list")) {

                Log.d(TAG, "getDataFromFirebase: dataSnapshot1.getKey().equals(experience_list)");

                ArrayList<deneyimModel> deneyimModelArrayList = dataSnapshot1.getValue(genericTypeIndicator2);
                if (deneyimModelArrayList.size() > 0) {
                    for (deneyimModel _deneyimModel : deneyimModelArrayList) {

                        deneyimList.add(new deneyimModel(_deneyimModel.pozisyon, _deneyimModel.kurum, _deneyimModel.lokasyon, _deneyimModel.ay));
                        Log.d(TAG, "getDataFromFirebase: DENEYİMMODEL LİST \t" + deneyimList);
                    }
                } else {
                    Log.d(TAG, "getDataFromFirebase: deneyimListBOLŞŞŞşş");


                }

            } else if (dataSnapshot1.getKey().equals("general_content")) {

                Log.d(TAG, "getDataFromFirebase: dataSnapshot1.getKey().equals(general_content)");

                bilgiModel = dataSnapshot1.getValue(genelBilgiModel.class);

            } else if (dataSnapshot1.getKey().equals("main_content")) {

                Log.d(TAG, "getDataFromFirebase: dataSnapshot1.getKey().equals(main_content)");

                content = dataSnapshot1.getValue(firebaseContent.class);


            } else if (dataSnapshot1.getKey().equals("profile_photo")) {
                Log.d(TAG, "getDataFromFirebase: profile_photo");
                photoUrl = (String) dataSnapshot1.getValue();

            } else if (dataSnapshot1.getKey().equals("skill_list")) {

                ArrayList<yetenekModel> yetenekModelArrayList = dataSnapshot1.getValue(genericTypeIndicator1);

                if (yetenekModelArrayList.size() > 0) {

                    for (yetenekModel _yetenekModel : yetenekModelArrayList) {

                        yetenekList.add(new yetenekModel(_yetenekModel.yetenekName, _yetenekModel.rate));
                        Log.d(TAG, "getDataFromFirebase: YETENEKLİST" + yetenekList);

                    }

                } else {

                    Log.d(TAG, "getDataFromFirebase: YETENEKLİSTBOŞŞŞŞŞŞ");
                }
            }

        }
        return new AllModelsList(egitimList, deneyimList, yetenekList, bilgiModel, content, about_me, photoUrl);
    }

    /**
     * Şimdilik buarada bu kontrolleri koyuyorum daha sonra kullanıcı kayıt yaparken bunları olusturucağım için
     * şimdilik kalsın.Ayrıca kontroller liste içleri boş olup olmadıgıda yapıalacak.
     */
       /*     if (dataSnapshot1.child(userId).exists()) { //userID child varsa ise
            if (dataSnapshot1.hasChild(userId))
                Log.d(TAG, "getDataFromFirebase:dataSnapshot1.hasChild(userId) ");
            if (dataSnapshot1.child(userId).child("education_list").exists()) {
                ArrayList<egitimListModel> egitimListModelArrayList =
                        dataSnapshot1.child(userId).child("education_list").getValue(genericTypeIndicator);

                if (egitimListModelArrayList.size() > 0) {
                    for (egitimListModel _egitimListModel : egitimListModelArrayList) {

                        egitimList.add(new egitimListModel(_egitimListModel.okul, _egitimListModel.bolum, _egitimListModel.ogrenimTuru, _egitimListModel.bsYılı, _egitimListModel.btsYılı));

                        Log.d(TAG, "onDataChange: EGİTİMLİSTMODEL" + egitimList);

                        //Log.d(TAG, "onDataChange: " + egitimListModel.okul + "\t" + egitimListModel.bolum + "\t" + egitimListModel.btsYılı);

                    }
                } else {

                    Log.d(TAG, "getDataFromFirebase: egitimListBOŞŞŞŞŞŞŞŞ");
                }

            }
            if (dataSnapshot1.child(userId).child("experience_list").exists()) {

                ArrayList<deneyimModel> deneyimModelArrayList = dataSnapshot1.child(userId).child("experience_list").getValue(genericTypeIndicator2);
                for (deneyimModel _deneyimModel : deneyimModelArrayList) {

                    //deneyimList.add(new deneyimModel(mdeneyimModel.getPozisyon(), mdeneyimModel.getKurum(), mdeneyimModel.getLokasyon(), mdeneyimModel.getAy()));
                    deneyimList.add(new deneyimModel(_deneyimModel.pozisyon, _deneyimModel.kurum, _deneyimModel.lokasyon, _deneyimModel.ay));
                    Log.d(TAG, "getDataFromFirebase: DENEYİMMODEL LİST \t" + deneyimList);
                }

            }
            if (dataSnapshot1.child(userId).child("skill_list").exists()) {

                ArrayList<yetenekModel> yetenekModelArrayList = dataSnapshot1.child(userId).child("skill_list").getValue(genericTypeIndicator1);

                if (yetenekModelArrayList.size() > 0) {

                    for (yetenekModel _yetenekModel : yetenekModelArrayList) {

                        yetenekList.add(new yetenekModel(_yetenekModel.yetenekName, _yetenekModel.rate));
                        Log.d(TAG, "getDataFromFirebase: YETENEKLİST" + yetenekList);

                    }

                } else {

                    Log.d(TAG, "getDataFromFirebase: YETENEKLİSTBOŞŞŞŞŞŞ");
                }

            }
            if (dataSnapshot1.child(userId).child("general_content").exists()) {

                bilgiModel = dataSnapshot1.child(userId).child("general_content").getValue(genelBilgiModel.class);

            }

            //else
            //  bilgiModel = new genelBilgiModel("Belirtilmemiş", "Belirtilmemiş", "Belirtilmemiş", "Belirtilmemiş", "Belirtilmemiş");


            if (dataSnapshot1.child(userId).child("main_content").exists()) {

                content = dataSnapshot1.child(userId).child("main_content").getValue(firebaseContent.class);


            }

            if (dataSnapshot1.child(userId).child("about_me").exists()) {
                about_me = String.valueOf(dataSnapshot1.child(userId).child("about_me").getValue());
            }
            if (dataSnapshot1.child(userId).child("profile_photo").exists()) {

                photoUrl = (String) dataSnapshot1.child(userId).child("profile_photo").getValue();

            }


                 /*   Log.d(TAG, "onDataChange: ARRAY LİST " + egitimListModelArrayList.size() +
                            deneyimModelArrayList.size() + yetenekModelArrayList.size()
                            + egitimListModelArrayList.get(0) + "\t" + deneyimModelArrayList.get(0) +
                            "\t" + bilgiModel + "\t" + content + "\t" + about_me + "\t" + photoUrl);
*/

    //   return new AllModelsList(egitimList, deneyimList, yetenekList, bilgiModel, content, about_me, photoUrl);

    //  } else{

    //  Log.d(TAG, "getDataFromFirebase: ChildBulunamadı");
    // }
}
