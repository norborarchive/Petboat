/*
 * AnalyticFacade.java
 * © 2011 nuboat.net. All rights reserved
 */
package net.nuboat.petboat.facade;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import net.nuboat.emergencyboard.R;
import net.nuboat.emergencyboard.helper.InformationHelper;

/**
 *
 * @author  Peerapat Asoktummarungsri, nuboat@gmail.com
 * @since   October 25,2011
 */
public class AnalyticFacade extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "AnalyticFacade";
    private static final GoogleAnalyticsTracker tracker = GoogleAnalyticsTracker.getInstance();

    private Context context;

    private String category;
    private String action;
    private String label;
    private int value;

    public AnalyticFacade(Context context, String action, String label, int value) {
        this.context = context;

        this.action = action;
        this.label = label;
        this.value = value;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            category = context.getString(R.string.app_name);
            Log.d(TAG, "Track category:" + category + " action:" + action + " label:" + label + " value:" + value);

            tracker.setProductVersion(
                     context.getString(R.string.app_name)
                    ,InformationHelper.getVersion(context));

            tracker.start(context.getString(R.string.evt_webid), context);

            tracker.trackEvent(category, action, label, value);

            tracker.dispatch();

        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));

        } finally {
            tracker.stop();
        }

        return null;
    }

}
