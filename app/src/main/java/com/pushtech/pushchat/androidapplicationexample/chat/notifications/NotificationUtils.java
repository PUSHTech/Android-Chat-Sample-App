package com.pushtech.pushchat.androidapplicationexample.chat.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.ChatListActivity;
import com.pushtech.sdk.ChatMessage;
import com.pushtech.sdk.ContactChatMessage;
import com.pushtech.sdk.LocationChatMessage;
import com.pushtech.sdk.PictureChatMessage;
import com.pushtech.sdk.PushtechApp;
import com.pushtech.sdk.TextChatMessage;
import com.pushtech.sdk.User;
import com.pushtech.sdk.VideoChatMessage;
import com.pushtech.sdk.chatAndroidExample.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    public static void remvoeAllNotifications(Context context) {
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
        notificationManager.cancel("messageReceived", id);
    }

    public static void generateNewMessageNotification(
            String tag, int id, Context context, ChatMessage chatMessage) {
        boolean image = false;
        StringBuilder chatName = new StringBuilder();
        User user = PushtechApp.with(context).getBaseManager().getUserManager().getUserInformationByJid(chatMessage.getFrom());
        if (user != null) {
            chatName.append(user.getName());
        } else {
            chatName.append(chatMessage.getFrom());
        }
        chatName.append(", ");

        StringBuilder sb = new StringBuilder();
        switch (chatMessage.getType()) {
            case TEXT:
                sb.append("message: ")
                        .append(((TextChatMessage) chatMessage).getMessage());
                break;
            case CONTACT:
                sb.append("contact name: ")
                        .append(((ContactChatMessage) chatMessage).getName());
                break;
            case LOCATION:
                sb.append("new location: ")
                        .append("lat:")
                        .append(((LocationChatMessage) chatMessage).getLatitude())
                        .append(", lng:")
                        .append(((LocationChatMessage) chatMessage).getLongitude());
                break;
            case PICTURE:
                image = true;
                sb.append("New photo from : ").append(chatName);

                sendNotification asyncTaskImage = new sendNotification(context, tag, sb.toString(), id, ((PictureChatMessage) chatMessage).getPictureUrl(), chatMessage);
                asyncTaskImage.execute();
                break;
            case VIDEO:
                image = true;
                sendNotification asyncTaskVideo = new sendNotification(context, tag, sb.toString(), id, ((VideoChatMessage) chatMessage).getVideoThumbnail(), chatMessage);
                asyncTaskVideo.execute();
                break;

        }
        if (!image && context != null && chatMessage != null && chatName != null) {
            notify(tag, id, context, chatName.toString(), sb.toString(), chatMessage);
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
            String tag, int id, Context context, String groupName, String invitationHostName) {
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


    public static Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void notify
            (String tag, int id, Context context, String title, String message, ChatMessage chatMessage) {
        context = context.getApplicationContext();
        Intent homeActivityIntent = new Intent(context, ChatListActivity.class);
        homeActivityIntent.putExtra(INTENT_CHAT_JID, chatMessage.getFrom());
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

    private static class sendNotification extends AsyncTask<String, Void, Bitmap> {

        Context ctx;
        String tag;
        int id;
        String message;
        String urlImage;
        ChatMessage chatMessage;

        public sendNotification(Context ctx, String tag, String messaage, int id, String urlImage, ChatMessage chatMessage) {
            super();
            this.ctx = ctx;
            this.tag = tag;
            this.id = id;
            this.urlImage = urlImage;
            this.chatMessage = chatMessage;
            this.message = messaage;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return getBitmapFromURL(urlImage);
        }

        @Override
        protected void onPostExecute(Bitmap image) {

            super.onPostExecute(image);
            try {
                Intent homeActivityIntent = new Intent(ctx, ChatListActivity.class);
                homeActivityIntent.putExtra(INTENT_CHAT_JID, chatMessage.getFrom());
                PendingIntent contentIntent = PendingIntent.getActivity
                        (ctx, notificationCounter.getAndIncrement(),
                                homeActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(ctx)
                                .setContentTitle(message)
                                .setSmallIcon(R.drawable.app_launcher)
                                .setLargeIcon(image)
                                .setStyle(new NotificationCompat.BigPictureStyle()
                                        .bigPicture(image))
                                .setDefaults(Notification.DEFAULT_SOUND |
                                        Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
                                .setAutoCancel(true)
                                .setContentIntent(contentIntent);

                NotificationManager notificationManager = (NotificationManager)
                        ctx.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(tag, id, notificationBuilder.build());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
