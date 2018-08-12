package com.example.harun.getjob.JobSearch.JobUtils;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harun.getjob.AddJobAdvert.ApplicantUserModel;
import com.example.harun.getjob.AddJobAdvert.ApplyAdvertModel;
import com.example.harun.getjob.AddJobAdvert.HelperStaticMethods;
import com.example.harun.getjob.Profile.UniversalImageLoader;
import com.example.harun.getjob.R;
import com.example.harun.getjob.UserIntro;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.altervista.andrearosa.statebutton.StateButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by mayne on 20.03.2018.
 */


/**
 * Sizin için öneriliyor Listesinin Adapteri ..
 */
public class SuggestJobAdvertAdapter extends RecyclerView.Adapter<SuggestJobAdvertAdapter.MyViewHolder> {

    private static final String TAG = "SuggestJobAdvertAdapter";
    private ArrayList<NearJobAdvertModel> jobAdvertModelArrayList;
    private LayoutInflater inflater;
    private Context mContext;
    private NearJobAdvertModel mAdvertModel;
    private int selectedPos = 0;
    AdvertOnclickCallback onclickCallback;
    String denemeurl = "http://wellnessmart.com/wp-content/uploads/md.png";

    public interface AdvertOnclickCallback {

        void advertOnclickListener(NearJobAdvertModel model);
    }

    public SuggestJobAdvertAdapter(Context context, ArrayList<NearJobAdvertModel> jobAdvertModelArrayList, AdvertOnclickCallback advertOnclickCallback) {
        Log.d(TAG, "SuggestJobAdvertAdapter: Constructor ");
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.jobAdvertModelArrayList = jobAdvertModelArrayList;
        this.onclickCallback = advertOnclickCallback;


        // circleAreaLayout.setLayoutAnimation(layoutAnimationController);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.suggest_job_advert, null, true);
        MyViewHolder viewHolder = new MyViewHolder(view);
        // int widht = (parent.getMeasuredWidth());
        // view.setMinimumWidth(widht);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        mAdvertModel = jobAdvertModelArrayList.get(position);

        holder.companyName.setText(mAdvertModel.getCompanyName());
        holder.companyJob.setText(mAdvertModel.getJobSector());
        holder.jobDescpriction.setText(mAdvertModel.getCompanyJob());
        holder.publishDate1.setText(HelperStaticMethods.getDateDifference(mAdvertModel.getPublishDate()));
        holder.companyDistance.setText(mAdvertModel.getCompanyDistance());
        holder.suggestType.setText(mAdvertModel.getSuggestType());
        holder.saveAdvert.setActivated(mAdvertModel.isSave());


        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.configuration());
        UniversalImageLoader.setImage("", holder.companyLogo, null, "");


       /* holder.saveAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.saveAdvert.isActivated()) {
                    holder.saveAdvert.setActivated(false);
                    mAdvertModel.setSave(false);
                    addFirebaseAppliedAdvert(mAdvertModel);
                } else {
                    holder.saveAdvert.setActivated(true);
                    mAdvertModel.setSave(true);
                    addFirebaseAppliedAdvert(mAdvertModel);

                }

                // Toast.makeText(mContext, "İlan Kayıt Edildi( Eklenecek. )", Toast.LENGTH_SHORT).show();
            }
        });

*/
    }

    private void addFirebaseAppliedAdvert(final NearJobAdvertModel mAdvertModel) {
        Log.d(TAG, "addFirebaseAppliedAdvert: mAdvertModel.getJobAdvertModel2().getJobSector() " + mAdvertModel.getJobAdvertModel2().getJobSector());
        String date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());
        final ApplyAdvertModel applyAdvertModel = new ApplyAdvertModel(
                mAdvertModel.getJobAdvertModel2().getJobID(),
                date, mAdvertModel.isSave(),
                false);
        final ApplicantUserModel applicantUserModel = new ApplicantUserModel(UserIntro.userID, date,"");
        FirebaseDatabase.getInstance().getReference()
                .child("users_data")
                .child(UserIntro.userID)
                .child("applyAdvert")
                .child(applyAdvertModel.getJobID()).setValue(applyAdvertModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Basvuru bilgisi kullanıcı basvurularına eklendi ");


                        FirebaseDatabase.getInstance().getReference()
                                .child("users_data")
                                .child(UserIntro.userID)
                                .child("suggestionKey").child(mAdvertModel.getJobAdvertModel2().getJobSector()).child(mAdvertModel.getJobAdvertModel2().getCompanyJob()).setValue(mAdvertModel.isSave()) //kayıtlara göre ise true değilse false yani basvurulara göre sıralancak
                                // .child(mJobAdvertModel.getJobSector()).child(mJobAdvertModel.getCompanyJob()).setValue(true)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: Kullanıcıya Daha sonra Önerilecek Olan  İlanlar İçin Sektör ve Meslek Bilgisi eklendi ");

                                        //Sadece basvuru işlemi gerçekleşmiş ise
                                        if (mAdvertModel.getBasvuruDurumu() == StateButton.BUTTON_STATES.DISABLED) {

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("jobAdvert")
                                                    .child("publishedAdverts")
                                                    .child(mAdvertModel.getJobAdvertModel2().getJobID())
                                                    .child("applyInfo")
                                                    .child(UserIntro.userID).setValue(applicantUserModel)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG, "onSuccess: basvuru bilgisi ilanın detaylarına eklendi");

                                                        }
                                                    });

                                        }


                                    }
                                });


                    }
                });


    }

