package com.hst.ops.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hst.ops.R;
import com.hst.ops.bean.support.ImageDetailGrid;
import com.hst.ops.bean.support.ImageDetailGrid;
import com.hst.ops.utils.OPSValidator;
import com.hst.ops.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ImageGridListAdapter extends RecyclerView.Adapter<ImageGridListAdapter.MyViewHolder> {

    private ArrayList<ImageDetailGrid> newsFeedArrayList;
    Context context;
    private ImageGridListAdapter.OnItemClickListener onItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtNewsfeedTitle, txtNewsDate, txtNewsfeedDescription, txtLikes, txtComments, txtShares;
        public LinearLayout newsfeedLayout;
        public ImageView newsImage;
        public MyViewHolder(View view) {
            super(view);
            newsfeedLayout = (LinearLayout) view.findViewById(R.id.newsfeed_layout);
            newsImage = (ImageView) view.findViewById(R.id.news_img);
            newsfeedLayout.setOnClickListener(this);
            txtNewsfeedTitle = (TextView) view.findViewById(R.id.news_title);
            txtNewsDate = (TextView) view.findViewById(R.id.news_date);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemCClick(v, getAdapterPosition());
            }
//            else {
//                onClickListener.onClick(Selecttick);
//            }
        }
    }


    public ImageGridListAdapter(ArrayList<ImageDetailGrid> newsFeedArrayList, ImageGridListAdapter.OnItemClickListener onItemClickListener) {
        this.newsFeedArrayList = newsFeedArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemCClick(View view, int position);
    }


    @Override
    public ImageGridListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_grid_image, parent, false);

        return new ImageGridListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageGridListAdapter.MyViewHolder holder, int position) {
        ImageDetailGrid newsFeed = newsFeedArrayList.get(position);

        if (OPSValidator.checkNullString(newsFeed.getGalleryUrl())) {
            Picasso.get().load(newsFeed.getGalleryUrl()).into(holder.newsImage);
        } else {
//            newsImage.setImageResource(R.drawable.news_banner);
        }
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    @Override
    public int getItemCount() {
        return newsFeedArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        /*if ((position + 1) % 7 == 4 || (position + 1) % 7 == 0) {
            return 2;
        } else {
            return 1;
        }*/
        if (newsFeedArrayList.get(position) != null || newsFeedArrayList.get(position).getSize() > 0)
            return 2;
        else
            return 1;
    }

}