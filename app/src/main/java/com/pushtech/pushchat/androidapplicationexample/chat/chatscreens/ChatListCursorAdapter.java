package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens;

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
 *
 */
public class ChatListCursorAdapter extends CursorAdapter {
    private Context context;

    public ChatListCursorAdapter(Context context, Cursor c) {
        super(context, c);
        this.context = context;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.item_chat_list, parent, false);
        return retView;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        TextView tv_name = (TextView) view.findViewById(R.id.tv_user_name);
        TextView tv_unreadMessages = (TextView) view.findViewById(R.id.tv_unread_badge);
        TextView tv_last_message = (TextView) view.findViewById(R.id.tv_last_message);
        ImageView iv_avatar = (ImageView) view.findViewById(R.id.iv_user_avatar);

        ChatContentValuesOp chatContentValuesOp = new ChatContentValuesOp();
        Chat chat = chatContentValuesOp.buildFromCursor(cursor);
        ChatMessageContentValuesOp messageContentValuesOp = new ChatMessageContentValuesOp();
        ChatMessage lastMessage = messageContentValuesOp.buildFromCursor(cursor);

        tv_name.setText(chat.getName());

        String lastMessageText = lastMessage.getText();

        if (!TextUtils.isEmpty(lastMessageText)) {
            tv_last_message.setText(lastMessageText);
            tv_last_message.setVisibility(View.VISIBLE);
        }

        int numberOfUnreadMessages = chat.getUnreadMessages();
        if (numberOfUnreadMessages > 0) {
            tv_unreadMessages.setText(Integer.toString(numberOfUnreadMessages));
            tv_unreadMessages.setVisibility(View.VISIBLE);
        } else {
            tv_unreadMessages.setVisibility(View.GONE);
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
                .into(iv_avatar);
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
