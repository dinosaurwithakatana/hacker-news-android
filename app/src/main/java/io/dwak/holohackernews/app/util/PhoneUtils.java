package io.dwak.holohackernews.app.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by kounalem on 1/10/2016.
 */
public class PhoneUtils {

    public static boolean isAppInstalledOnDevice(Context context, String appPackageName)
    {
        PackageManager pm = context.getPackageManager();
        try{
            pm.getPackageInfo(appPackageName,PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    public static String getVersionNameOfAppInstalledOnDevice(Context context, String appPackageName)
    {
        PackageManager pm = context.getPackageManager();
        try{
            PackageInfo packageInfo = pm.getPackageInfo(appPackageName,PackageManager.GET_ACTIVITIES);
            return packageInfo.versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return "";
        }


    }
}
