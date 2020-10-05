package com.hst.ops.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.hst.ops.R;
import com.hst.ops.fragment.EventFragment;
import com.hst.ops.fragment.GalleryFragment;
import com.hst.ops.fragment.HomeFragment;
import com.hst.ops.fragment.SocialFragment;
import com.hst.ops.utils.LocaleHelper;
import com.hst.ops.utils.PreferenceStorage;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgLanguage, imgNotification, imgProfile;
    private RelativeLayout homeLayout, galleryLayout, opsLayout, eventLayout, socialLayout;
    private RelativeLayout selectLanguageLayout, englishLayout, tamilLayout;
    private ImageView imgEnglishCheck, imgTamilCheck;
    private Button languageConfirm;
    private Boolean englishCheck = true;
    private ImageView imgHome, imgGallery, imgEvent, imgSocial;
    private TextView txtHome, txtGallery, txtEvent, txtSocial;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVariables();
        changeFragment(0);
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

        imgHome = findViewById(R.id.home_img);
        imgGallery = findViewById(R.id.gallery_img);
        imgEvent = findViewById(R.id.event_img);
        imgSocial = findViewById(R.id.social_img);

        txtHome = findViewById(R.id.home_title);
        txtGallery = findViewById(R.id.gallery_title);
        txtEvent = findViewById(R.id.event_title);
        txtSocial = findViewById(R.id.social_title);

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

        searchView = findViewById(R.id.search_view);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        if (PreferenceStorage.getLang(this).equalsIgnoreCase("tamil")) {
            searchView.setQueryHint("தேடல்");
        } else {
            searchView.setQueryHint("Search");
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

//                if (categoryArrayList.contains(query)) {
//                    Toast.makeText(this, "No Match found", Toast.LENGTH_LONG).show();
//                }
                if (query != null) {
                    makeSearch(query);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                return false;
            }
        });
        
    }

    private void makeSearch(String eventname) {
        PreferenceStorage.setSearchFor(this, eventname);
        startActivity(new Intent(this, SearchResultActivity.class));
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
            if (PreferenceStorage.getLang(this).equalsIgnoreCase("english")) {
                imgEnglishCheck.setVisibility(View.VISIBLE);
                imgTamilCheck.setVisibility(View.GONE);
                englishCheck = true;
            } else {
                imgTamilCheck.setVisibility(View.VISIBLE);
                imgEnglishCheck.setVisibility(View.GONE);
                englishCheck = false;
            }
        }
        if (v == imgNotification) {

        }
        if (v == imgProfile) {

        }
        if (v == homeLayout) {
            changeFragment(0);
        }
        if (v == galleryLayout) {
            changeFragment(1);
        }
        if (v == opsLayout) {
        }
        if (v == eventLayout) {
            changeFragment(2);
        }
        if (v == socialLayout) {
            changeFragment(3);
        }
        if (v == englishLayout) {
            imgEnglishCheck.setVisibility(View.VISIBLE);
            imgTamilCheck.setVisibility(View.GONE);
            englishCheck = true;
        }
        if (v == tamilLayout) {
            imgTamilCheck.setVisibility(View.VISIBLE);
            imgEnglishCheck.setVisibility(View.GONE);
            englishCheck = false;
        }
        if (v == languageConfirm) {
            selectLanguageLayout.setVisibility(View.GONE);
            homeLayout.setClickable(true);
            galleryLayout.setClickable(true);
            opsLayout.setClickable(true);
            eventLayout.setClickable(true);
            socialLayout.setClickable(true);
            if (englishCheck) {
                Toast.makeText(getApplicationContext(), "App language is set to English", Toast.LENGTH_SHORT).show();
                LocaleHelper.setLocale(MainActivity.this, "en");
                PreferenceStorage.saveLang(this, "english");
            } else {
                Toast.makeText(getApplicationContext(), "மொழி தமிழுக்கு அமைக்கப்பட்டுள்ளது", Toast.LENGTH_SHORT).show();
                LocaleHelper.setLocale(MainActivity.this, "ta");
                PreferenceStorage.saveLang(this, "tamil");
            }
            recreate();
        }
    }

    private void changeFragment(int position) {

        Fragment newFragment = null;

        if (position == 0) {
            newFragment = new HomeFragment();
            imgHome.setImageResource(R.drawable.ic_home_selected);
            txtHome.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            imgGallery.setImageResource(R.drawable.ic_gallery);
            txtGallery.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
            imgEvent.setImageResource(R.drawable.ic_event);
            txtEvent.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
            imgSocial.setImageResource(R.drawable.ic_social);
            txtSocial.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
        }
        else if (position == 1) {
            newFragment = new GalleryFragment();
            imgHome.setImageResource(R.drawable.ic_home);
            txtHome.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
            imgGallery.setImageResource(R.drawable.ic_gallery_selected);
            txtGallery.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            imgEvent.setImageResource(R.drawable.ic_event);
            txtEvent.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
            imgSocial.setImageResource(R.drawable.ic_social);
            txtSocial.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
        } else if (position == 2) {
            newFragment = new EventFragment();
            imgHome.setImageResource(R.drawable.ic_home);
            txtHome.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
            imgGallery.setImageResource(R.drawable.ic_gallery);
            txtGallery.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
            imgEvent.setImageResource(R.drawable.ic_event_selected);
            txtEvent.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            imgSocial.setImageResource(R.drawable.ic_social);
            txtSocial.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
        } else if (position == 3) {
            newFragment = new SocialFragment();
            imgHome.setImageResource(R.drawable.ic_home);
            txtHome.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
            imgGallery.setImageResource(R.drawable.ic_gallery);
            txtGallery.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
            imgEvent.setImageResource(R.drawable.ic_event);
            txtEvent.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
            imgSocial.setImageResource(R.drawable.ic_social_selected);
            txtSocial.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
//        else if (position == 4) {
//            newFragment = new MobileAppFragment();
//        } else if (position == 5) {
//            newFragment = new WebDevelopmentFragment();
//        } else if (position == 6) {
//            newFragment = new BrandingFragment();
//        }

        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragmentContainer, newFragment)
                .commit();
    }

}