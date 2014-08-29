package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.incoming;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.TimeDateUtils;
import com.pushtech.sdk.chat.model.message.ChatMessage;
import com.pushtech.sdk.chat.model.message.TextChatMessage;
import java.util.Date;

/**
 * Created by goda87 on 29/08/14.
 */
public class TextIncomingViewBinder extends IncomingViewBinder {

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextChatMessage message = (TextChatMessage) getChatMessage(cursor);
        TextView messageTextView = (TextView) view.findViewById(R.id.tv_message_from);
        messageTextView.setText(message.getText());

        if (isGroupChat()) {
            String userName = getUserName(context, message.getFromJid());
            TextView memberNameTextView = (TextView) view.findViewById(R.id.tv_member_name);
            memberNameTextView.setVisibility(View.VISIBLE);
            memberNameTextView.setText(userName);
        }

        TextView dateTextView = (TextView) view.findViewById(R.id.tv_date_from);
        Date sentDate = message.getSentDate();
        if (sentDate != null) {
            dateTextView.setText(TimeDateUtils.formatDateLong(context, sentDate.getTime()));
        }
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
        return R.layout.item_text_message_incoming;
    }
}
