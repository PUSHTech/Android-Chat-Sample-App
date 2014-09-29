package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.pushchat.androidapplicationexample.chat.notifications.NotificationManager;
import com.pushtech.sdk.chat.manager.ChatsManager;

/**
 * An activity representing a single Chat detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ChatListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link ChatDetailFragment}.
 */
public class ChatDetailActivity extends ChatMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        currentChat = ChatsManager.getInstance(getApplicationContext())
                .getChatWithId(getIntent().getStringExtra(ChatDetailFragment.ARG_ITEM_ID));

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(currentChat.getName());

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ChatDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ChatDetailFragment.ARG_ITEM_ID));
            ChatDetailFragment fragment = new ChatDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.chat_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    protected NotificationManager.TypeOfActivity getTypeOfActivity() {
        return NotificationManager.TypeOfActivity.CHAT;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_detail, menu);
        MenuItem groupActionsMenuItem = menu.findItem(R.id.overflow_menu);
        if (currentChat.isGroupChat()) {
            groupActionsMenuItem.setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }
}
