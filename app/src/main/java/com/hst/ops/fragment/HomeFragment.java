package com.hst.ops.fragment;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.hst.ops.interfaces.DialogClickListener;
import com.hst.ops.serviceinterfaces.IServiceListener;

import org.json.JSONObject;

public class HomeFragment extends Fragment implements IServiceListener, DialogClickListener, View.OnClickListener {
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onError(String error) {

    }
}