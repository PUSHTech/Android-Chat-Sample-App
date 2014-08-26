package com.pushtech.pushchat.androidapplicationexample.chat.registration;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Window;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.pushchat.androidapplicationexample.chat.ChatListActivity;

public class RegistrationActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_registration);

        if (savedInstanceState == null) {
            if (true) { //if not registered
                openRegistrationFragment();
            } else if (true) { // if registered but not confirmed
                openRegistrationFragment();
                openRegistrationValidationFragment();
            } else { // if already registered
                registrationFinished();
            }
        }
    }

    private void openRegistrationFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, RegistrationFragment.newInstance())
                .commit();
    }

    void openRegistrationValidationFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, RegistrationValidationFragment.newInstance())
                .addToBackStack(RegistrationValidationFragment.class.getName())
                .commit();
    }

    void registrationFinished() {
        Intent intent = new Intent(this, ChatListActivity.class);
        startActivity(intent);
        finish();
    }
}
