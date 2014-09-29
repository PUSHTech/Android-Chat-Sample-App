package com.pushtech.pushchat.androidapplicationexample.chat.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pushtech.pushchat.androidapplicationexample.R;


/**
 * Created by crm27 on 28/05/14.
 */
public class UserInfoSettingsView extends LinearLayout {
    private TextView tv_userName;

    public UserInfoSettingsView(Context context) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.view_user_info_settings, this, true);
        tv_userName = (TextView) findViewById(R.id.view_user_info_settings_user_name);
    }

    public String getText() {
        return tv_userName.getText().toString();
    }
}
