package com.pushtech.pushchat.androidapplicationexample.chat.messagecenter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.db.contentvaluesop.PushDeliveryContentValuesOp;
import com.pushtech.sdk.model.PushDelivery;

/**
 * Created by cristianrodriguezmoya on 02/05/14.
 */
public class CampaingsAdapter extends CursorAdapter {
    private Context context;
    private PushDeliveryContentValuesOp converter = new PushDeliveryContentValuesOp();
    private TextView text, title, url;

    public CampaingsAdapter(Context context, Cursor c) {
        super(context, c);
        this.context = context;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return  LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_campaign_messagecenter, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        PushDelivery pushDelivery = converter.buildFromCursor(cursor);
        ((TextView) view.findViewById(R.id.tv_notification_text)).setText(pushDelivery.getTitle());
        ((TextView) view.findViewById(R.id.tv_notification_title)).setText(pushDelivery.getText());
        ((TextView) view.findViewById(R.id.tv_notification_link)).setText(pushDelivery.getUrl());

    }
}
