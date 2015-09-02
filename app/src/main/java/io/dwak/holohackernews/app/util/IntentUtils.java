package io.dwak.holohackernews.app.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public final class IntentUtils {
    public static void openInBrowser(final Context context, final String uri){
        Intent browserIntent = new Intent();
        browserIntent.setAction(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(uri));
        context.startActivity(browserIntent);
    }

    public static void share(final Context context, final String text){
        share(context, text, "text/plain");
    }

    public static void share(final Context context, final String text, final String type){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType(type);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(shareIntent);
    }
}
