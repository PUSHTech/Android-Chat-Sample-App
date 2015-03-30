package com.pushtech.pushchat.androidapplicationexample.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.ChatListActivity;
import com.pushtech.pushchat.androidapplicationexample.chat.registration.RegistrationActivity;
import com.pushtech.sdk.Callbacks.PushtechAppbuildAsyncCallback;
import com.pushtech.sdk.ChatPreferences;
import com.pushtech.sdk.PushtechApp;
import com.pushtech.sdk.PushtechAppBuilder;
import com.pushtech.sdk.PushtechError;
import com.pushtech.sdk.chatAndroidExample.R;


public class SplashActivity extends Activity implements PushtechAppbuildAsyncCallback {

    private static final String TAG = SplashActivity.class.getName();

    private static final Handler splashTimeOutHandler = new Handler();
    public static final int SPLASH_TIMEOUT = 2000;
    private long openDate = 0;
    private PushtechAppBuilder pushtechAppBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preparePushSDK();
        ChatPreferences chatPreferences = new ChatPreferences(this);
        if (chatPreferences.isUserRegister()) {
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
        pushtechAppBuilder = new PushtechAppBuilder(this);
    }

    private void startSetup() {
        pushtechAppBuilder
                .setAppId(getString(R.string.appId))
                .setAppSecret(getString(R.string.secret))
                .setGcmSenderId(getString(R.string.gcmToken))
                .build(this);
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

    @Override
    public void onPushtechAppBuild(PushtechApp pushtechApp) {
        whaitUntilSplashTimeoutBeforeClosingSplash();
    }

    @Override
    public void onError(PushtechError error) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startSetup();
            }
        }, 5000);

    }
}
