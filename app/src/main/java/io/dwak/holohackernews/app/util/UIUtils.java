package io.dwak.holohackernews.app.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import io.dwak.holohackernews.app.preferences.UserPreferenceManager;

/**
 * Created by vishnu on 9/28/14.
 */
public class UIUtils {
    public static int dpAsPixels(Context context, int densityPixels){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (densityPixels * scale + 0.5f);
    }

    public static void setTextColor(@NonNull Context context, TextView... textViews){
        final boolean nightModeEnabled = UserPreferenceManager.isNightModeEnabled(context);
        for (TextView textView : textViews) {
            textView.setTextColor(nightModeEnabled ? context.getResources().getColor(android.R.color.white) : context.getResources().getColor(android.R.color.black));
        }
    }
}
