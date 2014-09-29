package com.pushtech.pushchat.androidapplicationexample.chat.gcm;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.ChatListActivity;
import com.pushtech.pushchat.androidapplicationexample.chat.notifications.NotificationManager;
import com.pushtech.sdk.chat.gcm.GCMEventIntentService;
import com.pushtech.sdk.chat.service.CommunicationService;
import com.pushtech.sdk.model.CampaignDelivery;

/**
 *
 */
public final class ExampleGCMIntentService extends GCMEventIntentService {

    @Override
    protected Class getActivityClass() {
        return ChatListActivity.class;
    }

    @Override
    protected CommunicationService.ChatEventsListener getCommunicationServiceListener() {
        return NotificationManager.with(this);
    }

    @Override
    protected void onCampaignDeliveryDataReceived(Context context, CampaignDelivery pushDelivery) {

        Log.d("GODA", "EIIEIE " + pushDelivery.getText());
        String text = pushDelivery.getText();
        if (!TextUtils.isEmpty(pushDelivery.getUrl())) {
            text += " " + pushDelivery.getUrl();
        }

        notify(this, pushDelivery.getTitle(), text,
                pushDelivery.getUrl(), R.drawable.app_launcher);
    }
}