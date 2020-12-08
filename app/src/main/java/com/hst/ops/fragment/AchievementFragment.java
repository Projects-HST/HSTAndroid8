package com.hst.ops.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hst.ops.R;
import com.hst.ops.activity.AboutOPS;
import com.hst.ops.adapter.AchievementAdapter;
import com.hst.ops.bean.support.AchievementList;
import com.hst.ops.helper.AlertDialogHelper;
import com.hst.ops.servicehelpers.ServiceHelper;
import com.hst.ops.serviceinterfaces.IServiceListener;
import com.hst.ops.utils.CommonUtils;
import com.hst.ops.utils.OPSConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AchievementFragment extends Fragment implements IServiceListener {

    private static final String TAG = AboutOPS.class.getName();
    private View rootView;
    private ListView achieveListView;

    private ArrayList<AchievementList> achievementList;
    private AchievementAdapter achieveAdapter;
    private ServiceHelper serviceHelper;

    public static AchievementFragment newInstance(int position) {
        AchievementFragment fragment = new AchievementFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_achievement, container, false);

        achieveListView = (ListView)rootView.findViewById(R.id.achievement_list);
        achievementList = new ArrayList<>();

        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);
        getAchievementList();
        return rootView;
    }

    private void getAchievementList() {

        if (CommonUtils.isNetworkAvailable(getActivity())) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(OPSConstants.KEY_USER_ID, "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            String serverUrl = OPSConstants.BUILD_URL + OPSConstants.GET_ACHIEVEMENTS;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), serverUrl);
        }
        else {
            AlertDialogHelper.showSimpleAlertDialog(getActivity(), getString(R.string.error_no_net));
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

        try{

            if (validateResponse(response)){

                JSONArray achieveArray = response.getJSONArray("achievement_result");
                JSONObject object = achieveArray.getJSONObject(0);
                int getLength = achieveArray.length();

                String id = "";
                String imageUrl = "";
                String title = "";
                String content = "";
                String date = "";

                for (int i=0; i<getLength; i++){

                    id = object.getString("achievement_id");
                    imageUrl = object.getString("achievement_image");
                    title = object.getString("achievement_title_en");
                    content = object.getString("achievement_text_en");
                    date = object.getString("achievement_date");

                    achievementList.add(new AchievementList(id,imageUrl,title,content,date));
                }
            }
            achieveAdapter = new AchievementAdapter(getActivity(), achievementList);
            achieveListView.setAdapter(achieveAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {

    }
}