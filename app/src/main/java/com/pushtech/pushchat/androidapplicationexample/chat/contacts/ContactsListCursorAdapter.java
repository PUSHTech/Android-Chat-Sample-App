package com.pushtech.pushchat.androidapplicationexample.chat.contacts;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.pushtech.sdk.chat.db.contentvaluesop.UserContentValuesOp;
import com.pushtech.sdk.chat.model.User;
import com.squareup.picasso.Picasso;
import com.pushtech.pushchat.androidapplicationexample.R;

/**
 * Created by goda87 on 27/08/14.
 */
public class ContactsListCursorAdapter extends CursorAdapter {

    private int layout;

    public ContactsListCursorAdapter(Context context, Cursor c, int layout) {
        super(context, c, 0);
        this.layout = layout;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(layout, parent, false);
        return retView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        ImageView iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
        UserContentValuesOp userContentValuesOp = new UserContentValuesOp();
        User user = userContentValuesOp.buildFromCursor(cursor);
        tv_name.setText(user.getAliasName());

        String avatarUrl = user.getAvatarUrl();
        if (avatarUrl != null) {
            Picasso.with(context)
                    .load(avatarUrl)
                    .fit()
                    .centerCrop()
                    .error(R.drawable.ab_default_avatar)
                    .placeholder(R.drawable.ab_default_avatar)
                    .into(iv_avatar);
        } else {
            Picasso.with(context)
                    .load(R.drawable.ab_default_avatar)
                    .fit()
                    .centerCrop()
                    .into(iv_avatar);
        }
    }


}
