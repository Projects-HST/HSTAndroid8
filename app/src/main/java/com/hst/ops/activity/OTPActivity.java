package com.hst.ops.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.hst.ops.R;
import com.hst.ops.bean.support.database.SQLiteHelper;
import com.hst.ops.customview.CustomOtpEditText;
import com.hst.ops.helper.AlertDialogHelper;
import com.hst.ops.helper.ProgressDialogHelper;
import com.hst.ops.interfaces.DialogClickListener;
import com.hst.ops.servicehelpers.ServiceHelper;
import com.hst.ops.serviceinterfaces.IServiceListener;
import com.hst.ops.utils.CommonUtils;
import com.hst.ops.utils.OPSConstants;
import com.hst.ops.utils.PreferenceStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.util.Log.d;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = OTPActivity.class.getName();

    private CustomOtpEditText otpEditText;
    private TextView tvResendOTP, tvCountDown;
    private Button btnConfirm;
    private Button btnChangeNumber;
    private String mobileNo;
    private String checkVerify;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private static final int SMS_CONSENT_REQUEST = 2;  // Set to an unused request code
    SQLiteHelper database;
    private SmsBrReceiver smsReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        database = new SQLiteHelper(getApplicationContext());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mobileNo = PreferenceStorage.getPhoneNumber(getApplicationContext());
        otpEditText = (CustomOtpEditText) findViewById(R.id.otp_view);
        tvResendOTP = (TextView) findViewById(R.id.tryAgain);
        tvResendOTP.setOnClickListener(this);
        tvCountDown = findViewById(R.id.contResend);
        btnConfirm = (Button) findViewById(R.id.verify);
        btnConfirm.setOnClickListener(this);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        SmsRetrieverClient client = SmsRetriever.getClient(this);
        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(OTPActivity.this, "Listening for otp...", Toast.LENGTH_SHORT).show();
                IntentFilter filter = new IntentFilter();
                filter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
                if (smsReceiver == null) {
                    smsReceiver = new OTPActivity.SmsBrReceiver();
                }
                getApplicationContext().registerReceiver(smsReceiver, filter);
