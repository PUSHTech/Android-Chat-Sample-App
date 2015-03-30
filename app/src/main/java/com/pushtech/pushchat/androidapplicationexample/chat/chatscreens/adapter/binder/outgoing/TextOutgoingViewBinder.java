package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.outgoing;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.pushtech.sdk.ChatMessage;
import com.pushtech.sdk.TextChatMessage;
import com.pushtech.sdk.chatAndroidExample.R;


/**
 * Created by goda87 on 1/09/14.
 */
public class TextOutgoingViewBinder extends OutgoingViewBinder {
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextChatMessage message = (TextChatMessage) getChatMessage(cursor);
        TextView messageTextView = (TextView) view.findViewById(R.id.tv_message);
        View pending = view.findViewById(R.id.message_status);
        if (!message.getStatus().equals(ChatMessage.MessageStatus.SEND)) {
            pending.setVisibility(View.VISIBLE);
        } else {
            pending.setVisibility(View.INVISIBLE);
        }
        messageTextView.setText(message.getMessage());
        setFromAndDateViews(view, context, message);
    }

    @Override
    public boolean isBindableFrom(Cursor cursor) {

        boolean isBindable = true;
        ChatMessage message = getChatMessage(cursor);
        isBindable &= ChatMessage.Direction.OUTGOING.equals(message.getDirection());
        isBindable &= message instanceof TextChatMessage;
        return isBindable;
    }

    @Override
    public int getViewLayout() {
        return R.layout.item_message_text_outgoing;
    }
}
