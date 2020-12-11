package com.hst.ops.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hst.ops.R;
import com.hst.ops.bean.support.Gallery;
import com.hst.ops.bean.support.PartyStateList;
import com.hst.ops.customview.CircleImageView;
import com.hst.ops.utils.OPSValidator;
import com.hst.ops.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PartyStateAdapter extends RecyclerView.Adapter<PartyStateAdapter.MyViewHolder> {

    private ArrayList<PartyStateList> stateList;
    private OnItemClickListener onItemClickListener;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView stateLogo;
        public TextView stateName;
        public LinearLayout stateListLayout;

        public MyViewHolder(View view) {
            super(view);
            stateListLayout = (LinearLayout)view.findViewById(R.id.stList);
            stateListLayout.setOnClickListener(this);
            stateLogo = (ImageView) view.findViewById(R.id.st_logo);
            stateName = (TextView) view.findViewById(R.id.st_name);
        }

        @Override
        public void onClick(View v) {

            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
//            else {
//                onClickListener.onClick(Selecttick);
//            }
        }
    }

    public PartyStateAdapter(ArrayList<PartyStateList> arrayList, OnItemClickListener onItemClickListener){

        this.onItemClickListener = onItemClickListener;
        this.stateList = arrayList;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_state, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PartyStateList state = stateList.get(position);

        if (PreferenceStorage.getLang(holder.stateName.getContext()).equalsIgnoreCase("english")) {
            holder.stateName.setText(capitalizeString(state.getState_name_en()));
        }
        else {
            holder.stateName.setText(capitalizeString(state.getState_name_ta()));
        }
        if (OPSValidator.checkNullString(state.getState_logo())) {
            Picasso.get().load(state.getState_logo()).into(holder.stateLogo);
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
        return stateList.size();
    }
}
