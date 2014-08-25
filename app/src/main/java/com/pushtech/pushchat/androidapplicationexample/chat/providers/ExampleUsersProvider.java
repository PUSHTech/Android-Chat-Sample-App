package com.pushtech.pushchat.androidapplicationexample.chat.providers;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.chat.provider.UsersProvider;

/**
 * Created by goda87 on 25/08/14.
 */
public class ExampleUsersProvider extends UsersProvider {
    @Override
    public final String getAuthority() {
        return getContext().getString(R.string.provider_users_authority);
    }
}