package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.outgoing;

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
import com.pushtech.sdk.TextChatMessage;
import com.pushtech.sdk.VideoChatMessage;
import com.pushtech.sdk.chatAndroidExample.R;

import java.util.Date;

/**
 * Created by goda87 on 1/09/14.
 */
public abstract class OutgoingViewBinder implements CursorViewBinder {

    protected void setFromAndDateViews(View view, Context context, ChatMessage message) {
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
