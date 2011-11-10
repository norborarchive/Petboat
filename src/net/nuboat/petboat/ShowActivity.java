/*
 * ShowActivity.java
 * © 2011 nuboat.net. All rights reserved
 */
package net.nuboat.petboat;

import android.app.ListActivity;
import android.os.Bundle;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show);

        listPhotos = new String[4];
        listPhotos[0] = "";
        listPhotos[1] = "";
        listPhotos[2] = "";
        listPhotos[3] = "";

        TextView header = (TextView) this.findViewById(R.id.show_header);
        header.setText( Cache.episode.getName() );

        setListAdapter(new ShowAdapter());
    }

    @Override
    public void onPause() {
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

            ImageView icon = (ImageView) row.findViewById(R.id.show_photos);
            icon.setImageBitmap( Cache.figure[position] );

            return row;
        }
    }

}
