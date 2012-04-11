/*
 * MainActivity.java
 * © 2011 nuboat.net. All rights reserved
 */
package net.nuboat.petboat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import net.nuboat.petboat.facade.AnalyticFacade;
import net.nuboat.petboat.facade.PetdoFacade;
import net.nuboat.petboat.facade.pojo.EpisodePojo;
import net.nuboat.petboat.helper.ImageLoader;
import net.nuboat.petboat.helper.InformationHelper;

/**
 *
 * @author  Peerapat Asoktummarungsri [nuboat@gmail.com]
 */
public class MainActivity extends Activity {
    
    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        String action = this.getString(R.string.evt_action_open);
        String label = InformationHelper.getPhonedetail(this);
        new AnalyticFacade(this, action, label, 1).execute();

        new DownloadPetdo().execute(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    private class DownloadPetdo extends AsyncTask<Context, Integer, String> {
        private String result;
        private Context context;

        @Override
        protected String doInBackground(Context... params) {
            try {
                context = params[0];
                EpisodePojo episode = PetdoFacade.findLatestestEpisode(context);

                String action = context.getString(R.string.evt_action_loaded);
                String label = episode.getNo() + "-" + episode.getName();
                new AnalyticFacade(context, action, label, 1).execute();

                int currentepisode = (Cache.episode != null) ? Integer.parseInt(Cache.episode.getNo()) : 0;
                int lastestepisode = Integer.parseInt(episode.getNo());
                if (lastestepisode > currentepisode)
                    saveEpisode(episode);

                result = "success";
            } catch (Exception ex) {
                Log.e(TAG, Log.getStackTraceString(ex));

                String action = context.getString(R.string.evt_action_error);
                String label = ex.getMessage();
                new AnalyticFacade(context, action, label, 1).execute();

                result = "fail";
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if( "success".equals(result) ) {
                Intent intent = new Intent(context, LatestActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            finish();

        }

        private void saveEpisode(EpisodePojo epi) throws Exception {
            Bitmap []buffer = new Bitmap[4];
            buffer[0] = ImageLoader.getBitmapFromURL(epi.getUrl1(), epi.getNo(), "1"); Log.i(TAG, "Loaded URL1");
            buffer[1] = ImageLoader.getBitmapFromURL(epi.getUrl2(), epi.getNo(), "2"); Log.i(TAG, "Loaded URL2");
            buffer[2] = ImageLoader.getBitmapFromURL(epi.getUrl3(), epi.getNo(), "3"); Log.i(TAG, "Loaded URL3");
            buffer[3] = ImageLoader.getBitmapFromURL(epi.getUrl4(), epi.getNo(), "4"); Log.i(TAG, "Loaded URL4");

            Cache.episode = epi;
            Cache.figure = buffer;
        }

    }

}
