package com.pushtech.pushchat.androidapplicationexample.chat.contacts;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.chat.exception.ChatCreationException;
import com.pushtech.sdk.chat.manager.ChatsManager;
import com.pushtech.sdk.chat.model.Chat;

public class ContactsActivity extends ActionBarActivity implements ChatsManager.CreateChatListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ContactsFragment.newInstance())
                    .commit();
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
            Toast.makeText(getApplicationContext(),
                    R.string.contacts_createChat_error_warning_toast,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(Chat chat) {
        setProgressBarIndeterminateVisibility(false);
        finish();
    }

    @Override
    public void onError() {
        Toast.makeText(getApplicationContext(),
                R.string.contacts_createChat_error_warning_toast,
                Toast.LENGTH_SHORT).show();
        setProgressBarIndeterminateVisibility(false);
    }
}
