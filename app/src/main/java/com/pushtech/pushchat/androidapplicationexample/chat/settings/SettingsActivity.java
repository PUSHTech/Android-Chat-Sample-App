package com.pushtech.pushchat.androidapplicationexample.chat.settings;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pushtech.pushchat.androidapplicationexample.chat.SplashActivity;
import com.pushtech.pushchat.androidapplicationexample.chat.notifications.ChatCommunicationTrackerActivity;
import com.pushtech.pushchat.androidapplicationexample.chat.notifications.NotificationManager;
import com.pushtech.sdk.Callbacks.GenericCallback;
import com.pushtech.sdk.Callbacks.UserProfileCallback;
import com.pushtech.sdk.PushtechApp;
import com.pushtech.sdk.PushtechError;
import com.pushtech.sdk.User;
import com.pushtech.sdk.chatAndroidExample.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by goda87 on 29/09/14.
 */
public class SettingsActivity extends ChatCommunicationTrackerActivity
        implements View.OnClickListener,
        UserImageSettingsIntentView.OnProfilePhotoItemSelect,
        UserProfileCallback {

    private ImageView iv_userImage;
    private TextView tv_userName;
    private Button bt_logOut;
    private User user;
    private AlertDialog dialog;
    private ProgressDialog logoutDialog;
    private File photoFile;
    private static final int REQUEST_CODE_PICK_PHOTO = 1001;
    private static final int REQUEST_CODE_TAKE_PHOTO = 1004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        user = PushtechApp.with(this).getBaseManager().getUserManager().getMyUser();
        initViews();
        setViews();
        setListeners();
    }

    @Override
    protected NotificationManager.TypeOfActivity getTypeOfActivity() {
        return NotificationManager.TypeOfActivity.SETTINGS;
    }

    private void initViews() {
        iv_userImage = (ImageView) findViewById(R.id.activity_settings_user_image);
        tv_userName = (TextView) findViewById(R.id.activity_settings_user_name);
        bt_logOut = (Button) findViewById(R.id.activity_settings_log_out);
    }

    private void setViews() {
        tv_userName.setText(user.getName());
        Picasso.with(this).load(user.getAvatarPicture())
                .placeholder(R.drawable.user_picture)
                .error(R.drawable.user_picture)
                .resize(200, 200)
                .centerCrop()
                .into(iv_userImage);
    }

    private void setListeners() {
        tv_userName.setOnClickListener(this);
        iv_userImage.setOnClickListener(this);
        bt_logOut.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_settings_log_out:
                logoutDialog = ProgressDialog.show(this, "",
                        "Loading. Please wait...", true);
                PushtechApp.with(this).getBaseManager().getUserManager().logout(new GenericCallback() {
                    @Override
                    public void onSuccess() {
                        logoutSuccessful();
                    }

                    @Override
                    public void onError(PushtechError error) {
                        logoutError();
                    }
                });
                break;
            case R.id.activity_settings_user_image:
                showDialogUpdateUserImage();
                break;
            case R.id.activity_settings_user_name:
                showDialogUpdateUserName();
                break;

        }
    }

    private void showDialogUpdateUserName() {
        final UserInfoSettingsView view = new UserInfoSettingsView(this);
        final Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCustomTitle(new AlertDialogCustomTitleView(this, getString(R.string.user_info)));
        builder.setView(view);
        builder.setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateUserName(view.getText());

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void updateUserName(String text) {
        PushtechApp.with(this).getBaseManager().getUserManager().updateProfileName(text, this);

    }

    private void showDialogUpdateUserImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCustomTitle(new AlertDialogCustomTitleView(this, getString(R.string.profile_photo)));
        builder.setView(new UserImageSettingsIntentView(this, this));
        dialog = builder.create();
        dialog.show();
    }

    private File getFileFromURI(Uri contentUri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, filePathColumn, null, null, null);

        if (cursor == null) {
            return null;
        }

        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return new File(picturePath);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PICK_PHOTO:
                    photoFile = getFileFromURI(data.getData());
                case REQUEST_CODE_TAKE_PHOTO:
                    Picasso.with(this).load(photoFile)
                            .placeholder(R.drawable.user_picture)
                            .error(R.drawable.user_picture)
                            .resize(200, 200)
                            .centerCrop().into(iv_userImage);
                    PushtechApp.with(this).getBaseManager().getUserManager()
                            .updateProfileImage(photoFile, this);
                    break;
                default:

            }
        }
    }


    @Override
    public void onGallerySelected() {
        dialog.dismiss();
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_CODE_PICK_PHOTO);


    }

    @Override
    public void onTakePhotoSelected() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
            }
        }

        dialog.dismiss();
    }

    @Override
    public void onDeletePhotoSelected() {
        dialog.dismiss();
    }

    private void logoutSuccessful() {
        logoutDialog.dismiss();
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private void logoutError() {
        logoutDialog.dismiss();
    }

    private File createImageFile() throws IOException {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    @Override
    public void onUserProfileAvailable(User user) {
        this.user = user;
        setViews();
    }

    @Override
    public void onError(PushtechError error) {

    }
}