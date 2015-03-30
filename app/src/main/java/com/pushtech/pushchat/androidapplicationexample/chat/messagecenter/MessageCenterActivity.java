package com.pushtech.pushchat.androidapplicationexample.chat.messagecenter;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pushtech.pushchat.androidapplicationexample.chat.notifications.ChatCommunicationTrackerActivity;
import com.pushtech.pushchat.androidapplicationexample.chat.notifications.NotificationManager;
import com.pushtech.sdk.DeliveryManager;
import com.pushtech.sdk.PushDeliveriesContentValuesOps;
import com.pushtech.sdk.PushDelivery;
import com.pushtech.sdk.PushtechApp;
import com.pushtech.sdk.chatAndroidExample.R;

/**
 * Created by goda87 on 29/09/14.
 */
public class MessageCenterActivity extends ChatCommunicationTrackerActivity {

    private static final int CONTEXT_MENU_DELETE_INDEX = 1;
    private ListView listViewCampaigns;
    private Cursor cursorCampaings;
    private PushDeliveriesContentValuesOps converter = new PushDeliveriesContentValuesOps();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        listViewCampaigns = (ListView) findViewById(R.id.list_views_campaings);
        cursorCampaings = PushtechApp.with(this).getDeliveriesManager().getDeliveryCursor(this,
                DeliveryManager.DELIVERIES_TYPE.PLATFORM);
        listViewCampaigns.setAdapter(new CampaingsAdapter(this, cursorCampaings));
        listViewCampaigns.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursorCampaings.moveToPosition(position);
                PushDelivery pushDelivery = converter.buildFromCursor(cursorCampaings);
                String url = pushDelivery.getUrl();
                if (url != null && !TextUtils.isEmpty(url)) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
        });
        registerForContextMenu(listViewCampaigns);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(Menu.NONE, CONTEXT_MENU_DELETE_INDEX, Menu.NONE, R.string.label_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info
                = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case CONTEXT_MENU_DELETE_INDEX:
                deleteCampaign(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected NotificationManager.TypeOfActivity getTypeOfActivity() {
        return NotificationManager.TypeOfActivity.MESSAGE_CENTER;
    }

    private void deleteCampaign(int position) {
        cursorCampaings.moveToPosition(position);
        PushDelivery pushDelivery = converter.buildFromCursor(cursorCampaings);
        PushtechApp.with(this).getDeliveriesManager().remove(this, pushDelivery);
    }
}
