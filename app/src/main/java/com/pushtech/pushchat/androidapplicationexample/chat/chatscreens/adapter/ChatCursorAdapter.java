package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.CursorViewBinder;
import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.incoming.ContactIncomingViewBinder;
import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.incoming.LocationIncomingViewBinder;
import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.incoming.PictureIncomingViewBinder;
import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.incoming.TextIncomingViewBinder;
import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.incoming.VideoIncomingViewBinder;
import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.outgoing.ContactOutgoingViewBinder;
import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.outgoing.LocationOutgoingViewBinder;
import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.outgoing.PictureOutgoingViewBinder;
import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.outgoing.TextOutgoingViewBinder;
import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.outgoing.VideoOutgoingViewBinder;
import com.pushtech.sdk.ChatMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author goda87
 */
public class ChatCursorAdapter extends CursorAdapter {
    private static final String TAG = ChatCursorAdapter.class.getSimpleName();

    private Map<String, String> mUserNameCache = new ConcurrentHashMap<String, String>();
    private Context mContext;
    private boolean mIsGroupChat;

    private List<CursorViewBinder> mCursorViewBinders;

    public ChatCursorAdapter(Context context, Cursor c, boolean isGroupChat) {
        super(context, c, FLAG_AUTO_REQUERY);
        mContext = context;
        mIsGroupChat = isGroupChat;
        mCursorViewBinders = new ArrayList<CursorViewBinder>();

        registerViewBinders();
    }

    private void registerViewBinders() {
        mCursorViewBinders.add(new TextIncomingViewBinder(mIsGroupChat, mUserNameCache));
        mCursorViewBinders.add(new TextOutgoingViewBinder());

        mCursorViewBinders.add(new PictureIncomingViewBinder(mIsGroupChat, mUserNameCache));
        mCursorViewBinders.add(new PictureOutgoingViewBinder());

        mCursorViewBinders.add(new VideoIncomingViewBinder(mIsGroupChat, mUserNameCache));
        mCursorViewBinders.add(new VideoOutgoingViewBinder());

        mCursorViewBinders.add(new LocationIncomingViewBinder(mIsGroupChat, mUserNameCache));
        mCursorViewBinders.add(new LocationOutgoingViewBinder());

        mCursorViewBinders.add(new ContactIncomingViewBinder(mIsGroupChat, mUserNameCache));
        mCursorViewBinders.add(new ContactOutgoingViewBinder());
    }

    @Override
    public int getViewTypeCount() {
        return mCursorViewBinders.size();
    }

    @Override
    public int getItemViewType(int position) {
        Cursor cursor = (Cursor) getItem(position);
        return getBinderId(cursor);
    }

    private CursorViewBinder getBinder(Cursor cursor) {
        for (CursorViewBinder cursorViewBinder : mCursorViewBinders) {
            if (cursorViewBinder.isBindableFrom(cursor)) {
                return cursorViewBinder;
            }
        }
        return null;
    }

    private int getBinderId(Cursor cursor) {
        int i = 0;
        for (CursorViewBinder cursorViewBinder : mCursorViewBinders) {
            if (cursorViewBinder.isBindableFrom(cursor)) {
                return i;
            }
            i++;
        }
        return 0;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int viewResource = getBinder(cursor).getViewLayout();
        return inflater.inflate(viewResource, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        CursorViewBinder binder = getBinder(cursor);
        getBinder(cursor).bindView(view, context, cursor);
    }

    public ChatMessage getChatMessage(int position) {
        Cursor cursor = (Cursor) getItem(position);
        return getChatMessage(cursor);
    }

    public ChatMessage getChatMessage(Cursor cursor) {
        return new ChatMessage().buildFromCursor(cursor);
    }
}
