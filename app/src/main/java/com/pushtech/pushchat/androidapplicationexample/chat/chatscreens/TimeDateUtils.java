package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens;

import android.content.Context;
import android.text.format.DateUtils;

/**
 * Created by goda87 on 18/06/14.
 */
public class TimeDateUtils {

    public static final String formatDateLong(final Context context, long date) {
        if (DateUtils.isToday(date)) {
            return DateUtils.formatDateTime(context, date, DateUtils.FORMAT_SHOW_TIME);
        }
        return DateUtils.formatDateTime(context, date,
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME);
    }
}
