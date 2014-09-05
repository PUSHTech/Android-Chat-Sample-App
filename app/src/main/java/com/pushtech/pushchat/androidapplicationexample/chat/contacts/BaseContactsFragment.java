package com.pushtech.pushchat.androidapplicationexample.chat.contacts;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.widget.AdapterView;

import com.pushtech.sdk.chat.db.contract.UsersContract;

/**
 * Created by goda87 on 4/09/14.
 */
public abstract class BaseContactsFragment extends Fragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String CONSTRAINT = "constraint";
    protected CursorAdapter adapter;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> loader = null;
        String constraint = null;
        if (args != null) {
            args.getString(CONSTRAINT);
        }

        Uri contentUri = null;
        switch (id) {
            case ContactsActivity.SINGLE_CHAT:
                contentUri = UsersContract.User.CONTENT_URI_NEW;
                break;
            case ContactsActivity.GROUP_CHAT:
                contentUri = UsersContract.User.CONTENT_URI;
                break;
            default:
        }

        loader = new CursorLoader(
                getActivity(),   // Parent activity context
                contentUri,        // Table to query
                UsersContract.User.ALL_COLUMNS,     // Projection to return
                constraint != null ? (UsersContract.User.PHONEBOOK_NAME + " LIKE '%" +
                        constraint.toString() + "%'") : null,
                null,            // No selection arguments
                null             // Default sort order
        );
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Change FragmentCursor
        adapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Avoid memory leaks by deleting references to cursor
        adapter.changeCursor(null);
    }
}
