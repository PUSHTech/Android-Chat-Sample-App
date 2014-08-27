package com.pushtech.pushchat.androidapplicationexample.chat.gcm;

import com.pushtech.pushchat.androidapplicationexample.chat.MainActivity;
import com.pushtech.sdk.chat.gcm.GCMEventIntentService;
import com.pushtech.sdk.chat.service.CommunicationService;

/**
 *
 */
public final class ExampleGCMIntentService extends GCMEventIntentService {

    @Override
    protected Class getActivityClass() {
        return MainActivity.class;
    }

    @Override
    protected CommunicationService.ChatEventsListener getCommunicationServiceListener() {
        return null;
//        return NotificationManager.with(this);
    }


}