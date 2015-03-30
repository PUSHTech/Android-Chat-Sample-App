package com.pushtech.pushchat.androidapplicationexample.chat.contacts;

import android.content.Intent;
import android.os.Bundle;

import com.pushtech.pushchat.androidapplicationexample.chat.notifications.ChatCommunicationTrackerActivity;
import com.pushtech.pushchat.androidapplicationexample.chat.notifications.NotificationManager;
import com.pushtech.sdk.Callbacks.GenericCallback;
import com.pushtech.sdk.ChatManager;
import com.pushtech.sdk.GroupChat;
import com.pushtech.sdk.GroupDataCallback;
import com.pushtech.sdk.PushtechApp;
import com.pushtech.sdk.PushtechError;
import com.pushtech.sdk.chatAndroidExample.R;

public class ContactsActivity extends ChatCommunicationTrackerActivity
        implements GenericCallback, GroupDataCallback {
    public static final String FRAGMENT_TYPE = "fragment_type";
    public static final int SINGLE_CHAT = 22;
    public static final int GROUP_CHAT = 23;
    public static final int ADD_MEMBER = 24;
    public static final int GROUP_INFO = 25;
    public static final String CHAT_JID_RESULT = "chat_jid_result";
    public static final String EXTRA_PARAM_GROUP_JID = "extra_param_group_jid";
    private ChatManager chatManager;
    public String chatJid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        chatManager = PushtechApp.with(this).getBaseManager().getChatManager();
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

    @Override
    protected NotificationManager.TypeOfActivity getTypeOfActivity() {
        return NotificationManager.TypeOfActivity.CONTACT_LIST;
    }

    void createSingleChat(final String chatJid) {
        setProgressBarIndeterminateVisibility(true);
        this.chatJid = chatJid;
        chatManager.createNewChat(chatJid, this);
    }

    void createGroupChat(final String groupName, final String members) {
        setProgressBarIndeterminateVisibility(true);
        chatManager.createNewGroupChat(groupName, members, this);
    }

    void addMemeberToGroup(final String chatJid, final String userJid) {
        this.chatJid = chatJid;
        chatManager.addMemberInGroup(chatJid, userJid, this);
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
    public void onSuccess() {
        setProgressBarIndeterminateVisibility(false);
        setActivityResult(this.chatJid);
        finish();
    }

    @Override
    public void onError(PushtechError error) {
        showToast(R.string.contacts_createChat_error_warning_toast);
        setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void onGroupDataAvailable(GroupChat group) {
        setProgressBarIndeterminateVisibility(false);
        setActivityResult(group.getJid());
        finish();
    }


}
