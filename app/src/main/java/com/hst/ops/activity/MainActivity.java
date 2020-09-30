package com.hst.ops.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.hst.ops.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgLanguage, imgNotification, imgProfile;
    private RelativeLayout homeLayout, galleryLayout, opsLayout, eventLayout, socialLayout;
    private RelativeLayout selectLanguageLayout, englishLayout, tamilLayout;
    private ImageView imgEnglishCheck, imgTamilCheck;
    private Button languageConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVariables();

    }

    private void setVariables() {
        imgLanguage = findViewById(R.id.language_change);
        imgNotification = findViewById(R.id.notification);
        imgProfile = findViewById(R.id.profile);

        homeLayout = findViewById(R.id.home_layout);
        galleryLayout = findViewById(R.id.gallery_layout);
        opsLayout = findViewById(R.id.center_button_layout);
        eventLayout = findViewById(R.id.event_layout);
        socialLayout = findViewById(R.id.social_layout);

        selectLanguageLayout = findViewById(R.id.select_language_layout);
        englishLayout = findViewById(R.id.english);
        tamilLayout = findViewById(R.id.tamil);
        imgEnglishCheck = findViewById(R.id.img_eng_check);
        imgTamilCheck = findViewById(R.id.img_tamil_check);
        languageConfirm = findViewById(R.id.confirm_language);

        imgLanguage.setOnClickListener(this);
        imgNotification.setOnClickListener(this);
        imgProfile.setOnClickListener(this);

        homeLayout.setOnClickListener(this);
        galleryLayout.setOnClickListener(this);
        opsLayout.setOnClickListener(this);
        eventLayout.setOnClickListener(this);
        socialLayout.setOnClickListener(this);

        englishLayout.setOnClickListener(this);
        tamilLayout.setOnClickListener(this);
        languageConfirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == imgLanguage) {
            selectLanguageLayout.setVisibility(View.VISIBLE);
            homeLayout.setClickable(false);
            galleryLayout.setClickable(false);
            opsLayout.setClickable(false);
            eventLayout.setClickable(false);
            socialLayout.setClickable(false);
        }
        if (v == imgNotification) {

        }
        if (v == imgProfile) {

        }
        if (v == homeLayout) {

        }
        if (v == galleryLayout) {

        }
        if (v == opsLayout) {

        }
        if (v == eventLayout) {

        }
        if (v == socialLayout) {

        }
        if (v == englishLayout) {
            imgEnglishCheck.setVisibility(View.VISIBLE);
            imgTamilCheck.setVisibility(View.GONE);
        }
        if (v == tamilLayout) {
            imgTamilCheck.setVisibility(View.VISIBLE);
            imgEnglishCheck.setVisibility(View.GONE);
        }
        if (v == languageConfirm) {
            selectLanguageLayout.setVisibility(View.GONE);
            homeLayout.setClickable(true);
            galleryLayout.setClickable(true);
            opsLayout.setClickable(true);
            eventLayout.setClickable(true);
            socialLayout.setClickable(true);
        }
    }

    private void changeFragment(int position) {

        Fragment newFragment = null;

//        if (position == 0) {
//            newFragment = new LandingFragment();
//        } else if (position == 1) {
//            newFragment = new MenuFragment();
//        } else if (position == 2) {
//            newFragment = new AboutUsFragment();
//        } else if (position == 3) {
//            newFragment = new DigitalMarketingFragment();
//        } else if (position == 4) {
//            newFragment = new MobileAppFragment();
//        } else if (position == 5) {
//            newFragment = new WebDevelopmentFragment();
//        } else if (position == 6) {
//            newFragment = new BrandingFragment();
//        }

        getFragmentManager().beginTransaction().replace(
                R.id.fragmentContainer, newFragment)
                .commit();
    }

}