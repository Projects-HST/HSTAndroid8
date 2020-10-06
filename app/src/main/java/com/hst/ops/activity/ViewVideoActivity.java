package com.hst.ops.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;

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
import com.hst.ops.utils.OPSValidator;
import com.hst.ops.utils.PreferenceStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, IServiceListener, DialogClickListener {
    private static final String TAG = "YoutubeActivity";
    static final String GOOGLE_API_KEY = "AIzaSyC_DtVVTEaZG0X42yelM0XOZd7an2bDnzw";
    private String YOUTUBE_VIDEO_ID = "";

    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private ConstraintLayout layout ;
    private RelativeLayout cancelLayout;
    private Button close;
    private TextView moreInfo;
    private String meetingID;
    private TextView txtNewsfeedTitle, txtNewsDate, txtNewsfeedDescription, txtLikes, txtComments, txtShares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);
        layout = (ConstraintLayout) findViewById(R.id.constraint_layout);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtNewsfeedTitle = (TextView) findViewById(R.id.news_title);
        txtNewsDate = (TextView) findViewById(R.id.news_date);
        txtLikes = (TextView) findViewById(R.id.likes_count);
        txtComments = (TextView) findViewById(R.id.comments_count);
        txtShares = (TextView) findViewById(R.id.shares_count);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        meetingID = getIntent().getStringExtra("meetingObj");
        String page = getIntent().getStringExtra("page");
        if (page.equalsIgnoreCase("AllVideo")) {
            getVideoDetail();
        } else {
            YOUTUBE_VIDEO_ID = meetingID;
            YouTubePlayerView playerView = new YouTubePlayerView(this);
            playerView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.addView(playerView);
            playerView.initialize(GOOGLE_API_KEY, this);
        }

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        Log.d(TAG, "onInitializationSuccess: The provider is " + provider.getClass().toString());
//        Toast.makeText(this, "Initialized Youtube Player Successfully", Toast.LENGTH_LONG).show();

        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);

        if(!wasRestored){
            youTubePlayer.cueVideo(YOUTUBE_VIDEO_ID);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        final int REQUEST_CODE = 1;

        if(youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(this, REQUEST_CODE).show();
        } else{
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
            jsonObject.put(OPSConstants.NEWSFEED_ID, meetingID);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = OPSConstants.BUILD_URL + OPSConstants.GET_NEWSFEED_DETAIL;
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
                JSONArray data = response.getJSONArray("news_result");
                if (PreferenceStorage.getLang(txtNewsfeedTitle.getContext()).equalsIgnoreCase("english")) {
                    txtNewsfeedTitle.setText(data.getJSONObject(0).getString("title_en"));
                    txtLikes.setText(data.getJSONObject(0).getString("likes_count") + " Likes");
                    txtComments.setText(data.getJSONObject(0).getString("comments_count") + " Comments");
                    txtShares.setText(data.getJSONObject(0).getString("share_count") + " Shares");
                } else {
                    txtNewsfeedTitle.setText(data.getJSONObject(0).getString("title_en"));
                    txtLikes.setText(data.getJSONObject(0).getString("likes_count") + " விருப்ப");
                    txtComments.setText(data.getJSONObject(0).getString("comments_count") + " கருத்து");
                    txtShares.setText(data.getJSONObject(0).getString("share_count") + " பகிர்");
                }
                txtNewsDate.setText(getserverdateformat(data.getJSONObject(0).getString("news_date")));
                YOUTUBE_VIDEO_ID = (data.getJSONObject(0).getString("nf_video_token_id"));
                YouTubePlayerView playerView = new YouTubePlayerView(this);
                playerView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                layout.addView(playerView);
                playerView.initialize(GOOGLE_API_KEY, this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error) {

    }

    private String getserverdateformat(String dd) {
        String serverFormatDate = "";
        if (dd != null && dd != "") {

            String date = dd;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date testDate = null;
            try {
                testDate = formatter.parse(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            serverFormatDate = sdf.format(testDate);
            System.out.println(".....Date..." + serverFormatDate);
        }
        return serverFormatDate;
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }
}