/*
 * LatestActivity.java
 * © 2011 nuboat.net. All rights reserved
 */
package net.nuboat.petboat;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

/**
 *
 * @author  Peerapat Asoktummarungsri [nuboat@gmail.com]
 */
public class LatestActivity extends ListActivity {

    private static final String TAG = "LatestActivity";

    private String [] listPhotos = new String[] {"", "", "", ""};

	private AdView adView;
    private Activity context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
		context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.show);

		AdRequest adRequest = new AdRequest();
		//adRequest.addTestDevice(AdRequest.TEST_EMULATOR);

        adView = new AdView(this, AdSize.BANNER, "a14f64b66fd1372");
		adView.loadAd(adRequest);
        ((RelativeLayout)findViewById(R.id.layout_showads)).addView(adView);

        TextView header = (TextView) this.findViewById(R.id.show_header);
        header.setText( Cache.episode.getName() );

        ((Button) findViewById(R.id.btn_arrow)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.btn_arrow)).setOnClickListener(
            new OnClickListener() {
                @Override public void onClick(View v) {
                    Cache.episodeno = Cache.episode.getNo();

                    Intent intent = new Intent(context, EpisodeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            } );

        setListAdapter(new ShowAdapter());
    }

	@Override
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}

		super.onDestroy();
	}

    private class ShowAdapter extends ArrayAdapter<String> {

        public ShowAdapter() {
            super(LatestActivity.this, R.layout.show_row, R.id.show_figure, listPhotos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);

            Bitmap bit = Cache.figure[position];

            ImageView img = (ImageView) row.findViewById(R.id.show_photos);
            img.setImageBitmap(bit);

            DisplayMetrics dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);

            int width = dm.widthPixels;
            int height = width * bit.getHeight() / bit.getWidth(); //mainImage is the Bitmap I'm drawing
            img.setMinimumWidth(width);
            img.setMinimumHeight(height);

            return row;
        }
    }

}
