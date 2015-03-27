package io.dwak.holohackernews.app.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

/**
 * Created by vishnu on 11/2/14.
 */
public class UserPreferenceManager {

    public static final String SHOULD_USE_EXTERNAL_BROWSER = "pref_system_browser";
    public static final String PREF_LINK_FIRST = "pref_link_first";
    public static final String PREF_LIST_ANIMATIONS = "pref_list_animations";
    public static final String PREF_NIGHT_MODE = "pref_night_mode";
    public static final String PREF_TEXT_SIZE = "pref_text_size";

    public static void useExternalBrowser(@NonNull final Context context, final boolean shouldUseSystemBrowser){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(SHOULD_USE_EXTERNAL_BROWSER, shouldUseSystemBrowser).apply();
    }

    public static boolean showLinkFirst(@NonNull final Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_LINK_FIRST, false);
    }

    public static String preferredTextSize(@NonNull final Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_TEXT_SIZE, "small");
    }

    public static boolean isExternalBrowserEnabled(@NonNull final Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(SHOULD_USE_EXTERNAL_BROWSER, false);
    }

    public static void registerOnSharedPreferenceChangeListener(final Context context,
                                                                SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.registerOnSharedPreferenceChangeListener(listener);
    }

    public static void unregisterOnSharedPreferenceChangeListener(final Context context,
                                                                  SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public static boolean isNightModeEnabled(@NonNull final Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_NIGHT_MODE, false);
    }
}
