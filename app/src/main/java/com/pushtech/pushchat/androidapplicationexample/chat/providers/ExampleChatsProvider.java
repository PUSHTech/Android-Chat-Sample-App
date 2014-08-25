package com.pushtech.pushchat.androidapplicationexample.chat.providers;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.chat.provider.ChatsProvider;

/**
 * Created by goda87 on 25/08/14.
 */
public class ExampleChatsProvider extends ChatsProvider {
    @Override
    public final String getAuthority() {
        return getContext().getString(R.string.provider_chats_authority);
    }
}