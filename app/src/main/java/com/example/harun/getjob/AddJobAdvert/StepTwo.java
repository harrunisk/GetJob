package com.example.harun.getjob.AddJobAdvert;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.harun.getjob.JobSearch.JobUtils.JobAdvertModel2;
import com.example.harun.getjob.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;

/**
 * Created by mayne on 1.05.2018.
 */

public class StepTwo extends Fragment implements Step, View.OnClickListener, TextWatcher, View.OnFocusChangeListener, TextView.OnEditorActionListener {

    private static final String TAG = "StepTwo";
    Button ilkbtn, lisebtn, unibtn, digerbtn, tecrubesizBtn, tecrubeliBtn,
            tecrubeFarketmezBtn, stajBtn, partTimeBtn, fullTimeBtn, freeTimeBtn,
            ehliyetVarbtn, ehliyetFarketmezBtn, bayBtn, bayanBtn,
            cinsiyetFarketmezBtn, askerlikTecilliBtn, askerlikYapildiBtn, askerlikFarketmezBtn;
    EditText tecrubeText, ehliyetTuru;
    RelativeLayout egitimSeviyesiFore, egitimSeviyesiBack, tecrubeForeground, tecrubeBackground,
            calismaSekliFore, calismaSekliBack, ekstraFore, ekstraBack;
    boolean textChange = false;

    String tecrube;
    LinearLayout askerlikLayout;
    NestedScrollView nestedScroll_step2;
    ArrayList<View> clickedEgitimList = new ArrayList<>(1);
    ArrayList<View> clickedTecrubeList = new ArrayList<>(1);
    ArrayList<View> clickedCalismaList = new ArrayList<>(1);
    ArrayList<View> clickedehliyetList = new ArrayList<>(1);
    ArrayList<View> clickedCinsiyetList = new ArrayList<>(1);
    ArrayList<View> clickedAskerlikList = new ArrayList<>(1);
    ArrayList[] arrayLists =
            {
                    clickedCalismaList, clickedAskerlikList, clickedTecrubeList, clickedCinsiyetList,
                    clickedEgitimList, clickedehliyetList
            };

    private JobAdvertModel2 model2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View v = inflater.inflate(R.layout.add_jobadvert_step2, container, false);

        gatherViews(v);

