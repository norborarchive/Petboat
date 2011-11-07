/*
 * EmergencyActivity.java
 * © 2011 nuboat.net. All rights reserved
 */
package net.nuboat.petboat;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import net.nuboat.petboat.facade.AnalyticFacade;
import net.nuboat.petboat.helper.InformationHelper;


/**
 *
 * @author  Peerapat Asoktummarungsri, nuboat@gmail.com
 * @since   October 24,2011
 */
public class ShowActivity extends ListActivity {

    private static final String TAG = "ShowActivity";

    private String [] listPhotos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show);
        setListAdapter(new ShowAdapter());

        String action = this.getString(R.string.evt_action_open);
        String label = InformationHelper.getPhonedetail(this);
        new AnalyticFacade(this, action, label, 1).execute();
    }

    private class ShowAdapter extends ArrayAdapter<String> {

        public ShowAdapter() {
            super(ShowActivity.this, R.layout.show_row, R.id.show_figure, listPhotos);
        }

    }

}
