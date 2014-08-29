package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.incoming;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.chat.model.message.ChatMessage;
import com.pushtech.sdk.chat.model.message.LocationChatMessage;
import com.squareup.picasso.Picasso;

import java.util.Map;

/**
 * Created by goda87 on 29/08/14.
 */
public class LocationIncomingViewBinder extends IncomingViewBinder {

    public LocationIncomingViewBinder() {
        super();
    }

    public LocationIncomingViewBinder(boolean isGroupChat, Map<String, String> groupComponents) {
        super(isGroupChat, groupComponents);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        LocationChatMessage message = (LocationChatMessage) getChatMessage(cursor);

        ImageView imageImageView = (ImageView) view.findViewById(R.id.iv_location_from);
        Picasso.with(context).
                load(message.getThumbnailUrl()).centerCrop().resize(120, 120)
                .into(imageImageView);

        setFromAndDateViews(view, context, message);
    }

    @Override
    public boolean isBindableFrom(Cursor cursor) {
        boolean isBindable = true;
        ChatMessage message = getChatMessage(cursor);
        isBindable &= ChatMessage.Direction.INCOMING.equals(message.getDirection());
        isBindable &= message instanceof LocationChatMessage;
        return isBindable;
    }

    @Override
    public int getViewLayout() {
        return R.layout.item_message_location_incoming;
    }
}
