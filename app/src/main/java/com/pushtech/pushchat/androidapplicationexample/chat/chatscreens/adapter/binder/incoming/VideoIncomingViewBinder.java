package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.incoming;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.chat.model.message.ChatMessage;
import com.pushtech.sdk.chat.model.message.VideoChatMessage;
import com.squareup.picasso.Picasso;

import java.util.Map;

/**
 * Created by goda87 on 29/08/14.
 */
public class VideoIncomingViewBinder extends IncomingViewBinder {

    public VideoIncomingViewBinder() {
        super();
    }

    public VideoIncomingViewBinder(boolean isGroupChat, Map<String, String> groupComponents) {
        super(isGroupChat, groupComponents);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final VideoChatMessage message = (VideoChatMessage) getChatMessage(cursor);
        ImageView videoImageView = (ImageView) view.findViewById(R.id.iv_video);

        Picasso.with(context)
                .load(message.getThumbnailUrl())
                .centerCrop()
                .resize(120, 120)
                .into(videoImageView);

        View tv_view_video = view.findViewById(R.id.video_button);
        tv_view_video.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(message.getContentUrl()), "video/*");
                context.startActivity(intent);
            }
        });
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
        return R.layout.item_message_video_incoming;
    }
}
