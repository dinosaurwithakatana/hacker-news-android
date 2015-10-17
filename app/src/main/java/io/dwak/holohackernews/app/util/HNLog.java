package io.dwak.holohackernews.app.util;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.dwak.holohackernews.app.HackerNewsApplication;

/**
 * This class contains passthrough methods to {@link Log} that only print if debugging is enabled
 * on the {@link HackerNewsApplication} class
 */
public class HNLog {
    private static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");

    public static void d(@NonNull String message){
        if(HackerNewsApplication.isDebug()){
            Log.d(getTag(), message);
        }
    }

    public static void d(@NonNull String tag, @NonNull String message){
        if(HackerNewsApplication.isDebug()){
            Log.d(tag, message);
        }
    }

    public static void e(@NonNull String tag, @NonNull String message){
        if(HackerNewsApplication.isDebug()){
            Log.e(tag, message);
        }
    }

    public static void i(@NonNull String tag, @NonNull String message){
        if(HackerNewsApplication.isDebug()){
            Log.i(tag, message);
        }
    }

    public static void w(@NonNull String tag, @NonNull String message){
        if(HackerNewsApplication.isDebug()){
            Log.w(tag, message);
        }
    }

    public static void v(@NonNull String tag, @NonNull String message){
        if(HackerNewsApplication.isDebug()){
            Log.v(tag, message);
        }
    }

    private static String getTag(){
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace.length <= 2) {
            throw new IllegalStateException(
                    "Synthetic stacktrace didn't have enough elements: are you using proguard?");
        }
        return createStackElementTag(stackTrace[2]);
    }

    protected static String createStackElementTag(StackTraceElement element) {
        String tag = element.getClassName();
        Matcher m = ANONYMOUS_CLASS.matcher(tag);
        if (m.find()) {
            tag = m.replaceAll("");
        }
        return tag.substring(tag.lastIndexOf('.') + 1);
    }
}
