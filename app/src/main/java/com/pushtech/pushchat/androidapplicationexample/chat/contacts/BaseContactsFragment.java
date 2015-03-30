package com.pushtech.pushchat.androidapplicationexample.chat.contacts;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.util.SparseBooleanArray;
import android.widget.AdapterView;
import android.widget.Toast;

import com.pushtech.sdk.PushtechApp;
import com.pushtech.sdk.User;
import com.pushtech.sdk.UserManager;
import com.pushtech.sdk.chatAndroidExample.R;

/**
 * Created by goda87 on 4/09/14.
 */
public abstract class BaseContactsFragment extends Fragment
        implements AdapterView.OnItemClickListener {

    public static final String SELECTION = "selection";
    public static final String SELECTION_ARGS = "selection_args";
    public static final String SORT_ORDER = "sort_order";
    protected CursorAdapter adapter;
    private UserManager userManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userManager = PushtechApp.with(getActivity()).getBaseManager().getUserManager();
    }


    protected User getUserFromAdapterPosition(int position) {
        Cursor cursor = adapter.getCursor();
        cursor.moveToPosition(position);
        return getUserFromCursor(cursor);
    }

    protected User getUserFromCursor(Cursor cursor) {
        return User.buildFromCursor(cursor);
    }

    protected String getSelectedUsersJidsFromAdapterAsString(SparseBooleanArray checkedUsers) {
        StringBuilder stringBuilder = new StringBuilder();
        Cursor cursor = adapter.getCursor();
        if (cursor.moveToFirst()) {
            int contactIndex = 0;
            do {
                if (checkedUsers.get(contactIndex)) {
                    stringBuilder.append(getUserFromCursor(cursor).getJid());
                    stringBuilder.append(",");
                }
                contactIndex++;
            } while (cursor.moveToNext());
        }
        return stringBuilder.toString();
    }

    protected void getAppContacts() {
        showActionBarProgress(true);
        Toast.makeText(getActivity(),
                R.string.contacts_syncingContacts_warning_toast,
                Toast.LENGTH_SHORT).show();

        userManager.buildContacts();
    }

    private void showActionBarProgress(final boolean active) {
        if (getActivity() != null) {
            getActivity().setProgressBarIndeterminateVisibility(active);
        }
    }
}
