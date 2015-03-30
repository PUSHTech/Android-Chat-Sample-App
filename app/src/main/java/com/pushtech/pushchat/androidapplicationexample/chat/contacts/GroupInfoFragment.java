package com.pushtech.pushchat.androidapplicationexample.chat.contacts;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pushtech.sdk.GroupChat;
import com.pushtech.sdk.PushtechApp;
import com.pushtech.sdk.User;
import com.pushtech.sdk.chatAndroidExample.R;

import java.util.ArrayList;

/**
 * Created by goda87 on 8/09/14.
 */
public class GroupInfoFragment extends Fragment {

    private ListView lv;

    public static GroupInfoFragment newInstance(String groupJid) {
        GroupInfoFragment fragment = new GroupInfoFragment();
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

        GroupChat chat = (GroupChat) PushtechApp.with(getActivity()).getBaseManager().getChatManager().getChatByJid(groupJid);
        ArrayList<User> groupUsers = chat.getMembers();
        ContactsAdapter adapter = new ContactsAdapter(getActivity(), groupUsers);
        lv = (ListView) v.findViewById(android.R.id.list);
        lv.setAdapter(adapter);
        lv.setEmptyView(v.findViewById(android.R.id.empty));
        lv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lv.setCacheColorHint(android.R.color.transparent);
        lv.setChoiceMode(ListView.CHOICE_MODE_NONE);
        return v;
    }
}
