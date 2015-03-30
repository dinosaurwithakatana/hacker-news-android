package io.dwak.holohackernews.app.util;

import android.support.annotation.NonNull;
import android.util.Log;

import io.dwak.holohackernews.app.HoloHackerNewsApplication;

/**
 * Created by vishnu on 3/30/15.
 */
public class HNLog {
    public static void d(@NonNull String tag, @NonNull String message){
        if(HoloHackerNewsApplication.isDebug()){
            Log.d(tag, message);
        }
    }

    public static void e(@NonNull String tag, @NonNull String message){
        if(HoloHackerNewsApplication.isDebug()){
            Log.e(tag, message);
        }
    }

    public static void i(@NonNull String tag, @NonNull String message){
        if(HoloHackerNewsApplication.isDebug()){
            Log.i(tag, message);
        }
    }

    public static void w(@NonNull String tag, @NonNull String message){
        if(HoloHackerNewsApplication.isDebug()){
            Log.w(tag, message);
        }
    }

    public static void v(@NonNull String tag, @NonNull String message){
        if(HoloHackerNewsApplication.isDebug()){
            Log.v(tag, message);
        }
    }
}
