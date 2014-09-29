package com.pushtech.pushchat.androidapplicationexample.chat.contacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.chat.db.agent.ChatsDBAgent;
import com.pushtech.sdk.chat.db.contract.UsersContract;
import com.pushtech.sdk.chat.model.GroupChat;
import com.pushtech.sdk.chat.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by goda87 on 8/09/14.
 */
public class AddMemberToGroupFragment extends BaseContactsFragment {

    private ListView lv;

    public static AddMemberToGroupFragment newInstance(String groupJid) {
        AddMemberToGroupFragment fragment = new AddMemberToGroupFragment();
        Bundle args = new Bundle();
        args.putString(ContactsActivity.EXTRA_PARAM_GROUP_JID, groupJid);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);

        String groupJid = getArguments().getString(ContactsActivity.EXTRA_PARAM_GROUP_JID);

        GroupChat chat = (GroupChat) ChatsDBAgent.getInstance(getActivity().getApplicationContext())
                .findChatByJid(groupJid);
        List<User> groupUsers = chat.getUsers();

        String groupUsersJids = "";
        if (groupUsers != null) {
            List<String> groupUsersJidsList = new ArrayList<String>(groupUsers.size());
            for (User user : groupUsers) {
                groupUsersJidsList.add(user.getJid());
            }

            groupUsersJids = groupUsersJidsList.toString()
                    .replace("[", "'").replace("]", "'")
                    .replace(", ", "','");
        }
        Bundle args = new Bundle();
        args.putString(BaseContactsFragment.SELECTION,
                UsersContract.User.JID + " NOT IN ("+groupUsersJids+")");

        getLoaderManager().initLoader(ContactsActivity.ADD_MEMBER, args, this);
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
        contactsActivity.addMemeberToGroup(
                getArguments().getString(ContactsActivity.EXTRA_PARAM_GROUP_JID), chatJid);
    }
}
