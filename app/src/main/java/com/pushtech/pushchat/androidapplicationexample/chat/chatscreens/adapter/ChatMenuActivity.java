package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter;

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
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.chat.manager.ChatsManager;
import com.pushtech.sdk.chat.manager.MessagingManager;
import com.pushtech.sdk.chat.model.Chat;

import java.io.File;

import static android.provider.MediaStore.MediaColumns.DATA;

/**
 * Created by goda87 on 2/09/14.
 */
public class ChatMenuActivity extends ActionBarActivity
    implements LocationListener {

    private static final int REQUEST_CODE_PICTURE_GALLERY = 1001;
    private static final int REQUEST_CODE_VIDEO_GALLERY = 1002;
    private static final int REQUEST_CODE_CONTACT = 1003;
    private static final int REQUEST_CODE_PICTURE_CAMERA = 1004;
    private static final int REQUEST_CODE_VIDEO_CAMERA = 1005;

    protected Chat currentChat;
    private Location location;

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
        startActivityForResult(intent, REQUEST_CODE_PICTURE_CAMERA);
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
        //todo
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
                    MessagingManager.PictureMessage ms = MessagingManager.getInstance(getApplicationContext())
                            .newPictureMessage(currentChat.getJid());
                            ms.setPicture(getFileFromUri(data.getData())).send();
                    break;
                case REQUEST_CODE_VIDEO_CAMERA:
                    MessagingManager.getInstance(getApplicationContext())
                            .newVideoMessage(currentChat.getJid())
                            .setVideo(getFileFromUri(data.getData())).send();
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
                        Toast.makeText(this, "Video too large", Toast.LENGTH_SHORT).show();
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
        Log.d("GODA", "photo uri: " + uri);

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
