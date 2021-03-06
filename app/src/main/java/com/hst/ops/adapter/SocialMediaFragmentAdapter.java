package com.hst.ops.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hst.ops.fragment.DailyhuntFragment;
import com.hst.ops.fragment.FacebookFragment;
import com.hst.ops.fragment.InstagramFragment;
import com.hst.ops.fragment.LinkedinFragment;
import com.hst.ops.fragment.SharechatFragment;
import com.hst.ops.fragment.TwitterFragment;
import com.hst.ops.fragment.YoutubeFragment;

public class SocialMediaFragmentAdapter extends FragmentStatePagerAdapter {

    public SocialMediaFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FacebookFragment();
            case 1:
                return new TwitterFragment();
            case 2:
                return new InstagramFragment();
            case 3:
                return new LinkedinFragment();
            case 4:
                return new YoutubeFragment();
            case 5:
                return new SharechatFragment();
            case 6:
                return new DailyhuntFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 7;
    }
}