        return v;
    }

    private void gatherViews(View v) {
        Log.d(TAG, "gatherViews: ");
        ilkbtn = v.findViewById(R.id.ilkbtn);
        lisebtn = v.findViewById(R.id.lisebtn);
        unibtn = v.findViewById(R.id.unibtn);
        digerbtn = v.findViewById(R.id.digerbtn);
        tecrubesizBtn = v.findViewById(R.id.tecrubesizBtn);
        tecrubeliBtn = v.findViewById(R.id.tecrubeliBtn);
        tecrubeText = v.findViewById(R.id.tecrubetext);
        tecrubeFarketmezBtn = v.findViewById(R.id.farketmezBtn);
        stajBtn = v.findViewById(R.id.stajBtn);
        fullTimeBtn = v.findViewById(R.id.fullTimeBtn);
        freeTimeBtn = v.findViewById(R.id.freeTimeBtn);
        partTimeBtn = v.findViewById(R.id.partTimeBtn);
        ehliyetVarbtn = v.findViewById(R.id.ehliyetVarbtn);
        ehliyetFarketmezBtn = v.findViewById(R.id.ehliyetFarketmezBtn);
        bayBtn = v.findViewById(R.id.bayBtn);
        bayanBtn = v.findViewById(R.id.bayanBtn);
        cinsiyetFarketmezBtn = v.findViewById(R.id.cinsiyetFarketmezBtn);
        ehliyetTuru = v.findViewById(R.id.ehliyetTuru);
        askerlikLayout = v.findViewById(R.id.askerlikLayout);
        askerlikTecilliBtn = v.findViewById(R.id.askerlikTecilliBtn);
        askerlikYapildiBtn = v.findViewById(R.id.askerlikYapildiBtn);
        askerlikFarketmezBtn = v.findViewById(R.id.askerlikFarketmezBtn);

        egitimSeviyesiFore = v.findViewById(R.id.egitimSeviyesiFore);
        egitimSeviyesiBack = v.findViewById(R.id.egitimSeviyesiBack);
        tecrubeForeground = v.findViewById(R.id.tecrubeForeground);
        tecrubeBackground = v.findViewById(R.id.tecrubeBackground);
        calismaSekliFore = v.findViewById(R.id.calismaSekliFore);
        calismaSekliBack = v.findViewById(R.id.calismaSekliBack);
        ekstraFore = v.findViewById(R.id.ekstraFore);
        ekstraBack = v.findViewById(R.id.ekstraBack);


        nestedScroll_step2 = v.findViewById(R.id.nestedScroll_step2);


    }

    @SuppressLint("UseSparseArrays")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        if (getArguments() != null) {
            Log.d(TAG, "onViewCreated: getArguments() != null ");
            model2 = getArguments().getParcelable("jobAdvertModel");
        }

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

    private void setJobDetails() {
        //burada liste bos kontrolleri yapılacak .
        Log.d(TAG, "setJobDetails: ");
        model2.setEducationLevel(((Button) clickedEgitimList.get(0)).getText().toString());
        model2.setGender(((Button) clickedCinsiyetList.get(0)).getText().toString());
        model2.setEmployeeHour(((Button) clickedCalismaList.get(0)).getText().toString());

        model2.setMilitary(
                clickedAskerlikList.isEmpty() ? "" : ((Button) clickedAskerlikList.get(0)).getText().toString());

        if (clickedTecrubeList.get(0) == tecrubeliBtn) {
            model2.setExpLevel(tecrubeText.getText().toString().concat("Yıl-") + ((Button) clickedTecrubeList.get(0)).getText().toString());
        } else {
            model2.setExpLevel(((Button) clickedTecrubeList.get(0)).getText().toString());
        }

        if (clickedehliyetList.get(0) == ehliyetVarbtn) {

            model2.setDrivingLicence(ehliyetTuru.getText().toString().toUpperCase().concat(" Tipi"));

        } else {

            model2.setDrivingLicence(((Button) clickedehliyetList.get(0)).getText().toString());
        }


    }

    @Nullable
    @Override
    public VerificationError verifyStep() {

        for (int i = 0; i < arrayLists.length; i++) {
            Log.d(TAG, "verifyStep:arrayLists.length " + arrayLists.length);

            if (validate(arrayLists[i])) {
                Log.d(TAG, "verifyStep: ");
                errorAnim(arrayLists[i]);
                return new VerificationError("Tüm secenekleri işaretleyiniz!");
            }
        }

        setJobDetails();
        return null;
    }

    private void errorAnim(ArrayList empty_arrayList) {
        Log.d(TAG, "errorAnim: ");
        if (empty_arrayList == clickedehliyetList) {
            Log.d(TAG, "errorAnim:clickedehliyetList ");
            ekstraFore.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.error_anim));

        } else if (empty_arrayList == clickedAskerlikList) {
            Log.d(TAG, "errorAnim:clickedAskerlikList ");
            ekstraFore.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.error_anim));
        } else if (empty_arrayList == clickedCinsiyetList) {
            Log.d(TAG, "errorAnim:clickedCinsiyetList ");
            ekstraFore.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.error_anim));

        } else if (empty_arrayList == clickedCalismaList) {
            Log.d(TAG, "errorAnim:clickedCalismaList ");
            calismaSekliFore.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.error_anim));

        } else if (empty_arrayList == clickedEgitimList) {
            Log.d(TAG, "errorAnim:clickedEgitimList ");
            egitimSeviyesiFore.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.error_anim));

        } else if (empty_arrayList == clickedTecrubeList) {
            Log.d(TAG, "errorAnim:clickedTecrubeList ");
            tecrubeForeground.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.error_anim));

        }

    }

    /*LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getActivity(),
            R.anim.error_anim);
    Log.d(TAG, "errorAnim: " + relativeLayoutArrayListHashMap.size());
    Log.d(TAG, "errorAnim: " + arrayList.size());
//        Log.d(TAG, "errorAnim: " + relativeLayoutArrayListHashMap.get(arrayList).toString());
    relativeLayoutArrayListHashMap.get(arrayList).startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.error_anim));


}
*/
    private boolean validate(ArrayList _arrayList) {
        Log.d(TAG, "validate: " + _arrayList);

        if (_arrayList.isEmpty()) {
            Log.d(TAG, "validate: arrayList BOŞŞŞ" + _arrayList);
            if (clickedCinsiyetList.contains(bayanBtn) && _arrayList == clickedAskerlikList) {
                Log.d(TAG, "verifyStep: arrayLists[i].contains(bayanBtn) || arrayLists[i] == clickedCinsiyetList");
                return false;
            }
            // errorAnim(_arrayList);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onSelected() {
        Log.d(TAG, "onSelected: ");
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Log.d(TAG, "onError: ");
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
                break;
            case R.id.ilkbtn:
                Log.d(TAG, "onClick: ");
                ilkbtn.setActivated(true);
                clearOtherActiveButton(clickedEgitimList, ilkbtn);
                clickedEgitimList.add(ilkbtn);
                break;
            case R.id.unibtn:
                Log.d(TAG, "onClick: ");
                unibtn.setActivated(true);
                clearOtherActiveButton(clickedEgitimList, unibtn);
                clickedEgitimList.add(unibtn);
                break;
            case R.id.digerbtn:
                Log.d(TAG, "onClick: ");
                digerbtn.setActivated(true);
                clearOtherActiveButton(clickedEgitimList, digerbtn);
                clickedEgitimList.add(digerbtn);
                break;
            //TECRUBE
            case R.id.tecrubesizBtn:
                Log.d(TAG, "onClick: ");
                tecrubesizBtn.setActivated(true);
                tecrubeText.setVisibility(View.GONE);
                clearOtherActiveButton(clickedTecrubeList, tecrubesizBtn);
                clickedTecrubeList.add(tecrubesizBtn);
                break;
            case R.id.tecrubeliBtn:
                Log.d(TAG, "onClick: ");
                tecrubeliBtn.setActivated(true);
                tecrubeText.setVisibility(View.VISIBLE);
                clearOtherActiveButton(clickedTecrubeList, tecrubeliBtn);
                clickedTecrubeList.add(tecrubeliBtn);
                break;
            case R.id.farketmezBtn:
                Log.d(TAG, "onClick: ");
                tecrubeFarketmezBtn.setActivated(true);
                tecrubeText.setVisibility(View.GONE);
                clearOtherActiveButton(clickedTecrubeList, tecrubeFarketmezBtn);
                clickedTecrubeList.add(tecrubeFarketmezBtn);
                break;
            //Calisma Sekli
            case R.id.stajBtn:
                Log.d(TAG, "onClick: ");
                stajBtn.setActivated(true);
                clearOtherActiveButton(clickedCalismaList, stajBtn);
                clickedCalismaList.add(stajBtn);
                break;
            case R.id.partTimeBtn:
                Log.d(TAG, "onClick: ");
                partTimeBtn.setActivated(true);
                clearOtherActiveButton(clickedCalismaList, partTimeBtn);
                clickedCalismaList.add(partTimeBtn);
                break;
            case R.id.fullTimeBtn:
                Log.d(TAG, "onClick: ");
                fullTimeBtn.setActivated(true);
                clearOtherActiveButton(clickedCalismaList, fullTimeBtn);
                clickedCalismaList.add(fullTimeBtn);
                break;
            case R.id.freeTimeBtn:
                Log.d(TAG, "onClick: ");
                freeTimeBtn.setActivated(true);
                clearOtherActiveButton(clickedCalismaList, freeTimeBtn);
                clickedCalismaList.add(freeTimeBtn);
                break;
            //Ehliyet
            case R.id.ehliyetVarbtn:
                Log.d(TAG, "onClick: ");
                ehliyetVarbtn.setActivated(true);
                ehliyetTuru.setVisibility(View.VISIBLE);
                ehliyetTuru.setActivated(true);

                nestedScroll_step2.smoothScrollBy(0,
                        nestedScroll_step2.getChildAt(nestedScroll_step2.getChildCount() - 1).getBottom());
                clearOtherActiveButton(clickedehliyetList, ehliyetVarbtn);
                clickedehliyetList.add(ehliyetVarbtn);
                break;
            case R.id.ehliyetFarketmezBtn:
                Log.d(TAG, "onClick: ");
                ehliyetFarketmezBtn.setActivated(true);
                ehliyetTuru.setVisibility(View.GONE);

                nestedScroll_step2.smoothScrollBy(0,
                        nestedScroll_step2.getChildAt(nestedScroll_step2.getChildCount() - 1).getBottom());
                clearOtherActiveButton(clickedehliyetList, ehliyetFarketmezBtn);
                clickedehliyetList.add(ehliyetFarketmezBtn);
                break;
            //Cinsiyet
            case R.id.bayanBtn:
                Log.d(TAG, "onClick: ");
                bayanBtn.setActivated(true);
                askerlikLayout.setVisibility(View.GONE);
                clearOtherActiveButton(clickedCinsiyetList, bayanBtn);
                clickedCinsiyetList.add(bayanBtn);
                break;
            case R.id.bayBtn:
                Log.d(TAG, "onClick: ");
                bayBtn.setActivated(true);

                askerlikLayout.setVisibility(View.VISIBLE);

                nestedScroll_step2.smoothScrollBy(0,
                        nestedScroll_step2.getChildAt(nestedScroll_step2.getChildCount() - 1).getBottom());
                clearOtherActiveButton(clickedCinsiyetList, bayBtn);
                clickedCinsiyetList.add(bayBtn);
                break;
            case R.id.cinsiyetFarketmezBtn:
                Log.d(TAG, "onClick: ");
                cinsiyetFarketmezBtn.setActivated(true);

                askerlikLayout.setVisibility(View.VISIBLE);

                nestedScroll_step2.smoothScrollBy(0,
                        nestedScroll_step2.getChildAt(nestedScroll_step2.getChildCount() - 1).getBottom());

                clearOtherActiveButton(clickedCinsiyetList, cinsiyetFarketmezBtn);
                clickedCinsiyetList.add(cinsiyetFarketmezBtn);
                break;
            //Askerlik
            case R.id.askerlikTecilliBtn:
                Log.d(TAG, "onClick: ");
                askerlikTecilliBtn.setActivated(true);
                clearOtherActiveButton(clickedAskerlikList, askerlikTecilliBtn);
                clickedAskerlikList.add(askerlikTecilliBtn);
                break;
            case R.id.askerlikYapildiBtn:
                Log.d(TAG, "onClick: ");
                askerlikYapildiBtn.setActivated(true);
                clearOtherActiveButton(clickedAskerlikList, askerlikYapildiBtn);
                clickedAskerlikList.add(askerlikYapildiBtn);
                break;
            case R.id.askerlikFarketmezBtn:
                Log.d(TAG, "onClick: ");
                askerlikFarketmezBtn.setActivated(true);
                clearOtherActiveButton(clickedAskerlikList, askerlikFarketmezBtn);
                clickedAskerlikList.add(askerlikFarketmezBtn);
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
                nestedScroll_step2.smoothScrollBy(0,
                        nestedScroll_step2.getChildAt(nestedScroll_step2.getChildCount() - 1).getBottom());
                break;
        }


    }

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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.d(TAG, "beforeTextChanged: ");

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.d(TAG, "onTextChanged: " + charSequence);

    }

    @Override
    public void afterTextChanged(Editable editable) {
        Log.d(TAG, "afterTextChanged: " + editable.toString());

        tecrubeText.removeTextChangedListener(this);
        tecrubeText.append("Yıl Tecrübeli");
        tecrubeText.addTextChangedListener(this);


    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            Log.d(TAG, "onFocusChange: ");
            //   tecrubeText.setActivated(true);
            clearOtherActiveButton(clickedTecrubeList, tecrubeText);
            clickedTecrubeList.add(tecrubeText);
            //tecrubeText.addTextChangedListener(this);
            //  textChange = false;
            //  tecrubeText.append(" Yıl Tecrübeli");
        }
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

    /**
     * Keyboard Acma Ve gizleme Methodu Yazılma Amacı Edittextlerin state Enable true olmasından dolayı klavye Tamam'a Basınca
     * kapanmıyor
     */
    private void setKeyboardVisibility() {
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(tecrubeText.getWindowToken(), 0);

        //   this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }


}
