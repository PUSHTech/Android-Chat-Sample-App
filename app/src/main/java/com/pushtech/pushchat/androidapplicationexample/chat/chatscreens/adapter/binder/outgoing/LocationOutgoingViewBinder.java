package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.outgoing;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.MapLauncher;
import com.pushtech.sdk.chat.model.message.ChatMessage;
import com.pushtech.sdk.chat.model.message.LocationChatMessage;
import com.squareup.picasso.Picasso;

/**
 * Created by goda87 on 1/09/14.
 */
public class LocationOutgoingViewBinder extends OutgoingViewBinder {
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final LocationChatMessage message = (LocationChatMessage) getChatMessage(cursor);
        if (message.getThumbnailUrl() != null) {
            ImageView imageImageView = (ImageView) view.findViewById(R.id.iv_location);
            Picasso.with(context).
                    load(message.getThumbnailUrl()).centerCrop().resize(120, 120)
                    .into(imageImageView);
        }
        View locationButton = view.findViewById(R.id.location_button);
        locationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MapLauncher.launchMap(
                        message.getLocation().getLatitude(),
                        message.getLocation().getLongitude(),
                        context);

            }
        });
        setFromAndDateViews(view, context, message);
    }

    @Override
    public boolean isBindableFrom(Cursor cursor) {
        boolean isBindable = true;
        ChatMessage message = getChatMessage(cursor);
        isBindable &= ChatMessage.Direction.OUTGOING.equals(message.getDirection());
        isBindable &= message instanceof LocationChatMessage;
        return isBindable;
    }

    @Override
    public int getViewLayout() {
        return R.layout.item_message_location_outgoing;
    }
}
