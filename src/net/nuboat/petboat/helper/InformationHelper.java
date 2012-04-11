/*
 * PackageInfoHelper.java
 * © 2011 nuboat.net. All rights reserved
 */

package net.nuboat.petboat.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;


/**
 *
 * @author  Peerapat Asoktummarungsri [nuboat@gmail.com]
 */
public class InformationHelper {

    private static final String TAG = "PackageInfoHelper";

    private InformationHelper() { }

    public static String getVersion(Context context) throws Exception {
        PackageInfo pkgInfo = null;
        PackageManager pkgManager = null;

        pkgManager = context.getPackageManager();
        pkgInfo = pkgManager.getPackageInfo(context.getPackageName(), 0);

        return pkgInfo.versionName;
    }

    public static String getPhonedetail(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        return Build.MANUFACTURER + "," + Build.MODEL + "," + telephonyManager.getNetworkOperatorName();
    }

}
