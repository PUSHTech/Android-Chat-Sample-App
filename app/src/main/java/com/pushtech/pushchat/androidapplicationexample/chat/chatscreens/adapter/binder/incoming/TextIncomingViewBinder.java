package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.incoming;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.pushtech.sdk.ChatMessage;
import com.pushtech.sdk.TextChatMessage;
import com.pushtech.sdk.chatAndroidExample.R;

import java.util.Map;

/**
 * Created by goda87 on 29/08/14.
 */
public class TextIncomingViewBinder extends IncomingViewBinder {

    public TextIncomingViewBinder() {
        super();
    }

    public TextIncomingViewBinder(boolean isGroupChat, Map<String, String> groupComponents) {
        super(isGroupChat, groupComponents);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextChatMessage message = (TextChatMessage) getChatMessage(cursor);
        TextView messageTextView = (TextView) view.findViewById(R.id.tv_message);

        messageTextView.setText(message.getMessage());
        setFromAndDateViews(view, context, message);
    }

    @Override
    public boolean isBindableFrom(Cursor cursor) {
        boolean isBindable = true;
        ChatMessage message = getChatMessage(cursor);
        isBindable &= ChatMessage.Direction.INCOMING.equals(message.getDirection());
        isBindable &= message instanceof TextChatMessage;
        return isBindable;
    }

    @Override
    public int getViewLayout() {
        return R.layout.item_message_text_incoming;
    }
}
