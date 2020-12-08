package com.hst.ops.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.hst.ops.R;
import com.hst.ops.adapter.AboutOpsFragmentAdapter;
import com.hst.ops.fragment.AboutPartyFragment;

public class AboutOPS extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private TabLayout.TabLayoutOnPageChangeListener tab_indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_ops);

//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }

        back = (ImageView)findViewById(R.id.img_back);

        back.setOnClickListener(this);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_lay);
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Biography"));
        tabLayout.addTab(tabLayout.newTab().setText("Achievements"));
        tabLayout.addTab(tabLayout.newTab().setText("About Party"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_START);

        AboutOpsFragmentAdapter opsAdapter = new AboutOpsFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(opsAdapter);

        tab_indicator = new TabLayout.TabLayoutOnPageChangeListener(tabLayout);
        viewPager.addOnPageChangeListener(tab_indicator);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                viewPager.getCurrentItem();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                viewPager.getCurrentItem();
            }
        });
        if (tabLayout.getTabCount() <= 2) {
            tabLayout.setTabMode(TabLayout.
                    MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.
                    MODE_SCROLLABLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == back){
            Intent backIntent = new Intent(this, MainActivity.class);
            startActivity(backIntent);
        }
    }
}