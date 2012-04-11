/*
 * EpisodeActivity.java
 * © 2011 nuboat.net. All rights reserved
 */
package net.nuboat.petboat;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.List;
import net.nuboat.petboat.facade.AnalyticFacade;
import net.nuboat.petboat.facade.PetdoFacade;
import net.nuboat.petboat.facade.pojo.EpisodePojo;
import net.nuboat.petboat.helper.ImageLoader;

/**
 *
 * @author  Peerapat Asoktummarungsri [nuboat@gmail.com]
 */
public class EpisodeActivity extends ListActivity {

    private static final String TAG = "EpisodeActivity";
    private static final int    MAXCOUNT = 25;

    private String getepisodeno;
    private String [] listEpisode;
    private List<EpisodePojo> list_episode;

    private Activity context;
    private ProgressDialog pd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.episode);

        context = this;

        ((Button) findViewById(R.id.btn_episodearrow)).setOnClickListener(
            new OnClickListener() {
                @Override public void onClick(View v) {
                    pd = ProgressDialog.show(context, null, null, true, false);
                    pd.setIcon(R.drawable.icon);
                    pd.setMessage("Loading ...");

                    new DownloadPetdoList().execute(context);
                }
            } );

        pd = ProgressDialog.show(context, null, null, true, false);
        pd.setIcon(R.drawable.icon);
        pd.setMessage("Loading ...");

        new DownloadPetdoList().execute(context);
    }

    @Override
    public void onListItemClick(ListView parent, View v, int position, long id) {
        pd = ProgressDialog.show(context, null, null, true, false);
        pd.setIcon(R.drawable.icon);
        pd.setMessage("Loading ...");

        getepisodeno = list_episode.get(position).getNo();

        new DownloadPetdo().execute(context);
    }

    private class ShowAdapter extends ArrayAdapter<String> {

        public ShowAdapter() {
            super(EpisodeActivity.this, R.layout.episode_row, R.id.episode_figure, listEpisode);
        }

    }

    private class DownloadPetdoList extends AsyncTask<Context, Integer, String> {
        private String result;
        private Context context;

        @Override
        protected String doInBackground(Context... params) {
            try {
                context = params[0];
                list_episode = PetdoFacade.findListEpisode(context, Cache.episodeno, MAXCOUNT);

                String action = context.getString(R.string.evt_action_loadedlist);
                String label = Cache.episodeno;
                new AnalyticFacade(context, action, label, 1).execute();

                EpisodePojo lastepisode = list_episode.get(list_episode.size()-1);
                Cache.episodeno = lastepisode.getNo();

                Log.i(TAG, "Number of List Episode : " + list_episode.size());
                listEpisode = new String[list_episode.size()-1];
                for(int i=0; i<(list_episode.size()-1); i++)
                    listEpisode[i] = list_episode.get(i).getNo() + " : " + list_episode.get(i).getName();

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

            pd.dismiss();

            if( "success".equals(result) ) {
                setListAdapter(new ShowAdapter());
            }


        }

    }

    private class DownloadPetdo extends AsyncTask<Context, Integer, String> {
        private String result;
        private Context context;

        @Override
        protected String doInBackground(Context... params) {
            try {
                context = params[0];
                EpisodePojo episode = PetdoFacade.findEpisode(context, getepisodeno);

                String action = context.getString(R.string.evt_action_loaded);
                String label = episode.getNo() + "-" + episode.getName();
                new AnalyticFacade(context, action, label, 1).execute();

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

            pd.dismiss();

            if( "success".equals(result) ) {
                Intent intent = new Intent(context, ShowActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

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
