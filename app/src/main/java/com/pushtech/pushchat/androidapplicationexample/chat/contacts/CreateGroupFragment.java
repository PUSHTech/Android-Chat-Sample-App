package com.pushtech.pushchat.androidapplicationexample.chat.contacts;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.chat.db.contract.UsersContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by goda87 on 4/09/14.
 */
public class CreateGroupFragment extends BaseContactsFragment implements View.OnClickListener {

    private View bCreateGroup;
    private EditText etGroupName;
    private ListView lv;

    public static CreateGroupFragment newInstance() {
        return new CreateGroupFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_contacts_create_group, container, false);
        lv = (ListView) v.findViewById(R.id.lv_groupMembers);

        getLoaderManager().initLoader(ContactsActivity.GROUP_CHAT, null, this);

        lv.setOnItemClickListener(this);


        adapter = new ContactsListCursorAdapter(
                getActivity(), null, R.layout.item_contact_list_checkable);
        lv.setAdapter(adapter);
        lv.setEmptyView(v.findViewById(android.R.id.empty));
        lv.setItemsCanFocus(false);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        bCreateGroup = v.findViewById(R.id.b_create_group);
        bCreateGroup.setOnClickListener(this);

        etGroupName = (EditText) v.findViewById(R.id.et_groupName);
        etGroupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateUIWithValidation();
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(getActivity(),
                R.string.contacts_syncingContacts_warning_toast,
                Toast.LENGTH_SHORT).show();
    }

    private void updateUIWithValidation() {
        bCreateGroup.setEnabled(
                etGroupName.getText().length() > 0
                        && getSelectedContacts().length > 1
        );
    }

    private String[] getSelectedContacts() {
        List<String> contactJids = new ArrayList<String>();
        SparseBooleanArray checkedContacts = lv.getCheckedItemPositions();
        Cursor cursor = adapter.getCursor();
        if (cursor.moveToFirst()) {
            int contactIndex = 0;
            do {
                if (checkedContacts.get(contactIndex)) {
                    contactJids.add(cursor.getString(
                            cursor.getColumnIndex(UsersContract.User.JID)));
                }
                contactIndex++;
            } while (cursor.moveToNext());
        }
        return contactJids.toArray(new String[contactJids.size()]);
    }

    @Override
    public void onClick(View v) {
        ContactsActivity contactsActivity = (ContactsActivity) getActivity();
        contactsActivity.createGroupChat(etGroupName.getText().toString(), getSelectedContacts());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        updateUIWithValidation();
    }
}
