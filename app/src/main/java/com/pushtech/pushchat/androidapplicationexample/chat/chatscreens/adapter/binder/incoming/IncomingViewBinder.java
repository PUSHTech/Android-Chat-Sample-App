package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.incoming;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.TimeDateUtils;
import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.CursorViewBinder;
import com.pushtech.sdk.ChatMessage;
import com.pushtech.sdk.ContactChatMessage;
import com.pushtech.sdk.LocationChatMessage;
import com.pushtech.sdk.PictureChatMessage;
import com.pushtech.sdk.PushtechApp;
import com.pushtech.sdk.TextChatMessage;
import com.pushtech.sdk.User;
import com.pushtech.sdk.VideoChatMessage;
import com.pushtech.sdk.chatAndroidExample.R;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by goda87 on 29/08/14.
 */
public abstract class IncomingViewBinder implements CursorViewBinder {

    private Map<String, String> mUserNames = new ConcurrentHashMap<String, String>();
    private boolean isGroupChat;

    public IncomingViewBinder() {
        this(false, null);
    }

    public IncomingViewBinder(boolean isGroupChat, Map<String, String> groupComponents) {
        this.isGroupChat = isGroupChat;
        this.mUserNames = groupComponents;
    }

    protected boolean isGroupChat() {
        return isGroupChat;
    }

    protected String getUserName(Context context, String fromJid) {
        String userName = mUserNames.get(fromJid);
        if (userName == null) {
            User user = PushtechApp.with(context)
                    .getBaseManager().getUserManager().getUserInformationByJid(fromJid);
            userName = user != null ? user.getName() : parseName(fromJid);
            mUserNames.put(fromJid, userName);
        }
        return userName;
    }

    private String parseName(final String jid) {
        String[] parts = jid.split("@");
        return parts[0];
    }

    protected void setFromAndDateViews(View view, Context context, ChatMessage message) {
        if (isGroupChat()) {

            String userName = getUserName(context, message.getFrom());
            TextView memberNameTextView = (TextView) view.findViewById(R.id.tv_member_name);
            memberNameTextView.setVisibility(View.VISIBLE);
            memberNameTextView.setText(userName);
        }

        TextView dateTextView = (TextView) view.findViewById(R.id.tv_date);
        Date sentDate = message.getDate();
        if (sentDate != null) {
            dateTextView.setText(TimeDateUtils.formatDateLong(context, sentDate.getTime()));
        }
    }

    public ChatMessage getChatMessage(Cursor cursor) {
        ChatMessage chatMessage = new ChatMessage().buildFromCursor(cursor);
        switch (chatMessage.getType()) {
            case PICTURE:
                return new PictureChatMessage().buildFromCursor(cursor);
            case TEXT:
                return new TextChatMessage().buildFromCursor(cursor);
            case LOCATION:
                return new LocationChatMessage().buildFromCursor(cursor);
            case VIDEO:
                return new VideoChatMessage().buildFromCursor(cursor);
            case CONTACT:
                return new ContactChatMessage().buildFromCursor(cursor);
            default:
                return chatMessage;
        }
    }
}
