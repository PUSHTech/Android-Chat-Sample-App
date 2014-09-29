package com.pushtech.pushchat.androidapplicationexample.chat.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.pushtech.pushchat.androidapplicationexample.R;


/**
 * Created by crm27 on 28/05/14.
 */
public class UserImageSettingsIntentView extends LinearLayout implements View.OnClickListener {
    private OnProfilePhotoItemSelect listener;

    public UserImageSettingsIntentView(Context context, OnProfilePhotoItemSelect listener) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.view_profile_photo_settings, this, true);
        this.listener = listener;
        findViewById(R.id.view_profile_photo_settings_gallery).setOnClickListener(this);
        findViewById(R.id.view_profile_photo_settings_delete_photo).setOnClickListener(this);
        findViewById(R.id.view_profile_photo_settings_take_photo).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_profile_photo_settings_gallery:
                listener.onGallerySelected();
                break;
            case R.id.view_profile_photo_settings_delete_photo:
                listener.onDeletePhotoSelected();
                break;
            case R.id.view_profile_photo_settings_take_photo:
                listener.onTakePhotoSelected();
                break;
        }
    }

    public interface OnProfilePhotoItemSelect{
        void onGallerySelected();
        void onTakePhotoSelected();
        void onDeletePhotoSelected();
    }
}
