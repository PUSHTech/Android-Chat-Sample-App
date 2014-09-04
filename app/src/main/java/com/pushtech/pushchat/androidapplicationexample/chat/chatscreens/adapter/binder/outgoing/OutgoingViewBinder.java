package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.outgoing;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.TimeDateUtils;
import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.CursorViewBinder;
import com.pushtech.sdk.chat.db.contentvaluesop.ChatMessageContentValuesOp;
import com.pushtech.sdk.chat.model.message.ChatMessage;

import java.util.Date;

/**
 * Created by goda87 on 1/09/14.
 */
public abstract class OutgoingViewBinder implements CursorViewBinder {

    protected void setFromAndDateViews(View view, Context context, ChatMessage message) {
        TextView dateTextView = (TextView) view.findViewById(R.id.tv_date);
        Date sentDate = message.getSentDate();
        if (sentDate != null) {
            dateTextView.setText(TimeDateUtils.formatDateLong(context, sentDate.getTime()));
        }
    }

    public ChatMessage getChatMessage(Cursor cursor) {
        ChatMessageContentValuesOp contentValuesOp = new ChatMessageContentValuesOp();
        return contentValuesOp.buildFromCursor(cursor);
    }
}
