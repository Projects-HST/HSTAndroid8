package com.hst.ops.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.hst.ops.R;
import com.hst.ops.helper.AlertDialogHelper;
import com.hst.ops.helper.ProgressDialogHelper;
import com.hst.ops.servicehelpers.ServiceHelper;
import com.hst.ops.serviceinterfaces.IServiceListener;
import com.hst.ops.utils.CommonUtils;
import com.hst.ops.utils.OPSConstants;
import com.hst.ops.utils.OPSValidator;
import com.hst.ops.utils.PermissionUtil;
import com.hst.ops.utils.PreferenceStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import static android.util.Log.d;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener {

    private static final String TAG = LoginActivity.class.getName();
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private EditText edtNumber;
    private Button signIn;
    private ImageView back;
    String IMEINo = "", resString = "";
    private static final int PERMISSION_REQUEST_CODE = 1;
//    private static String[] PERMISSIONS_ALL = {Manifest.permission.READ_CALENDAR,
//            Manifest.permission.WRITE_CALENDAR, Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
//    private static final int REQUEST_PERMISSION_All = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        back = findViewById(R.id.img_back);
        back.setOnClickListener(this);
        edtNumber = (EditText) findViewById(R.id.edtMobileNumber);
        signIn = findViewById(R.id.login);
        signIn.setOnClickListener(this);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            TelephonyManager tm = (TelephonyManager)
                    this.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.READ_PHONE_STATE};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                IMEINo = tm.getImei(1);
                IMEINo = String.valueOf(generateRandom(12));
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                        == PackageManager.PERMISSION_DENIED) {
                    IMEINo = "";
                } else {
                    IMEINo = String.valueOf(generateRandom(12));
                }
            }
            PreferenceStorage.saveIMEI(this, IMEINo);
        }

        if (PreferenceStorage.getLang(this).isEmpty()) {
//            showLangAlert();
        }

    }

    public static long generateRandom(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
//                INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(new View(this).getWindowToken(), 0);
//        return true;
//    }

//    private void requestAllPermissions() {
//
//        boolean requestPermission = PermissionUtil.requestAllPermissions(this);
//
//        if (requestPermission) {
//
//            Log.i(TAG,
//                    "Displaying contacts permission rationale to provide additional context.");
//
//            // Display a SnackBar with an explanation and a button to trigger the request.
//
//            ActivityCompat
//                    .requestPermissions(this, PERMISSIONS_ALL,
//                            REQUEST_PERMISSION_All);
//        } else {
//
//            ActivityCompat.requestPermissions(this, PERMISSIONS_ALL, REQUEST_PERMISSION_All);
//        }
//    }


    @Override
    public void onClick(View v) {

        if (CommonUtils.haveNetworkConnection(getApplicationContext())) {
            if (v == signIn) {
                if (validateFields()) {
                    String number = edtNumber.getText().toString();
                    PreferenceStorage.saveMobile_no(this, number);
                    String GCMKey = PreferenceStorage.getGCM(getApplicationContext());

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(OPSConstants.PHONE_NUMBER, number);
//                        jsonObject.put(SkilExConstants.REFERRAL_CODE, referral);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                    String url = OPSConstants.BUILD_URL + OPSConstants.GENERATE_OTP;
                    serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
                }
            }
        }

        if (v == back){
            Intent homeIntent = new Intent(this, MainActivity.class);
            startActivity(homeIntent);
        }
    }

    private boolean validateFields() {
        if (!OPSValidator.checkMobileNumLength(this.edtNumber.getText().toString().trim())) {
            edtNumber.setError(getString(R.string.error_number));
            requestFocus(edtNumber);
            return false;
        }
        if (!OPSValidator.checkNullString(this.edtNumber.getText().toString().trim())) {
            edtNumber.setError(getString(R.string.empty_entry));
            requestFocus(edtNumber);
            return false;
        } else {
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateSignInResponse(JSONObject response) {
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
        try{

            if (validateSignInResponse(response)){

                String mobile = "";
                mobile = response.getString("mobile_number");
                PreferenceStorage.saveMobile_no(this, mobile);
            }
            Intent otpIntent = new Intent(this, OTPActivity.class);
            startActivity(otpIntent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}