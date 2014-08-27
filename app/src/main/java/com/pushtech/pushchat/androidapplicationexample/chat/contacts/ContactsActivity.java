package com.pushtech.pushchat.androidapplicationexample.chat.contacts;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.pushtech.pushchat.androidapplicationexample.R;

public class ContactsActivity extends ActionBarActivity {

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
}
