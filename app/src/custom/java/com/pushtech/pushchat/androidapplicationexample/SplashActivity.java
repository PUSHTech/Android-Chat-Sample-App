package com.pushtech.pushchat.androidapplicationexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.ChatListActivity;
import com.pushtech.pushchat.androidapplicationexample.chat.registration.RegistrationActivity;
import com.pushtech.sdk.PushSetup;
import com.pushtech.sdk.chat.manager.UserManager;

import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends Activity implements PushSetup.SetupCompleteListener,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private static final String TAG = SplashActivity.class.getName();

    private static final Handler splashTimeOutHandler = new Handler();
    public static final int SPLASH_TIMEOUT = 2000;
    private long openDate = 0;
    private PushSetup pushSetup;
    private EditText appIdEt, appSecretEt, senderIdEt;
    private View registerButton;
    private String appId;
    private String secretId;
    private String sender_id;
    private Spinner endPointSpinner;
    private int enviorement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preparePushSDK();

        if (UserManager.getInstance(this).isUserRegistered()) {
            startActivity(ChatListActivity.class);
            finish();
        } else {
            initViews();

        }
    }

    private void initViews() {
        setContentView(R.layout.activity_splash);
        appIdEt = (EditText) findViewById(R.id.app_id);
        appSecretEt = (EditText) findViewById(R.id.app_secret);
        senderIdEt = (EditText) findViewById(R.id.sender_id);
        registerButton = findViewById(R.id.register_app_button);
        endPointSpinner = (Spinner) findViewById(R.id.endpoint_url);

        registerButton.setOnClickListener(this);
        setSpinner();
    }

    private void setSpinner() {
        List<String> list = new ArrayList<String>();
        list.add("Production");
        list.add("Local");
        list.add("Pre");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, R.layout.view_spinner, list);
        endPointSpinner.setAdapter(dataAdapter);
        endPointSpinner.setOnItemSelectedListener(this);
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
    }

    private void startSetup() {

        Log.d(TAG, "Sender id: " + sender_id);
        openDate = System.currentTimeMillis();
        pushSetup.start(getApplicationContext(), enviorement,
                this, appId, secretId, sender_id);
    }

    @Override
    public void onSetupComplete(boolean success, String regId, String pushDeviceID) {
        Log.d(TAG, "ID that GCM uses for identify this app in this phone: " + pushDeviceID);
        Log.d(TAG, "ID that Pushtech uses for identify this app in this phone: " + regId);
        if (!success) {
            Toast.makeText(this, "Incorrect data", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        appId = appIdEt.getText().toString();
        sender_id = senderIdEt.getText().toString();
        secretId = appSecretEt.getText().toString();
        if (!TextUtils.isEmpty(appId)
                && !TextUtils.isEmpty(sender_id)
                && !TextUtils.isEmpty(secretId)) {
            startSetup();
        } else {
            Toast.makeText(this, "The field should not be empty", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        enviorement = position * (-1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
