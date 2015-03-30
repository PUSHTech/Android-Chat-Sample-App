package com.pushtech.pushchat.androidapplicationexample.chat.gcm;

import com.pushtech.pushchat.androidapplicationexample.chat.notifications.NotificationUtils;
import com.pushtech.sdk.ChatMessage;
import com.pushtech.sdk.GCMPushIntentService;
import com.pushtech.sdk.GroupChat;

/**
 *
 */
public final class ExampleGCMIntentService extends GCMPushIntentService {
    @Override
    public void newChatMessage(ChatMessage message) {
        NotificationUtils.generateNewMessageNotification(
                "messageReceived", message.getFrom().hashCode(),
                this, message);
    }


    @Override
    public void startTyping(String userJid) {
        super.startTyping(userJid);
    }

    @Override
    public void stopTyping(String userJid) {
        super.stopTyping(userJid);
    }

    @Override
    public void onInviteAGroup(GroupChat chat) {
        super.onInviteAGroup(chat);
    }

    @Override
    public void onJoinGroup(GroupChat chat) {
        super.onJoinGroup(chat);
    }

    @Override
    public void onLeaveGroup(GroupChat chat) {
        super.onLeaveGroup(chat);
    }
}