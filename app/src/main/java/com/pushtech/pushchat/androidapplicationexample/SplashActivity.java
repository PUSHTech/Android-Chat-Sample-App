package com.pushtech.pushchat.androidapplicationexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.pushtech.sdk.PushSetup;
import com.pushtech.sdk.chat.manager.UserManager;

import com.pushtech.pushchat.androidapplicationexample.chat.ChatListActivity;
import com.pushtech.pushchat.androidapplicationexample.chat.RegistrationActivity;


public class SplashActivity extends Activity implements PushSetup.SetupCompleteListener {

    private static final String TAG = SplashActivity.class.getName();

    private static final Handler splashTimeOutHandler = new Handler();
    public static final int SPLASH_TIMEOUT = 2000;
    private long openDate = 0;
    private PushSetup pushSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preparePushSDK();

        if (UserManager.getInstance(this).isUserRegistered()) {
            startActivity(ChatListActivity.class);
            finish();
        } else {
            setContentView(R.layout.activity_splash);
            startSetup();
        }
    }

    private void startSplashTimeOut(final long delayMillis) {
        splashTimeOutHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(RegistrationActivity.class);
            }
        }, delayMillis);
    }

    private void preparePushSDK() {
        pushSetup = new PushSetup();
        pushSetup.init(this);
//        PushtechPreferences.getInstance(this);
//        RestClient.resetRestClient();
    }

    private void startSetup() {
        String appId = getString(R.string.pushtech_app_id);
        String secretId = getString(R.string.pushtech_app_secret);
        String sender_id = getString(R.string.gcm_sender_id);
        openDate = System.currentTimeMillis();
        pushSetup.start(getApplicationContext(), PushSetup.Environment.PRODUCTION,
                this, appId, secretId, sender_id);
    }

    @Override
    public void onSetupComplete(boolean success, String regId, String pushDeviceID) {
        Log.d(TAG, "ID that GCM uses for identify this app in this phone: " + pushDeviceID);
        Log.d(TAG, "ID that Pushtech uses for identify this app in this phone: " + regId);
        if (!success) {
            // This creates a bucle that tries to complete the push setup until is successfull
            startSetup();
        } else {
            whaitUntilSplashTimeoutBeforeClosingSplash();
        }
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    private void whaitUntilSplashTimeoutBeforeClosingSplash() {
        long elapsedTime = System.currentTimeMillis() - openDate;
        if (elapsedTime < SPLASH_TIMEOUT) {
            startSplashTimeOut(SPLASH_TIMEOUT - elapsedTime);
        } else {
            startSplashTimeOut(0);
        }
    }

    private void startActivity(Class activityClass) {
        Intent startActivityIntent = new Intent(this, activityClass);
        startActivity(startActivityIntent);
        finish();
    }

}
