/*
 * Petwidget.java
 * © 2011 nuboat.net. All rights reserved
 */
package net.nuboat.petboat;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

/**
 *
 * @author  Peerapat Asoktummarungsri
 * @email   nuboat@gmail.com
 * @twitter @nuboat
 */
public class PetWidget extends AppWidgetProvider {

    private static final String TAG = "PetWidget";

    public static final String BTN_READFULLSTORY = "READFULLSTORY";

    @Override
    public void onEnabled(Context context) {
        Log.i(TAG, "onEnabled");

        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i(TAG, "onUpdate");

        Intent i = new Intent(context, PetServices.class);
        i.putExtra("isScreenOn", true);
        context.startService(i);


        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.i(TAG, "onDeleted");

        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        Log.i(TAG, "onDisabled");

        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive with Action : " + intent.getAction());

        super.onReceive(context, intent);
    }

    public static void updateScreen(Context context, String name, Bitmap figure1) {
        Log.i(TAG, "updateScreen");
        RemoteViews rvs = new RemoteViews(context.getPackageName(), R.layout.petwidget);

        rvs.setViewVisibility(R.id.errorMessage, View.GONE);
        rvs.setViewVisibility(R.id.progressBar, View.GONE);

        rvs.setTextViewText(R.id.episodename, name);
        rvs.setImageViewBitmap(R.id.figure, figure1);

        updateWidget(context, rvs);
    }

    private static void registerReceive(Context context, RemoteViews rvs) {
        Log.i(TAG, "registerReceive");
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(BTN_READFULLSTORY);
        PendingIntent pintent = PendingIntent.getActivity(context, 0, intent, 0);

        rvs.setOnClickPendingIntent(R.id.btnRead, pintent);
    }

    private static void updateWidget(Context context, RemoteViews rvs) {
        registerReceive(context, rvs);
        //AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, rvs);
        ComponentName thisWidget = new ComponentName(context, PetWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, rvs);
    }

}
