package com.hst.ops.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.hst.ops.R;
import com.hst.ops.activity.LiveEventActivity;
import com.hst.ops.activity.NewsfeedDetailActivity;
import com.hst.ops.adapter.NewsFeedListAdapter;
import com.hst.ops.adapter.SocialMediaFragmentAdapter;
import com.hst.ops.bean.support.NewsFeed;
import com.hst.ops.bean.support.NewsFeedList;
import com.hst.ops.helper.AlertDialogHelper;
import com.hst.ops.helper.ProgressDialogHelper;
import com.hst.ops.interfaces.DialogClickListener;
import com.hst.ops.servicehelpers.ServiceHelper;
import com.hst.ops.serviceinterfaces.IServiceListener;
import com.hst.ops.utils.CommonUtils;
import com.hst.ops.utils.OPSConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SocialFragment extends Fragment {

    private static final String TAG = SocialFragment.class.getName();
    private View view;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;

    private TabLayout.TabLayoutOnPageChangeListener tabatab;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static EventFragment newInstance(int position) {
        EventFragment frag = new EventFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_social, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        initialiseTabs();

        return view;
    }

        private void initialiseTabs() {
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.facebook)));
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.twitter)));
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.instagram)));
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.linkedin)));
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.youtube)));
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.sharechat)));
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.dailyhunt)));

            final SocialMediaFragmentAdapter adapter = new SocialMediaFragmentAdapter(getChildFragmentManager());

            viewPager.setAdapter(adapter);
            tabatab = new TabLayout.TabLayoutOnPageChangeListener(tabLayout);
            viewPager.addOnPageChangeListener(tabatab);
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
    //Bonus Code : If your tab layout has more than 2 tabs then tab will scroll other wise they will take whole width of the screen
            if (tabLayout.getTabCount() <= 2) {
                tabLayout.setTabMode(TabLayout.
                        MODE_FIXED);
            } else {
                tabLayout.setTabMode(TabLayout.
                        MODE_SCROLLABLE);
            }
        }

}