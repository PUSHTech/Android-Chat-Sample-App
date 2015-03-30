package com.pushtech.pushchat.androidapplicationexample.chat.messagecenter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.pushtech.sdk.PushDeliveriesContentValuesOps;
import com.pushtech.sdk.PushDelivery;
import com.pushtech.sdk.chatAndroidExample.R;


/**
 * Created by cristianrodriguezmoya on 02/05/14.
 */
public class CampaingsAdapter extends CursorAdapter {
    private PushDeliveriesContentValuesOps converter = new PushDeliveriesContentValuesOps();

    public CampaingsAdapter(Context context, Cursor c) {
        super(context, c);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_campaign_messagecenter, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        PushDelivery pushDelivery = converter.buildFromCursor(cursor);
        ((TextView) view.findViewById(R.id.tv_notification_text)).setText(pushDelivery.getTitle());
        ((TextView) view.findViewById(R.id.tv_notification_title)).setText(pushDelivery.getText());
        final String url = pushDelivery.getUrl();
        ((TextView) view.findViewById(R.id.tv_notification_link)).setText(url);
        if (!TextUtils.isEmpty(url)) {
            view.findViewById(R.id.iv_notification_icon).setVisibility(View.VISIBLE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                }
            });
        } else {
            view.findViewById(R.id.iv_notification_icon).setVisibility(View.INVISIBLE);
        }

    }
}
