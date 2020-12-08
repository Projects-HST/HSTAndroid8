package com.hst.ops.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hst.ops.fragment.AboutPartyFragment;
import com.hst.ops.fragment.AchievementFragment;
import com.hst.ops.fragment.BiographyFragment;

public class AboutOpsFragmentAdapter extends FragmentStatePagerAdapter {

    public AboutOpsFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BiographyFragment();
            case 1:
                return new AchievementFragment();
            case 2:
                return new AboutPartyFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}