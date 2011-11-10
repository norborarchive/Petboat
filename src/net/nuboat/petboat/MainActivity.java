/*
 * MainActivity.java
 * © 2011 nuboat.net. All rights reserved
 */
package net.nuboat.petboat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
 * @author  Peerapat Asoktummarungsri
 * @email   nuboat@gmail.com
 * @twitter @nuboat
 */
public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private ProgressDialog pd;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        String action = this.getString(R.string.evt_action_open);
        String label = InformationHelper.getPhonedetail(this);
        new AnalyticFacade(this, action, label, 1).execute();

        pd = ProgressDialog.show(this, null, null, true, false);
        pd.setMessage("Loading from iampetdo.com");

        new DownloadPetdo().execute(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
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

                if ( Cache.episode == null )
                    saveEpisode(episode);
                else if ( !episode.getNo().equals( Cache.episode.getNo()) )
                    saveEpisode(episode);

            } catch (Exception ex) {
                Log.e(TAG, Log.getStackTraceString(ex));

                String action = context.getString(R.string.evt_action_error);
                String label = ex.getMessage();
                new AnalyticFacade(context, action, label, 1).execute();
            } finally {
                pd.dismiss();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Intent intent = new Intent(context, ShowActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            finish();
        }

        private void saveEpisode(EpisodePojo episode) {
            Cache.episode = episode;
            Cache.figure[0] = ImageLoader.getBitmapFromURL(Cache.episode.getUrl1());
            Cache.figure[1] = ImageLoader.getBitmapFromURL(Cache.episode.getUrl2());
            Cache.figure[2] = ImageLoader.getBitmapFromURL(Cache.episode.getUrl3());
            Cache.figure[3] = ImageLoader.getBitmapFromURL(Cache.episode.getUrl4());
        }

    }

}
