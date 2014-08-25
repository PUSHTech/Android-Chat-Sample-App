package com.pushtech.pushchat.androidapplicationexample.chat.gcm;

import com.pushtech.sdk.gcm.GCMIntentService;
import com.pushtech.sdk.receiver.GcmBroadcastReceiver;

/**
 *
 */
public final class ExampleGCMBroadcastReceiver extends GcmBroadcastReceiver {
    @Override
    protected Class<? extends GCMIntentService> getGcmServiceClass() {
        return ExampleGCMIntentService.class;
    }
}
