package io.dwak.holohackernews.app.ui.browser;

/**
 * Created by kounalem on 1/10/2016.
 */

import android.content.Context;
import android.content.Intent;

/**
 * Helper class for Custom Tabs.
 */
public class CustomTabsHelper {
    private static final String TAG = "CustomTabsHelper";
    static final String STABLE_PACKAGE = "com.android.chrome";
    private static final String EXTRA_CUSTOM_TABS_KEEP_ALIVE =
            "android.support.customtabs.extra.KEEP_ALIVE";

    private CustomTabsHelper() {}

    public static void addKeepAliveExtra(Context context, Intent intent) {
        Intent keepAliveIntent = new Intent().setClassName(
                context.getPackageName(), KeepAliveService.class.getCanonicalName());
        intent.putExtra(EXTRA_CUSTOM_TABS_KEEP_ALIVE, keepAliveIntent);
    }

}