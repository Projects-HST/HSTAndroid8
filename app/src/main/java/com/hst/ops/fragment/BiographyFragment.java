package com.hst.ops.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hst.ops.R;
import com.hst.ops.activity.AboutOPS;
import com.hst.ops.dialogfragments.AlertDialogForFragment;
import com.hst.ops.helper.AlertDialogHelper;
import com.hst.ops.servicehelpers.ServiceHelper;
import com.hst.ops.serviceinterfaces.IServiceListener;
import com.hst.ops.utils.CommonUtils;
import com.hst.ops.utils.OPSConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class BiographyFragment extends Fragment implements IServiceListener {

    private static final String TAG = AboutOPS.class.getName();
    private View rootView;
    private TextView cont_personal, cont_political;

    private ServiceHelper serviceHelper;

    public static BiographyFragment newInstance(int position) {
        BiographyFragment fragment = new BiographyFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_biography, container, false);

        cont_personal = (TextView)rootView.findViewById(R.id.cont_personal);
        cont_political = (TextView)rootView.findViewById(R.id.cont_political);

        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);

        getAboutOps();

        return rootView;

    }

    private void getAboutOps(){

        if (CommonUtils.isNetworkAvailable(getActivity())) {

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(OPSConstants.KEY_USER_ID, "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url = OPSConstants.BUILD_URL + OPSConstants.GET_BIOGRAPHY;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
        }
        else {
            AlertDialogHelper.showSimpleAlertDialog(getActivity(), getString(R.string.error_no_net));
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

            if (validateResponse(response)) {

                JSONArray biogrphy_result = response.getJSONArray("biogrphy_result");
                JSONObject object = biogrphy_result.getJSONObject(0);

                String personal = "";
                String political = "";

                for (int i=0; i<biogrphy_result.length(); i++){

                    personal = object.getString("personal_life_text_en");
                    cont_personal.setText(Html.fromHtml(personal));
                    political = object.getString("political_career_text_en");
                    cont_political.setText(Html.fromHtml(political));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {

    }
}