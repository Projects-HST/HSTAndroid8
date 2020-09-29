package com.hst.ops.activity;

import android.app.Fragment;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hst.ops.R;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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