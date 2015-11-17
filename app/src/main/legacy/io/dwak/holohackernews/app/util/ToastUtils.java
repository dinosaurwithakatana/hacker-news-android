package io.dwak.holohackernews.app.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

public class ToastUtils {
    public static void showToast(@NonNull Context context, @StringRes int message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
