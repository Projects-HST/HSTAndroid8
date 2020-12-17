package com.hst.ops.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;

import com.hst.ops.R;
import com.hst.ops.adapter.PartyResultAdapter;
import com.hst.ops.bean.support.PartyResultList;
import com.hst.ops.bean.support.PartyStateList;
import com.hst.ops.helper.AlertDialogHelper;
import com.hst.ops.servicehelpers.ServiceHelper;
import com.hst.ops.serviceinterfaces.IServiceListener;
import com.hst.ops.utils.OPSConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StateDetailActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener {

    private static final String TAG = StateDetailActivity.class.getName();
    private ImageView back;
    private TextView pageTitle;
    private ListView st_resultList, nt_resultList;
    private ServiceHelper serviceHelper;
    private ArrayList<PartyResultList> resultArrayList;
    private PartyResultAdapter resultAdapter;
    private String stateId, stateName;
    private ArrayList<PartyStateList>partyStateList;
    PartyStateList stateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_detail);

        back = (ImageView)findViewById(R.id.img_back);
        back.setOnClickListener(this);

        pageTitle =(TextView)findViewById(R.id.st_na);
        st_resultList = (ListView)findViewById(R.id.st_eleList);
        nt_resultList = (ListView)findViewById(R.id.nt_eleList);

        stateId = getIntent().getStringExtra("stateId");
        stateName = getIntent().getStringExtra("page");

        partyStateList = new ArrayList<>();
        resultArrayList = new ArrayList<>();
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);

//        pageTitle.setText(stateList.getState_name_en());

        if (stateName != null){
//            for (int i=0; i<partyStateList.size(); i++) {
//                stateList = partyStateList.get(i);
                pageTitle.setText(stateName);
//            }
        }
        getPartyElectResult();
    }
    private void getPartyElectResult(){

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(OPSConstants.STATE_ID, stateId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String serverUrl = OPSConstants.BUILD_URL + OPSConstants.ELECTION_LIST;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), serverUrl);
    }

    @Override
    public void onClick(View v) {

        if (v == back){
            Intent backIntent = new Intent(this, AboutOPS.class);
            startActivity(backIntent);
            finish();
        }
    }

    private boolean validateResponse(JSONObject response) {
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
                        AlertDialogHelper.showSimpleAlertDialog(this, msg);
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
            if (validateResponse(response)) {

                JSONArray stateResult = response.getJSONArray("MLA_result");
                JSONObject object = stateResult.getJSONObject(0);
                int getLength = stateResult.length();
                Log.d(TAG, object.toString());

                String id = "";
                String st_id = "";
                String year = "";
                String leader = "";
                String seats_won = "";

                for (int i = 0; i < getLength; i++) {
                    id = stateResult.getJSONObject(i).getString("id");
                    st_id = stateResult.getJSONObject(i).getString("state_info_id");
                    year = stateResult.getJSONObject(i).getString("election_year");
                    leader = stateResult.getJSONObject(i).getString("party_leader_en");
                    seats_won = stateResult.getJSONObject(i).getString("seats_won");

                    resultArrayList.add(new PartyResultList(id, st_id, year, leader, seats_won));
                }
                resultAdapter = new PartyResultAdapter(resultArrayList, this);
                st_resultList.setAdapter(resultAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {

    }
}