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

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.pushchat.androidapplicationexample.chat.contacts.ContactsActivity;
import com.pushtech.pushchat.androidapplicationexample.utils.ChatCommunicationTrackerActivity;
import com.pushtech.sdk.chat.manager.ChatsManager;
import com.pushtech.sdk.chat.manager.MessagingManager;
import com.pushtech.sdk.chat.model.Chat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.MediaColumns.DATA;

/**
 * Created by goda87 on 2/09/14.
 */
public class ChatMenuActivity extends ChatCommunicationTrackerActivity
    implements LocationListener {

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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (tmpFile != null) {
            String tmpFilePath = tmpFile.getAbsolutePath();
            savedInstanceState.putString(TMP_FILE_PATH, tmpFilePath);
        }
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
        CharSequence galleryOptions[] = new CharSequence[] {
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
            MessagingManager.getInstance(this).newLocationMessage(currentChat.getJid()).
                    setLocation(location.getLatitude(), location.getLongitude()).send();
        }
    }
    private void showGroupInfo() {
        //todo
    }
    private void addMemberToGroup() {
        Intent i = new Intent(this, ContactsActivity.class);
        i.putExtra(ContactsActivity.FRAGMENT_TYPE, ContactsActivity.ADD_MEMBER);
        i.putExtra(ContactsActivity.EXTRA_PARAM_GROUP_JID, currentChat.getJid());
        startActivity(i);
    }
    private void leaveGroup() {
        ChatsManager.getInstance(this).deleteChat(currentChat.getJid(), Chat.Type.GROUPCHAT);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PICTURE_CAMERA:
                    MessagingManager.PictureMessage ms
                            = MessagingManager.getInstance(getApplicationContext())
                                    .newPictureMessage(currentChat.getJid());
                    ms.setPicture(tmpFile);
                    ms.send();
                    break;
                case REQUEST_CODE_VIDEO_CAMERA:
                    MessagingManager.getInstance(getApplicationContext())
                            .newVideoMessage(currentChat.getJid())
                            .setVideoUri(data.getData()).send();
                    break;
                case REQUEST_CODE_PICTURE_GALLERY:
                    MessagingManager.getInstance(getApplicationContext())
                            .newPictureMessage(currentChat.getJid())
                            .setPicture(getFileFromContentURI(data.getData())).send();
                    break;
                case REQUEST_CODE_VIDEO_GALLERY:
                    File video = getFileFromContentURI(data.getData());
                    if (video != null && video.length() < 18 * 1024 * 1024) {
                        MessagingManager.getInstance(getApplicationContext())
                                .newVideoMessage(currentChat.getJid())
                                .setVideo(video).send();
                    } else {
                        showToast("Video too large");
                    }
                    break;
                case REQUEST_CODE_CONTACT:
                    MessagingManager.getInstance(this)
                            .newContactMessage(currentChat.getJid())
                            .setVCardUri(data.getData()).send();
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

}
