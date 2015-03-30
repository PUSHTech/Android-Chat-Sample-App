package com.pushtech.pushchat.androidapplicationexample.chat.contacts;

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

import com.pushtech.sdk.PushtechApp;
import com.pushtech.sdk.chatAndroidExample.R;


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

        adapter = new ContactsListCursorAdapter(
                getActivity(), PushtechApp.with(getActivity())
                .getBaseManager()
                .getUserManager().getAllContactsCursor(), R.layout.item_contact_list_checkable);

        lv = (ListView) v.findViewById(R.id.lv_groupMembers);
        lv.setOnItemClickListener(this);
        lv.setAdapter(adapter);
        lv.setEmptyView(v.findViewById(android.R.id.empty));
        lv.setItemsCanFocus(false);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        bCreateGroup = v.findViewById(R.id.b_create_group);
        bCreateGroup.setOnClickListener(this);
        bCreateGroup.setEnabled(false);

        etGroupName = (EditText) v.findViewById(R.id.et_groupName);
        etGroupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateUIWithValidation();
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
        getAppContacts();
    }

    private void updateUIWithValidation() {
        bCreateGroup.setEnabled(
                etGroupName.getText().length() > 0
                        && getSelectedContacts().contains(",")
        );
    }

    private String getSelectedContacts() {
        SparseBooleanArray checkedContacts = lv.getCheckedItemPositions();
        return getSelectedUsersJidsFromAdapterAsString(checkedContacts);
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
