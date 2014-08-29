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

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.chat.db.agent.ChatsDBAgent;
import com.pushtech.sdk.chat.db.contentvaluesop.ChatContentValuesOp;
import com.pushtech.sdk.chat.db.contentvaluesop.ChatMessageContentValuesOp;
import com.pushtech.sdk.chat.model.Chat;
import com.pushtech.sdk.chat.model.message.ChatMessage;
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
        return getChat(cursor);
    }

    public ChatMessage getLastMessage(int position) {
        Cursor cursor = (Cursor) getItem(position);
        return getLastMessage(cursor);
    }

    private Chat getChat(Cursor cursor) {
        ChatContentValuesOp chatContentValuesOp = new ChatContentValuesOp();
        return chatContentValuesOp.buildFromCursor(cursor);
    }

    private ChatMessage getLastMessage(Cursor cursor) {
        ChatMessageContentValuesOp chatMessageContentValuesOp = new ChatMessageContentValuesOp();
        return chatMessageContentValuesOp.buildFromCursor(cursor);
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

        Chat chat = getChat(cursor);
        ChatMessage lastMessage = getLastMessage(cursor);

        tvName.setText(chat.getName());

        String lastMessageText = null;
        if (lastMessage != null) {
            lastMessageText = lastMessage.getText();
        }

        if (!TextUtils.isEmpty(lastMessageText)) {
            tvLastMessage.setText(lastMessageText);
            tvLastMessage.setVisibility(View.VISIBLE);
        }

        int numberOfUnreadMessages = chat.getUnreadMessages();
        if (numberOfUnreadMessages > 0) {
            tvUnreadMessages.setText(Integer.toString(numberOfUnreadMessages));
            tvUnreadMessages.setVisibility(View.VISIBLE);
        } else {
            tvUnreadMessages.setVisibility(View.GONE);
        }

        int placeholder;
        if (chat.getType().equals(Chat.Type.CHAT)) {
            placeholder = R.drawable.ab_default_avatar;
        } else {
            placeholder = R.drawable.ab_default_group_avatar;
        }

        String avatarUrl = chat.getAvatarUrl();
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
        Cursor cursor = ChatsDBAgent.getInstance(context)
                .getChatsFilterByName(constraint.toString());
        this.changeCursor(cursor);
        return cursor;
    }
}
