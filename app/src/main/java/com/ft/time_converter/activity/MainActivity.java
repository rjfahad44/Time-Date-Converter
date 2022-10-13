package com.ft.time_converter.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.InterstitialAdListener;
import com.ft.time_converter.BuildConfig;
import com.ft.time_converter.R;
import com.ft.time_converter.adapter.TimeDateRecyclerViewAdapter;
import com.ft.time_converter.adapter.TimeDateSearchItemAdapter;
import com.ft.time_converter.dao.TimeDateDao;
import com.ft.time_converter.database.AppDatabase;
import com.ft.time_converter.listener.OnItemClickListener;
import com.ft.time_converter.listener.OnItemDeleteClickListener;
import com.ft.time_converter.listener.OnSetDateTimeClickListener;
import com.ft.time_converter.modles.TimeZoneListModel;
import com.ft.time_converter.modles.TimeZoneSearchListModel;
import com.ft.time_converter.setting.TimeZoneSettings;
import com.ft.time_converter.utils.Utils;
import com.ft.time_converter.viewmodel.TimeDateViewModel;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements OnItemClickListener, OnItemDeleteClickListener, OnSetDateTimeClickListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private RecyclerView searchRecyclerView, mainRecyclerView;
    private TimeDateSearchItemAdapter searchAdapter;
    private TimeDateRecyclerViewAdapter timeDateRecyclerViewAdapter;
    private TimeDateViewModel timeDateViewModel;
    private TextView utc_date;
    private TextView utc_time;
    private LinearLayout linearLayout;
    private ImageView refreshImgBtn;
    private ImageView deleteImgBtn;
    private TextView show_text_tv;
    private EditText searchEdt;
    private ImageButton plusBtn;
    private CheckBox checkBoxUTCTime;
    private CheckBox checkBoxTimeZone;
    private CheckBox checkBoxHolidayAndOffice;
    private CheckBox checkBoxCurrentTime;
    private CheckBox checkBoxTimeDifference;
    private CheckBox timeFormatChangeCheckBoxId;
    private RelativeLayout relativeLayoutAboutId;
    private TextView removeTv;
    private RelativeLayout userSetTimeManuallyAlertRelativeLayout;
    private RelativeLayout userGuidelineRelativeLayoutId;

    private List<TimeZoneSearchListModel> timeZoneListModels;
    private TimeDateDao timeDateDao;
    private TimeZoneSettings settings;

    private Dialog timeDatePickerDialog;
    private Dialog exitDialog;
    private Dialog deleteDialog;
    private Dialog deleteAllZoneDialog;
    private Dialog refreshDialog;
    private boolean isDate = true;
    private boolean isTime = true;
    private boolean isDateTimeChanged = false;

    private InterstitialAd AdMobInterstitialAd;
    private com.facebook.ads.InterstitialAd faceBookInterstitialAd;

    public static final int ITEMS_PER_AD = 3;
    private final ArrayList<Object> AddListItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //widget Initialize//
        initWidgets();

        //AdMob Ads SDK Setup//
        initAdMobAdsSDK();

        //Admob InterstitialAds Initialize//
        initAdmobInterstitialAds();

        //Facebook InterstitialAds Show//
        initFacebookInterstitialAds();

        //BannerAds Initialize//
        bannerAds();

        //load data and show into the recyclerview//
        loadData();

        //recyclerview setup//
        recyclerViewShowAddZoneListSetup();

        //AdMob Banner Ads Initialize//
        addAdMobBannerAds();

        //adapter setup //
        adapterSetup();

        //Load Banner Ads//
        loadBannerAds();

        //load Settings SharePreferences //
        loadSharePreference();

        //checkBoxClickListener//
        checkBoxClickListener();

        //Custom Toolbar//
        customToolbar();

        //search data methode//
        searchData();

        //Refresh View when refresh button click//
        refreshButtonClick();

        //load UTC Date Time//
        loadUtcDateTime();

        //about section//
        about();

        //userGuideline//
        userGuideline();

        //clear all zone list//
        clearAllZone();

        searchRecyclerViewSetup();
    }

    private void showUserGuideline() {
        //initialized sharePreference 1st time show user guid line//
        SharedPreferences sp = getSharedPreferences("USER_GUIDELINE_SHOW_FIRST_TIME", MODE_PRIVATE);
        if (!sp.getBoolean("isFirstTime", false)) {
            SharedPreferences.Editor editor = getSharedPreferences("USER_GUIDELINE_SHOW_FIRST_TIME", MODE_PRIVATE).edit();
            editor.putBoolean("isFirstTime", true);
            editor.apply();
        }
    }

    private void initAdMobAdsSDK() {
        MobileAds.initialize(this, (InitializationStatus initializationStatus) -> {
        });
    }

    private void initWidgets() {
        settings = (TimeZoneSettings) getApplication();
        timeDatePickerDialog = new Dialog(this);
        exitDialog = new Dialog(this);
        deleteDialog = new Dialog(this);
        deleteAllZoneDialog = new Dialog(this);
        refreshDialog = new Dialog(this);
        timeZoneListModels = new ArrayList<>();
        timeDateViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(TimeDateViewModel.class);
        timeDateDao = AppDatabase.getInstance(this).timeDateDao();

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        searchRecyclerView = findViewById(R.id.search_recyclerView);
        utc_date = findViewById(R.id.utc_date_tv);
        utc_time = findViewById(R.id.utc_time_tv);
        linearLayout = findViewById(R.id.linearLayoutUtcId);
        refreshImgBtn = findViewById(R.id.refreshImgBtn);
        deleteImgBtn = findViewById(R.id.deleteImgBtn);
        show_text_tv = findViewById(R.id.show_text_tv);
        searchEdt = findViewById(R.id.searchEdt);
        plusBtn = findViewById(R.id.plus_btn);
        mainRecyclerView = findViewById(R.id.mainRecyclerView);
        userSetTimeManuallyAlertRelativeLayout = findViewById(R.id.userSetTimeManuallyAlertRelativeLayout);
        removeTv = findViewById(R.id.removeTv);

        checkBoxUTCTime = findViewById(R.id.utcTimeCheckBoxId);
        checkBoxTimeZone = findViewById(R.id.timeZoneCheckBoxId);
        checkBoxHolidayAndOffice = findViewById(R.id.holyDayAndOfficeCheckBoxId);
        checkBoxCurrentTime = findViewById(R.id.currentTimeCheckBoxId);
        checkBoxTimeDifference = findViewById(R.id.timeDifferenceFromFirstCityCheckBoxId);
        timeFormatChangeCheckBoxId = findViewById(R.id.timeFormatChangeCheckBoxId);
        relativeLayoutAboutId = findViewById(R.id.aboutId);
        userGuidelineRelativeLayoutId = findViewById(R.id.userGuidelineRelativeLayoutId);

        //app Version Code//
        TextView appVersionCode = findViewById(R.id.versionCode);
        appVersionCode.setText("Version Code " + BuildConfig.VERSION_CODE);
    }

    private void tapTargetUserGuideLine1() {
        TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(plusBtn, "Add new time zone", "Adding one or more timezones help you compare times in different regions.")
                                .outerCircleColor(R.color.app_bg_color)
                                .outerCircleAlpha(0.90f)
                                .targetCircleColor(R.color.tap_target_circle_color)
                                .titleTextSize(24)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(14)
                                .descriptionTextColor(R.color.white)
                                .textColor(R.color.white)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .cancelable(true)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(35)
                                .id(1),
                        TapTarget.forView(deleteImgBtn, "Remove all time zones", "Use this button to remove all selected timezones.")
                                .outerCircleColor(R.color.app_bg_color)
                                .outerCircleAlpha(0.90f)
                                .targetCircleColor(R.color.tap_target_circle_color)
                                .titleTextSize(24)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(14)
                                .descriptionTextColor(R.color.white)
                                .textColor(R.color.white)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .cancelable(true)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(20)
                                .id(2),
//                        TapTarget.forView(refreshImgBtn, "Reset user defined date and time", "Press this button to clear date and time manually added and restore the app state to current time.")
//                                .outerCircleColor(R.color.app_bg_color)
//                                .outerCircleAlpha(0.90f)
//                                .targetCircleColor(R.color.tap_target_circle_color)
//                                .titleTextSize(24)
//                                .titleTextColor(R.color.white)
//                                .descriptionTextSize(14)
//                                .descriptionTextColor(R.color.white)
//                                .textColor(R.color.white)
//                                .textTypeface(Typeface.SANS_SERIF)
//                                .dimColor(R.color.black)
//                                .drawShadow(true)
//                                .cancelable(true)
//                                .tintTarget(true)
//                                .transparentTarget(true)
//                                .targetRadius(20)
//                                .id(3),
                        TapTarget.forView(toolbar.getChildAt(3), "App Menu", "Choose the menu according to your needs.")
                                .outerCircleColor(R.color.app_bg_color)
                                .outerCircleAlpha(0.90f)
                                .targetCircleColor(R.color.tap_target_circle_color)
                                .titleTextSize(24)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(14)
                                .descriptionTextColor(R.color.white)
                                .textColor(R.color.white)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .cancelable(true)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(20)
                                .id(3));

        Dialog dialog = new Dialog(this, RelativeLayout.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.user_guideline_show_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        Button userGuidelineSkipBtn = dialog.findViewById(R.id.userGuidelineSkipBtn);
        Button userGuidelineNextBtn = dialog.findViewById(R.id.userGuidelineNextBtn);

        SharedPreferences sp = getSharedPreferences("USER_GUIDELINE_SHOW_FIRST_TIME", MODE_PRIVATE);

        userGuidelineSkipBtn.setOnClickListener(view -> {
            //check user guideline for the 1st time//
            if (!sp.getBoolean("isFirstTime", false)) {
                showUserGuideline();
            }
            dialog.dismiss();
        });
        userGuidelineNextBtn.setOnClickListener(view -> {
            sequence.start();
            dialog.dismiss();
        });
        dialog.show();
    }

    private void tapTargetUserGuideLine2(View v) {
        TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(v.findViewById(R.id.tvUtcTimeDifferenceId), "Modify time", "Modifying the time helps you to understand working hours of different regions prior to set a meeting or call.")
                                .outerCircleColor(R.color.app_bg_color)
                                .outerCircleAlpha(0.90f)
                                .targetCircleColor(R.color.tap_target_circle_color)
                                .titleTextSize(24)
                                .titleTextColor(R.color.black)
                                .descriptionTextSize(14)
                                .descriptionTextColor(R.color.black)
                                .textTypeface(Typeface.DEFAULT)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .cancelable(true)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(30)
                                .id(1),
                        TapTarget.forView(v.findViewById(R.id.tvDateId), "Modifying date", "Helps you to understand weekends, holidays from different regions in order to set a future meeting or call.")
                                .outerCircleColor(R.color.app_bg_color)
                                .outerCircleAlpha(0.90f)
                                .targetCircleColor(R.color.tap_target_circle_color)
                                .titleTextSize(24)
                                .titleTextColor(R.color.black)
                                .descriptionTextSize(14)
                                .descriptionTextColor(R.color.black)
                                .textTypeface(Typeface.DEFAULT)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .cancelable(true)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(70)
                                .id(2));
        sequence.listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                //check user guideline for the 1st time//
                SharedPreferences sp = getSharedPreferences("USER_GUIDELINE_SHOW_FIRST_TIME", MODE_PRIVATE);
                if (!sp.getBoolean("isFirstTime", false)) {
                    showUserGuideline();
                }
            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {

            }
        }).start();
    }

    public void initAdmobInterstitialAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, getString(R.string.admob_INTERSTITIAL_UNIT_ID), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                AdMobInterstitialAd = interstitialAd;
                AdMobInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        initAdmobInterstitialAds();
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                AdMobInterstitialAd = null;
            }
        });
        //showAdmobInterstitialAds();
    }

    private void initFacebookInterstitialAds() {
        //this 3lines of code using for showing test ads purpose//
        AdSettings.setTestAdType(AdSettings.TestAdType.IMG_16_9_APP_INSTALL);
        AdSettings.setDebugBuild(true);
        AdSettings.getTestAdType();

        faceBookInterstitialAd = new com.facebook.ads.InterstitialAd(this, getString(R.string.facebook_placement_id));
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                //faceBookInterstitialAd.show();
                //showFaceBookInterstitialAds();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };

        faceBookInterstitialAd.loadAd(
                faceBookInterstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }

    public void bannerAds() {
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.admob_BANNER_UNIT_ID));

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    private void showAdmobInterstitialAds() {
        if (AdMobInterstitialAd != null) {
            AdMobInterstitialAd.show(MainActivity.this);
        } else {
            Log.d("TAG", "The Admob interstitial ad wasn't ready yet.");
        }
    }

    private void showFaceBookInterstitialAds() {
        if (faceBookInterstitialAd == null || !faceBookInterstitialAd.isAdLoaded() || faceBookInterstitialAd.isAdInvalidated()) {
            return;
        } else {
            faceBookInterstitialAd.show();
            //Log.d("TAG", "The Facebook interstitial ad wasn't ready yet.");
        }
    }

    private void addAdMobBannerAds() {
        for (int i = ITEMS_PER_AD; i <= AddListItems.size(); i += ITEMS_PER_AD) {

            final AdView adView = new AdView(MainActivity.this);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(getResources().getString(R.string.admob_BANNER_UNIT_ID));
            AddListItems.add(i, adView);
            //Log.d("TAG", ""+i);
        }
        loadBannerAds();
    }

    private void loadBannerAds() {
        //Load the first banner ad in the items list (subsequent ads will be loaded automatically in sequence).
        loadBannerAd(ITEMS_PER_AD);
    }

    private void loadBannerAd(final int index) {
        if (index >= AddListItems.size()) {
            return;
        }

        Object item = AddListItems.get(index);
        if (!(item instanceof AdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a banner ad" + " ad.");
        }

        final AdView adView = (AdView) item;

        // Set an AdListener on the AdView to wait for the previous banner ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // The previous banner ad loaded successfully, call this method again to
                // load the next ad in the items list.
                loadBannerAd(index + ITEMS_PER_AD);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                // The previous banner ad failed to load. Call this method again to load
                // the next ad in the items list.
                loadBannerAd(index + ITEMS_PER_AD);
            }
        });

        // Load the banner ad.
        adView.loadAd(new AdRequest.Builder().build());
    }

    private void loadSharePreference() {
        SharedPreferences sharedPreferences = getSharedPreferences(TimeZoneSettings.PREFERENCE, MODE_PRIVATE);

        //Show Global UTC Settings//
        String utcTimeSettingsOff = sharedPreferences.getString(TimeZoneSettings.UTC_TIME, TimeZoneSettings.UTC_TIME_OFF);
        settings.setUtcTimeSettings(utcTimeSettingsOff);

        //Show Zone UTC Settings//
        String timeZoneOn = sharedPreferences.getString(TimeZoneSettings.TIME_ZONE, TimeZoneSettings.TIME_ZONE_ON);
        settings.setTimeZoneSettings(timeZoneOn);

        //Show Holidays And Office Hours Settings//
        String holidayAndOfficeOn = sharedPreferences.getString(TimeZoneSettings.HOLIDAYS_AND_OFFICE, TimeZoneSettings.HOLIDAYS_AND_OFFICE_ON);
        settings.setHolidaysAndOfficeSettings(holidayAndOfficeOn);

        //Show Current Time Settings//
        String currentTimeOff = sharedPreferences.getString(TimeZoneSettings.CURRENT_TIME, TimeZoneSettings.CURRENT_TIME_OFF);
        settings.setCurrentTimeSettings(currentTimeOff);

        //Time Difference From First City Settings//
        String timeDifferenceOff = sharedPreferences.getString(TimeZoneSettings.TIME_DIFFERENCE, TimeZoneSettings.TIME_DIFFERENCE_OFF);
        settings.setTimeDifferenceSettings(timeDifferenceOff);

        //Time Format Settings//
        String timeFormat12Or24 = sharedPreferences.getString(TimeZoneSettings.ICON_SWITCH, TimeZoneSettings.ICON_SWITCH_24);
        settings.setTimeFormatSettings(timeFormat12Or24);

        updateView();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void checkBoxClickListener() {
        checkBoxUTCTime.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                settings.setUtcTimeSettings(TimeZoneSettings.UTC_TIME_ON);
            } else {
                settings.setUtcTimeSettings(TimeZoneSettings.UTC_TIME_OFF);
            }
            updateView();
            SharedPreferences.Editor editor = getSharedPreferences(TimeZoneSettings.PREFERENCE, MODE_PRIVATE).edit();
            editor.putString(TimeZoneSettings.UTC_TIME, settings.getUtcTimeSettings());
            editor.apply();
        });

        checkBoxTimeZone.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                settings.setTimeZoneSettings(TimeZoneSettings.TIME_ZONE_ON);
            } else {
                settings.setTimeZoneSettings(TimeZoneSettings.TIME_ZONE_OFF);
            }
            updateView();
            SharedPreferences.Editor editor = getSharedPreferences(TimeZoneSettings.PREFERENCE, MODE_PRIVATE).edit();
            editor.putString(TimeZoneSettings.TIME_ZONE, settings.getTimeZoneSettings());
            editor.apply();

            if (timeDateRecyclerViewAdapter != null) {
                timeDateRecyclerViewAdapter.setTimeZone(isChecked);
                timeDateRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

        checkBoxHolidayAndOffice.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                settings.setHolidaysAndOfficeSettings(TimeZoneSettings.HOLIDAYS_AND_OFFICE_ON);
            } else {
                settings.setHolidaysAndOfficeSettings(TimeZoneSettings.HOLIDAYS_AND_OFFICE_OFF);
            }
            updateView();
            SharedPreferences.Editor editor = getSharedPreferences(TimeZoneSettings.PREFERENCE, MODE_PRIVATE).edit();
            editor.putString(TimeZoneSettings.HOLIDAYS_AND_OFFICE, settings.getHolidaysAndOfficeSettings());
            editor.apply();

            if (timeDateRecyclerViewAdapter != null) {
                timeDateRecyclerViewAdapter.setHoliday(isChecked);
                timeDateRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

        checkBoxCurrentTime.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                settings.setCurrentTimeSettings(TimeZoneSettings.CURRENT_TIME_ON);
            } else {
                settings.setCurrentTimeSettings(TimeZoneSettings.CURRENT_TIME_OFF);
            }
            updateView();
            SharedPreferences.Editor editor = getSharedPreferences(TimeZoneSettings.PREFERENCE, MODE_PRIVATE).edit();
            editor.putString(TimeZoneSettings.CURRENT_TIME, settings.getCurrentTimeSettings());
            editor.apply();

            if (timeDateRecyclerViewAdapter != null) {
                timeDateRecyclerViewAdapter.setCurrentTime(isChecked);
                timeDateRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

        checkBoxTimeDifference.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                settings.setTimeDifferenceSettings(TimeZoneSettings.TIME_DIFFERENCE_ON);
            } else {
                settings.setTimeDifferenceSettings(TimeZoneSettings.TIME_DIFFERENCE_OFF);
            }
            updateView();
            SharedPreferences.Editor editor = getSharedPreferences(TimeZoneSettings.PREFERENCE, MODE_PRIVATE).edit();
            editor.putString(TimeZoneSettings.TIME_DIFFERENCE, settings.getTimeDifferenceSettings());
            editor.apply();

            if (timeDateRecyclerViewAdapter != null) {
                timeDateRecyclerViewAdapter.setTimeDifference(isChecked);
                timeDateRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

        timeFormatChangeCheckBoxId.setOnCheckedChangeListener((compoundButton, isChecked) -> {

            if (isChecked) {
                settings.setTimeFormatSettings(TimeZoneSettings.ICON_SWITCH_12);
            } else {
                settings.setTimeFormatSettings(TimeZoneSettings.ICON_SWITCH_24);
            }

            updateView();
            SharedPreferences.Editor editor = getSharedPreferences(TimeZoneSettings.PREFERENCE, MODE_PRIVATE).edit();
            editor.putString(TimeZoneSettings.ICON_SWITCH, settings.getTimeFormatSettings());
            editor.apply();

            if (timeDateRecyclerViewAdapter != null) {
                timeDateRecyclerViewAdapter.setTimeDateFormat(isChecked);
                timeDateRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateView() {
        try {
            if (settings.getUtcTimeSettings().equals(TimeZoneSettings.UTC_TIME_ON)) {
                linearLayout.setVisibility(View.VISIBLE);
                loadUtcDateTime();
                checkBoxUTCTime.setChecked(true);
            } else {
                linearLayout.setVisibility(View.GONE);
                checkBoxUTCTime.setChecked(false);
            }

            if (settings.getTimeZoneSettings().equals(TimeZoneSettings.TIME_ZONE_ON)) {
                checkBoxTimeZone.setChecked(true);
                timeDateRecyclerViewAdapter.setTimeZone(true);
            } else {
                checkBoxTimeZone.setChecked(false);
                timeDateRecyclerViewAdapter.setTimeZone(false);
            }

            if (settings.getHolidaysAndOfficeSettings().equals(TimeZoneSettings.HOLIDAYS_AND_OFFICE_ON)) {
                checkBoxHolidayAndOffice.setChecked(true);
                timeDateRecyclerViewAdapter.setHoliday(true);
            } else {
                checkBoxHolidayAndOffice.setChecked(false);
                timeDateRecyclerViewAdapter.setHoliday(false);
            }

            if (settings.getCurrentTimeSettings().equals(TimeZoneSettings.CURRENT_TIME_ON)) {
                checkBoxCurrentTime.setChecked(true);
                timeDateRecyclerViewAdapter.setCurrentTime(true);
            } else {
                checkBoxCurrentTime.setChecked(false);
                timeDateRecyclerViewAdapter.setCurrentTime(false);
            }

            if (settings.getTimeDifferenceSettings().equals(TimeZoneSettings.TIME_DIFFERENCE_ON)) {
                checkBoxTimeDifference.setChecked(true);
                timeDateRecyclerViewAdapter.setTimeDifference(true);
            } else {
                checkBoxTimeDifference.setChecked(false);
                timeDateRecyclerViewAdapter.setTimeDifference(false);
            }

            if (settings.getTimeFormatSettings().equals(TimeZoneSettings.ICON_SWITCH_12)) {
                timeFormatChangeCheckBoxId.setChecked(true);
                timeDateRecyclerViewAdapter.setTimeDateFormat(true);
            } else {
                timeFormatChangeCheckBoxId.setChecked(false);
                timeDateRecyclerViewAdapter.setTimeDateFormat(false);
            }

            timeDateRecyclerViewAdapter.notifyDataSetChanged();
            refreshData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recyclerViewShowAddZoneListSetup() {
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainRecyclerView.setHasFixedSize(true);
        mainRecyclerView.animate();
    }

    private void adapterSetup() {
        timeDateRecyclerViewAdapter = new TimeDateRecyclerViewAdapter(this, this, this);
        mainRecyclerView.setAdapter(timeDateRecyclerViewAdapter);
        timeDateRecyclerViewAdapter.updateData(AddListItems);
    }

    private void setListIntoTheAdapter() {
        timeDateRecyclerViewAdapter.updateData(AddListItems);
    }

    private void customToolbar() {
        //toolbar.setTitle("Time Date Converter");
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.ic_menu_icon);
        //check user guideline for the 1st time//
        SharedPreferences sp = getSharedPreferences("USER_GUIDELINE_SHOW_FIRST_TIME", MODE_PRIVATE);
        boolean isTrue = sp.getBoolean("isFirstTime", false);
        if (!isTrue) {
            tapTargetUserGuideLine1();
        }
    }

    private void loadData() {
        AddListItems.clear();
        AddListItems.addAll(timeDateDao.getAllListData());
        SharedPreferences timeDateSp = getSharedPreferences("SET_DATE_TIME", MODE_PRIVATE);
        if (timeDateSp.getAll().isEmpty()) {
            //userSetDateTimeShowAlertMessage.setVisibility(View.GONE);
            userSetTimeManuallyAlertRelativeLayout.setVisibility(View.GONE);
            refreshImgBtn.setClickable(false);
            refreshImgBtn.setColorFilter(ContextCompat.getColor(this, R.color.refresh_btn_disable_color));
        }
        //Log.d("TEST", "" + AddListItems.isEmpty());
        if (timeDateDao.getAllListData().isEmpty()) {
            mainRecyclerView.setVisibility(View.INVISIBLE);
            show_text_tv.setVisibility(View.VISIBLE);
            deleteImgBtn.setClickable(false);
            deleteImgBtn.setColorFilter(ContextCompat.getColor(this, R.color.refresh_btn_disable_color));
        } else {
            show_text_tv.setVisibility(View.GONE);
            deleteImgBtn.setClickable(true);
            deleteImgBtn.setColorFilter(ContextCompat.getColor(this, R.color.refresh_btn_enable_color));

            //here i'm find the first city/country and put the city into the SharedPreference//
            SharedPreferences.Editor editor = getSharedPreferences("FIRST_CITY", MODE_PRIVATE).edit();
            editor.putString("name", timeDateDao.getAllListData().get(0).getCity());
            editor.putString("utc", timeDateDao.getAllListData().get(0).getUtc_offset());
            editor.apply();
            mainRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void clearAllZone() {
        deleteAllZoneDialog.setContentView(R.layout.delete_allzone_dialog);
        deleteAllZoneDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button noBtn = deleteAllZoneDialog.findViewById(R.id.noBtn_all_d);
        Button yesBtn = deleteAllZoneDialog.findViewById(R.id.yesBtn_all_d);
        deleteImgBtn.setOnClickListener(view -> deleteAllZoneDialog.show());
        noBtn.setOnClickListener(view -> deleteAllZoneDialog.dismiss());
        yesBtn.setOnClickListener(view -> {
            SharedPreferences.Editor editor = getSharedPreferences("SET_DATE_TIME", MODE_PRIVATE).edit();
            editor.clear().apply();
            timeDateDao.deleteAllTimeZones();
            refreshData();
            refreshImgBtn.setClickable(false);
            refreshImgBtn.setColorFilter(ContextCompat.getColor(this, R.color.refresh_btn_disable_color));
            deleteAllZoneDialog.dismiss();
            Toast.makeText(this, "Cleared all locations", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadUtcDateTime() {
        utc_date.setText(Utils.getUtcDate(getApplicationContext()));
        utc_time.setText(Utils.getUtcTime(getApplicationContext()));
    }

    private void searchData() {
        //Search RecyclerView Method//
        AtomicBoolean oneTab = new AtomicBoolean(true);
        plusBtn.setOnClickListener(view -> {
            if (oneTab.get()) {
                searchRecyclerView.setVisibility(View.VISIBLE);
                filter("Bangladesh");
                oneTab.set(false);
            } else {
                oneTab.set(true);
                searchRecyclerView.setVisibility(View.GONE);
            }
        });

        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    show_text_tv.setVisibility(View.GONE);
                    searchRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    if (timeDateDao.checkDb().isEmpty()) {
                        show_text_tv.setVisibility(View.VISIBLE);
                    }
                    searchRecyclerView.setVisibility(View.GONE);
                }
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void filter(String query) {
        //filter ArrayList initialize//
        List<TimeZoneSearchListModel> zoneList = timeDateViewModel.searchDataList(query.toLowerCase());
        searchAdapter.filterList(zoneList);
        timeZoneListModels = zoneList;
    }

    @SuppressLint("CommitPrefEdits")
    private void refreshButtonClick() {
        refreshDialog.setContentView(R.layout.refresh_dialog);
        refreshDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button noBtn = refreshDialog.findViewById(R.id.noBtn_refresh);
        Button yesBtn = refreshDialog.findViewById(R.id.yesBtn_refresh);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.clockwise_rotate);

        refreshImgBtn.setOnClickListener(view -> refreshDialog.show());
        removeTv.setOnClickListener(view -> refreshDialog.show());

        noBtn.setOnClickListener(view -> refreshDialog.dismiss());

        yesBtn.setOnClickListener(view -> {
            SharedPreferences.Editor editor = getSharedPreferences("SET_DATE_TIME", MODE_PRIVATE).edit();
            editor.clear().apply();
            refreshImgBtn.startAnimation(animation);
            searchRecyclerView.setVisibility(View.GONE);
            searchEdt.setText("");
            new Handler().postDelayed(() -> {
                refreshImgBtn.clearAnimation();
                refreshData();
                refreshImgBtn.setClickable(false);
                refreshImgBtn.setColorFilter(ContextCompat.getColor(this, R.color.refresh_btn_disable_color));
                Toast.makeText(this, "Date Time Reset Successfully", Toast.LENGTH_SHORT).show();
            }, 1000);
            refreshDialog.dismiss();
        });
    }

    private void closeKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    long previousTime;

    private void refreshData() {
        if (!(2000 + previousTime > (previousTime = System.currentTimeMillis()))) {
            loadData();
            addAdMobBannerAds();
            loadBannerAds();
            setListIntoTheAdapter();
            loadUtcDateTime();
        }
    }

    private void searchRecyclerViewSetup() {
        //get all TimeZoneList from device database//
        //timezonesIdList = new ArrayList<>(Arrays.asList(TimeZone.getAvailableIDs()));

        //Search adapter setup//
        searchAdapter = new TimeDateSearchItemAdapter(timeZoneListModels, this, this);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setAdapter(searchAdapter);
    }

    @Override
    public void onItemClick(View view, TimeZoneSearchListModel model, int position) {
        //showInterstitialAds();
        searchRecyclerView.setVisibility(View.GONE);
        //RecyclerView Item Click Listener//
//        if (timeDateViewModel.getFindCity(model.getCity(), model.getCountry(), model.getTitle()).isEmpty())
        {
            //add data into the roomDatabase//
            TimeZoneListModel Model = new TimeZoneListModel(model.getCity(), model.getCity_ascii(), model.getLat(), model.getLng(), model.getPop(), model.getCountry(), model.getIso2(), model.getIso3(), model.getProvince(), model.getTimezone(), model.getUtc_offset(), model.getUtc_dst(), model.getCode(), model.getParent_code(), model.getTitle(), model.getLocation(), model.getWeek_end());
            //showCaseView();
            timeDateDao.insert(Model);
            searchEdt.setText("");
            loadData();
            addAdMobBannerAds();
            loadBannerAds();
            setListIntoTheAdapter();
            //check user guideline for the 1st time//
            SharedPreferences sp = getSharedPreferences("USER_GUIDELINE_SHOW_FIRST_TIME", MODE_PRIVATE);
            boolean isTrue = sp.getBoolean("isFirstTime", false);
            if (!isTrue) {
                new Handler().postDelayed(() -> {
                    View v = Objects.requireNonNull(mainRecyclerView.getLayoutManager()).findViewByPosition(0);
                    if (v != null) {
                        tapTargetUserGuideLine2(v);
                    }
                }, 500);
            }
            Toast.makeText(MainActivity.this, model.getCity() + " Zone is Added.", Toast.LENGTH_SHORT).show();
        }
//        else {
//            Toast.makeText(MainActivity.this, model.getCity() + " is already added please try another zone.", Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    public void onItemDeleteClick(TimeZoneListModel model) {
        //showInterstitialAds();
        //delete data from roomDatabase//
        deleteDialog.setContentView(R.layout.delete_dialog);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deleteDialog.setCancelable(true);

        Button noBtn = deleteDialog.findViewById(R.id.noBtn_d);
        Button yesBtn = deleteDialog.findViewById(R.id.yesBtn_d);

        TextView city_and_country_name_tv = deleteDialog.findViewById(R.id.city_and_country_name_tv);
        String cityAndCountry = model.getCity() + ", " + model.getCountry();
        city_and_country_name_tv.setText(cityAndCountry);

        noBtn.setOnClickListener(view -> {
            //Toast.makeText(getApplicationContext(),"Cancel",Toast.LENGTH_SHORT).show();
            deleteDialog.dismiss();
        });

        yesBtn.setOnClickListener(view -> {
            timeDateDao.delete(model);
            loadData();
            addAdMobBannerAds();
            loadBannerAds();
            setListIntoTheAdapter();

            Toast.makeText(MainActivity.this, model.getCity() + " successfully removed.", Toast.LENGTH_SHORT).show();
            deleteDialog.dismiss();
        });
        deleteDialog.show();
    }

    @SuppressLint({"NewApi", "NotifyDataSetChanged", "SimpleDateFormat"})
    @Override
    public void onSetDateTimeClick(View view, TimeZoneListModel model, int position) {
        timeDatePickerDialog.setContentView(R.layout.time_date_pickers_dialog_layout);
        timeDatePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timeDatePickerDialog.setCancelable(true);

        SharedPreferences.Editor editor = getSharedPreferences("SET_DATE_TIME", MODE_PRIVATE).edit();

        DatePicker datePicker = timeDatePickerDialog.findViewById(R.id.date_picker_id);
        TimePicker timePicker = timeDatePickerDialog.findViewById(R.id.time_picker_id);

        int year = Utils.dateTime(model.getTimezone()).get(Calendar.YEAR);
        int month = Utils.dateTime(model.getTimezone()).get(Calendar.MONTH);
        int day = Utils.dateTime(model.getTimezone()).get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, month, day, null);

        int hour = Utils.dateTime(model.getTimezone()).get(Calendar.HOUR_OF_DAY);
        int min = Utils.dateTime(model.getTimezone()).get(Calendar.MINUTE);
        timePicker.setHour(hour);
        timePicker.setMinute(min);

        //DateFormat dff = new SimpleDateFormat("EEEE, dd MMM yyyy");
        DateFormat dff = new SimpleDateFormat("EEEE, dd MMM yyyy");
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone(model.getTimezone()));
        editor.putString("DATE", dff.format(calendar.getTime()));


        datePicker.setOnDateChangedListener((datePicker1, year1, month1, day1) -> {
            isDate = false;
            isDateTimeChanged = true;
            calendar.set(Calendar.DAY_OF_MONTH, day1);
            calendar.set(Calendar.MONTH, month1);
            calendar.set(Calendar.YEAR, year1);

            editor.putInt("DAY", day1);
            editor.putInt("MONTH", month1);
            editor.putInt("YEAR", year1);
            editor.putBoolean("SET_DATE", true);
            editor.putString("DATE", dff.format(calendar.getTime()));
        });

        if (isDate) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);

            editor.putInt("DAY", day);
            editor.putInt("MONTH", month);
            editor.putInt("YEAR", year);
            editor.putString("DATE", dff.format(calendar.getTime()));
        }

        timePicker.setOnTimeChangedListener((timePicker1, hour1, min1) -> {
            isTime = false;
            isDateTimeChanged = true;
            calendar.set(Calendar.HOUR_OF_DAY, hour1);
            calendar.set(Calendar.MINUTE, min1);

            editor.putInt("TIME_HOURS", hour1);
            editor.putInt("TIME_MINUTES", min1);

            int H = (Integer.parseInt(model.getUtc_offset().substring(1, 3))) * 60 + Integer.parseInt(model.getUtc_offset().substring(4, 6));
            if (!(model.getUtc_offset().charAt(0) == '+')) {
                H = H * (-1);
            }
            int HH = H / 60;

            if (hour1 < HH) {
                hour1 = 12 + hour1 + HH;
            } else {
                hour1 = Math.abs(hour1 - HH);
            }
            editor.putInt("UTC_HH", hour1);
            int MM = 0;
            if (Integer.parseInt(model.getUtc_offset().substring(4, 6)) > 0) {
                MM = Math.abs(60 - Integer.parseInt(model.getUtc_offset().substring(4, 6)));
            }
            editor.putInt("UTC_MM", MM);
        });

        if (isTime) {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, min);
            editor.putInt("TIME_HOURS", hour);
            editor.putInt("TIME_MINUTES", min);
        }

        Button setBtn = timeDatePickerDialog.findViewById(R.id.setTimeDateBtn);
        setBtn.setOnClickListener(view1 -> {
            if (isDateTimeChanged) {
                editor.apply();
                isDateTimeChanged = false;
                refreshImgBtn.setClickable(true);
                //userSetDateTimeShowAlertMessage.setVisibility(View.VISIBLE);
                userSetTimeManuallyAlertRelativeLayout.setVisibility(View.VISIBLE);
                refreshImgBtn.setColorFilter(ContextCompat.getColor(this, R.color.refresh_btn_enable_color));
                if (timeDateRecyclerViewAdapter != null) {
                    timeDateRecyclerViewAdapter.notifyDataSetChanged();
                }
                refreshData();
            } else {
                Toast.makeText(this, "Select date time and try again..", Toast.LENGTH_SHORT).show();
            }
            timeDatePickerDialog.dismiss();
        });
        timeDatePickerDialog.show();
    }

    private void about() {
        relativeLayoutAboutId.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AboutActivity.class)));
    }

    private void userGuideline() {
        userGuidelineRelativeLayoutId.setOnClickListener(view -> {
            drawerLayout.closeDrawers();
            tapTargetUserGuideLine();
        });
    }

    private void tapTargetUserGuideLine() {
        TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(plusBtn, "Add new time zone", "Adding one or more timezones help you compare times in different regions.")
                                .outerCircleColor(R.color.app_bg_color)
                                .outerCircleAlpha(0.90f)
                                .targetCircleColor(R.color.tap_target_circle_color)
                                .titleTextSize(24)
                                .titleTextColor(R.color.black)
                                .descriptionTextSize(14)
                                .descriptionTextColor(R.color.black)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.white)
                                .drawShadow(true)
                                .cancelable(true)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(35)
                                .id(1),
                        TapTarget.forView(deleteImgBtn, "Remove all time zones", "Use this button to remove all selected timezones.")
                                .outerCircleColor(R.color.app_bg_color)
                                .outerCircleAlpha(0.90f)
                                .targetCircleColor(R.color.tap_target_circle_color)
                                .titleTextSize(24)
                                .titleTextColor(R.color.black)
                                .descriptionTextSize(14)
                                .descriptionTextColor(R.color.black)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .cancelable(true)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(20)
                                .id(2),
//                        TapTarget.forView(refreshImgBtn, "Reset user defined date and time", "Press this button to clear date and time manually added and restore the app state to current time.")
//                                .outerCircleColor(R.color.app_bg_color)
//                                .outerCircleAlpha(0.90f)
//                                .targetCircleColor(R.color.tap_target_circle_color)
//                                .titleTextSize(24)
//                                .titleTextColor(R.color.white)
//                                .descriptionTextSize(14)
//                                .descriptionTextColor(R.color.white)
//                                .textColor(R.color.white)
//                                .textTypeface(Typeface.SANS_SERIF)
//                                .dimColor(R.color.black)
//                                .drawShadow(true)
//                                .cancelable(true)
//                                .tintTarget(true)
//                                .transparentTarget(true)
//                                .targetRadius(20)
//                                .id(3),
                        TapTarget.forView(toolbar.getChildAt(3), "App Menu", "Choose the menu according to your needs.")
                                .outerCircleColor(R.color.app_bg_color)
                                .outerCircleAlpha(0.90f)
                                .targetCircleColor(R.color.tap_target_circle_color)
                                .titleTextSize(24)
                                .titleTextColor(R.color.black)
                                .descriptionTextSize(14)
                                .descriptionTextColor(R.color.black)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .cancelable(true)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(20)
                                .id(3));
        sequence.listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                View v = Objects.requireNonNull(mainRecyclerView.getLayoutManager()).findViewByPosition(0);
                if (v != null) {
                    TapTargetSequence sequence1 = new TapTargetSequence(MainActivity.this)
                            .targets(
                                    TapTarget.forView(v.findViewById(R.id.tvUtcTimeDifferenceId), "Modify time", "Modifying the time helps you to understand working hours of different regions prior to set a meeting or call.")
                                            .outerCircleColor(R.color.app_bg_color)
                                            .outerCircleAlpha(0.90f)
                                            .targetCircleColor(R.color.tap_target_circle_color)
                                            .titleTextSize(24)
                                            .titleTextColor(R.color.black)
                                            .descriptionTextSize(14)
                                            .descriptionTextColor(R.color.black)
                                            .textTypeface(Typeface.DEFAULT)
                                            .dimColor(R.color.black)
                                            .drawShadow(true)
                                            .cancelable(true)
                                            .tintTarget(true)
                                            .transparentTarget(true)
                                            .targetRadius(30)
                                            .id(1),
                                    TapTarget.forView(v.findViewById(R.id.tvDateId), "Modifying date", "Helps you to understand weekends, holidays from different regions in order to set a future meeting or call.")
                                            .outerCircleColor(R.color.app_bg_color)
                                            .outerCircleAlpha(0.90f)
                                            .targetCircleColor(R.color.tap_target_circle_color)
                                            .titleTextSize(24)
                                            .titleTextColor(R.color.black)
                                            .descriptionTextSize(14)
                                            .descriptionTextColor(R.color.black)
                                            .textTypeface(Typeface.DEFAULT)
                                            .dimColor(R.color.black)
                                            .drawShadow(true)
                                            .cancelable(true)
                                            .tintTarget(true)
                                            .transparentTarget(true)
                                            .targetRadius(70)
                                            .id(2));
                    sequence1.start();
                }
            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {

            }
        }).start();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            closeKeyboard();
        }
        return super.dispatchTouchEvent(ev);
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            //Log.d("TAG", "Drawer");
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else if (searchRecyclerView.isShown()) {
            //Log.d("TAG", "Search Recyclerview");
            searchRecyclerView.setVisibility(View.GONE);
        } else {
            //Log.d("TAG", "Back");
            exitDialog.setContentView(R.layout.exit_dialog);
            exitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            exitDialog.setCancelable(false);

            Button noBtn = exitDialog.findViewById(R.id.noBtn);
            Button yesBtn = exitDialog.findViewById(R.id.yesBtn);
            noBtn.setOnClickListener(view -> {
                //Toast.makeText(getApplicationContext(),"Cancel",Toast.LENGTH_SHORT).show();
                exitDialog.dismiss();
            });

            yesBtn.setOnClickListener(view -> {
                super.onBackPressed();
                finish();
                exitDialog.dismiss();
            });
            exitDialog.show();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        searchRecyclerView.setVisibility(View.GONE);
        //refreshData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //refreshData();
        //InterstitialAds Show//
        //new Handler().postDelayed(this::showFaceBookInterstitialAds, 30000);
        //new Handler().postDelayed(this::showAdmobInterstitialAds, 40000);
    }

    @Override
    protected void onResume() {
        for (Object item : AddListItems) {
            if (item instanceof AdView) {
                AdView adView = (AdView) item;
                adView.resume();
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        for (Object item : AddListItems) {
            if (item instanceof AdView) {
                AdView adView = (AdView) item;
                adView.pause();
            }
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        for (Object item : AddListItems) {
            if (item instanceof AdView) {
                AdView adView = (AdView) item;
                adView.destroy();
            }
        }
        super.onDestroy();
    }
}