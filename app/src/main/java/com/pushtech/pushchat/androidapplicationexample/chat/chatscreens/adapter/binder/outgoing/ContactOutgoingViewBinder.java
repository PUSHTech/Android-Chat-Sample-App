package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.outgoing;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.VCardParser;
import com.pushtech.sdk.ChatMessage;
import com.pushtech.sdk.ContactChatMessage;
import com.pushtech.sdk.chatAndroidExample.R;


/**
 * Created by goda87 on 1/09/14.
 */
public class ContactOutgoingViewBinder extends OutgoingViewBinder {
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ContactChatMessage message = (ContactChatMessage) getChatMessage(cursor);
        TextView contactNameView = (TextView) view.findViewById(R.id.tv_contact_name);
        contactNameView.setText(message.getName());
        contactNameView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VCardParser.parseVCard(context, message.getvCard());
            }
        });
        setFromAndDateViews(view, context, message);
    }

    @Override
    public boolean isBindableFrom(Cursor cursor) {
        boolean isBindable = true;
        ChatMessage message = getChatMessage(cursor);
        isBindable &= ChatMessage.Direction.OUTGOING.equals(message.getDirection());
        isBindable &= message instanceof ContactChatMessage;
        return isBindable;
    }

    @Override
    public int getViewLayout() {
        return R.layout.item_message_contact_outgoing;
    }
}
