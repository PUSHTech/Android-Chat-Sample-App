package com.pushtech.pushchat.androidapplicationexample.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by goda87 on 2/09/14.
 */
public class MapLauncher {

    public static void launchMap(Double latitude, Double longitude, Context context) {
        String lat = Double.toString(latitude);
        String lon = Double.toString(longitude);
        launchMap(lat, lon, context);
    }

    public static void launchMap(String latitude, String longitude, Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("geo:0,0?q=");
        stringBuilder.append(latitude);
        stringBuilder.append(",");
        stringBuilder.append(longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(stringBuilder.toString()));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        try {
            context.startActivity(intent);

        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(stringBuilder.toString()));
                context.startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Toast.makeText(context,
                        "This intent is not supported in this device", Toast.LENGTH_SHORT).show();
                innerEx.printStackTrace();
            }
        }
    }
}
