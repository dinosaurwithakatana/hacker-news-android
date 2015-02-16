package io.dwak.holohackernews.app.util;

import android.content.Context;

/**
 * Created by vishnu on 9/28/14.
 */
public class UIUtils {
    public static int dpAsPixels(Context context, int densityPixels){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (densityPixels * scale + 0.5f);
    }

}
