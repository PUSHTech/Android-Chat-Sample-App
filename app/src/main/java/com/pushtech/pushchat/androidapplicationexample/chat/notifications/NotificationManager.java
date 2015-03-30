package com.pushtech.pushchat.androidapplicationexample.chat.notifications;

import android.content.Context;

import com.pushtech.sdk.Chat;
import com.pushtech.sdk.ChatMessage;
import com.pushtech.sdk.CommunicationServiceCallbacks;
import com.pushtech.sdk.GroupChat;
import com.pushtech.sdk.PushtechApp;

/**
 *
 */
public class NotificationManager implements CommunicationServiceCallbacks {

    private volatile static NotificationManager notificationManagerHelper;
    private String chatJid;
    private TypingEventListener listenerTypingEvents;
    private OnChatEvents listenerChatEvents;
    private OnGroupEvents listenerGroupEvents;
    private Context context;

    private TypeOfActivity typeOfActivity;


    @Override
    public void newChatMessage(ChatMessage message) {
        if (typeOfActivity == null) {
            NotificationUtils.generateNewMessageNotification("messageReceived",
                    message.getFrom().hashCode(),
                    context, message);
        } else {
            switch (typeOfActivity) {
                case CHAT:
                    if (!message.getFrom().equals(chatJid)) {
                        NotificationUtils.generateNewMessageNotification(
                                "messageReceived", message.getFrom().hashCode(),
                                context, message);
                    }
                    break;
                case CHAT_LIST:
                    break;
                case SHOW_IMAGE:
                case CONTACT_LIST:
                case MESSAGE_CENTER:
                case SETTINGS:
                    NotificationUtils.generateNewMessageNotification("messageReceived",
                            message.getFrom().hashCode(),
                            context, message);
                    break;

            }
        }

    }

    @Override
    public void startTyping(String userJid) {
        if (chatJid != null) {
            if (userJid.equals(chatJid)) {
                listenerTypingEvents.contactIsTyping(userJid);
            }
        }

    }

    @Override
    public void stopTyping(String userJid) {
        if (chatJid != null) {
            if (userJid.equals(chatJid)) {
                listenerTypingEvents.contactStoppedTyping(userJid);
            }
        }
    }

    @Override
    public void onInviteAGroup(GroupChat chat) {

    }

    @Override
    public void onJoinGroup(GroupChat chat) {

    }

    @Override
    public void onLeaveGroup(GroupChat chat) {

    }

    public enum TypeOfActivity {
        CHAT_LIST, CONTACT_LIST, CHAT,
        MESSAGE_CENTER, SETTINGS, SHOW_IMAGE
    }

    private NotificationManager(Context context) {
        this.context = context;
        PushtechApp.with(context).getBaseManager().getCommunicationService().setListener(this);
    }


    public static NotificationManager with(Context context) {
        NotificationManager notificationManager = notificationManagerHelper;
        if (notificationManager == null) {
            synchronized (NotificationManager.class) {
                notificationManager = notificationManagerHelper;
                if (notificationManager == null) {
                    notificationManagerHelper = new NotificationManager(context);
                    notificationManager = notificationManagerHelper;
                }
            }
        }
        return notificationManager;
    }

    public interface TypingEventListener {
        void contactIsTyping(String userJid);

        void contactStoppedTyping(String userJid);
    }

    public interface OnChatEvents {
        void onNewChatCreated(Chat chat);

        void onDeleteChat(Chat chat);
    }

    public interface OnGroupEvents {
        void onNewGroupChatCreated(Chat chat);
    }

    public void setTypeOfActivity(TypeOfActivity type) {
        this.typeOfActivity = type;
        if (typeOfActivity.equals(TypeOfActivity.CHAT_LIST)) {
            NotificationUtils.remvoeAllNotifications(context);
        }
    }

    public void setListenerChatEvents(OnChatEvents listenerChatEvents) {
        this.listenerChatEvents = listenerChatEvents;
    }

    public void removeListenerChatEvents() {
        this.listenerChatEvents = null;
    }

    public void setListenerGroupEvents(OnGroupEvents groupEvents) {
        this.listenerGroupEvents = groupEvents;
    }

    public void removeListenerGroupEvents() {
        this.listenerGroupEvents = null;
    }

    public void resetTypeOfActivity() {
        this.typeOfActivity = null;
    }

    public void setActiveChat(String chatJid, TypingEventListener listener) {
        NotificationUtils.removeNotification(context, chatJid.hashCode());
        this.chatJid = chatJid;
        this.listenerTypingEvents = listener;
    }

    public void removeActiveChat() {
        this.chatJid = null;
    }

   /* @Override
    public void newChat(Chat chat) {
        if (chat.isGroupChat()) {
            if (this.listenerGroupEvents != null) {
                this.listenerGroupEvents.onNewGroupChatCreated(chat);
            } else {
                NotificationUtils.generateGroupInviteNotification("newChat",
                        //TODO add name;
                        chat.getJid().hashCode(), context, chat.getJid());
            }
        }
        if (this.listenerChatEvents != null) {
            this.listenerChatEvents.onNewChatCreated(chat);
        }

    }*/

   /* @Override
    public void deletedChat(Chat chat) {
        if (chat.isGroupChat()) {
            NotificationUtils.generateGroupDeletedNotification("deletedChat",
                    //TODO add chat name;
                    chat.getJid().hashCode(), context, chat.getName());
        }
        if (this.listenerChatEvents != null) {
            this.listenerChatEvents.onDeleteChat(chat);
        }
    }*/

    /*@Override
    public void friendHasJoinedGroup(User user, GroupChat groupChat) {

    }

    @Override
    public void friendHasLeftGroup(User user, String groupChatName) {

    }*/

    /*@Override
    public void messageReceived(MessageManager.TextMessage chatMessage) {
        if (typeOfActivity == null) {
            NotificationUtils.generateNewMessageNotification("messageReceived",
                    chatMessage.getChatJid().hashCode(),
                    context, chatMessage);
        } else {
            switch (typeOfActivity) {
                case chat:
                    if (!chatMessage.getChatJid().equals(chatJid)) {
                        NotificationUtils.generateNewMessageNotification(
                                "messageReceived", chatMessage.getChatJid().hashCode(),
                                context, chatMessage);
                    }
                    break;
                case CHAT_LIST:
                    break;
                case SHOW_IMAGE:
                case CONTACT_LIST:
                case MESSAGE_CENTER:
                case SETTINGS:
                    NotificationUtils.generateNewMessageNotification("messageReceived",
                            chatMessage.getChatJid().hashCode(),
                            context, chatMessage);
                    break;

            }
        }
    }

    @Override
    public void messageSent(ChatMessage chatMessage) {

    }

    @Override
    public void sendingMessageError(SendMessageException exception) {

    }*/


}
