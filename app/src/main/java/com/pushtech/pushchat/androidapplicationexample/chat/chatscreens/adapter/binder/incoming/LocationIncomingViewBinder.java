package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.incoming;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;

import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.MapLauncher;
import com.pushtech.sdk.ChatMessage;
import com.pushtech.sdk.LocationChatMessage;
import com.pushtech.sdk.chatAndroidExample.R;
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
    public void bindView(View view, final Context context, Cursor cursor) {
        final LocationChatMessage message = (LocationChatMessage) getChatMessage(cursor);

        if (message.getLocationThumbnail() != null) {
            ImageView imageImageView = (ImageView) view.findViewById(R.id.iv_location);
            Picasso.with(context).
                    load(message.getLocationThumbnail()).centerCrop().resize(120, 120)
                    .into(imageImageView);
        }
        View locationButton = view.findViewById(R.id.location_button);
        locationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MapLauncher.launchMap(
                        message.getLatitude(),
                        message.getLongitude(),
                        context);

            }
        });
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
