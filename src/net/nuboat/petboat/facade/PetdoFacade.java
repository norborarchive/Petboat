/*
 * ElevationFacade.java
 * © 2011 nuboat.net. All rights reserved
 */
package net.nuboat.petboat.facade;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import net.nuboat.emergencyboard.helper.RestClient;
import net.nuboat.petboat.facade.pojo.EpisodePojo;
import net.nuboat.petboat.helper.RestClient;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author  Peerapat Asoktummarungsri, nuboat@gmail.com
 * @since   October 25,2011
 */
public class PetdoFacade {

    private static final String TAG = "PetdoFacade";

    private static final String API_PETBOAT_LATEST = "http://nuboat.net/petboat/latest/";

    public static EpisodePojo findLatestestEpisode(Context context)
            throws Exception {

        String url = getUrl(context);

        Log.d(TAG, "Call : " + url);
        RestClient restclient = new RestClient("PetdoFacade");

        JSONObject jsonobj = restclient.getJsonbyHttpGet(url);
        JSONObject latest = jsonobj.getJSONObject("results");

        EpisodePojo epi = new EpisodePojo();
        epi.setNo(latest.getString("no"));
        epi.setName(latest.getString("name"));
        epi.setCredit(latest.getString("storycredit"));
        epi.setUrl1(latest.getString("url1"));
        epi.setUrl2(latest.getString("url2"));
        epi.setUrl3(latest.getString("url3"));
        epi.setUrl4(latest.getString("url4"));

        return epi;

    }

    private static String getUrl(Context context) {
        return String.format(API_PETBOAT_LATEST);
    }

}
