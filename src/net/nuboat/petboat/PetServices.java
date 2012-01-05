/*
 * PetServices.java
 * © 2011 nuboat.net. All rights reserved
 */
package net.nuboat.petboat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import java.util.Calendar;
import java.util.Date;
import net.nuboat.petboat.facade.AnalyticFacade;
import net.nuboat.petboat.facade.PetdoFacade;
import net.nuboat.petboat.facade.pojo.EpisodePojo;
import net.nuboat.petboat.helper.ImageLoader;

/**
 *
 * @author  Peerapat Asoktummarungsri
 * @email   nuboat@gmail.com
 * @twitter @nuboat
 */
public class PetServices extends Service {

    private static final String TAG = "PetServices";

    public static EpisodePojo episode = null;
    public static Bitmap []figure = new Bitmap[4];

    public Date lastload = null;

    private NotificationManager mNotificationManager;

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        BroadcastReceiver mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.i(TAG, "onStart");
        super.onStart(intent, startId);

        if (intent.getBooleanExtra("isScreenOn", false) == false)
            return;

        long now  = Calendar.getInstance().getTimeInMillis();
        long last = (lastload != null) ? lastload.getTime() : 0;
        long diff = (now-last)/1000;
        Log.i(TAG, "Time Diff  : " + diff + " seconds");
        Log.i(TAG, "Check time : " + intent.getBooleanExtra("isCheckTime", false));
        if (diff < 3600 && intent.getBooleanExtra("isCheckTime", false) == true) // 60minutes
            return;

        try {
            //int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
            EpisodePojo epi = PetdoFacade.findLatestestEpisode(this);

            int currentepisode = (episode != null) ? Integer.parseInt(episode.getNo()) : 0;
            int lastestepisode = Integer.parseInt(epi.getNo());
            if (lastestepisode > currentepisode) {
                String action = this.getString(R.string.evt_action_loaded);
                String label = epi.getNo() + "-" + epi.getName();
                new AnalyticFacade(this, action, label, 1).execute();

                saveEpisode(epi);
                notificationAlert(episode.getName());
            }

            PetWidget.updateScreen(this, episode.getName(), figure[0]);

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));

            String action = this.getString(R.string.evt_action_error);
            String label = ex.getMessage();
            new AnalyticFacade(this, action, label, 1).execute();

            PetWidget.updateScreen(this, label);
        } finally {
            lastload = Calendar.getInstance().getTime();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    private void saveEpisode(EpisodePojo epi) throws Exception {
        Bitmap []buffer = new Bitmap[4];
        buffer[0] = ImageLoader.getBitmapFromURL(epi.getUrl1()); Log.i(TAG, "Loaded URL1");
        buffer[1] = ImageLoader.getBitmapFromURL(epi.getUrl2()); Log.i(TAG, "Loaded URL2");
        buffer[2] = ImageLoader.getBitmapFromURL(epi.getUrl3()); Log.i(TAG, "Loaded URL3");
        buffer[3] = ImageLoader.getBitmapFromURL(epi.getUrl4()); Log.i(TAG, "Loaded URL4");

        episode = epi;
        figure = buffer;
    }

    private void notificationAlert(String contentTitle) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        int icon = R.drawable.icon;
        long when = System.currentTimeMillis();

        CharSequence tickerText   = "A new episode of Petdo is released.";

        Context context = getApplicationContext();
        //CharSequence contentTitle = "A new episode of Petdo is released.";
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification(icon, tickerText, when);
        notification.setLatestEventInfo(context, contentTitle, "", contentIntent);
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        mNotificationManager.notify(icon, notification);
    }

    public class ScreenReceiver extends BroadcastReceiver {

        private static final String TAG = "ScreenReceiver";

        private boolean isScreenOn = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive");
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
                isScreenOn = true;

            Intent i = new Intent(context, PetServices.class);
            i.putExtra("isScreenOn", isScreenOn);
            i.putExtra("isCheckTime", true);
            context.startService(i);
        }

    }

}
