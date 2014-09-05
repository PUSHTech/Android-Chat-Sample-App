package com.pushtech.pushchat.androidapplicationexample.chat.contacts;

import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        if (savedInstanceState == null) {

            switch (getIntent().getIntExtra(FRAGMENT_TYPE, 0)) {
                case SINGLE_CHAT:
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, ContactsFragment.newInstance())
                            .commit();
                    break;
                case GROUP_CHAT:
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, CreateGroupFragment.newInstance())
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

    /*
     * Called when the chat is successfully created.
     */
    @Override
    public void onCreate(Chat chat) {
        setProgressBarIndeterminateVisibility(false);
        // todo set activity result
        finish();
    }

    @Override
    public void onError() {
        showToast(R.string.contacts_createChat_error_warning_toast);
        setProgressBarIndeterminateVisibility(false);
    }
}
