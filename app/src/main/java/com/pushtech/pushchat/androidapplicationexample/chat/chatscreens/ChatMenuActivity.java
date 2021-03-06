package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.pushtech.pushchat.androidapplicationexample.chat.contacts.ContactsActivity;
import com.pushtech.pushchat.androidapplicationexample.chat.notifications.ChatCommunicationTrackerActivity;
import com.pushtech.pushchat.androidapplicationexample.chat.notifications.NotificationManager;
import com.pushtech.sdk.Callbacks.GenericCallback;
import com.pushtech.sdk.Chat;
import com.pushtech.sdk.ChatManager;
import com.pushtech.sdk.FileError;
import com.pushtech.sdk.MessageManager;
import com.pushtech.sdk.PushtechApp;
import com.pushtech.sdk.PushtechError;
import com.pushtech.sdk.chatAndroidExample.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.MediaColumns.DATA;

/**
 * Created by goda87 on 2/09/14.
 */
public abstract class ChatMenuActivity extends ChatCommunicationTrackerActivity
        implements LocationListener, NotificationManager.TypingEventListener {

    private static final int REQUEST_CODE_PICTURE_GALLERY = 1001;
    private static final int REQUEST_CODE_VIDEO_GALLERY = 1002;
    private static final int REQUEST_CODE_CONTACT = 1003;
    private static final int REQUEST_CODE_PICTURE_CAMERA = 1004;
    private static final int REQUEST_CODE_VIDEO_CAMERA = 1005;

    private static final String PICTURES_FOLDER = "/PUSHTECH";
    private static final String TMP_FILE_PATH = "tmpFile";

    protected Chat currentChat;
    private Location location;
    private File tmpFile;
    private MessageManager messageManager;
    private ChatManager chatManager;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        if (tmpFile != null) {
            String tmpFilePath = tmpFile.getAbsolutePath();
            savedInstanceState.putString(TMP_FILE_PATH, tmpFilePath);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageManager = PushtechApp.with(this).getBaseManager().getMessageManager();
        chatManager = PushtechApp.with(this).getBaseManager().getChatManager();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String tmpFilePath = savedInstanceState.getString(TMP_FILE_PATH);
        if (!TextUtils.isEmpty(tmpFilePath)) {
            tmpFile = new File(tmpFilePath);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        prepareLocationListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        activeOrDisableMenuItems(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    private void activeOrDisableMenuItems(Menu menu) {
        MenuItem groupActionsMenuItem = menu.findItem(R.id.group_overflow_menu);
        MenuItem attachMenuItem = menu.findItem(R.id.attach_menu);
        if (groupActionsMenuItem != null && attachMenuItem != null) {
            if (currentChat != null) {
                attachMenuItem.setVisible(true);
                if (currentChat.isGroupChat()) {
                    groupActionsMenuItem.setVisible(true);
                } else {
                    groupActionsMenuItem.setVisible(false);
                }
            } else {
                groupActionsMenuItem.setVisible(false);
                attachMenuItem.setVisible(false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_photo_camera:
                takePictureFromCamera();
                break;
            case R.id.menu_video_camera:
                takeVideoFromCamera();
                break;
            case R.id.menu_gallery:
                showGalleryOptions();
                break;
            case R.id.menu_contact:
                sendContact();
                break;
            case R.id.menu_location:
                sendLocation();
                break;
            case R.id.menu_group_info:
                showGroupInfo();
                break;
            case R.id.menu_invite_member:
                addMemberToGroup();
                break;
            case R.id.menu_leave_group:
                leaveGroup();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void takePictureFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        tmpFile = createTmpImageFile();
        if (tmpFile != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(tmpFile));
            startActivityForResult(intent, REQUEST_CODE_PICTURE_CAMERA);
        }
    }

    private void takeVideoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 18 * 1024 * 1024); //18Mb
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        startActivityForResult(intent, REQUEST_CODE_VIDEO_CAMERA);
    }

    private void showGalleryOptions() {
        CharSequence galleryOptions[] = new CharSequence[]{
                getString(R.string.menu_picture_from_gallery),
                getString(R.string.menu_video_from_gallery)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(galleryOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    selectPictureFromGallery();
                } else {
                    selectVideoFromGallery();
                }
            }
        });
        builder.show();
    }

    private void selectPictureFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_CODE_PICTURE_GALLERY);
    }

    private void selectVideoFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_CODE_VIDEO_GALLERY);
    }

    private void sendContact() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_CONTACT);
    }

    private void sendLocation() {
        if (location != null) {
            messageManager.newLocationMessage(currentChat.getJid())
                    .setLongitude(location.getLongitude())
                    .setLatitude(location.getLatitude())
                    .send();
        }
    }

    private void showGroupInfo() {
        Intent i = new Intent(this, ContactsActivity.class);
        i.putExtra(ContactsActivity.FRAGMENT_TYPE, ContactsActivity.GROUP_INFO);
        i.putExtra(ContactsActivity.EXTRA_PARAM_GROUP_JID, currentChat.getJid());
        startActivity(i);
    }

    private void addMemberToGroup() {
        Intent i = new Intent(this, ContactsActivity.class);
        i.putExtra(ContactsActivity.FRAGMENT_TYPE, ContactsActivity.ADD_MEMBER);
        i.putExtra(ContactsActivity.EXTRA_PARAM_GROUP_JID, currentChat.getJid());
        startActivity(i);
    }

    private void leaveGroup() {
        chatManager.deleteChat(currentChat, new GenericCallback() {
            @Override
            public void onSuccess() {
                finish();
            }

            @Override
            public void onError(PushtechError error) {
                //TODO show error
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PICTURE_CAMERA:
                    try {
                        messageManager.newPictureMessage(currentChat.getJid())
                                .setImage(tmpFile)
                                .send();
                    } catch (FileError fileError) {
                        showToast(fileError.getMessage());
                    }
                    break;
                case REQUEST_CODE_VIDEO_CAMERA:
                    try {
                        messageManager.newVideoMessage(currentChat.getJid())
                                .setVideo(data.getData())
                                .send();
                    } catch (FileError fileError) {
                        showToast(fileError.getMessage());
                    }
                    break;
                case REQUEST_CODE_PICTURE_GALLERY:
                    try {
                        messageManager.newPictureMessage(currentChat.getJid())
                                .setImage(getFileFromContentURI(data.getData())).send();
                    } catch (FileError fileError) {
                        showToast(fileError.getMessage());
                    }

                    break;
                case REQUEST_CODE_VIDEO_GALLERY:
                    File video = getFileFromContentURI(data.getData());
                    try {
                        messageManager.newVideoMessage(currentChat.getJid())
                                .setVideo(video)
                                .send();
                    } catch (FileError fileError) {
                        showToast(fileError.getMessage());
                    }

                    break;
                case REQUEST_CODE_CONTACT:
                    try {
                        PushtechApp.with(this).getBaseManager().getMessageManager()
                                .newContactMessage(currentChat.getJid())
                                .setVCard(data.getData()).send();
                    } catch (FileError fileError) {
                        fileError.printStackTrace();
                    }
                    break;
                default:

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private File createTmpImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM).getAbsolutePath() + PICTURES_FOLDER;
        File storageDir = new File(path);
        storageDir.mkdirs();
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private File getFileFromContentURI(Uri contentUri) {
        Cursor cursor = getContentResolver().query(
                contentUri,
                new String[]{DATA},
                null, null, null);
        cursor.moveToFirst();
        File file = new File(cursor.getString(cursor.getColumnIndex(DATA)));
        cursor.close();
        return file;
    }

    private File getFileFromUri(Uri uri) {
        return new File(uri.getPath());
    }

    private void prepareLocationListener() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        Location location = null;
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
        if (location != null) {
            onLocationChanged(location);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void contactIsTyping(String userJid) {
        ((ChatDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.chat_detail_container)).typing(true);
    }

    @Override
    public void contactStoppedTyping(String userJid) {
        ((ChatDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.chat_detail_container)).typing(false);

    }
}
