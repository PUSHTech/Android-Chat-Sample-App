package com.pushtech.pushchat.androidapplicationexample.chat.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.ChatListActivity;
import com.pushtech.sdk.Callbacks.AutomaticUserValidationCallback;
import com.pushtech.sdk.PushtechApp;
import com.pushtech.sdk.chatAndroidExample.R;


public class RegistrationActivity extends ActionBarActivity implements AutomaticUserValidationCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_registration);

        if (savedInstanceState == null) {
            openRegistrationFragment();
        }
        PushtechApp.with(this)
                .getChatRegister()
                .setAutomaticUserValidationCallback(this);
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

    @Override
    public void onValidateUser() {
        registrationFinished();
    }
}
