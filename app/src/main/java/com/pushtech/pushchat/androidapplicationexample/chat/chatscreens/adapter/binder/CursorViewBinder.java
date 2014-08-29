package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder;

import android.database.Cursor;


/**
 * Created by goda87 on 29/08/14.
 */
public interface CursorViewBinder {
    public boolean isBindableFrom(Cursor cursor);
    public int getViewLayout();
}