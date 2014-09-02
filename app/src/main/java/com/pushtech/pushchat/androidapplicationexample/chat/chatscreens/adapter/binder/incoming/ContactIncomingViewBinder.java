package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.incoming;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.pushchat.androidapplicationexample.utils.VCardParser;
import com.pushtech.sdk.chat.model.message.ChatMessage;
import com.pushtech.sdk.chat.model.message.ContactVCardChatMessage;

import java.util.Map;

/**
 * Created by goda87 on 29/08/14.
 */
public class ContactIncomingViewBinder extends IncomingViewBinder {

    public ContactIncomingViewBinder() {
        super();
    }

    public ContactIncomingViewBinder(boolean isGroupChat, Map<String, String> groupComponents) {
        super(isGroupChat, groupComponents);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ContactVCardChatMessage message = (ContactVCardChatMessage) getChatMessage(cursor);
        TextView contactNameView = (TextView) view.findViewById(R.id.tv_contact_name);
        contactNameView.setText(message.getName());
        contactNameView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VCardParser.parseVCard(context, message.getVCard());
            }
        });
        setFromAndDateViews(view, context, message);
    }

    @Override
    public boolean isBindableFrom(Cursor cursor) {
        boolean isBindable = true;
        ChatMessage message = getChatMessage(cursor);
        isBindable &= ChatMessage.Direction.INCOMING.equals(message.getDirection());
        isBindable &= message instanceof ContactVCardChatMessage;
        return isBindable;
    }

    @Override
    public int getViewLayout() {
        return R.layout.item_message_contact_incoming;
    }
}
