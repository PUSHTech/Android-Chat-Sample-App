package com.pushtech.pushchat.androidapplicationexample.chat.contacts;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.Constants;
import com.pushtech.sdk.chat.db.agent.ChatsDBAgent;
import com.pushtech.sdk.chat.db.contract.ChatsContract;
import com.pushtech.sdk.chat.db.contract.UsersContract;
import com.pushtech.sdk.chat.manager.ChatsManager;
import com.pushtech.sdk.chat.model.Chat;
import com.pushtech.sdk.chat.model.GroupChat;
import com.pushtech.sdk.chat.model.SingleChat;
import com.pushtech.sdk.chat.model.User;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

//        GroupChat chat = (GroupChat) ChatsDBAgent
//                .getInstance(getActivity().getApplicationContext()).findChatByJid(groupJid);
        GroupChat chat = getChat(groupJid);
        List<User> groupUsers = chat.getUsers();
        Log.d("GODA", "users : " + groupUsers);
        Log.d("GODA", "wanted chat JID: " + groupJid + "  || obtained jid: " + chat.getJid());

        String groupUsersJids = "";
        if (groupUsers != null) {
            List<String> groupUsersJidsList = new ArrayList<String>(groupUsers.size());
            for (User user : groupUsers) {
                groupUsersJidsList.add(user.getJid());
            }
            groupUsersJids = groupUsers.toString()
                    .replace("[", "'").replace("]", "'")
                    .replace(", ", "','");
        }
        Bundle args = new Bundle();
        args.putString(BaseContactsFragment.SELECTION, UsersContract.User.JID + " NOT IN (?)");
        args.putStringArray(BaseContactsFragment.SELECTION_ARGS, new String[]{groupUsersJids});

        getLoaderManager().initLoader(ContactsActivity.ADD_MEMBER, args, this);
        adapter = new ContactsListCursorAdapter(getActivity(), null, R.layout.item_contact_list);
        lv = (ListView) v.findViewById(android.R.id.list);
        lv.setOnItemClickListener(this);
        lv.setAdapter(adapter);
        lv.setEmptyView(v.findViewById(android.R.id.empty));
        return v;
    }


    private GroupChat getChat(String jid) {
        GroupChat existingChat = null;
        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(ChatsContract.Chat.CONTENT_URI,
                ChatsContract.Chat.ALL_COLUMNS,
                ChatsContract.Chat.JID + "=?",
                new String[]{jid},
                null);
        if (cursor.moveToNext()) {
            existingChat = (GroupChat) createChatFromCursor(cursor);
        }
        cursor.close();
        return existingChat;
    }

    public static Chat createChatFromCursor(Cursor cursor) {
        long chatId = cursor.getLong(cursor.getColumnIndex(ChatsContract.Chat._ID));

        Chat chat = null;

        Chat.Type type = Chat.Type.valueOf(cursor.getString(cursor.getColumnIndex(ChatsContract
                .Chat.TYPE)));
        if (type.equals(Chat.Type.CHAT)) {
            SingleChat singleChat = new SingleChat(chatId);
            chat = singleChat;
        } else {
            Log.d("GODA", "is group chat");
            GroupChat groupChat = new GroupChat(chatId);
            groupChat.setOccupants(cursor.getInt(cursor.getColumnIndex(ChatsContract.Chat
                    .OCCUPANTS)));
            String membersIds = cursor.getString(cursor.getColumnIndex(ChatsContract.Chat
                    .MEMBERS_IDS));
            Log.d("GODA", "memebers ids: " + membersIds);

            Long memberIDS = cursor.getLong(cursor.getColumnIndex(ChatsContract.Chat.MEMBERS_IDS));
            Log.d("GODA", "memebers ids long: " + memberIDS);
            for (int j = 0; j < cursor.getColumnCount(); j++) {
                Log.d("GODA", String.format("column[%d]: %s -> %s", j, cursor.getColumnName(j), cursor.getString(j)));
            }

            if (membersIds != null) {
                String[] ids = membersIds.split(",");
                List<User> users = new ArrayList<User>();
                for (int i = 0; i < ids.length; i++) {
                    User user = new User();
                    user.setJid(ids[i]);
                    users.add(user);
                }
                groupChat.setUsers(users);
            }
            chat = groupChat;
        }

        chat.setJid(cursor.getString(cursor.getColumnIndex(ChatsContract.Chat.JID)));
        chat.setName(cursor.getString(cursor.getColumnIndex(ChatsContract.Chat.NAME)));
        chat.setUnreadMessages(cursor.getInt(cursor.getColumnIndex(ChatsContract.Chat
                .UNREAD_MESSAGES)));
        chat.setAvatarUrl(cursor.getString(cursor.getColumnIndex(ChatsContract.Chat.AVATAR_URL)));

        try {
            Date date = Constants.DateAndTime.ISO8061_DATE_FORMAT.parse(
                    cursor.getString(cursor.getColumnIndex(ChatsContract.Chat.CREATED_DATE)));
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime(date);
            chat.setCreatedDate(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return chat;
        }
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
