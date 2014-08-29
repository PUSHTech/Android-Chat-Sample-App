package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.incoming;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.chat.model.message.ChatMessage;
import com.pushtech.sdk.chat.model.message.VideoChatMessage;

/**
 * Created by goda87 on 29/08/14.
 */
public class VideoIncomingViewBinder extends IncomingViewBinder {

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        VideoChatMessage message = (VideoChatMessage) getChatMessage(cursor);

        //TODO set video views
        setFromAndDateViews(view, context, message);
    }

    @Override
    public boolean isBindableFrom(Cursor cursor) {
        boolean isBindable = true;
        ChatMessage message = getChatMessage(cursor);
        isBindable &= ChatMessage.Direction.INCOMING.equals(message.getDirection());
        isBindable &= message instanceof VideoChatMessage;
        return isBindable;
    }

    @Override
    public int getViewLayout() {
        return R.layout.item_video_message_incoming;
    }
}
