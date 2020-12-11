package com.hst.ops.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hst.ops.R;
import com.hst.ops.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

import java.io.PipedInputStream;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout logoutLayout;
    private ImageView profileImage, profileClick, langClick, shareClick, rateClick;
    private SwitchCompat notify;
    private TextView profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profileImage = (ImageView)findViewById(R.id.profileImage);
        profileName = (TextView)findViewById(R.id.userName);
        profileClick = (ImageView)findViewById(R.id.proClick);
        langClick = (ImageView)findViewById(R.id.langClick);
        shareClick = (ImageView)findViewById(R.id.shareClick);
        rateClick = (ImageView)findViewById(R.id.rateClick);

        profileClick.setOnClickListener(this);
        langClick.setOnClickListener(this);
        shareClick.setOnClickListener(this);
        rateClick.setOnClickListener(this);

        logoutLayout = (RelativeLayout)findViewById(R.id.logLayout);
        logoutLayout.setOnClickListener(this);


        String imgUrl = PreferenceStorage.getUserPicture(this);
        if (((imgUrl != null) && !(imgUrl.isEmpty()))) {
            Picasso.get().load(imgUrl).placeholder(R.drawable.ic_default_profile)
                    .error(R.drawable.ic_default_profile).into(profileImage);
        }
        String nameUrl = PreferenceStorage.getUserName(this);
        if (((nameUrl != null) && (nameUrl.isEmpty()))){
            profileName.setText(nameUrl);
        }
    }

    @Override
    public void onClick(View v) {

        if (v == profileClick){
            Intent proIntent = new Intent(this, ProfileActivity.class);
            startActivity(proIntent);
        }
        if (v == langClick){

        }
        if (v == shareClick){

        }
        if (v == rateClick){

        }
    }
}