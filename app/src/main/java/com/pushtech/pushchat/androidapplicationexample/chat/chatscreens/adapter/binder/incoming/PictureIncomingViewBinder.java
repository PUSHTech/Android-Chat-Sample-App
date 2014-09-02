package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.incoming;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.chat.model.message.ChatMessage;
import com.pushtech.sdk.chat.model.message.PictureChatMessage;
import com.squareup.picasso.Picasso;

import java.util.Map;

/**
 * Created by goda87 on 29/08/14.
 */
public class PictureIncomingViewBinder extends IncomingViewBinder {

    public PictureIncomingViewBinder() {
        super();
    }

    public PictureIncomingViewBinder(boolean isGroupChat, Map<String, String> groupComponents) {
        super(isGroupChat, groupComponents);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final PictureChatMessage message = (PictureChatMessage) getChatMessage(cursor);
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
        View viewImageButton = view.findViewById(R.id.image_button);
        viewImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(message.getContentUrl()), "image/*");
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
        isBindable &= message instanceof PictureChatMessage;
        return isBindable;
    }

    @Override
    public int getViewLayout() {
        return R.layout.item_message_picture_incoming;
    }
}
