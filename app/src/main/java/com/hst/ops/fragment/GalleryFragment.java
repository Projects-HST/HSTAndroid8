package com.hst.ops.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.hst.ops.R;
import com.hst.ops.activity.AllImagesActivity;
import com.hst.ops.activity.AllVideosActivity;
import com.hst.ops.activity.NewsfeedDetailActivity;
import com.hst.ops.activity.ViewImageActivity;
import com.hst.ops.activity.ViewVideoActivity;
import com.hst.ops.adapter.GalleryListAdapter;
import com.hst.ops.adapter.ImageListAdapter;
import com.hst.ops.bean.support.Gallery;
import com.hst.ops.bean.support.GalleryImageList;
import com.hst.ops.bean.support.GalleryVideoList;
import com.hst.ops.helper.AlertDialogHelper;
import com.hst.ops.helper.ProgressDialogHelper;
import com.hst.ops.interfaces.DialogClickListener;
import com.hst.ops.servicehelpers.ServiceHelper;
import com.hst.ops.serviceinterfaces.IServiceListener;
import com.hst.ops.utils.CommonUtils;
import com.hst.ops.utils.OPSConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GalleryFragment extends Fragment implements IServiceListener, DialogClickListener, GalleryListAdapter.OnItemClickListener, ImageListAdapter.OnItemClickListener, View.OnClickListener {

    private static final String TAG = GalleryFragment.class.getName();
    private View view;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    int cc = 0;
    private GridLayoutManager mLayoutManager, mLayoutManagerImages;
    private RecyclerView recyclerView, recyclerViewImages;
    SwipeRefreshLayout swipeRefreshLayout, swipeRefreshLayoutImages;
    int listcount = 0;
    GalleryImageList galleryImageList;
    GalleryVideoList galleryVideoList;
    ArrayList<Gallery> galleryArrayList = new ArrayList<>();
    ArrayList<Gallery> galleryImagesArrayList = new ArrayList<>();
    GalleryListAdapter mAdapter;
    ImageListAdapter mAdapterImages;
    private LinearLayout images, videos;
    private TextView viewMoreImages, viewMoreVideos;

    public static GalleryFragment newInstance(int position) {
        GalleryFragment frag = new GalleryFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery, container, false);
        serviceHelper = new ServiceHelper(view.getContext());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(view.getContext());

        images = view.findViewById(R.id.images_layout);
        videos = view.findViewById(R.id.videos_layout);
        viewMoreImages = view.findViewById(R.id.view_all_images);
        viewMoreVideos = view.findViewById(R.id.view_all_video);
        viewMoreImages.setOnClickListener(this);
        viewMoreVideos.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.list_refresh);

        recyclerViewImages = view.findViewById(R.id.recycler_view_images);
        swipeRefreshLayoutImages = (SwipeRefreshLayout) view.findViewById(R.id.list_refresh_images);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(false);

            }
        });
        swipeRefreshLayoutImages.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayoutImages.setRefreshing(false);

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

        getNewsfeed(String.valueOf(listcount));

        return view;
    }

    private void loadmore() {
        listcount = listcount + 50;
        getNewsfeed(String.valueOf(listcount));
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateResponse(response)) {
//            try {
//                constituentCount.setText(response.getString("meeting_count") + " Records");
//                cc = response.getJSONArray("meeting_details").length();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            Gson gson = new Gson();

            galleryVideoList = gson.fromJson(response.toString(), GalleryVideoList.class);
            galleryArrayList.addAll(galleryVideoList.getGalleryArrayList());
            mAdapter = new GalleryListAdapter(galleryArrayList, this);
            mLayoutManager = new GridLayoutManager(getActivity(), 4);
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

            galleryImageList = gson.fromJson(response.toString(), GalleryImageList.class);
            galleryImagesArrayList.addAll(galleryImageList.getGalleryArrayList());
            mAdapterImages = new ImageListAdapter(galleryImagesArrayList, this);
            mLayoutManagerImages = new GridLayoutManager(getActivity(), 4);
            mLayoutManagerImages.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mAdapterImages.getItemViewType(position) > 0) {
                        return mAdapterImages.getItemViewType(position);
                    } else {
                        return 1;
                    }
                    //return 2;
                }
            });
            recyclerViewImages.setLayoutManager(mLayoutManagerImages);
            recyclerViewImages.setAdapter(mAdapterImages);
            swipeRefreshLayoutImages.setRefreshing(false);

            if (galleryArrayList.isEmpty()) {
                videos.setVisibility(View.GONE);
            }
            if (galleryImagesArrayList.isEmpty()) {
                images.setVisibility(View.GONE);
            }

        }
    }

    private void getNewsfeed(String count) {
        if (CommonUtils.isNetworkAvailable(getActivity())) {
            JSONObject jsonObject = new JSONObject();
            try {
//                if (val == 0) {
//                    paguthiID = "ALL";
//                }
                jsonObject.put(OPSConstants.KEY_USER_ID, "");

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = OPSConstants.BUILD_URL + OPSConstants.GET_GALLERY;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
        } else {
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
                        if (listcount == 0) {
                            swipeRefreshLayout.setVisibility(View.GONE);
                        }
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onItemClick(View view, int position) {
        Gallery meeting = null;
        meeting = galleryArrayList.get(position);
        Intent intent;
        intent = new Intent(getActivity(), ViewVideoActivity.class);
        intent.putExtra("meetingObj", meeting.getId());
        startActivity(intent);
    }

    @Override
    public void onItemCClick(View view, int position) {
        Gallery meeting = null;
        meeting = galleryImagesArrayList.get(position);
        Intent intent;
        intent = new Intent(getActivity(), ViewImageActivity.class);
        intent.putExtra("meetingObj", meeting.getId());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v == viewMoreImages) {
            Intent i = new Intent(getActivity(), AllImagesActivity.class);
            startActivity(i);
        }
        if (v == viewMoreVideos) {
            Intent i = new Intent(getActivity(), AllVideosActivity.class);
            startActivity(i);
        }
    }
}