package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.outgoing;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.chat.model.message.ChatMessage;
import com.pushtech.sdk.chat.model.message.ContactVCardChatMessage;
import com.pushtech.sdk.chat.model.message.PictureChatMessage;
import com.pushtech.sdk.chat.model.message.TextChatMessage;
import com.squareup.picasso.Picasso;

/**
 * Created by goda87 on 1/09/14.
 */
public class PictureOutgoingViewBinder extends OutgoingViewBinder{
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        PictureChatMessage message = (PictureChatMessage) getChatMessage(cursor);
        ImageView pictureImageView = (ImageView) view.findViewById(R.id.iv_picture);
        if (!TextUtils.isEmpty(message.getLocalContentPath())) {
            Picasso.with(context)
                    .load(message.getThumbnailUrl())
                    .centerCrop()
                    .resize(120, 120)
                    .into(pictureImageView);
        } else {
            Picasso.with(context)
                    .load(message.getLocalContentPath())
                    .centerCrop()
                    .resize(120, 120)
                    .into(pictureImageView);
        }
        setFromAndDateViews(view, context, message);
    }

    @Override
    public boolean isBindableFrom(Cursor cursor) {

        boolean isBindable = true;
        ChatMessage message = getChatMessage(cursor);
        isBindable &= ChatMessage.Direction.OUTGOING.equals(message.getDirection());
        isBindable &= message instanceof PictureChatMessage;
        return isBindable;
    }

    @Override
    public int getViewLayout() {
        return R.layout.item_message_picture_outgoing;
    }
}
