package com.hst.ops.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hst.ops.R;
import com.hst.ops.utils.OPSValidator;
import com.hst.ops.utils.PreferenceStorage;

public class SplashScreenActivity extends Activity {

    private static int SPLASH_TIME_OUT = 3000;
    //    AppSignatureHelper appSignatureHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        /*ArrayList<String> appCodes = new ArrayList<>();
        SmsVerification hash = new SmsVerification(getBaseContext());
        appCodes= hash.getAppSignatures();
        String yourhash = appCodes.get(0);
        Log.d("Hash Key: ", yourhash);*/

//        Toast.makeText(SplashScreenActivity.this, "Hash key...  " + yourhash , Toast.LENGTH_SHORT).show();
//        hashFromSHA1("60:53:85:D2:89:8C:8A:AA:8B:55:D1:DE:73:A9:2D:C1:97:C7:27:A7");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PreferenceStorage.isFirstTimeLaunch(getApplicationContext())) {
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplashScreenActivity.this, new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            String newToken = instanceIdResult.getToken();
                            Log.e("newToken", newToken);
                            PreferenceStorage.saveGCM(getApplicationContext(), newToken);
                        }
                    });
                    Intent i = new Intent(SplashScreenActivity.this, YoutubeActivity.class);
                    PreferenceStorage.saveLang(getApplicationContext(), "english");
                    i.putExtra("page", "splash");
                    startActivity(i);
                } else {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    public void hashFromSHA1(String sha1) {
        String[] arr = sha1.split(":");
        byte[] byteArr = new byte[arr.length];

        for (int i = 0; i < arr.length; i++) {
            byteArr[i] = Integer.decode("0x" + arr[i]).byteValue();
        }

        Log.e("hashfaka : ", Base64.encodeToString(byteArr, Base64.NO_WRAP));
    }

}