//                            startActivity(homeIntent);
//                            finish();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OTPActivity.this, "Failed listening for otp...", Toast.LENGTH_SHORT).show();
            }
        });
        countDownTimers();
    }

    void countDownTimers() {
        new CountDownTimer(30 * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvResendOTP.setVisibility(View.GONE);
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                tvCountDown.setText("Resend in " + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds) + " seconds");
            }

            public void onFinish() {
                tvCountDown.setText("Try again...");
                tvCountDown.setVisibility(View.GONE);
                tvResendOTP.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(new View(this).getWindowToken(), 0);
        return true;
    }

    @Override
    public void onClick(View v) {

        if (CommonUtils.isNetworkAvailable(getApplicationContext())){

            if (v == tvResendOTP) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(R.string.resend_otp);
                alertDialogBuilder.setMessage(getString(R.string.mobile_number) + getString(R.string.mobile_number_tag) +
                        PreferenceStorage.getPhoneNumber(getApplicationContext()));
                alertDialogBuilder.setPositiveButton(R.string.alert_button_ok,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                checkVerify = "Resend";
                                countDownTimers();
                                tvCountDown.setVisibility(View.VISIBLE);
                                JSONObject jsonObject = new JSONObject();
                                try {

                                    jsonObject.put(OPSConstants.PHONE_NUMBER, PreferenceStorage.getMobile_no(getApplicationContext()));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                                String url = OPSConstants.BUILD_URL + OPSConstants.GENERATE_OTP;
                                serviceHelper.makeGetServiceCall(jsonObject.toString(), url);

                            }
                        });
                    alertDialogBuilder.setNegativeButton(R.string.alert_button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

//                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialogBuilder.show();

            } else if (v == btnConfirm) {
                if (otpEditText.hasValidOTP()) {
                    checkVerify = "Confirm";
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(OPSConstants.PHONE_NUMBER, PreferenceStorage.getMobile_no(getApplicationContext()));
                        jsonObject.put(OPSConstants.OTP, otpEditText.getOTP());
                        jsonObject.put(OPSConstants.DEVICE_TOKEN, PreferenceStorage.getGCM(getApplicationContext()));
                        jsonObject.put(OPSConstants.MOBILE_TYPE, "1");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                    String url = OPSConstants.BUILD_URL + OPSConstants.LOGIN;
                    serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
                } else {
                    if (PreferenceStorage.getLang(this).equalsIgnoreCase("tamil")) {
                        AlertDialogHelper.showSimpleAlertDialog(this, "தவறான கடவுச்சொல்!");
                    } else {
                        AlertDialogHelper.showSimpleAlertDialog(this, "Invalid OTP");
                    }
                }

            }
        }

    }

    private boolean validateResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(OPSConstants.PARAM_MESSAGE);
                d(TAG, "status val" + status + "msg" + msg);

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


        progressDialogHelper.hideProgressDialog();
        if (validateResponse(response)) {
            try {
                if (checkVerify.equalsIgnoreCase("Resend")) {

                    Toast.makeText(getApplicationContext(), "OTP resent successfully", Toast.LENGTH_SHORT).show();
                }
                else if (checkVerify.equalsIgnoreCase("Confirm") || checkVerify.equalsIgnoreCase("verified")) {
                    PreferenceStorage.setFirstTimeLaunch(getApplicationContext(), false);
                    database.app_info_check_insert("Y");
                    Log.d(TAG, response.toString());
                    JSONObject data = response.getJSONObject("userData");

                    String userId = data.getString("user_id");
                    String fullName = data.getString("full_name");
                    String phoneNo = data.getString("phone_number");
                    String language = data.getString("language_id");
                    String profilePic = data.getString("profile_pic");

                    PreferenceStorage.saveUserId(getApplicationContext(), userId);
                    PreferenceStorage.savePhoneNumber(getApplicationContext(), phoneNo);
                    PreferenceStorage.saveUserName(getApplicationContext(), fullName);
                    PreferenceStorage.saveUserPicture(getApplicationContext(), profilePic);
                    PreferenceStorage.saveLanguageId(getApplicationContext(), language);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            startActivity(profileIntent);
        }
    }
    @Override
    public void onError(String error) {

    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    class SmsBrReceiver extends BroadcastReceiver {

        private String parseCode(String message) {
            Pattern p = Pattern.compile("\\b\\d{4}\\b");
            Matcher m = p.matcher(message);
            String code = "";
            while (m.find()) {
                code = m.group(0);
            }
            return code;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (status.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get SMS message contents
                        String smsMessage = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                        // Extract one-time code from the message and complete verification
                        // by sending the code back to your server.
                        Log.d(TAG, "Retrieved sms code: " + smsMessage);
                        if (smsMessage != null) {
                            String sms = parseCode(smsMessage);
                            verifyMessage(sms);
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Waiting for SMS timed out (5 minutes)
                        // Handle the error ...
                        break;
                }
            }
        }

    }

    public void verifyMessage(String otp) {
        checkVerify = "verified";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(OPSConstants.PHONE_NUMBER, PreferenceStorage.getMobile_no(getApplicationContext()));
            jsonObject.put(OPSConstants.OTP, otp);
            jsonObject.put(OPSConstants.DEVICE_TOKEN, PreferenceStorage.getGCM(getApplicationContext()));
            jsonObject.put(OPSConstants.MOBILE_TYPE, "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = OPSConstants.BUILD_URL + OPSConstants.LOGIN;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }


//    private void setSda() {
//        checkVerify = "set_lang";
//        String langID = "";
//        if (PreferenceStorage.getLang(getApplicationContext()).equalsIgnoreCase("eng")) {
//            langID = "2";
//        } else {
//            langID = "1";
//        }
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put(OPSConstants.USER_MASTER_ID, PreferenceStorage.getUserId(getApplicationContext()));
//            jsonObject.put(OPSConstants.LANG_ID, langID);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
//        String url = OPSConstants.BUILD_URL + OPSConstants.SET_USER_LANG;
//        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
//    }

}