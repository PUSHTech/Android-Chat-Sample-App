package com.pushtech.pushchat.androidapplicationexample.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.widget.Toast;

/**
 *
 */
public abstract class ChatCommunicationTrackerActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

    }

    @Override
    protected void onStart() {
        super.onStart();
//        ChatCommunicationTracker.getInstance().startTracking(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
//        NotificationManager.with(getApplicationContext()).setTypeOfActivity(getTypeOfActivity());
    }

    @Override
    protected void onPause() {
        super.onPause();
//        NotificationManager.with(getApplicationContext()).resetTypeOfActivity();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        ChatCommunicationTracker.getInstance().stopTracking(getApplicationContext());
    }

//    protected abstract NotificationManager.TypeOfActivity getTypeOfActivity();

    protected void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int resId) {
        Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_SHORT).show();
    }
}
