package com.pushtech.pushchat.androidapplicationexample.chat.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pushtech.sdk.chatAndroidExample.R;


/**
 * Created by crm27 on 28/05/14.
 */
public class AlertDialogCustomTitleView extends LinearLayout {

    public AlertDialogCustomTitleView(Context context, String text){
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.view_custom_title_alert_dialog, this, true);
        ((TextView)findViewById(R.id.view_custom_title_alert_dialog)).setText(text);
    }

}
