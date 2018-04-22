package com.example.harun.getjob.JobSearch.JobUtils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.harun.getjob.R;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

/**
 * Created by mayne on 9.04.2018.
 */

public class NearJobAdvertAdapter extends RecyclerView.Adapter<NearJobAdvertAdapter.MyViewHolder> {
    private static final String TAG = "NearJobAdvertAdapter";
    Context mcontext;
    private ArrayList<JobAdvertModel> jobAdvertModelArrayList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private Animation animationUp;
    private Animation animDown;
    private Animation animPulse;
    private JobAdvertModel mJobAdvertModel;
    private ArrayList<MyViewHolder> expandedList = new ArrayList<>(1); //Expand edilen itemin view elemanlarını tutuyorum

    public NearJobAdvertAdapter(Context context, ArrayList<JobAdvertModel> jobAdvertModelArrayList) {
        Log.d(TAG, "NearJobAdvertAdapter: ");
        this.jobAdvertModelArrayList = jobAdvertModelArrayList;
        this.layoutInflater = LayoutInflater.from(context);
        this.mcontext = context;
        animationUp = AnimationUtils.loadAnimation(context, R.anim.slide_in_up);
        animDown = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        animPulse = AnimationUtils.loadAnimation(context, R.anim.pulse);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.joblist_row, null, false);
        //MyViewHolder viewHolder = new MyViewHolder(view);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        mJobAdvertModel = jobAdvertModelArrayList.get(position);
        Log.d(TAG, "onBindViewHolder: ");
        setData(holder);
        holder.bindMapLocation(mJobAdvertModel);

        holder.onay.setActivated(mJobAdvertModel.getBasvuruDurumu().getValue() == 0);

