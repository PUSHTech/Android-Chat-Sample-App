package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.pushtech.pushchat.androidapplicationexample.R;
/**
 * Created by goda87 on 29/08/14.
 */
public class HeaderChatListView extends LinearLayout {
    private View progress;
    private View container;
    private View headerText;
    private Context context;
    private OnHeaderChatClicked headerChatClicked;

    public HeaderChatListView(Context context, OnHeaderChatClicked headerChatClicked) {
        super(context);
        this.context = context;
        this.headerChatClicked = headerChatClicked;
        initViews();
        setListeners();
    }
    private void initViews() {
        LayoutInflater.from(context).inflate(R.layout.view_chat_header,this,true);
        headerText = findViewById(R.id.view_chat_header_text);
        progress = findViewById(R.id.view_chat_header_progress);
        container = findViewById(R.id.view_chat_header_container);
    }
    private void setListeners() {
        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                headerText.setVisibility(View.GONE);
                headerChatClicked.onHeaderChatClicked();
            }
        });
    }
    public void hideHeader(){
        container.setVisibility(View.GONE);
    }
    public void showHeader() {
        container.setVisibility(View.VISIBLE);
    }
    public void finishRequest(){
        progress.setVisibility(View.GONE);
        headerText.setVisibility(View.VISIBLE);
    }
    public interface OnHeaderChatClicked{
        void onHeaderChatClicked();
    }
}
