package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.incoming;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.CursorViewBinder;
import com.pushtech.sdk.chat.db.agent.UsersDBAgent;
import com.pushtech.sdk.chat.db.contentvaluesop.ChatMessageContentValuesOp;
import com.pushtech.sdk.chat.model.User;
import com.pushtech.sdk.chat.model.message.ChatMessage;
import com.pushtech.sdk.chat.utils.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by goda87 on 29/08/14.
 */
public abstract class IncomingViewBinder implements CursorViewBinder {

    private Map<String, String> mUserNames = new ConcurrentHashMap<String, String>();
    private boolean isGroupChat;

    public IncomingViewBinder() {
        this(false, null);
    }
    public IncomingViewBinder(boolean isGroupChat, Map<String, String> groupComponents) {
        this.isGroupChat = isGroupChat;
        this.mUserNames = groupComponents;
    }

    protected boolean isGroupChat() {
        return isGroupChat;
    }

    protected String getUserName(Context context, String fromJid) {
        String userName = mUserNames.get(fromJid);
        if(userName == null) {
            User user = UsersDBAgent.getInstance(context).findUser(fromJid);
            userName = user != null ? user.getAliasName() : StringUtils.parseName(fromJid);
            mUserNames.put(fromJid, userName);
        }
        return userName;
    }

    public abstract void bindView(View view, Context context, Cursor cursor);

    public ChatMessage getChatMessage(Cursor cursor) {
        ChatMessageContentValuesOp contentValuesOp = new ChatMessageContentValuesOp();
        return contentValuesOp.buildFromCursor(cursor);
    }
}
