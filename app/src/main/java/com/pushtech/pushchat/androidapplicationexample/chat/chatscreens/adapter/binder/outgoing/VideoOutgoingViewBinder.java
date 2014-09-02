package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.outgoing;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.chat.model.message.ChatMessage;
import com.pushtech.sdk.chat.model.message.ContactVCardChatMessage;
import com.pushtech.sdk.chat.model.message.VideoChatMessage;
import com.squareup.picasso.Picasso;

/**
 * Created by goda87 on 1/09/14.
 */
public class VideoOutgoingViewBinder extends OutgoingViewBinder {
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final VideoChatMessage message = (VideoChatMessage) getChatMessage(cursor);
        ImageView videoImageView = (ImageView) view.findViewById(R.id.iv_video);
        if (!TextUtils.isEmpty(message.getLocalContentPath())) {
            Picasso.with(context)
                    .load(message.getThumbnailUrl())
                    .centerCrop()
                    .resize(120, 120)
                    .into(videoImageView);
        } else {
            Picasso.with(context)
                    .load(message.getLocalContentPath())
                    .centerCrop()
                    .resize(120, 120)
                    .into(videoImageView);
        }
        View tv_view_video = view.findViewById(R.id.video_button);
        tv_view_video.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (TextUtils.isEmpty(message.getLocalContentPath())) {
                    intent.setDataAndType(Uri.parse(message.getContentUrl()), "video/*");
                } else {
                    intent.setDataAndType(Uri.parse(message.getLocalContentPath()), "video/*");
                }
                context.startActivity(intent);
            }
        });
        setFromAndDateViews(view, context, message);
    }

    @Override
    public boolean isBindableFrom(Cursor cursor) {
        boolean isBindable = true;
        ChatMessage message = getChatMessage(cursor);
        isBindable &= ChatMessage.Direction.OUTGOING.equals(message.getDirection());
        isBindable &= message instanceof VideoChatMessage;
        return isBindable;
    }

    @Override
    public int getViewLayout() {
        return R.layout.item_message_video_outgoing;
    }
}
