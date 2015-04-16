package com.pushtech.pushchat.androidapplicationexample.chat.chatscreens;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.ChatCursorAdapter;
import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.HeaderChatListView;
import com.pushtech.sdk.Chat;
import com.pushtech.sdk.ChatManager;
import com.pushtech.sdk.HistoricCallback;
import com.pushtech.sdk.MessageManager;
import com.pushtech.sdk.PushtechApp;
import com.pushtech.sdk.PushtechError;
import com.pushtech.sdk.chatAndroidExample.R;


/**
 * A fragment representing a single Chat detail screen.
 * This fragment is either contained in a {@link ChatListActivity}
 * in two-pane mode (on tablets) or a {@link ChatDetailActivity}
 * on handsets.
 */
public class ChatDetailFragment extends Fragment
        implements View.OnClickListener,
        HeaderChatListView.OnHeaderChatClicked,
        HistoricCallback {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private static final int NUM_OF_MESSAGE_HISTORY = 25;

    /**
     * The chat wich this fragment is presenting.
     */
    private Chat chat;

    /**
     * The EditText where users writes its messages.
     */
    private EditText tv;
    /**
     * View that shows if the other user is typing.
     */
    private View tv_typing;
    private ListView lv_chatMessages;
    private HeaderChatListView headerView;
    private View sendButton;
    private boolean justEntered = true;
    private ChatManager chatManager;
    private MessageManager messageManager;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChatDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatManager = PushtechApp.with(getActivity()).getBaseManager().getChatManager();
        messageManager = PushtechApp.with(getActivity()).getBaseManager().getMessageManager();
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            chat = chatManager.getChatByJid(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_detail, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sendButton = view.findViewById(R.id.b_sendMessage);
        sendButton.setOnClickListener(this);
        sendButton.setEnabled(false);
        headerView = new HeaderChatListView(getActivity(), this);
        headerView.showHeader();
        tv_typing = (TextView) view.findViewById(R.id.typing);
        tv = (EditText) view.findViewById(R.id.et_message);
        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    sendButton.setEnabled(false);
                    PushtechApp.with(getActivity()).getBaseManager().getCommunicationManager()
                            .stopTyping(chat.getJid());
                } else {
                    sendButton.setEnabled(true);
                    PushtechApp.with(getActivity()).getBaseManager().getCommunicationManager()
                            .startTyping(chat.getJid());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        lv_chatMessages = (ListView) view.findViewById(R.id.lv_chatMessages);
    }

    @Override
    public void onResume() {
        super.onResume();
        justEntered = true;
        Cursor cursor = messageManager.getMessagesFromChat(chat.getJid());
        gotChatMessages(cursor, chat.isGroupChat());
        if (!cursor.moveToFirst()) {
            messageManager.getHistoricMessages(chat.getJid(), chat.isGroupChat(), this);
            headerView.hideHeader();
        } else {
            headerView.showHeader();
        }
    }


    @Override
    public void onPause() {
        new Thread() {
            @Override
            public void run() {
                messageManager.markAllMessageRead(chat.getJid());
                chatManager.resetUnreadMessages(chat.getJid());
            }
        }.start();
        super.onPause();
    }

    public void typing(boolean value) {
        if (value) {
            tv_typing.setVisibility(View.VISIBLE);
        } else {
            tv_typing.setVisibility(View.GONE);
        }
    }


    protected void gotChatMessages(final Cursor chatMessagesCursor, boolean isGroupChat) {
        final ChatCursorAdapter adapter
                = new ChatCursorAdapter(getActivity(), chatMessagesCursor, isGroupChat);
        lv_chatMessages.addHeaderView(headerView);
        lv_chatMessages.setAdapter(adapter);
        if (justEntered) {
            justEntered = false;
            new AsyncTask<String, Integer, Integer>() {

                @Override
                protected Integer doInBackground(String... params) {

                    int unread = chatManager.getUnreadMessages(chat.getJid());
                    messageManager.markAllMessageRead(chat.getJid());
                    return unread;
                }

                @Override
                protected void onPostExecute(final Integer unreadMessages) {
                    lv_chatMessages.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ListView listView = lv_chatMessages;
                            listView.setSelection(chatMessagesCursor.getCount() - unreadMessages);
                        }
                    }, 500);
                }
            }.execute(chat.getJid());

        } else {
            new ResetUnreadThread().start();
        }
    }


    @Override
    public void onHeaderChatClicked() {
        messageManager.getHistoricMessages(chat.getJid(), chat.isGroupChat(), this);
    }


    @Override
    public void onSuccessHistoric(final int numOfMessage) {
        lv_chatMessages.post(new Runnable() {
            @Override
            public void run() {
                ListView listView = lv_chatMessages;
                listView.setSelection(numOfMessage);
            }
        });
        headerView.finishRequest();
        headerView.showHeader();
        new ResetUnreadThread().start();
    }

    // OnMessageHistoryReceiver
    @Override
    public void onNoMoreMessages() {
        lv_chatMessages.removeHeaderView(headerView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_sendMessage:
                messageManager
                        .newTextMessage(chat.getJid())
                        .setText(tv.getText().toString())
                        .send();
                tv.setText("");
                break;
            default:
        }
    }

    @Override
    public void onError(PushtechError error) {
        headerView.showHeader();
        headerView.finishRequest();
        new ResetUnreadThread().start();
    }

    private class ResetUnreadThread extends Thread {
        @Override
        public void run() {
            messageManager.markAllMessageRead(chat.getJid());

        }
    }
}
