package com.hst.ops.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hst.ops.R;
import com.hst.ops.bean.support.AchievementList;
import com.hst.ops.bean.support.PartyResultList;
import com.hst.ops.utils.OPSValidator;
import com.hst.ops.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PartyResultAdapter extends BaseAdapter {

    private ArrayList<PartyResultList>partyList;
    private Context context;

    public PartyResultAdapter(ArrayList<PartyResultList> partyList, Context context) {
        this.partyList = partyList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return partyList.size();
    }

    @Override
    public Object getItem(int position) {
        return partyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_party_results, parent, false);

            holder = new ViewHolder();
            holder.eleYear = (TextView) convertView.findViewById(R.id.yr);
            holder.partyLeader = (TextView)convertView.findViewById(R.id.ldr);
            holder.seatsWon = (TextView)convertView.findViewById(R.id.won);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        PartyResultList result = partyList.get(position);

        holder.eleYear.setText(result.getElection_year());

        if (PreferenceStorage.getLang(holder.partyLeader.getContext()).equalsIgnoreCase("english")) {
            holder.partyLeader.setText(capitalizeString(result.getParty_leader_en()));
        } else {
            holder.partyLeader.setText(capitalizeString(result.getParty_leader_ta()));
        }
        holder.seatsWon.setText(result.getSeats_won());

        return convertView;
    }

    public class ViewHolder {

        TextView eleYear, partyLeader, seatsWon;
    }
}
