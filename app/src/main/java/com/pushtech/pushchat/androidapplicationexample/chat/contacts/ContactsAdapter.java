package com.pushtech.pushchat.androidapplicationexample.chat.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pushtech.sdk.User;
import com.pushtech.sdk.chatAndroidExample.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by crm27 on 17/2/15.
 */
public class ContactsAdapter extends BaseAdapter {
    private ArrayList<User> users;
    private Context ctx;

    static class ViewHolder {
        TextView userName;
        ImageView userAvatar;
    }

    public ContactsAdapter(Context ctx, ArrayList<User> users) {
        this.ctx = ctx;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_contact_list, null);
            holder = new ViewHolder();

            holder.userName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.userAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User user = (User) getItem(position);
        holder.userName.setText(user.getName());

        String avatarUrl = user.getAvatarIcon();
        if (avatarUrl != null) {
            Picasso.with(ctx)
                    .load(avatarUrl)
                    .fit()
                    .centerCrop()
                    .error(R.drawable.ab_default_avatar)
                    .placeholder(R.drawable.ab_default_avatar)
                    .into(holder.userAvatar);
        } else {
            Picasso.with(ctx)
                    .load(R.drawable.ab_default_avatar)
                    .fit()
                    .centerCrop()
                    .into(holder.userAvatar);
        }
        return convertView;
    }
}
