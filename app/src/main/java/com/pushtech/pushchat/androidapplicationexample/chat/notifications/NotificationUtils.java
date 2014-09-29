package com.pushtech.pushchat.androidapplicationexample.chat.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.ChatListActivity;
import com.pushtech.sdk.chat.model.message.ChatMessage;
import com.pushtech.sdk.chat.model.message.ContactVCardChatMessage;
import com.pushtech.sdk.chat.model.message.LocationChatMessage;
import com.pushtech.sdk.chat.model.message.TextChatMessage;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public final class NotificationUtils {

    private static AtomicInteger notificationCounter = new AtomicInteger();
    public static final String INTENT_CHAT_JID = "chatJId";

    private NotificationUtils() {
    }
    public static void remvoeAllNotifications(Context context){
        context = context.getApplicationContext();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
    public static void removeNotification(Context context, int id) {
        context = context.getApplicationContext();
        Log.d("removeNotification", String.valueOf(id));
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel("messageReceived",id);
    }

    public static void generateNewMessageNotification(
            String tag, int id, Context context, ChatMessage chatMessage) {
        Log.d("generateNewMessageNotification", chatMessage.getData().toString());
        String chatName = new StringBuilder()
                .append("NEW CHAT MESSAGE FROM CHAT JID: ")
                .append(chatMessage.getChatJid())
                .append(", ")
                .toString();

        StringBuilder sb = new StringBuilder();
        if (chatMessage.isTextMessage()) {
            sb.append("new text message: ")
                    .append(((TextChatMessage) chatMessage).getText());
        } else if (chatMessage.isContactMessage()) {
            sb.append("contact name: ")
                    .append(((ContactVCardChatMessage) chatMessage).getName());
        } else if (chatMessage.isVideoMessage()) {
            sb.append("new video");
        } else if (chatMessage.isPictureMessage()) {
            sb.append("new picture");
        } else if (chatMessage.isLocationMessage()) {
            sb.append("new location: ")
                    .append("lat:")
                    .append(((LocationChatMessage) chatMessage).getLocation().getLatitude())
                    .append(", lng:")
                    .append(((LocationChatMessage) chatMessage).getLocation().getLongitude());
        } else if (chatMessage.isCustomMessage()) {
            sb.append("new custom message");
        }
        if (context != null && chatMessage != null && chatName != null) {
            notify(tag, id, context, chatName, sb.toString(), chatMessage);
        }

    }


    public static void generateGroupDeletedNotification(
            String tag, int id, Context context, String groupName) {
        String titleString = context.getString(R.string.notif_group_deleted_title);

        StringBuilder sb = new StringBuilder();
        sb.append(context.getString(R.string.notif_group_deleted_body_1));
        sb.append(!TextUtils.isEmpty(groupName) ? groupName : "");
        sb.append(context.getString(R.string.notif_group_deleted_body_2));
        sb.append(!TextUtils.isEmpty(groupName) ? groupName : "");
        String messageString = sb.toString();


        notify(tag, id, context, titleString, messageString);
    }

    public static void generateGroupInviteNotification(
            String tag, int id, Context context, String groupName) {
        notify(tag, id, context, "YOU", "have joined the group " + groupName);
    }

    public static void generateGroupJoinedNotification(
            String tag, int id, Context context, String groupName, String userThatJoinedName) {

        String titleString = context.getString(R.string.notif_group_join_title);

        StringBuilder sb = new StringBuilder();

        if (!TextUtils.isEmpty(userThatJoinedName)) {
            sb.append(!TextUtils.isEmpty(userThatJoinedName) ? userThatJoinedName : "");
            sb.append(context.getString(R.string.notif_group_join_body_2));
            sb.append(!TextUtils.isEmpty(groupName) ? groupName : "");
        } else {
            sb.append(context.getString(R.string.notif_group_join_empty_user));
            sb.append(!TextUtils.isEmpty(groupName) ? groupName : "");
        }

        String messageString = sb.toString();


        notify(tag, id, context, titleString, messageString);
    }

    //invitationHostName is the name of the user that has invited to this user
    public static void generateFriendJoinedGroupNotification(
            String tag,int id, Context context, String groupName, String invitationHostName) {
        String titleString = context.getString(R.string.notif_group_invite_title);

        StringBuilder sb = new StringBuilder();

        if (!TextUtils.isEmpty(invitationHostName)) {
            sb.append(!TextUtils.isEmpty(invitationHostName) ? invitationHostName : "");
            sb.append(context.getString(R.string.notif_group_invite_body));
            sb.append(!TextUtils.isEmpty(groupName) ? groupName : "");
        } else {
            sb.append(context.getString(R.string.notif_group_invite_no_hostname_body));
            sb.append(!TextUtils.isEmpty(groupName) ? groupName : "");
        }

        String messageString = sb.toString();


        notify(tag, id, context, titleString, messageString);

    }

    public static void generateGroupLeftNotification(
            String tag, int id, Context context, String groupName, String userWhoHasLeftName) {

        String titleString = context.getString(R.string.notif_group_left_title);

        StringBuilder sb = new StringBuilder();
        sb.append(context.getString(R.string.notif_group_left_body_1));
        sb.append(!TextUtils.isEmpty(userWhoHasLeftName) ? userWhoHasLeftName : "");
        sb.append(context.getString(R.string.notif_group_left_body_2));
        sb.append(!TextUtils.isEmpty(groupName) ? groupName : "");
        String messageString = sb.toString();


        notify(tag, id, context, titleString, messageString);
    }

    private static void notify
            (String tag, int id, Context context, String title, String message, ChatMessage chatMessage) {
        context = context.getApplicationContext();
        Intent homeActivityIntent = new Intent(context, ChatListActivity.class);
        homeActivityIntent.putExtra(INTENT_CHAT_JID, chatMessage.getChatJid());
        PendingIntent contentIntent = PendingIntent.getActivity
                (context, notificationCounter.getAndIncrement(),
                        homeActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setWhen(Calendar.getInstance().getTimeInMillis())
                        .setSmallIcon(R.drawable.app_launcher)
                        .setDefaults(Notification.DEFAULT_SOUND |
                                Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(tag, id, notificationBuilder.build());
    }

    private static void notify(String tag, int id, Context context, String title, String message) {
        Intent homeActivityIntent = new Intent(context, ChatListActivity.class);
        context = context.getApplicationContext();

        PendingIntent contentIntent = PendingIntent.getActivity
                (context, notificationCounter.getAndIncrement(),
                        homeActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setWhen(Calendar.getInstance().getTimeInMillis())
                        .setSmallIcon(R.drawable.app_launcher)
                        .setDefaults(Notification.DEFAULT_SOUND |
                                Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(tag, id, notificationBuilder.build());
    }
}
