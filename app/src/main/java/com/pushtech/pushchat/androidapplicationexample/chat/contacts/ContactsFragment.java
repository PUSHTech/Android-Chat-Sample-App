package com.pushtech.pushchat.androidapplicationexample.chat.contacts;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.chat.db.agent.UsersDBAgent;
import com.pushtech.sdk.chat.db.contentvaluesop.UserContentValuesOp;
import com.pushtech.sdk.chat.manager.ContactsManager;
import com.pushtech.sdk.chat.model.User;


/**
 * Created by goda87 on 27/08/14.
 */
public class ContactsFragment extends BaseContactsFragment {

    private ListView lv;

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);
        adapter = new ContactsListCursorAdapter(getActivity(), null, R.layout.item_contact_list);
        lv = (ListView) v.findViewById(android.R.id.list);
        lv.setOnItemClickListener(this);
        lv.setAdapter(adapter);
        lv.setEmptyView(v.findViewById(android.R.id.empty));
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(getActivity(),
                R.string.contacts_syncingContacts_warning_toast,
                Toast.LENGTH_SHORT).show();
        getAppContacts();
    }

    private void getAppContacts(){
        showActionBarProgress(true);
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
                        Toast.makeText(getActivity(),
                                R.string.contacts_syncingContacts_error_warning_toast,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = ((ContactsListCursorAdapter) lv.getAdapter()).getCursor();
        cursor.moveToPosition(position);
        UserContentValuesOp userContentValuesOp = new UserContentValuesOp();
        User user = userContentValuesOp.buildFromCursor(cursor);
        contactSelected(user.getJid());
    }

    private void contactSelected(final String chatJid) {
        ContactsActivity contactsActivity = (ContactsActivity) getActivity();
        contactsActivity.createSingleChat(chatJid);
    }

    private void showActionBarProgress(final boolean active) {
        if (getActivity() != null) {
            getActivity().setProgressBarIndeterminateVisibility(active);
        }
    }
}
