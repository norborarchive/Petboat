/*
 * NetworkHelper.java
 * © 2011 nuboat.net. All rights reserved
 */
package net.nuboat.petboat.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

/**
 *
 * @author  Peerapat Asoktummarungsri
 * @email   nuboat@gmail.com
 * @twitter @nuboat
 */
public class NetworkHelper {

    private static final String TAG = "PetServices";

    private NetworkHelper() {}
    
    public static boolean isAvailable(Context context) {
        // Check any network information such as 3G, 2G, Wifi, Nonetwork
        NetworkInfo netInfo = (NetworkInfo) ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        Log.d(TAG, "State : "+ ((netInfo!=null)?netInfo.getState():""));
        if ((netInfo == null) || (!State.CONNECTED.equals(netInfo.getState()) ))
            return false;
        else
            return true;
    }
}
