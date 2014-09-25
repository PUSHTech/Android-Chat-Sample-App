package com.pushtech.pushchat.androidapplicationexample.chat.contacts;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.chat.db.contentvaluesop.UserContentValuesOp;
import com.pushtech.sdk.chat.model.User;


/**
 * Created by goda87 on 27/08/14.
 */
public class CreateSingleChatFragment extends BaseContactsFragment {

    private ListView lv;

    public static CreateSingleChatFragment newInstance() {
        return new CreateSingleChatFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);

        getLoaderManager().initLoader(ContactsActivity.SINGLE_CHAT, null, this);
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
        getAppContacts();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User user = getUserFromAdapterPosition(position);
        contactSelected(user.getJid());
    }

    private void contactSelected(final String chatJid) {
        ContactsActivity contactsActivity = (ContactsActivity) getActivity();
        contactsActivity.createSingleChat(chatJid);
    }

}