        //Listenin yan taraflarına renkler attım düzenlenecek ..
        if (position % 4 == 0) {
            holder.viewleft.setBackgroundTintList(ColorStateList.valueOf(mcontext.getColor(R.color.jumbo)));
        } else if (position % 4 == 1) {
            holder.viewleft.setBackgroundTintList(ColorStateList.valueOf(mcontext.getColor(R.color.tw__blue_default)));
        } else if (position % 4 == 2) {
            holder.viewleft.setBackgroundTintList(ColorStateList.valueOf(mcontext.getColor(R.color.SeaGreen)));
        } else {
            holder.viewleft.setBackgroundTintList(ColorStateList.valueOf(mcontext.getColor(R.color.Tan)));

        }


/*        holder.btnBasvur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                holder.btnBasvur.setState(StateButton.BUTTON_STATES.DISABLED);
                holder.btnBasvur.setClickable(false);
                holder.onay.setActivated(true);

            }
        });
        holder.save_this_advert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.save_this_advert.isActivated()) {
                    holder.save_this_advert.setActivated(false);
                } else
                    holder.save_this_advert.setActivated(true);


            }
        });
*/
        holder.showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.expandcard.isShown()) {

                    holder.expandcard.startAnimation(animDown);
                    CountDownTimer countDownTimerStatic = new CountDownTimer(500, 16) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {

                            holder.expandcard.setVisibility(View.GONE);
                            holder.tvdetail.setText("DETAYLAR");
                            holder.tvdetail.setTextColor(mcontext.getResources().getColor(R.color.black));
                            expandedList.clear();
                        }
                    };
                    countDownTimerStatic.start();


                } else {
                    closeIfOpenView(); //Daha önceden açılıp kapanmamıs olan eleman varsa onu bi kontrol et ..
                    expandedList.add(holder); //tıklanan itemin view elemanları listeye al ki bu liste boş değilse kapanmamıs view var demektir..
                    holder.expandcard.setVisibility(View.VISIBLE);
                    holder.expandcard.startAnimation(animationUp);
                    holder.tvdetail.setText("KAPAT");
                    holder.tvdetail.setTextColor(mcontext.getResources().getColor(R.color.red));
                    holder.detail_image.startAnimation(animPulse);


                    //  getMapView(holder);


                }


            }
        });
    }

   /* private void getMapView(MyViewHolder holder) {

        // FragmentManager fragmentManager=((FragmentManager)mcontext).getSupportFragmentManager();
        fragmentManager = ((NearJobListActivity) mcontext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mapView, new MapViewFragment(mJobAdvertModel.getPosition()));
        Log.d(TAG, "getMapView: " + mJobAdvertModel.getPosition());
        fragmentTransaction.commit();

    }*/

    /**
     * Sadece 1 view'in expand edilmesini sağlayan method.
     * Açılan (expand edilen her view ) ExpandList de saklanıyor ..
     * Başka bir expand işlemi yapılmadan önce bu fonksiyon çalışarak açık olan  View kapatılıyor ..
     * Ayrıca aynı itemim açılıp kapanması durumunda kapatılırken liste temizlendiği için burası sadece acık olan var ise çalışıyor .
     */
    private void closeIfOpenView() {
        if (expandedList.size() > 0) {
            MyViewHolder holde1 = expandedList.get(0);
            Log.d(TAG, "setExpandedPositionItem: EXPANDLİST SİZE " + expandedList.size());
            if (holde1.expandcard.isShown()) {
                Log.d(TAG, "setExpandedPositionItem: AÇIK İTEM TESPİT EDİLDİ KAPANIYOR -->" + holde1.getAdapterPosition());
                holde1.expandcard.setVisibility(View.GONE);
                holde1.tvdetail.setText("DETAYLAR");
                holde1.tvdetail.setTextColor(mcontext.getResources().getColor(R.color.black));
                expandedList.clear();
            }
        }
    }


    private void setData(MyViewHolder holder) {
        holder.tvjobname.setText(mJobAdvertModel.getCompanyJob());
        holder.tvbasvuru_count.setText(mJobAdvertModel.getCountApply());
        holder.companyname.setText(mJobAdvertModel.getCompanyName());
        holder.publishdate.setText(mJobAdvertModel.getPublishDate());
        holder.tvdistance.setText(mJobAdvertModel.getCompanyDistance());
        holder.tvexperience.setText(mJobAdvertModel.getExperienceinfo());
        //  holder.tvjobtype.setText(mJobAdvertModel.getJobDescpriction());

    }

    @Override
    public int getItemCount() {
        return jobAdvertModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements AdvertDetails.ChangeMarkerCluster {
        View viewleft;
        TextView companyname, tvbasvuru_count, tvjobname, tvjobtype, publishdate, tvexperience, tvdistance, tvdetail;
        ImageView companyLogo1, detail_image, onay;
        RelativeLayout showDetails;
        RelativeLayout expandcard;
        public GoogleMap mMap;
        AdvertDetails.ViewHolder viewHolder;

        public MyViewHolder(View itemView) {
            super(itemView);

            viewleft = itemView.findViewById(R.id.viewleft);
            companyname = itemView.findViewById(R.id.companyname);
            companyLogo1 = itemView.findViewById(R.id.companyLogo1);
            tvbasvuru_count = itemView.findViewById(R.id.tvbasvuru_count);
            tvjobname = itemView.findViewById(R.id.tvjobname);
            onay = itemView.findViewById(R.id.onay);
            publishdate = itemView.findViewById(R.id.publishdate);
            tvexperience = itemView.findViewById(R.id.tvexperience);
            tvdistance = itemView.findViewById(R.id.tvdistance);
            showDetails = itemView.findViewById(R.id.details);
            expandcard = itemView.findViewById(R.id.expandcard);
            detail_image = itemView.findViewById(R.id.showdetails);
            tvdetail = itemView.findViewById(R.id.tvdetail);
            viewHolder = new AdvertDetails.ViewHolder(itemView, mcontext, this); //İlan detayları
        }

        /**
         * Listte halinde her pozisyonun görüntüsü oluşturulurken buradada her itemi AdvertDetails teki
         * mapview 'e taglıyorum.
         *
         * @param fromNearJobAdapter->
         */
        private void bindMapLocation(JobAdvertModel fromNearJobAdapter) {
            viewHolder.bindMapLocation(fromNearJobAdapter);
        }

        @Override
        public void changeMarker(JobAdvertModel item, boolean isBasvuru) {


            for (JobAdvertModel list : MyclusterManager.getInstance().getMyClusterManager().getAlgorithm().getItems()) {

                if (list.getPosition().equals(item.getPosition())) {
                    Log.d(TAG, "deneme2: " + list.getCompanyName());
                    if (isBasvuru) {
                        list.setBasvuruDurumu(1);
                        onay.setActivated(true);
                        list.setIcon(MapHelperMethods.getApplyMarkerDrawable(mcontext));
                        Log.d(TAG, "changeMarker: " + MyclusterManager.getInstance().getMyClusterMarker());

                        /*Burada Markerlerin cluster olmassı(hepsinin birleşmesi ) durumunda asagıda bir hata meydana geliyor Programın
                        * durmaması için try catche aldım .*/
                        try {
                            MyclusterManager.getInstance().getMyClusterMarker().
                                    getMarker(list).setIcon(MapHelperMethods.getApplyMarkerDrawable(mcontext));

                        } catch (Exception e) {

                            e.printStackTrace();
                        }


                    } else {
                        list.setSave(item.isSave());
                    }

                    Log.d(TAG, "deneme2: " + list);
                    break;
                }


            }

        }

    }
}