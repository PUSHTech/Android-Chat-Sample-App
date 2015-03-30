package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;

import com.pushtech.pushchat.androidapplicationexample.chat.notifications.NotificationManager;
import com.pushtech.sdk.GroupChat;
import com.pushtech.sdk.PushtechApp;
import com.pushtech.sdk.SingleChat;
import com.pushtech.sdk.chatAndroidExample.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * An activity representing a single Chat detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ChatListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link ChatDetailFragment}.
 */
public class ChatDetailActivity extends ChatMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        currentChat = PushtechApp.with(this).getBaseManager().getChatManager().getChatByJid(
                getIntent().getStringExtra(ChatDetailFragment.ARG_ITEM_ID));

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(currentChat.getChatName());
        setupActionBarImage();

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

    private void setupActionBarImage() {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                getSupportActionBar().setIcon(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        if (currentChat instanceof SingleChat) {
            Picasso.with(this).load(((SingleChat) currentChat).getChatPicture()).into(target);
        } else {
            Picasso.with(this).load(((GroupChat) currentChat).getGroupPicture()).into(target);
        }
    }

    @Override
    protected NotificationManager.TypeOfActivity getTypeOfActivity() {
        return NotificationManager.TypeOfActivity.CHAT;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
