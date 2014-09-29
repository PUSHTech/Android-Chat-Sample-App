package com.pushtech.pushchat.androidapplicationexample.chat.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.pushchat.androidapplicationexample.utils.ChatCommunicationTrackerActivity;
import com.pushtech.sdk.chat.exception.ChatCreationException;
import com.pushtech.sdk.chat.manager.ChatsManager;
import com.pushtech.sdk.chat.model.Chat;

public class ContactsActivity extends ChatCommunicationTrackerActivity
        implements ChatsManager.CreateChatListener {
    public static final String FRAGMENT_TYPE = "fragment_type";
    public static final int SINGLE_CHAT = 22;
    public static final int GROUP_CHAT = 23;
    public static final int ADD_MEMBER = 24;
    public static final int GROUP_INFO = 25;
    public static final String CHAT_JID_RESULT = "chat_jid_result";
    public static final String EXTRA_PARAM_GROUP_JID = "extra_param_group_jid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        if (savedInstanceState == null) {

            switch (getIntent().getIntExtra(FRAGMENT_TYPE, 0)) {
                case SINGLE_CHAT:
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, CreateSingleChatFragment.newInstance())
                            .commit();
                    break;
                case GROUP_CHAT:
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, CreateGroupFragment.newInstance())
                            .commit();
                    break;
                case ADD_MEMBER:
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, AddMemberToGroupFragment.newInstance(
                                    getIntent().getStringExtra(EXTRA_PARAM_GROUP_JID)))
                            .commit();
                    break;
                case GROUP_INFO:
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, GroupInfoFragment.newInstance(
                                    getIntent().getStringExtra(EXTRA_PARAM_GROUP_JID)))
                            .commit();
                    break;
                default:
                    finish();
            }

        }
    }

    void createSingleChat(final String chatJid) {
        try {
            setProgressBarIndeterminateVisibility(true);
            ChatsManager.getInstance(getApplicationContext())
                    .newSingleChat()
                    .setChatJid(chatJid)
                    .create(this);
        } catch (ChatCreationException e) {
            showToast(R.string.contacts_createChat_error_warning_toast);
        }
    }

    void createGroupChat(final String groupName, final String[] members) {
        for (int i = 0; i < members.length; i++) {
            Log.d("GODA", String.format("members[%d]: %s", i, members[i]));
        }
        try {
            setProgressBarIndeterminateVisibility(true);
            ChatsManager.getInstance(getApplicationContext())
                    .newGroupChat()
                    .setGroupName(groupName)
                    .setMembers(members)
                    .create(this);
        } catch (ChatCreationException e) {
            showToast(R.string.contacts_createChat_error_warning_toast);
        }
    }

    void addMemeberToGroup(final String chatJid, final String userJid) {
        ChatsManager.getInstance(this).addMemberToGroupChat(chatJid, userJid);
        setActivityResult(userJid);
        finish();
    }

    void setActivityResult(String jid) {
        Intent intent = this.getIntent();
        intent.putExtra(CHAT_JID_RESULT, jid);
        this.setResult(RESULT_OK, intent);
    }

    /*
     * Called when the chat is successfully created.
     */
    @Override
    public void onCreate(Chat chat) {
        setProgressBarIndeterminateVisibility(false);
        setActivityResult(chat.getJid());
        finish();
    }

    @Override
    public void onError() {
        showToast(R.string.contacts_createChat_error_warning_toast);
        setProgressBarIndeterminateVisibility(false);
    }
}
