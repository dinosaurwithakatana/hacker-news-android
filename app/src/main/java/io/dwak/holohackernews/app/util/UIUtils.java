package io.dwak.holohackernews.app.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class UIUtils {
    public static int dpAsPixels(Context context, int densityPixels){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (densityPixels * scale + 0.5f);
    }

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
