package com.pushtech.pushchat.androidapplicationexample.chat.notifications;

import android.content.Context;

import com.pushtech.sdk.PushtechApp;


/**
 *
 */
public final class ChatCommunicationTracker {

    private static ChatCommunicationTracker sChatCommunicationTracker;

    private int numberOfTrackedActivities = 0;

    private ChatCommunicationTracker() {
    }

    public static synchronized ChatCommunicationTracker getInstance() {
        if (sChatCommunicationTracker == null) {
            sChatCommunicationTracker = new ChatCommunicationTracker();
        }
        return sChatCommunicationTracker;
    }

    public synchronized void startTracking(Context context) {
        incrementTracker();
        if (numberOfTrackedActivities == 1) {
            PushtechApp.with(context).getBaseManager().getCommunicationManager().start();
        }
    }

    public synchronized void stopTracking(Context context) {
        decrementTracker();
        if (numberOfTrackedActivities <= 0) {
            PushtechApp.with(context).getBaseManager().getCommunicationManager().stop();
        }
    }

    private void incrementTracker() {
        numberOfTrackedActivities++;
    }

    private void decrementTracker() {
        numberOfTrackedActivities--;
    }
}