//
//    private void startAnim(View view) {
//        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_in);
//        view.startAnimation(anim);
//        anim.setFillAfter(true);
//        anim.setFillEnabled(true);
//
    // ObjectAnimator fade = ObjectAnimator.ofFloat(holder.itemView, View.ALPHA, 1f,.3f);
    // ObjectAnimator fade = ObjectAnimator
    // fade.setDuration(1500);
    // fade.setInterpolator(new LinearInterpolator());
    // fade.start();
//    }

    @Override
    public int getItemCount() {
        return jobAdvertModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView companyName, companyJob, jobDescpriction, publishDate1, companyDistance, suggestType;
        ImageView companyLogo, saveAdvert;
        CardView suggestAdvertCard;
        // FloatingActionButton saveAdvert;

        public MyViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "MyViewHolder: ");
            companyName = itemView.findViewById(R.id.companyName);
            suggestAdvertCard = itemView.findViewById(R.id.suggestAdvertCard);
            companyJob = itemView.findViewById(R.id.companyJob);
            jobDescpriction = itemView.findViewById(R.id.jobDescpriction);
            publishDate1 = itemView.findViewById(R.id.publishDate1);
            companyDistance = itemView.findViewById(R.id.companyDistance);
            companyLogo = itemView.findViewById(R.id.companyLogo);
            saveAdvert = itemView.findViewById(R.id.saveAdvert);
            suggestType = itemView.findViewById(R.id.whySuggest);

            saveAdvert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: " + getAdapterPosition());
                    selectedPos = getAdapterPosition();
                    saveAdvertToFirebase(saveAdvert, selectedPos);
                }
            });
            suggestAdvertCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d(TAG, "onClick: suggestAdvertCard");
                    onclickCallback.advertOnclickListener(jobAdvertModelArrayList.get(getAdapterPosition()));

                }
            });

        }
    }

    private void saveAdvertToFirebase(View _saveAdvert, int selectedPos) {
        if (_saveAdvert.isActivated()) {
            _saveAdvert.setActivated(false);
            jobAdvertModelArrayList.get(selectedPos).setSave(false);
            // mAdvertModel.setSave(false);
            removeFirebaseAppliedAdvert(jobAdvertModelArrayList.get(selectedPos));
        } else {
            _saveAdvert.setActivated(true);
            jobAdvertModelArrayList.get(selectedPos).setSave(true);
            addFirebaseAppliedAdvert(jobAdvertModelArrayList.get(selectedPos));

        }

    }

    /**
     * ZAten basvuru veya kayıt işlemi yapılmamıs ilanlar gösterildiğinden eger kayıt işlemi iptal edilirse bunu database den silmemiz lazım
     * Çünkü ne kayıt ne basvuru yapılmamıs databasede tutmanın anlamı yok .
     *
     * @param nearJobAdvertModel
     */
    private void removeFirebaseAppliedAdvert(NearJobAdvertModel nearJobAdvertModel) {

        FirebaseDatabase.getInstance().getReference()
                .child("users_data")
                .child(UserIntro.userID)
                .child("applyAdvert")
                .child(nearJobAdvertModel.getJobAdvertModel2().getJobID()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: İLAN KAYIDI GERİ ALINDI ");
            }
        });

    }
}
