package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.binder;

import android.content.Context;
import android.database.Cursor;
import android.view.View;


/**
 * Created by goda87 on 29/08/14.
 */
public interface CursorViewBinder {
    public void bindView(View view, Context context, Cursor cursor);
    public boolean isBindableFrom(Cursor cursor);
    public int getViewLayout();
}