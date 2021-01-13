package com.hst.ops.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.hst.ops.R;
import com.hst.ops.helper.AlertDialogHelper;
import com.hst.ops.helper.ProgressDialogHelper;
import com.hst.ops.interfaces.DialogClickListener;
import com.hst.ops.servicehelpers.ServiceHelper;
import com.hst.ops.serviceinterfaces.IServiceListener;
import com.hst.ops.utils.OPSConstants;
import com.hst.ops.utils.PreferenceStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class LiveEventActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, IServiceListener, DialogClickListener {
    private static final String TAG = "YoutubeActivity";
    static final String GOOGLE_API_KEY = "AIzaSyC_DtVVTEaZG0X42yelM0XOZd7an2bDnzw";
    private String YOUTUBE_VIDEO_ID = "";

    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private LinearLayout listLayout;
    private ConstraintLayout videoLayout;
    private RelativeLayout cancelLayout;
    private Button close;
    private TextView moreInfo;
    private String meetingID;
    private TextView txtNewsfeedTitle, txtNewsDate, txtNewsfeedDescription, txtLikes, txtComments, txtShares;
    YouTubePlayerView playerView;
    ScrollView vvvv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_events);
        listLayout = (LinearLayout) findViewById(R.id.list_layout);
        videoLayout = (ConstraintLayout) findViewById(R.id.constraint_layout);
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        vvvv = findViewById(R.id.vvvv);
        cancelLayout = findViewById(R.id.cancel_layout);
        txtNewsfeedTitle = (TextView) findViewById(R.id.news_title);
        txtNewsDate = (TextView) findViewById(R.id.news_date);
        txtLikes = (TextView) findViewById(R.id.likes_count);
        txtComments = (TextView) findViewById(R.id.comments_count);
        txtShares = (TextView) findViewById(R.id.shares_count);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        meetingID = getIntent().getStringExtra("meetingObj");

        getVideoDetail();

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        Log.d(TAG, "onInitializationSuccess: The provider is " + provider.getClass().toString());
//        Toast.makeText(this, "Initialized Youtube Player Successfully", Toast.LENGTH_LONG).show();

        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);

        if (!wasRestored) {
            youTubePlayer.loadVideo(YOUTUBE_VIDEO_ID);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        final int REQUEST_CODE = 1;

        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, REQUEST_CODE).show();
        } else {
            String errorMessage = String.format("There was an error initializing the YoutubePlayer (%1$s)", youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {
//            Toast.makeText(YoutubeActivity.this, "Good, Video is playing", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPaused() {
//            Toast.makeText(YoutubeActivity.this, "Video has paused", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onStopped() {
//            Toast.makeText(YoutubeActivity.this, "Good, Video has stopped", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {
//            Toast.makeText(YoutubeActivity.this, "Ad has started. Click on Ad.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onVideoStarted() {
//            Toast.makeText(YoutubeActivity.this, "Video has started", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onVideoEnded() {
//            Toast.makeText(YoutubeActivity.this, "Congratulations on finishing another video", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    };

    private void getVideoDetail() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(OPSConstants.KEY_USER_ID, "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = OPSConstants.BUILD_URL + OPSConstants.GET_LIVE_EVENT_LIST;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
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
        progressDialogHelper.hideProgressDialog();
        if (validateResponse(response)) {
            try {
                JSONArray data = response.getJSONArray("liveevent_result");
                createliveList(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error) {

    }

    private void createliveList(JSONArray data) {
        for (int i = 0; i < data.length(); i++) {
            try {

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(20, 40, 20, 0);

                TextView textViewTitle = new TextView(this);
                if (PreferenceStorage.getLang(this).equalsIgnoreCase("english")) {
                    textViewTitle.setText(data.getJSONObject(i).getString("title_en"));
                } else {
                    textViewTitle.setText(data.getJSONObject(i).getString("title_ta"));
                }
                textViewTitle.setLayoutParams(layoutParams);
                textViewTitle.setTextSize(20.0f);
                textViewTitle.setTextColor(ContextCompat.getColor(this, R.color.black));

                LinearLayout.LayoutParams layoutParamsVutton = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                layoutParamsVutton.setMargins(20, 20, 20, 20);
                TextView viewVideo = new TextView(this);
                viewVideo.setText(R.string.live_event_video_view);
                viewVideo.setLayoutParams(layoutParamsVutton);
                viewVideo.setGravity(Gravity.CENTER_HORIZONTAL);
                viewVideo.setTextColor(ContextCompat.getColor(this, R.color.live_event_fab));
                viewVideo.setPadding(20, 20, 20, 20);
                viewVideo.setTextSize(20.0f);
                viewVideo.setTypeface(Typeface.DEFAULT_BOLD);
                int finalI = i;
                viewVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        try {
                            YOUTUBE_VIDEO_ID = (data.getJSONObject(finalI).getString("live_url"));
                            showvideopopup();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                ImageView imageView = new ImageView(this);
                imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_video_play));
                imageView.setLayoutParams(layoutParamsVutton);


                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                linearLayout.addView(viewVideo);
                linearLayout.addView(imageView);



                LinearLayout.LayoutParams lineView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
                lineView.setMargins(20,0,20,0);
                View view = new View(this);
                view.setLayoutParams(lineView);
                view.setBackgroundColor(ContextCompat.getColor(this, R.color.lite_grey));


                listLayout.addView(textViewTitle);
                listLayout.addView(linearLayout);
                listLayout.addView(view);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showvideopopup() {
        Intent intent = new Intent(this, ViewVideoActivity.class);
        intent.putExtra("meetingObj", YOUTUBE_VIDEO_ID);
        intent.putExtra("page", "LiveVideo");
        startActivity(intent);
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }
}