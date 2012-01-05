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
import net.nuboat.petboat.facade.AnalyticFacade;
import net.nuboat.petboat.helper.InformationHelper;

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

        String action = context.getString(R.string.evt_action_activate_widget);
        String label = InformationHelper.getPhonedetail(context);
        new AnalyticFacade(context, action, label, 1).execute();

        Intent i = new Intent(context, PetServices.class);
        i.putExtra("isScreenOn", true);
        i.putExtra("isCheckTime", false);
        context.startService(i);

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.i(TAG, "onDeleted");

        String action = context.getString(R.string.evt_action_deactivate_widget);
        String label = InformationHelper.getPhonedetail(context);
        new AnalyticFacade(context, action, label, 1).execute();

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

    public static void updateScreen(Context context, String message) {
        Log.i(TAG, "updateScreen");
        RemoteViews rvs = new RemoteViews(context.getPackageName(), R.layout.petwidget);

        rvs.setTextViewText(R.id.errorMessage, message);
        rvs.setViewVisibility(R.id.errorMessage, View.VISIBLE);
        rvs.setViewVisibility(R.id.figure, View.GONE);

        updateWidget(context, rvs);
    }

    public static void updateScreen(Context context, String name, Bitmap figure1) {
        Log.i(TAG, "updateScreen");
        RemoteViews rvs = new RemoteViews(context.getPackageName(), R.layout.petwidget);

        rvs.setViewVisibility(R.id.errorMessage, View.GONE);
        //rvs.setViewVisibility(R.id.progressBar, View.GONE);
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
