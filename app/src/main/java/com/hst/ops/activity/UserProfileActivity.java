package com.hst.ops.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hst.ops.R;
import com.hst.ops.helper.AlertDialogHelper;
import com.hst.ops.servicehelpers.ServiceHelper;
import com.hst.ops.serviceinterfaces.IServiceListener;
import com.hst.ops.utils.CommonUtils;
import com.hst.ops.utils.OPSConstants;
import com.hst.ops.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PipedInputStream;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener {

    public static final String TAG = UserProfileActivity.class.getName();
    private RelativeLayout logoutLayout;
    private ImageView back, profileImage, profileClick, langClick, shareClick, rateClick;
    private SwitchCompat pushNotification;
    private TextView profileName;
    private String resString;
    private ServiceHelper serviceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        back = (ImageView)findViewById(R.id.img_back);
        profileImage = (ImageView)findViewById(R.id.profileImage);
        profileName = (TextView)findViewById(R.id.userName);
        pushNotification = (SwitchCompat)findViewById(R.id.notify);
        profileClick = (ImageView)findViewById(R.id.proClick);
        langClick = (ImageView)findViewById(R.id.langClick);
        shareClick = (ImageView)findViewById(R.id.shareClick);
        rateClick = (ImageView)findViewById(R.id.rateClick);

        back.setOnClickListener(this);
        profileClick.setOnClickListener(this);
        langClick.setOnClickListener(this);
        shareClick.setOnClickListener(this);
        rateClick.setOnClickListener(this);

        logoutLayout = (RelativeLayout)findViewById(R.id.logLayout);
        logoutLayout.setOnClickListener(this);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);

        String imgUrl = PreferenceStorage.getUserPicture(this);
        if (((imgUrl != null) && !(imgUrl.isEmpty()))) {
            Picasso.get().load(imgUrl).placeholder(R.drawable.ic_default_profile)
                    .error(R.drawable.ic_default_profile).into(profileImage);
        }

        if (PreferenceStorage.getUserName(this).equalsIgnoreCase("")){
            profileName.setText("");
        }else {
            profileName.setText(PreferenceStorage.getUserName(this));
        }

        pushNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getPushNotification();
            }
        });
    }

    private boolean validateResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(OPSConstants.PARAM_MESSAGE);
                Log.d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (status.equalsIgnoreCase("Success")) {
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
    public void onClick(View v) {

        if (v == back) {
            Intent homeIntent = new Intent(this, MainActivity.class);
            startActivity(homeIntent);
        }
        if (v == profileClick) {
            Intent proIntent = new Intent(this, ProfileActivity.class);
            startActivity(proIntent);
        }
        if (v == langClick) {

        }
        if (v == shareClick) {

        }
        if (v == rateClick) {

        }
        if (v == logoutLayout) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Logout");
            alertDialogBuilder.setMessage("Do you really want to logout?");
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(UserProfileActivity.this);
                    sharedPreferences.edit().clear().apply();

                    Intent homeIntent = new Intent(UserProfileActivity.this, SplashScreenActivity.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                }
            });
            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialogBuilder.show();
        }
    }

    private void getPushNotification() {

        if (CommonUtils.isNetworkAvailable(getApplicationContext())) {

            resString = "push_notification";

            JSONObject jsonObject = new JSONObject();
            boolean state = pushNotification.isChecked();
            String status = "";

            if (state){
                status = "1";
            } else {
                status = "0";
            }
            try {
                jsonObject.put(OPSConstants.KEY_USER_ID, "");
                jsonObject.put(OPSConstants.KEY_STATUS, status);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url = OPSConstants.BUILD_URL + OPSConstants.SUBSCRIPTION_UPDATE;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.error_no_net));
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            if (validateResponse(response)) {
                if (resString.equalsIgnoreCase("push_notification")) {
                    Log.d(TAG, response.toString());
                    Toast.makeText(this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {

    }
}