package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.pushchat.androidapplicationexample.chat.contacts.ContactsActivity;
import com.pushtech.sdk.chat.manager.ChatsManager;

/**
 * An activity representing a list of Chats. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ChatDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ChatListFragment} and the item details
 * (if present) is a {@link ChatDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ChatListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ChatListActivity extends ChatMenuActivity
        implements ChatListFragment.Callbacks {

    private static final int REQUEST_CODE_NEW_CHAT_SINGLE = 501;
    private static final int REQUEST_CODE_NEW_CHAT_GROUP = 502;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        if (findViewById(R.id.chat_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ChatListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.chat_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
        // TODO: route to chat when comming from notification.
        // TODO: route to chat when comming from new chat.
    }

    /**
     * Callback method from {@link ChatListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ChatDetailFragment.ARG_ITEM_ID, id);
            ChatDetailFragment fragment = new ChatDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.chat_detail_container, fragment)
                    .commit();
            currentChat = ChatsManager.getInstance(getApplicationContext())
                    .getChatWithId(id);

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ChatDetailActivity.class);
            detailIntent.putExtra(ChatDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (mTwoPane) {
            //todo allow detail menu in master-detail
        } else {
            getMenuInflater().inflate(R.menu.chat_list, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_newChat:
                openContacts();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.menu_new_group:
                openNewGroup();
                return true;
            case R.id.menu_msg_center:
                openMessageCenter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openContacts() {
        Intent intent = new Intent(this, ContactsActivity.class);
        intent.putExtra(ContactsActivity.FRAGMENT_TYPE, ContactsActivity.SINGLE_CHAT); //todo use constants
        startActivityForResult(intent, REQUEST_CODE_NEW_CHAT_SINGLE);
    }

    private void openSettings() {
        showToast("openSettigns");
    }

    private void openMessageCenter() {
        showToast("openMessageCenter");
    }

    private void openNewGroup() {
        Intent intent = new Intent(this, ContactsActivity.class);
        intent.putExtra(ContactsActivity.FRAGMENT_TYPE, ContactsActivity.GROUP_CHAT); //todo use constants
        startActivityForResult(intent, REQUEST_CODE_NEW_CHAT_GROUP);
    }
}
