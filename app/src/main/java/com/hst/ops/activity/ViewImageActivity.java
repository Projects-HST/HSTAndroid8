package com.hst.ops.activity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.hst.ops.R;
import com.hst.ops.adapter.GalleryListAdapter;
import com.hst.ops.adapter.ImageGridListAdapter;
import com.hst.ops.adapter.ImageListAdapter;
import com.hst.ops.bean.support.Gallery;
import com.hst.ops.bean.support.GalleryImageList;
import com.hst.ops.bean.support.GalleryVideoList;
import com.hst.ops.bean.support.ImageDetailGrid;
import com.hst.ops.bean.support.ImageDetailGridList;
import com.hst.ops.customview.AViewFlipper;
import com.hst.ops.helper.AlertDialogHelper;
import com.hst.ops.helper.ProgressDialogHelper;
import com.hst.ops.interfaces.DialogClickListener;
import com.hst.ops.servicehelpers.ServiceHelper;
import com.hst.ops.serviceinterfaces.IServiceListener;
import com.hst.ops.utils.OPSConstants;
import com.hst.ops.utils.OPSValidator;
import com.hst.ops.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.view.View.GONE;

public class ViewImageActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, ImageGridListAdapter.OnItemClickListener,DialogClickListener {

    private static final String TAG = "NotificationActivity";
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;

    private ImageView touchImage;
    private RelativeLayout touchViewLay;
    private GridLayoutManager mLayoutManager, mLayoutManagerImages;
    private RecyclerView recyclerView, recyclerViewImages;
    private SwipeRefreshLayout swipeRefreshLayout, swipeRefreshLayoutImages;
    private ImageDetailGridList galleryImageList;
    private ArrayList<ImageDetailGrid> galleryImagesArrayList = new ArrayList<>();
    private ImageGridListAdapter imageGridListAdapter;



    private String meetingID;
    private TextView txtNewsfeedTitle, txtNewsDate, txtNewsfeedDescription, txtLikes, txtComments, txtShares;
    public ImageView newsImage;
    private ArrayList<String> imgUrl = new ArrayList<>();
//    AViewFlipper aViewFlipper;

//    private LinearLayout dotsLayout;
//    private TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        aViewFlipper = findViewById(R.id.view_flipper);
//        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);

        newsImage = (ImageView) findViewById(R.id.news_img);
        txtNewsfeedTitle = (TextView) findViewById(R.id.news_title);
        txtNewsDate = (TextView) findViewById(R.id.news_date);
        txtLikes = (TextView) findViewById(R.id.likes_count);
        txtComments = (TextView) findViewById(R.id.comments_count);
        txtShares = (TextView) findViewById(R.id.shares_count);

        meetingID = getIntent().getStringExtra("meetingObj");

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);


        touchImage = findViewById(R.id.viewImage);
        touchViewLay = (RelativeLayout) findViewById(R.id.touchView);
        touchViewLay.setOnClickListener(this);

        recyclerViewImages = findViewById(R.id.recycler_view_images);
        swipeRefreshLayoutImages = findViewById(R.id.list_refresh_images);
        recyclerViewImages.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_UP) {

                    if (!recyclerViewImages.canScrollVertically(1)) {

                        swipeRefreshLayoutImages.setRefreshing(false);

//                        loadmore();

                    }
                }
                return false;
            }
        });

        getVideoDetail();
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        super.dispatchTouchEvent(event);
//        return gestureDetector.onTouchEvent(event);
//    }
//
//    GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
//
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//                               float velocityY) {
//
//            float sensitvity = 100;
//            if (imgUrl.size() >= 1) {
//                if ((e1.getX() - e2.getX()) > sensitvity) {
//                    SwipeLeft();
//                } else if ((e2.getX() - e1.getX()) > sensitvity) {
//                    SwipeRight();
//                }
//            }
//
//            return true;
//        }
//
//    };

//    GestureDetector gestureDetector = new GestureDetector(simpleOnGestureListener);


//    private void SwipeLeft() {
//        aViewFlipper.setInAnimation(this, R.anim.in_from_right);
//        aViewFlipper.showNext();
//        addBottomDots(aViewFlipper.getDisplayedChild());
//    }
//
//
//    private void SwipeRight() {
//        aViewFlipper.setInAnimation(this, R.anim.in_from_left);
//        aViewFlipper.showPrevious();
//        addBottomDots(aViewFlipper.getDisplayedChild());
//    }

    @Override
    public void onClick(View v) {
        if (v == touchViewLay){
            touchViewLay.setVisibility(GONE);
        }
    }

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
                if (OPSValidator.checkNullString(data.getJSONObject(0).getString("nf_cover_image"))) {
                    imgUrl.add(data.getJSONObject(0).getString("nf_cover_image"));
                }
                Gson gson = new Gson();

                galleryImageList = gson.fromJson(response.toString(), ImageDetailGridList.class);
                galleryImagesArrayList.addAll(galleryImageList.getGalleryArrayList());
                imageGridListAdapter = new ImageGridListAdapter(galleryImagesArrayList, this);
                mLayoutManagerImages = new GridLayoutManager(this, 4);
                mLayoutManagerImages.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (imageGridListAdapter.getItemViewType(position) > 0) {
                            return imageGridListAdapter.getItemViewType(position);
                        } else {
                            return 1;
                        }
                        //return 2;
                    }
                });
                recyclerViewImages.setLayoutManager(mLayoutManagerImages);
                recyclerViewImages.setAdapter(imageGridListAdapter);
                swipeRefreshLayoutImages.setRefreshing(false);

//                JSONArray images = response.getJSONArray("image_result");
//                if (imgUrl.size() >= 0) {
//                    for (int i = 0; i < images.length(); i++) {
//                        imgUrl.add(images.getJSONObject(i).getString("gallery_url"));
//                    }
//                    for (int a = 0; a < imgUrl.size(); a++) {
////                        setImageInFlipr(imgUrl.get(a));
////                        addBottomDots(aViewFlipper.getDisplayedChild());
//                    }
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
    public void onItemCClick(View view, int position) {
        ImageDetailGrid meeting = null;
        meeting = galleryImagesArrayList.get(position);
//        imgLay.setClickable(true);
        String imageUrl = meeting.getGalleryUrl();
        if (OPSValidator.checkNullString(imageUrl)){
            touchViewLay.setVisibility(View.VISIBLE);
            touchImage.setVisibility(View.VISIBLE);
            Picasso.get().load(imageUrl).into(touchImage);
        } else {
            touchViewLay.setVisibility(GONE);
            touchImage.setVisibility(GONE);
        }
    }

//    private void setImageInFlipr(String imgUrl) {
//        ImageView image = new ImageView(this);
//        Picasso.get().load(imgUrl).into(image);
//        image.setScaleType(ImageView.ScaleType.FIT_XY);
//        aViewFlipper.addView(image);
//    }
//
//    private void addBottomDots(int currentPage) {
//        dots = new TextView[imgUrl.size()];
//
//        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
//        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
//
//        dotsLayout.removeAllViews();
//        for (int i = 0; i < dots.length; i++) {
//            dots[i] = new TextView(this);
//            dots[i].setText(Html.fromHtml("&#8226;"));
//            dots[i].setTextSize(30);
//            dots[i].setTextColor(colorsInactive[currentPage]);
//            dotsLayout.addView(dots[i]);
//        }
//
//        if (dots.length > 0) {
//            dots[currentPage].setTextColor(colorsActive[currentPage]);
//            dots[currentPage].setTextSize(35);
//        }
//
//    }

}