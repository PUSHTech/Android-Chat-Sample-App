package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder.outgoing;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.pushtech.sdk.ChatMessage;
import com.pushtech.sdk.PictureChatMessage;
import com.pushtech.sdk.chatAndroidExample.R;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by goda87 on 1/09/14.
 */
public class PictureOutgoingViewBinder extends OutgoingViewBinder{
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final PictureChatMessage message = (PictureChatMessage) getChatMessage(cursor);
        ImageView pictureImageView = (ImageView) view.findViewById(R.id.iv_picture);
        if (!TextUtils.isEmpty(message.getLocalPath())) {
            Picasso.with(context)
                    .load(message.getPictureThumbnail())
                    .centerCrop()
                    .resize(120, 120)
                    .into(pictureImageView);
        } else {
            Picasso.with(context)
                    .load(message.getLocalPath())
                    .centerCrop()
                    .resize(120, 120)
                    .into(pictureImageView);
        }
        View viewImageButton = view.findViewById(R.id.image_button);
        viewImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (fileExists(message.getLocalPath())) {
                    intent.setDataAndType(
                            Uri.parse("file://" + message.getLocalPath()), "image/*");
                } else {
                    intent.setDataAndType(Uri.parse(message.getPictureUrl()), "image/*");
                }
                context.startActivity(intent);
            }
        });
        setFromAndDateViews(view, context, message);
    }

    private boolean fileExists(String path) {
        boolean exists = false;
        if (!TextUtils.isEmpty(path)) {
            File f = new File(path);
            exists = f.exists();
        }
        return exists;
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
