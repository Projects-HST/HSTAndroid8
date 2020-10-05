package com.hst.ops.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.hst.ops.R;
import com.hst.ops.adapter.GalleryListAdapter;
import com.hst.ops.bean.support.Gallery;
import com.hst.ops.bean.support.GalleryImageList;
import com.hst.ops.bean.support.GalleryVideoList;
import com.hst.ops.helper.AlertDialogHelper;
import com.hst.ops.helper.ProgressDialogHelper;
import com.hst.ops.interfaces.DialogClickListener;
import com.hst.ops.servicehelpers.ServiceHelper;
import com.hst.ops.serviceinterfaces.IServiceListener;
import com.hst.ops.utils.OPSConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllVideosActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener, GalleryListAdapter.OnItemClickListener {

    private static final String TAG = "AllImagesActivity";
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    int listcount = 0;
    GalleryVideoList galleryImageList;
    ArrayList<Gallery> galleryArrayList = new ArrayList<>();
    GalleryListAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_gallery);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        recyclerView = findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.list_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(false);

            }
        });
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_UP) {

                    if (!recyclerView.canScrollVertically(1)) {

                        swipeRefreshLayout.setRefreshing(false);

//                        loadmore();

                    }
                }
                return false;
            }
        });
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        getVideoDetail();
    }

    @Override
    public void onClick(View v) {

    }

    private void getVideoDetail() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(OPSConstants.KEY_USER_ID, "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = OPSConstants.BUILD_URL + OPSConstants.GET_ALL_VIDEOS;
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
            Gson gson = new Gson();

            galleryImageList = gson.fromJson(response.toString(), GalleryVideoList.class);
            galleryArrayList.addAll(galleryImageList.getGalleryArrayList());
            mAdapter = new GalleryListAdapter(galleryArrayList, this);
            mLayoutManager = new GridLayoutManager(this, 4);
            mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mAdapter.getItemViewType(position) > 0) {
                        return mAdapter.getItemViewType(position);
                    } else {
                        return 1;
                    }
                    //return 2;
                }
            });
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            swipeRefreshLayout.setRefreshing(false);
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

    @Override
    public void onItemClick(View view, int position) {
        Gallery meeting = null;
        meeting = galleryArrayList.get(position);
        Intent intent = new Intent(this, ViewVideoActivity.class);
        intent.putExtra("meetingObj", meeting.getId());
        startActivity(intent);
    }
}