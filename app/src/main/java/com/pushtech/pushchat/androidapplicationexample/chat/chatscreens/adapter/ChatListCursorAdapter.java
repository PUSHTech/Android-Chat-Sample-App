package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.TextView;

import com.pushtech.sdk.Chat;
import com.pushtech.sdk.ChatMessage;
import com.pushtech.sdk.GroupChat;
import com.pushtech.sdk.PushtechApp;
import com.pushtech.sdk.SingleChat;
import com.pushtech.sdk.TextChatMessage;
import com.pushtech.sdk.chatAndroidExample.R;
import com.squareup.picasso.Picasso;


/**
 * Created by goda87 on 29/08/14.
 */
public class ChatListCursorAdapter extends CursorAdapter {
    private Context context;

    public ChatListCursorAdapter(Context context, Cursor c) {
        super(context, c);
        this.context = context;
    }

    public Chat getChat(int position) {
        Cursor cursor = (Cursor) getItem(position);
        return Chat.buildFromCursor(cursor);
    }

    public ChatMessage getLastMessage(int position) {
        Cursor cursor = (Cursor) getItem(position);
        return new ChatMessage().buildFromCursor(cursor);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.item_chat_list, parent, false);
        return retView;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        TextView tvName = (TextView) view.findViewById(R.id.tv_user_name);
        TextView tvUnreadMessages = (TextView) view.findViewById(R.id.tv_unread_badge);
        TextView tvLastMessage = (TextView) view.findViewById(R.id.tv_last_message);
        ImageView ivAvatar = (ImageView) view.findViewById(R.id.iv_user_avatar);

        Chat chat = Chat.buildFromCursor(cursor);
        TextChatMessage lastMessage = new TextChatMessage().buildFromCursor(cursor);
        tvName.setText(chat.getChatName());

        String lastMessageText = null;
        if (lastMessage != null) {
            if (ChatMessage.ChatMessageType.TEXT.equals(lastMessage.getType())) {
                lastMessageText = ((TextChatMessage) lastMessage).getMessage();
            }
        }

        if (!TextUtils.isEmpty(lastMessageText)) {
            tvLastMessage.setText(lastMessageText);
            tvLastMessage.setVisibility(View.VISIBLE);
        }

        long numberOfUnreadMessages = chat.getUnreadMessages();
        if (numberOfUnreadMessages > 0) {
            tvUnreadMessages.setText(Long.toString(numberOfUnreadMessages));
            tvUnreadMessages.setVisibility(View.VISIBLE);
        } else {
            tvUnreadMessages.setVisibility(View.GONE);
        }

        int placeholder;
        String avatarUrl;
        if (!chat.isGroupChat()) {
            placeholder = R.drawable.ab_default_avatar;
            avatarUrl = ((SingleChat) chat).getChatIcon();
        } else {
            placeholder = R.drawable.ab_default_group_avatar;
            avatarUrl = ((GroupChat) chat).getGroupIcon();
        }


        Picasso.with(context)
                .load(avatarUrl)
                .fit()
                .centerCrop()
                .error(placeholder)
                .placeholder(placeholder)
                .into(ivAvatar);
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        FilterQueryProvider filter = getFilterQueryProvider();
        if (filter != null) {
            Cursor cursor = filter.runQuery(constraint);
            changeCursor(cursor);
            return cursor;
        }
        Cursor cursor = PushtechApp.with(context).getBaseManager()
                .getChatManager().getChatsFilterByName(constraint.toString());

        this.changeCursor(cursor);
        return cursor;
    }
}
