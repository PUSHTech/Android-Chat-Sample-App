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
import android.widget.Toast;

import com.pushtech.pushchat.androidapplicationexample.R;

import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.ChatCursorAdapter;
import com.pushtech.pushchat.androidapplicationexample.chat.chatscreens.adapter.HeaderChatListView;
import com.pushtech.sdk.chat.db.agent.ChatsDBAgent;
import com.pushtech.sdk.chat.manager.ChatsManager;
import com.pushtech.sdk.chat.manager.MessagingManager;
import com.pushtech.sdk.chat.model.Chat;
import com.pushtech.sdk.chat.model.message.ChatMessage;
import com.pushtech.sdk.chat.service.CommunicationService;

/**
 * A fragment representing a single Chat detail screen.
 * This fragment is either contained in a {@link ChatListActivity}
 * in two-pane mode (on tablets) or a {@link ChatDetailActivity}
 * on handsets.
 */
public class ChatDetailFragment extends Fragment
        implements View.OnClickListener,
        HeaderChatListView.OnHeaderChatClicked,
        ChatsManager.OnMessageHistoryReceiver,
        MessagingManager.OnMessagesRetrievedListener {
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

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChatDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            chat = ChatsManager.getInstance(getActivity().getApplicationContext())
                    .getChatWithId(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (chat != null) {
            retrieveMessagesFromChat(chat.getJid(), chat.isGroupChat());
        }
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
                    CommunicationService.sendUserStoppedTypingToUser(getActivity(), chat.getJid());
                } else {
                    sendButton.setEnabled(true);
                    CommunicationService.sendUserIsTypingToUser(getActivity(), chat.getJid());
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
        ChatMessage localmessage = ChatsDBAgent.getInstance(getActivity().getApplicationContext())
                .getFirstLocalMessage(chat.getJid());
        if (localmessage == null) {
            ChatsManager.getInstance(getActivity().getApplicationContext())
                    .getChatHistory(chat.getJid(), chat.isGroupChat());
            headerView.hideHeader();
        }
    }


    @Override
    public void onPause() {
        new Thread() {
            @Override
            public void run() {
                ChatsDBAgent.getInstance(getActivity()).flagMessagesAsReadFromChat(chat.getJid());
                ChatsDBAgent.getInstance(getActivity()).resetUnreadMessagesCounter(chat.getJid());
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

    private void retrieveMessagesFromChat(final String chatJid, final boolean isGroupChat) {
        MessagingManager.getInstance(getActivity()).getMessagesFromChat(chatJid, this);
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
                    return ChatsDBAgent.getInstance(getActivity())
                            .getChatFromJid(params[0]).getUnreadMessages();
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
            new Thread() {
                @Override
                public void run() {
                    ChatsDBAgent.getInstance(getActivity().getApplicationContext())
                            .flagMessagesAsReadFromChat(chat.getJid());
                    ChatsDBAgent.getInstance(getActivity().getApplicationContext())
                            .resetUnreadMessagesCounter(chat.getJid());
                }
            }.start();
        }
    }

    //OnMessagesRetrievedListener
    @Override
    public void gotMessagesFromChat(String s, Cursor cursor) {
        gotChatMessages(cursor, chat.isGroupChat());
    }

    //OnMessagesRetrievedListener.onError
    @Override
    public void onError(Exception e) {
        Toast.makeText(getActivity(),
                R.string.chat_detail_syncingMessages_warning_error,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHeaderChatClicked() {
        ChatMessage localMessage = ChatsDBAgent.getInstance(getActivity().getApplicationContext())
                .getFirstLocalMessage(chat.getJid());
        if (localMessage != null) {
            ChatsManager.getInstance(getActivity()).getChatHistory(chat.isGroupChat(),
                    chat.getJid(), NUM_OF_MESSAGE_HISTORY, localMessage.getMessageId(), this);
        }
    }
    // OnMessageHistoryReceiver
    @Override
    public void OnSuccessHistory(final int numMessages) {
        lv_chatMessages.post(new Runnable() {
            @Override
            public void run() {
                ListView listView = lv_chatMessages;
                listView.setSelection(numMessages);
            }
        });
        headerView.finishRequest();
        headerView.showHeader();
    }

    // OnMessageHistoryReceiver.onError
    @Override
    public void onError() {
        headerView.showHeader();
        headerView.finishRequest();
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
                MessagingManager.getInstance(getActivity())
                        .newTextMessage(chat.getJid())
                        .setText(tv.getText().toString())
                        .send();
                tv.setText("");
                break;
            default:
        }
    }
}
