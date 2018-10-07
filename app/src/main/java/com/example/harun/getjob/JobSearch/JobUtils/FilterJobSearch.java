package com.example.harun.getjob.JobSearch.JobUtils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.harun.getjob.AddJobAdvert.HelperStaticMethods;
import com.example.harun.getjob.R;
import com.example.harun.getjob.TagLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by mayne on 4.09.2018.
 */

public class FilterJobSearch extends BottomSheetDialogFragment implements View.OnClickListener, TextView.OnEditorActionListener {
    private static final String TAG = "FilterJobSearch";
    private TextView choosenSector, choosenJob,
            advertCount, advertCount1;
    private ViewSwitcher filterViewSwitch;
    private View filterTextView;

    private TagLayout filterContent;
    private Button ilkbtn, lisebtn, unibtn, digerbtn, tecrubesizBtn, tecrubeliBtn,
            tecrubeFarketmezBtn, stajBtn, partTimeBtn, fullTimeBtn, freeTimeBtn,
            ehliyetVarbtn, ehliyetFarketmezBtn, bayBtn, bayanBtn,
            cinsiyetFarketmezBtn, askerlikTecilliBtn, askerlikYapildiBtn, askerlikFarketmezBtn;
    private EditText tecrubeText, ehliyetTuru;
    private RelativeLayout egitimSeviyesiFore, egitimSeviyesiBack, tecrubeForeground, tecrubeBackground,
            calismaSekliFore, calismaSekliBack, ekstraFore, ekstraBack;
    private LinearLayout askerlikLayout;
    private ArrayList<View> clickedEgitimList = new ArrayList<>(1);
    private ArrayList<View> clickedTecrubeList = new ArrayList<>(1);
    private ArrayList<View> clickedCalismaList = new ArrayList<>(1);
    private ArrayList<View> clickedehliyetList = new ArrayList<>(1);
    private ArrayList<View> clickedCinsiyetList = new ArrayList<>(1);
    private ArrayList<View> clickedAskerlikList = new ArrayList<>(1);
    private ArrayList[] arrayLists =
            {
                    clickedCalismaList, clickedAskerlikList, clickedTecrubeList, clickedCinsiyetList,
                    clickedEgitimList, clickedehliyetList
            };
    private HashMap<String, Button> filteredButtonGroup = new HashMap<>();


    private NestedScrollView nestedScroll_step2;

