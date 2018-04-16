package com.example.harun.getjob.JobSearch.JobUtils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

import org.altervista.andrearosa.statebutton.StateButton;

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
    FragmentManager fragmentManager;
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
        holder.bindMapLocation(position);
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


        holder.btnBasvur.setOnClickListener(new View.OnClickListener() {
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


    public class MyViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        View viewleft;
        TextView companyname, tvbasvuru_count, tvjobname, tvjobtype, publishdate, tvexperience, tvdistance, tvdetail;
        ImageView companyLogo1, detail_image, onay, save_this_advert;
        RelativeLayout showDetails;
        RelativeLayout expandcard;
        StateButton btnBasvur;
        public MapView mapview;
        public GoogleMap mMap;

        public MyViewHolder(View itemView) {
            super(itemView);

            viewleft = itemView.findViewById(R.id.viewleft);
            companyname = itemView.findViewById(R.id.companyname);
            companyLogo1 = itemView.findViewById(R.id.companyLogo1);
            tvbasvuru_count = itemView.findViewById(R.id.tvbasvuru_count);
            tvjobname = itemView.findViewById(R.id.tvjobname);
            //   tvjobtype = itemView.findViewById(R.id.tvjobtype);
            onay = itemView.findViewById(R.id.onay);
            publishdate = itemView.findViewById(R.id.publishdate);
            tvexperience = itemView.findViewById(R.id.tvexperience);
            tvdistance = itemView.findViewById(R.id.tvdistance);
            showDetails = itemView.findViewById(R.id.details);
            expandcard = itemView.findViewById(R.id.expandcard);
            detail_image = itemView.findViewById(R.id.showdetails);
            tvdetail = itemView.findViewById(R.id.tvdetail);
            btnBasvur = itemView.findViewById(R.id.basvurbtn);
            save_this_advert = itemView.findViewById(R.id.save_this_advert);
            mapview = itemView.findViewById(R.id.mapView);

            if (mapview != null) {
                Log.d(TAG, "MyViewHolder: ");
                mapview.onCreate(null);
                mapview.getMapAsync(this);
            }


        }


        @Override
        public void onMapReady(GoogleMap googleMap) {
            Log.d(TAG, "onMapReady: ");
            MapsInitializer.initialize(mcontext);
            mMap = googleMap;
            // mMap.getUiSettings().setZoomControlsEnabled(true);
            // mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(true);

            setMapLocation();
        }

        public void setMapLocation() {
            JobAdvertModel mjobAdvertModel = (JobAdvertModel) mapview.getTag();
            if (mjobAdvertModel == null) return;
            // Log.d(TAG, "setMapLocation: " + mjobAdvertModel.getPosition());
            if (mMap == null) return;
            mMap.addMarker(new MarkerOptions().position(mjobAdvertModel.getPosition())
                    .icon(MapHelperMethods.getMarkerDrawable(mcontext))
                    .title(mJobAdvertModel.getCompanyName()+"\n".concat(mjobAdvertModel.getCompanyJob())));
            ;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mjobAdvertModel.getPosition(), 15f));
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

        private void bindMapLocation(int position) {
            JobAdvertModel mjobAdvertModel = jobAdvertModelArrayList.get(position);
            //  Log.d(TAG, "bindMapLocation: "+mjobAdvertModel);
            mapview.setTag(mjobAdvertModel);
            setMapLocation();
        }


    }

}
