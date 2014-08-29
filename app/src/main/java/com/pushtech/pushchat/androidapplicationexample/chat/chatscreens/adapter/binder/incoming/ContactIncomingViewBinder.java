package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.incoming;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.chat.db.contract.ChatsContract;
import com.pushtech.sdk.chat.model.message.ChatMessage;
import com.pushtech.sdk.chat.model.message.ContactVCardChatMessage;

/**
 * Created by goda87 on 29/08/14.
 */
public class ContactIncomingViewBinder extends IncomingViewBinder {

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ContactVCardChatMessage message = (ContactVCardChatMessage) getChatMessage(cursor);
        TextView contactNameTextView = (TextView) view.findViewById(R.id.tv_contact_name_from);

        contactNameTextView.setText(message.getName());

        final String vCardString = cursor.getString(cursor.getColumnIndex(ChatsContract
                .ChatMessage.VCARD));

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
