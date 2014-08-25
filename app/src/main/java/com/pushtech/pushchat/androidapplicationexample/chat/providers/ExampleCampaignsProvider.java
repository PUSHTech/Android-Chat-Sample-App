package com.pushtech.pushchat.androidapplicationexample.chat.providers;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.provider.CampaignsProvider;

/**
 * Created by goda87 on 25/08/14.
 */
public class ExampleCampaignsProvider extends CampaignsProvider {
    @Override
    public final String getAuthority() {
        return getContext().getString(R.string.provider_campaigns_authority);
    }
}
