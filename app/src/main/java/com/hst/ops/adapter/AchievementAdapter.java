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
import com.hst.ops.utils.OPSValidator;
import com.hst.ops.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AchievementAdapter extends BaseAdapter {

    private ArrayList<AchievementList>achievementArrayList;
    private Context context;

    public AchievementAdapter(Context ctx, ArrayList<AchievementList>arrayList) {

        this.context = ctx;
        this.achievementArrayList = arrayList;
    }

    @Override
    public int getCount() {
        return achievementArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return achievementArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
            convertView = inflater.inflate(R.layout.list_item_achievement, parent, false);

            holder = new ViewHolder();
            holder.achieve_image = (ImageView)convertView.findViewById(R.id.img_list);
            holder.achieve_title = (TextView) convertView.findViewById(R.id.title_achieve);
            holder.achieve_content = (TextView)convertView.findViewById(R.id.title_content);
            holder.achieve_date = (TextView)convertView.findViewById(R.id.date);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        AchievementList achieve = achievementArrayList.get(position);

        if (OPSValidator.checkNullString(achieve.getAchievement_image())){
            Picasso.get().load(achieve.getAchievement_image()).into(holder.achieve_image);
        }
        else {
//            holder.achieve_image.setImageResource(R.drawable.img_dcm);
        }

        if (PreferenceStorage.getLang(holder.achieve_title.getContext()).equalsIgnoreCase("english")) {
            holder.achieve_title.setText(capitalizeString(achieve.getAchievement_title_en()));
        } else {
            holder.achieve_title.setText(capitalizeString(achieve.getAchievement_title_ta()));
        }

        if (PreferenceStorage.getLang(holder.achieve_content.getContext()).equalsIgnoreCase("english")) {
            holder.achieve_content.setText(capitalizeString(achieve.getAchievement_text_en()));
        } else {
            holder.achieve_content.setText(capitalizeString(achieve.getAchievement_text_ta()));
        }

        holder.achieve_date.setText(getserverdateformat(achieve.getAchievement_date()));

        return convertView;
    }

    public class ViewHolder {

        ImageView achieve_image;
        TextView achieve_title, achieve_content, achieve_date;
    }
}
