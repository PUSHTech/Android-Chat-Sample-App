package com.pushtech.pushchat.androidapplicationexample.chat.notifications;

import android.content.Context;

import com.pushtech.sdk.chat.ChatCommunication;

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
            ChatCommunication.start(context);
        }
    }

    public synchronized void stopTracking(Context context) {
        decrementTracker();
        if (numberOfTrackedActivities <= 0) {
            ChatCommunication.stop(context);
        }
    }

    private void incrementTracker() {
        numberOfTrackedActivities++;
    }

    private void decrementTracker() {
        numberOfTrackedActivities--;
    }
}
