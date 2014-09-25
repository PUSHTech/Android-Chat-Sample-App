package com.pushtech.pushchat.androidapplicationexample.chat.contacts;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.AdapterView;
import android.widget.Toast;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.chat.db.contentvaluesop.UserContentValuesOp;
import com.pushtech.sdk.chat.db.contract.UsersContract;
import com.pushtech.sdk.chat.manager.ChatsManager;
import com.pushtech.sdk.chat.manager.ContactsManager;
import com.pushtech.sdk.chat.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by goda87 on 4/09/14.
 */
public abstract class BaseContactsFragment extends Fragment
        implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String SELECTION = "selection";
    public static final String SELECTION_ARGS = "selection_args";
    public static final String SORT_ORDER = "sort_order";
    protected CursorAdapter adapter;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        if (args != null) {
            selection = args.getString(SELECTION);
            selectionArgs = args.getStringArray(SELECTION_ARGS);
            sortOrder = args.getString(SORT_ORDER);
        }

        Uri contentUri = null;
        switch (id) {
            case ContactsActivity.SINGLE_CHAT:
                contentUri = UsersContract.User.CONTENT_URI_NEW;
                break;
            case ContactsActivity.GROUP_CHAT:
            case ContactsActivity.ADD_MEMBER:
                contentUri = UsersContract.User.CONTENT_URI;
                break;
            default:
        }

        Loader<Cursor> loader = new CursorLoader(
                getActivity(),   // Parent activity context
                contentUri,        // Table to query
                UsersContract.User.ALL_COLUMNS,     // Projection to return
                selection,
                selectionArgs,
                sortOrder
        );
        Log.d("GODA", "loader: " + loader);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Change FragmentCursor
        Log.d("GODA", "cursor lenght : " + data.getCount());
        adapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Avoid memory leaks by deleting references to cursor
        Log.d("GODA", "cursor reset : " + this.getClass().getSimpleName());
        adapter.changeCursor(null);
    }

    protected User getUserFromAdapterPosition(int position) {
        Cursor cursor = adapter.getCursor();
        cursor.moveToPosition(position);
        return getUserFromCursor(cursor);
    }

    protected User getUserFromCursor(Cursor cursor) {
        UserContentValuesOp userContentValuesOp = new UserContentValuesOp();
        return userContentValuesOp.buildFromCursor(cursor);
    }

    protected String[] getSelectedUsersJidsFromAdapterAsArray(SparseBooleanArray checkedUsers) {
        List<String> usersJids = new ArrayList<String>();
        Cursor cursor = adapter.getCursor();
        if (cursor.moveToFirst()) {
            int contactIndex = 0;
            do {
                if (checkedUsers.get(contactIndex)) {
                    usersJids.add(getUserFromCursor(cursor).getJid());
                }
                contactIndex++;
            } while (cursor.moveToNext());
        }
        return usersJids.toArray(new String[usersJids.size()]);
    }

    protected void getAppContacts(){
        showActionBarProgress(true);
        Toast.makeText(getActivity(),
                R.string.contacts_syncingContacts_warning_toast,
                Toast.LENGTH_SHORT).show();
        String contacts = ContactsManager.getInstance(
                getActivity()).getNumbersFromPhoneBookAsString();

        ContactsManager.getInstance(getActivity()).updatedContacts(contacts, true,
                new ContactsManager.ContactListUpdateCallback() {
                    @Override
                    public void gotContactsUpdated(User[] contacts) {
                        showActionBarProgress(false);
                    }

                    @Override
                    public void gotContactsFromDB(User[] contacts) {

                    }

                    @Override
                    public void onError(Exception exception) {
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(),
                                    R.string.contacts_syncingContacts_error_warning_toast,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showActionBarProgress(final boolean active) {
        if (getActivity() != null) {
            getActivity().setProgressBarIndeterminateVisibility(active);
        }
    }
}