    public FilterJobSearch() {
    }

//    @SuppressLint("ValidFragment")
//    public FilterJobSearch(String sektorName) {
//        Log.d(TAG, "FilterJobSearch: Construct" + sektorName);
//    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
        advertCount.setText(String.valueOf(MyclusterManager.getInstance().getMyClusterManager().getAlgorithm().getItems().size()));
//        for (NearJobAdvertModel advertModel : MyclusterManager.getInstance().getMyClusterManager().getAlgorithm().getItems()) {
//
//
//            Log.d(TAG, "onActivityCreated: " + advertModel);
//            Log.d(TAG, "onActivityCreated: " + advertModel.getJobSector());
//
//
//        }
        if (getArguments() != null) {
            choosenSector.setText(getArguments().getString("SektorName"));
            if (getArguments().getString("JobName") != null)
                choosenJob.setText(getArguments().getString("JobName"));
            else choosenJob.setText("Meslek Bilgisi Bilinmiyor");


        }


    }


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        Log.d(TAG, "setupDialog: ");
        View contentView = View.inflate(getContext(), R.layout.filterjob, null);
        dialog.setContentView(contentView);

        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();

        int width = (displayMetrics.widthPixels);
        int height = (displayMetrics.heightPixels);

        int maxHeight = (int) (height);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();


        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) contentView.getParent());
        mBehavior.setPeekHeight(maxHeight);

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(callback);
        }
        gatherViews(contentView);
    }

    private void gatherViews(View contentView) {

        choosenSector = contentView.findViewById(R.id.choosenSector);
        choosenJob = contentView.findViewById(R.id.choosenJob);
        //  advertCount = contentView.findViewById(R.id.advertCount);
        filterViewSwitch = contentView.findViewById(R.id.filterViewSwitch);
        filterContent = contentView.findViewById(R.id.filterContent);
        ilkbtn = contentView.findViewById(R.id.ilkbtn);
        lisebtn = contentView.findViewById(R.id.lisebtn);
        unibtn = contentView.findViewById(R.id.unibtn);
        digerbtn = contentView.findViewById(R.id.digerbtn);
        tecrubesizBtn = contentView.findViewById(R.id.tecrubesizBtn);
        tecrubeliBtn = contentView.findViewById(R.id.tecrubeliBtn);
        tecrubeText = contentView.findViewById(R.id.tecrubetext);
        tecrubeFarketmezBtn = contentView.findViewById(R.id.farketmezBtn);
        stajBtn = contentView.findViewById(R.id.stajBtn);
        fullTimeBtn = contentView.findViewById(R.id.fullTimeBtn);
        freeTimeBtn = contentView.findViewById(R.id.freeTimeBtn);
        partTimeBtn = contentView.findViewById(R.id.partTimeBtn);
        ehliyetVarbtn = contentView.findViewById(R.id.ehliyetVarbtn);
        ehliyetFarketmezBtn = contentView.findViewById(R.id.ehliyetFarketmezBtn);
        bayBtn = contentView.findViewById(R.id.bayBtn);
        bayanBtn = contentView.findViewById(R.id.bayanBtn);
        cinsiyetFarketmezBtn = contentView.findViewById(R.id.cinsiyetFarketmezBtn);
        ehliyetTuru = contentView.findViewById(R.id.ehliyetTuru);
        askerlikLayout = contentView.findViewById(R.id.askerlikLayout);
        askerlikTecilliBtn = contentView.findViewById(R.id.askerlikTecilliBtn);
        askerlikYapildiBtn = contentView.findViewById(R.id.askerlikYapildiBtn);
        askerlikFarketmezBtn = contentView.findViewById(R.id.askerlikFarketmezBtn);
        egitimSeviyesiFore = contentView.findViewById(R.id.egitimSeviyesiFore);
        egitimSeviyesiBack = contentView.findViewById(R.id.egitimSeviyesiBack);
        tecrubeForeground = contentView.findViewById(R.id.tecrubeForeground);
        tecrubeBackground = contentView.findViewById(R.id.tecrubeBackground);
        calismaSekliFore = contentView.findViewById(R.id.calismaSekliFore);
        calismaSekliBack = contentView.findViewById(R.id.calismaSekliBack);
        ekstraFore = contentView.findViewById(R.id.ekstraFore);
        ekstraBack = contentView.findViewById(R.id.ekstraBack);
        advertCount = contentView.findViewById(R.id.advertCount);
        init();

    }

    private void init() {
        ilkbtn.setOnClickListener(this);
        lisebtn.setOnClickListener(this);
        unibtn.setOnClickListener(this);
        digerbtn.setOnClickListener(this);
        tecrubesizBtn.setOnClickListener(this);
        stajBtn.setOnClickListener(this);
        freeTimeBtn.setOnClickListener(this);
        partTimeBtn.setOnClickListener(this);
        fullTimeBtn.setOnClickListener(this);
        tecrubeFarketmezBtn.setOnClickListener(this);
        tecrubeliBtn.setOnClickListener(this);
        //   tecrubeText.setOnFocusChangeListener(this);
        tecrubeText.setOnEditorActionListener(this);
        ehliyetTuru.setOnEditorActionListener(this);
        ehliyetVarbtn.setOnClickListener(this);
        ehliyetFarketmezBtn.setOnClickListener(this);
        askerlikTecilliBtn.setOnClickListener(this);
        askerlikYapildiBtn.setOnClickListener(this);
        askerlikFarketmezBtn.setOnClickListener(this);
        bayanBtn.setOnClickListener(this);
        bayBtn.setOnClickListener(this);
        cinsiyetFarketmezBtn.setOnClickListener(this);

        egitimSeviyesiFore.setOnClickListener(this);
        tecrubeForeground.setOnClickListener(this);
        calismaSekliFore.setOnClickListener(this);
        ekstraFore.setOnClickListener(this);


    }

    /**
     * Edittext için açılan keyboard tipi imeOptions=Action Done İçin tecrübe textine Yıl tecrüneli ekliyor
     *
     * @param textView
     * @param action
     * @param keyEvent
     * @return
     */
    @Override
    public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent) {

        Log.d(TAG, "onEditorAction:1 ");
        if ((action == EditorInfo.IME_ACTION_DONE)) {
            Log.d(TAG, "onEditorAction: 2");
            if (tecrubeText.getText().length() > 0 && !tecrubeText.getText().toString().contains(getString(R.string.yiltecrubeli))) {
                Log.d(TAG, "onEditorAction: 3");
                tecrubeText.append(getString(R.string.yiltecrubeli));

            }
            setKeyboardVisibility();
            tecrubeText.clearFocus();

            return true;
        }
        return false;
    }

    private BottomSheetBehavior.BottomSheetCallback callback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {

            if (newState == BottomSheetBehavior.STATE_HIDDEN) {

                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    /**
     * View lar  arasındaki Active olma durumunu kontol eden method
     * Method Sırasıyla su işlemleri yapıyor -->
     * tıklanan view i tuttugum listede eleman varsa o view aktif ise
     * aynı view'e(@param _currentView) tıklanmıyor ise(aktif olan ´view a  2.kez tıklanma durumu)
     * listedeki aktif olan tek view i false yapıyorum.listeyi temizliyorum.
     *
     * @param _clickedList-->tıklanan ögelerin bulundugu liste
     * @param currentView-->tıklanan  active olan button
     */
    private void clearOtherActiveButton(ArrayList<View> _clickedList, View currentView) {

        if (_clickedList.size() > 0) {
            Log.d(TAG, "clearOtherActiveButton: clickedEgitimList.size() > 0");
            if (_clickedList.get(0).isActivated() && _clickedList.get(0) != currentView) {

                Log.d(TAG, "clearOtherActiveButton: clickedEgitimList.get(0).isActivated() && clickedEgitimList.get(0) != activebutton");
                _clickedList.get(0).setActivated(false);
                _clickedList.get(0).clearFocus();
                _clickedList.clear();
            }

        } else {
            Log.d(TAG, "clearOtherActiveButton:Size<0 ");
        }

    }

    /**
     * Keyboard Acma Ve gizleme Methodu Yazılma Amacı Edittextlerin state Enable true olmasından dolayı klavye Tamam'a Basınca
     * kapanmıyor
     */
    private void setKeyboardVisibility() {
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(tecrubeText.getWindowToken(), 0);

        //   this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            //Egitim
            case R.id.lisebtn:
                Log.d(TAG, "onClick: ");
                lisebtn.setActivated(true);
                clearOtherActiveButton(clickedEgitimList, lisebtn);
                clickedEgitimList.add(lisebtn);
                editFilterGroup(lisebtn, "Eğitim");
                //  addFilter(lisebtn.getText().toString(), 1);

                break;
            case R.id.ilkbtn:
                Log.d(TAG, "onClick: ");
                ilkbtn.setActivated(true);
                clearOtherActiveButton(clickedEgitimList, ilkbtn);
                clickedEgitimList.add(ilkbtn);
                editFilterGroup(ilkbtn, "Eğitim");
                break;
            case R.id.unibtn:
                Log.d(TAG, "onClick: ");
                unibtn.setActivated(true);
                clearOtherActiveButton(clickedEgitimList, unibtn);
                clickedEgitimList.add(unibtn);
                editFilterGroup(unibtn, "Eğitim");

                break;
            case R.id.digerbtn:
                Log.d(TAG, "onClick: ");
                digerbtn.setActivated(true);
                clearOtherActiveButton(clickedEgitimList, digerbtn);
                clickedEgitimList.add(digerbtn);
                editFilterGroup(digerbtn, "Eğitim");

                break;
            //TECRUBE
            case R.id.tecrubesizBtn:
                Log.d(TAG, "onClick: ");
                tecrubesizBtn.setActivated(true);
                tecrubeText.setVisibility(View.GONE);
                clearOtherActiveButton(clickedTecrubeList, tecrubesizBtn);
                clickedTecrubeList.add(tecrubesizBtn);
                editFilterGroup(tecrubesizBtn, "Tecrübe");

                break;
            case R.id.tecrubeliBtn:
                Log.d(TAG, "onClick: ");
                tecrubeliBtn.setActivated(true);
                //tecrubeText.setVisibility(View.VISIBLE);
                clearOtherActiveButton(clickedTecrubeList, tecrubeliBtn);
                clickedTecrubeList.add(tecrubeliBtn);
                editFilterGroup(tecrubeliBtn, "Tecrübe");

                break;
            case R.id.farketmezBtn:
                Log.d(TAG, "onClick: ");
                tecrubeFarketmezBtn.setActivated(true);
                tecrubeText.setVisibility(View.GONE);
                clearOtherActiveButton(clickedTecrubeList, tecrubeFarketmezBtn);
                clickedTecrubeList.add(tecrubeFarketmezBtn);
                editFilterGroup(tecrubeFarketmezBtn, "Tecrübe");

                break;
            //Calisma Sekli
            case R.id.stajBtn:
                Log.d(TAG, "onClick: ");
                stajBtn.setActivated(true);
                clearOtherActiveButton(clickedCalismaList, stajBtn);
                clickedCalismaList.add(stajBtn);
                editFilterGroup(stajBtn, "Çalışma Şekli");

                break;
            case R.id.partTimeBtn:
                Log.d(TAG, "onClick: ");
                partTimeBtn.setActivated(true);
                clearOtherActiveButton(clickedCalismaList, partTimeBtn);
                clickedCalismaList.add(partTimeBtn);
                editFilterGroup(partTimeBtn, "Çalışma Şekli");

                break;
            case R.id.fullTimeBtn:
                Log.d(TAG, "onClick: ");
                fullTimeBtn.setActivated(true);
                clearOtherActiveButton(clickedCalismaList, fullTimeBtn);
                clickedCalismaList.add(fullTimeBtn);
                editFilterGroup(fullTimeBtn, "Çalışma Şekli");

                break;
            case R.id.freeTimeBtn:
                Log.d(TAG, "onClick: ");
                freeTimeBtn.setActivated(true);
                clearOtherActiveButton(clickedCalismaList, freeTimeBtn);
                clickedCalismaList.add(freeTimeBtn);
                editFilterGroup(freeTimeBtn, "Çalışma Şekli");

                break;
            //Ehliyet
            case R.id.ehliyetVarbtn:
                Log.d(TAG, "onClick: ");
                ehliyetVarbtn.setActivated(true);
                // ehliyetTuru.setVisibility(View.VISIBLE);
                ehliyetTuru.setActivated(true);


                //   nestedSmoothScroll();

//                nestedScroll_step2.smoothScrollBy(0,
//                        nestedScroll_step2.getChildAt(nestedScroll_step2.getChildCount() - 1).getBottom());
                clearOtherActiveButton(clickedehliyetList, ehliyetVarbtn);
                clickedehliyetList.add(ehliyetVarbtn);
                editFilterGroup(ehliyetVarbtn, "Ehliyet");
                break;
            case R.id.ehliyetFarketmezBtn:
                Log.d(TAG, "onClick: ");
                ehliyetFarketmezBtn.setActivated(true);
                ehliyetTuru.setVisibility(View.GONE);
                // nestedSmoothScroll();

//                nestedScroll_step2.smoothScrollBy(0,
//                        nestedScroll_step2.getChildAt(nestedScroll_step2.getChildCount() - 1).getBottom());
                clearOtherActiveButton(clickedehliyetList, ehliyetFarketmezBtn);
                clickedehliyetList.add(ehliyetFarketmezBtn);
                editFilterGroup(ehliyetFarketmezBtn, "Ehliyet");
                break;
            //Cinsiyet
            case R.id.bayanBtn:
                Log.d(TAG, "onClick: ");
                bayanBtn.setActivated(true);
                askerlikLayout.setVisibility(View.GONE);
                clickedAskerlikList.clear();
                clearOtherActiveButton(clickedCinsiyetList, bayanBtn);
                clickedCinsiyetList.add(bayanBtn);
                editFilterGroup(bayanBtn, "Cinsiyet");

                break;
            case R.id.bayBtn:
                Log.d(TAG, "onClick: ");
                bayBtn.setActivated(true);

                askerlikLayout.setVisibility(View.VISIBLE);
                //    nestedSmoothScroll();

//                nestedScroll_step2.smoothScrollBy(0,
//                        nestedScroll_step2.getChildAt(nestedScroll_step2.getChildCount() - 1).getBottom());
                clearOtherActiveButton(clickedCinsiyetList, bayBtn);
                clickedCinsiyetList.add(bayBtn);
                editFilterGroup(bayBtn, "Cinsiyet");

                break;
            case R.id.cinsiyetFarketmezBtn:
                Log.d(TAG, "onClick: ");
                cinsiyetFarketmezBtn.setActivated(true);

                askerlikLayout.setVisibility(View.VISIBLE);

//                nestedScroll_step2.smoothScrollBy(0,
//                        nestedScroll_step2.getChildAt(nestedScroll_step2.getChildCount() - 1).getBottom());
                //     nestedSmoothScroll();

                clearOtherActiveButton(clickedCinsiyetList, cinsiyetFarketmezBtn);
                clickedCinsiyetList.add(cinsiyetFarketmezBtn);
                editFilterGroup(cinsiyetFarketmezBtn, "Cinsiyet");

                break;
            //Askerlik
            case R.id.askerlikTecilliBtn:
                Log.d(TAG, "onClick: ");
                askerlikTecilliBtn.setActivated(true);
                clearOtherActiveButton(clickedAskerlikList, askerlikTecilliBtn);
                clickedAskerlikList.add(askerlikTecilliBtn);
                editFilterGroup(askerlikTecilliBtn, "Askerlik");

                break;
            case R.id.askerlikYapildiBtn:
                Log.d(TAG, "onClick: ");
                askerlikYapildiBtn.setActivated(true);
                clearOtherActiveButton(clickedAskerlikList, askerlikYapildiBtn);
                clickedAskerlikList.add(askerlikYapildiBtn);
                editFilterGroup(askerlikYapildiBtn, "Askerlik");

                break;
            case R.id.askerlikFarketmezBtn:
                Log.d(TAG, "onClick: ");
                askerlikFarketmezBtn.setActivated(true);
                clearOtherActiveButton(clickedAskerlikList, askerlikFarketmezBtn);
                clickedAskerlikList.add(askerlikFarketmezBtn);
                editFilterGroup(askerlikFarketmezBtn, "Askerlik");

                break;

            //Step2 Foreground
            case R.id.egitimSeviyesiFore:
                Log.d(TAG, "onClick: ");
                checkVisibilityLayout(egitimSeviyesiBack);
                break;
            case R.id.tecrubeForeground:
                Log.d(TAG, "onClick: ");
                checkVisibilityLayout(tecrubeBackground);
                break;
            case R.id.calismaSekliFore:
                Log.d(TAG, "onClick: ");
                checkVisibilityLayout(calismaSekliBack);
                break;
            case R.id.ekstraFore:
                Log.d(TAG, "onClick: ");
                checkVisibilityLayout(ekstraBack);
                //     nestedSmoothScroll();

//                nestedScroll_step2.smoothScrollBy(0,
//                        nestedScroll_step2.getChildAt(nestedScroll_step2.getChildCount()).getBottom());
                break;

            //Eklenen Filtrenin tagları
            //Tag Textleri için Dinamik ıd ataması yapıldıgından case 0 1 2 3 4 addFilters-> methodu setId ve setonClick
            case 0:
                Log.d(TAG, "onClick: " + view.getId());
                removeViewFilterContent(view);
                break;
            case 1:
                Log.d(TAG, "onClick: " + view.getId());
                removeViewFilterContent(view);
                break;
            case 2:
                Log.d(TAG, "onClick: " + view.getId());
                removeViewFilterContent(view);
                break;
            case 3:
                Log.d(TAG, "onClick: " + view.getId());
                removeViewFilterContent(view);
                break;
            case 4:
                Log.d(TAG, "onClick: " + view.getId());
                removeViewFilterContent(view);
                break;
            case 5:
                Log.d(TAG, "onClick: " + view.getId());
                removeViewFilterContent(view);
                break;
            case 6:
                Log.d(TAG, "onClick: " + view.getId());
                removeViewFilterContent(view);
                break;
        }

    }

    /**
     * Hangi tag tıklanmıs ise Yani bir filtre eklendiğinde eklenen tagın üzerine tıklandıgında bu method calısacak ve o tag
     * kaldırılacak(silinecek) ve removeItemFilteredGroup methoduna tagın baslığını parametre olarak gönderecek.
     *
     * @param view
     */
    private void removeViewFilterContent(View view) {

        ((ViewGroup) view.getParent()).removeView(view);
        removeItemFilteredGroupHash(view.getTag());

    }

    /**
     * Tıklanan view 'in silinecek kaldırılacak tagın filteredButtonGroup hashmapinden silinme işlemi gerçekleşiyor ve
     * o tag'ı içeren butonun aktifliği kaldırılıyor
     * Yani ben Eğitim alanında  Üniversite Mezunu butonuna tıklayarak  bir filtre ekledim Bu filtre filterContent'e tag olarak ekleniyor eger bu tagı
     * kaldırmak istersem üzerine tıklıyorum önce onu filterContent'en kaldırıp ardından ÜniversiteMezunu butonunu aktiflikten cıkarıyor
     *
     * @param view
     */
    private void removeItemFilteredGroupHash(Object view) {

        Log.d(TAG, "removeItemFilteredGroup: " + view);

        filteredButtonGroup.get(view).setActivated(false);
        filteredButtonGroup.remove(view);

        switch (view.toString()) {
            case "Eğitim":
                Log.d(TAG, "removeItemFilteredGroup: CLİCKED EGİTİM LİSTE TEMİZLENDİ ");
                clickedEgitimList.clear();
                break;
            case "Tecrübe":
                Log.d(TAG, "removeItemFilteredGroup: CLİCKED EGİTİM LİSTE TEMİZLENDİ ");
                clickedTecrubeList.clear();
                break;
            case "Askerlik":
                Log.d(TAG, "removeItemFilteredGroup: CLİCKED EGİTİM LİSTE TEMİZLENDİ ");
                clickedAskerlikList.clear();
                break;
            case "Cinsiyet":
                Log.d(TAG, "removeItemFilteredGroup: CLİCKED EGİTİM LİSTE TEMİZLENDİ ");
                clickedCinsiyetList.clear();
                break;
            case "Çalışma Şekli":
                Log.d(TAG, "removeItemFilteredGroup: CLİCKED EGİTİM LİSTE TEMİZLENDİ ");
                clickedCalismaList.clear();
                break;
            case "Ehliyet":
                Log.d(TAG, "removeItemFilteredGroup: CLİCKED EGİTİM LİSTE TEMİZLENDİ ");
                clickedehliyetList.clear();
                break;


        }
        Log.d(TAG, "removeItemFilteredGroup: " + filteredButtonGroup.keySet());


    }

    /**
     * Bu methodda kullanıcının ilanı filtrelemek için seçmiş oldugu
     * Örn(Üniversite Mezunu butonu veya Tecrübesiz Butonu ) butonu
     * türüne göre hashleme işlemi yapılıyor
     * Kullanıcı hangisine tıklarsa her type değeri için o buton aktif olmus oluyor filtrelemek için
     *
     * @param filteredButton-->filtrelemek için kullanılan buton
     * @param type                         --> Eğitim  Tecrübe  Çalışma Şekli Ehliyet  Cinsiyer  Askerlik
     */
    private void editFilterGroup(Button filteredButton, String type) {
        Log.d(TAG, "editFilterGroup: " + filteredButton + "\t" + type);
        filteredButtonGroup.put(type, filteredButton);
        // Log.d(TAG, "editFilterGroup:"+ filteredButtonGroup.values());
        Log.d(TAG, "editFilterGroup:ENTRYSET " + filteredButtonGroup.entrySet());
        Log.d(TAG, "editFilterGroup:VALUES " + filteredButtonGroup.values());
        // addFilters( filteredButtonGroup.values());
        addFilters(filteredButtonGroup.entrySet());
    }

    /**
     * Bu metod her cagırıldıgında önce filterContent taglayoutunu temizilyor Ardından Hashmap İçerisindeki key ve value değerlerini
     * Custom bir textview oluşturup manuel olarak taglayout içine ekliyor
     *
     * @param entries
     */
    private void addFilters(Set<Map.Entry<String, Button>> entries) {
        Log.d(TAG, "addFilters: " + entries.toString());
        filterViewSwitch.setDisplayedChild(1);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        filterContent.removeAllViews();
        int i = 0;
        //     filterContent.forceLayout();
//        for (Iterator<Button> iterator = values.iterator(); iterator.hasNext(); ) {
//            Log.d(TAG, "addFilters: "+iterator.next().getText().toString());
//            filterText.setText(iterator.next().getText().toString());
//            HelperStaticMethods.setMargins(filterText, 5, 5, 5, 0);
//            filterContent.addView(filterTextView);
//        }e

        for (Map.Entry<String, Button> text : entries) {
            Log.d(TAG, "addFilters: i Değeri--> " + i);
            filterTextView = layoutInflater.inflate(R.layout.filtertextview, null, false);
            TextView filterText = filterTextView.findViewById(R.id.tagText);
            Log.d(TAG, "addFilters: " + text.getKey() + text.getValue().getText().toString());
            filterText.setText(text.getKey() + "/" + text.getValue().getText().toString());
            filterTextView.setId(i);
            filterTextView.setTag(text.getKey());
            filterTextView.setOnClickListener(this);
            i++;
            Log.d(TAG, "addFilters: i değeri" + i);
            HelperStaticMethods.setMargins(filterText, 5, 5, 5, 0);
            filterContent.addView(filterTextView);
        }


    }
//
//    private void addFilter(String text, int mode) {
//
//        Log.d(TAG, "addPossibility: ");
//        filterViewSwitch.setDisplayedChild(1);
//        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        filterTextView = layoutInflater.inflate(R.layout.pos_textview, null, false);
//        TextView filterText = filterTextView.findViewById(R.id.tagText);
////        if (mode == 1) {
////
////
////        }
//        filterText.setText(text);
//        HelperStaticMethods.setMargins(filterText, 5, 5, 5, 0);
//        filterContent.addView(filterTextView);
//
//
//    }

    /**
     * Bu methodda parametre olarak gelen layouta bir animasyon ekliypruz ve visibility ayarlarını yapıyoruz.
     *
     * @param _background->hangi layout ile işlem yapıcagımızı parametre olarak belirliyourz
     */
    private void checkVisibilityLayout(RelativeLayout _background) {

        if (!_background.isShown()) {
            LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_anim2);
            _background.setLayoutAnimation(layoutAnimationController);
            _background.setVisibility(View.VISIBLE);
        } else {

//            LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation);
//            _background.setLayoutAnimation(layoutAnimationController);
            _background.setVisibility(View.GONE);

        }

    }

    private void nestedSmoothScroll() {
        nestedScroll_step2.smoothScrollBy(0,
                nestedScroll_step2.getChildAt(nestedScroll_step2.getChildCount() - 1).getBottom());
    }


/*
    public class NestedScrollableViewHelper extends ScrollableViewHelper {
        public int getScrollableViewScrollPosition(View scrollableView, boolean isSlidingUp) {
            if (mScrollableView instanceof NestedScrollView) {
                if(isSlidingUp){
                    return mScrollableView.getScrollY();
                } else {
                    NestedScrollView nsv = ((NestedScrollView) mScrollableView);
                    View child = nsv.getChildAt(0);
                    return (child.getBottom() - (nsv.getHeight() + nsv.getScrollY()));
                }
            } else {
                return 0;
            }
        }
    }



*/

  /*  @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmentjoblist, container, false);

        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        int maxHeight = (int) (height * 0.88);

        // getDialog().getWindow().setDimAmount(height);
//        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) v.getParent());
        //  mBehavior.setPeekHeight(maxHeight);

        return v;


    }*/


}

