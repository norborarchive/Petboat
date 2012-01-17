/*
 * ShowActivity.java
 * © 2011 nuboat.net. All rights reserved
 */
package net.nuboat.petboat;

import android.app.Activity;
import android.app.ListActivity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *
 * @author  Peerapat Asoktummarungsri
 * @email   nuboat@gmail.com
 * @twitter @nuboat
 */
public class ShowActivity extends ListActivity {

    private static final String TAG = "ShowActivity";

    private String [] listPhotos;

    private Activity context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.show);

        listPhotos = new String[] {"", "", "", ""};

        TextView header = (TextView) this.findViewById(R.id.show_header);
        header.setText( Cache.episode.getName() );

        context = this;
        setListAdapter(new ShowAdapter());
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");

        super.onPause();
        finish();
    }

    private class ShowAdapter extends ArrayAdapter<String> {

        public ShowAdapter() {
            super(ShowActivity.this, R.layout.show_row, R.id.show_figure, listPhotos);
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
