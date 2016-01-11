package io.dwak.holohackernews.app.ui.browser;

/**
 * Created by kounalem on 1/10/2016.
 */

import android.support.customtabs.CustomTabsClient;

/**
 * Callback for events when connecting and disconnecting from Custom Tabs Service.
 */
public interface ServiceConnectionCallback {

    void onServiceConnected(CustomTabsClient client);

    void onServiceDisconnected();
}
