package com.hst.ops.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hst.ops.R;
import com.hst.ops.activity.AboutOPS;
import com.hst.ops.activity.StateDetailActivity;
import com.hst.ops.activity.ViewVideoActivity;
import com.hst.ops.adapter.PartyStateAdapter;
import com.hst.ops.bean.support.Gallery;
import com.hst.ops.bean.support.PartyStateList;
import com.hst.ops.helper.AlertDialogHelper;
import com.hst.ops.helper.ProgressDialogHelper;
import com.hst.ops.servicehelpers.ServiceHelper;
import com.hst.ops.serviceinterfaces.IServiceListener;
import com.hst.ops.utils.CommonUtils;
import com.hst.ops.utils.OPSConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;


public class AboutPartyFragment extends Fragment implements View.OnClickListener, IServiceListener,
        PartyStateAdapter.OnItemClickListener {

    private static final String TAG = AboutOPS.class.getName();
    private View rootView;
    private RelativeLayout partyLayout, achieveLayout;
    private TextView abt_party, party_name, party_achievement, party_content;
    private RecyclerView stateList;
    private RecyclerView.LayoutManager mlayoutManager;
    private ArrayList<PartyStateList> stateArrayList;
    private PartyStateAdapter stateAdapter;
    private ProgressDialogHelper dialogHelper;
    private ServiceHelper serviceHelper;
    private String resString;

    public static AboutPartyFragment newInstance(int position) {
        AboutPartyFragment fragment = new AboutPartyFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_about_party, container, false);
        partyLayout = (RelativeLayout)rootView.findViewById(R.id.partyLayout);
        achieveLayout = (RelativeLayout)rootView.findViewById(R.id.achieveLayout);
        abt_party = (TextView)rootView.findViewById(R.id.abt_party);
        abt_party.setOnClickListener(this);
        party_achievement = (TextView)rootView.findViewById(R.id.achievements);
        party_achievement.setOnClickListener(this);
        party_name = (TextView)rootView.findViewById(R.id.party);
        party_content = (TextView) rootView.findViewById(R.id.viewText);

        stateList = (RecyclerView) rootView.findViewById(R.id.st_party_list);
        stateArrayList = new ArrayList<>();

        dialogHelper = new ProgressDialogHelper(getActivity());
        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);

        getAboutParty();
        return rootView;
    }

    private void getAboutParty(){

        if (CommonUtils.isNetworkAvailable(getActivity())) {

            resString = "about_party";

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(OPSConstants.KEY_USER_ID, "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String serverUrl = OPSConstants.BUILD_URL + OPSConstants.ABOUT_PARTY;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), serverUrl);
        }
        else {
            AlertDialogHelper.showSimpleAlertDialog(getActivity(), getString(R.string.error_no_net));
        }
    }

    private void getPartyStateList(){

        if (CommonUtils.isNetworkAvailable(getActivity())) {

            resString = "party_achievement";

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(OPSConstants.KEY_USER_ID, "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            dialogHelper.showProgressDialog(getResources().getString(R.string.progress_loading));
            String serverUrl = OPSConstants.BUILD_URL + OPSConstants.PARTY_STATES;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), serverUrl);
        }
        else {
            AlertDialogHelper.showSimpleAlertDialog(getActivity(), getString(R.string.error_no_net));
        }
    }

    @Override
    public void onClick(View v) {

        if (v == abt_party){

//            abt_party.setEnabled(true);
            partyLayout.setVisibility(View.VISIBLE);
            achieveLayout.setVisibility(View.GONE);
            abt_party.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bt_enabled));
            abt_party.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            party_achievement.setBackground(null);
            party_achievement.setTextColor(getResources().getColor(R.color.txt_disabled));

            getAboutParty();
        }

        if (v == party_achievement) {

//            party_achievement.setEnabled(true);
            partyLayout.setVisibility(View.GONE);
            achieveLayout.setVisibility(View.VISIBLE);
            party_achievement.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bt_enabled));
            party_achievement.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            abt_party.setBackground(null);
            abt_party.setTextColor(getResources().getColor(R.color.txt_disabled));
            stateArrayList.clear();
            getPartyStateList();
        }
    }

    public boolean validateResponse(JSONObject response){
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(OPSConstants.PARAM_MESSAGE);
                Log.d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (status.equalsIgnoreCase("success")) {
                        signInSuccess = true;
                    } else {
                        signInSuccess = false;
                        Log.d(TAG, "Show error dialog");
//                        AlertDialogHelper.showSimpleAlertDialog(getActivity(), msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    @Override
    public void onResponse(JSONObject response) {

        try {
            if (validateResponse(response)){

                if (resString.equalsIgnoreCase("about_party")){

                    JSONArray partyArray = response.getJSONArray("party_result");
                    JSONObject object = partyArray.getJSONObject(0);

                    String about = "";

                    about = object.getString("party_text_en");
                    party_content.setText(Html.fromHtml(about));
                }

                if (resString.equalsIgnoreCase("party_achievement")){

                    JSONArray stateArray = response.getJSONArray("state_result");
                    JSONObject object = stateArray.getJSONObject(0);
                    int getLength = stateArray.length();

                    String id = "";
                    String logo = "";
                    String name = "";

                    for (int i=0; i<getLength; i++){
                        id = stateArray.getJSONObject(i).getString("state_id");
                        name = stateArray.getJSONObject(i).getString("state_name_en");
                        logo = stateArray.getJSONObject(i).getString("state_logo");

                        stateArrayList.add(new PartyStateList(id,name,logo));
                    }
                    stateAdapter = new PartyStateAdapter(stateArrayList, this);
                    mlayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false);
                    stateList.setLayoutManager(mlayoutManager);
                    stateList.setAdapter(stateAdapter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onItemClick(View view, int position) {
        PartyStateList partyStateList = null;
        partyStateList = stateArrayList.get(position);
        Intent intent;
        intent = new Intent(getActivity(), StateDetailActivity.class);
        intent.putExtra("stateId", partyStateList.getState_id());
        intent.putExtra("page", partyStateList.getState_name_en());
        startActivity(intent);
    }
}