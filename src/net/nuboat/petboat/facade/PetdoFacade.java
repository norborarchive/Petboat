/*
 * PetdoFacade.java
 * © 2011 nuboat.net. All rights reserved
 */
package net.nuboat.petboat.facade;

import android.content.Context;
import android.util.Log;
import java.util.LinkedList;
import java.util.List;
import net.nuboat.petboat.facade.pojo.EpisodePojo;
import net.nuboat.petboat.helper.RestClient;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author  Peerapat Asoktummarungsri [nuboat@gmail.com]
 */
public class PetdoFacade {

    private static final String TAG = "PetdoFacade";

    private static final String API_PETBOAT_GET     = "http://nuboat.net/petboat/get/%s";
    private static final String API_PETBOAT_LIST    = "http://nuboat.net/petboat/list/%s/%s";
    private static final String API_PETBOAT_LATEST  = "http://nuboat.net/petboat/latest";

    public static EpisodePojo findEpisode(Context context, String episodeno)
            throws Exception {

        String url = getEpisodeUrl(episodeno);

        Log.i(TAG, "Call : " + url);
        RestClient restclient = new RestClient("PetdoFacade");

        JSONObject jsonobj = restclient.getJsonbyHttpGet(url);
        JSONObject latest = jsonobj.getJSONObject("result");

        EpisodePojo epi = new EpisodePojo();
        epi.setNo(latest.getString("no"));
        epi.setName(latest.getString("name"));
        epi.setCredit(latest.getString("storycredit"));
        epi.setUrl1(latest.getString("url1"));
        epi.setUrl2(latest.getString("url2"));
        epi.setUrl3(latest.getString("url3"));
        epi.setUrl4(latest.getString("url4"));

        Log.i(TAG, "Name: "+epi.getName());

        return epi;

    }

    public static List<EpisodePojo> findListEpisode(Context context, String episodeno, int count)
            throws Exception {

        String url = getListUrl(episodeno, Integer.toString(count));

        Log.i(TAG, "Call : " + url);
        RestClient restclient = new RestClient("PetdoFacade");

        JSONObject jsonobj = restclient.getJsonbyHttpGet(url);
        JSONArray  jsonarr = jsonobj.getJSONArray("result");

        List<EpisodePojo> list = new LinkedList<EpisodePojo>();

        for (int i=0; i<jsonarr.length(); i++) {
            JSONObject obj = jsonarr.getJSONObject(i);

            EpisodePojo epi = new EpisodePojo();
            epi.setNo(obj.getString("no"));
            epi.setName(obj.getString("name"));
            list.add(epi);

            Log.i(TAG, "Name: "+epi.getName());
        }

        return list;

    }

    public static EpisodePojo findLatestestEpisode(Context context)
            throws Exception {

        String url = getLatestUrl();

        Log.i(TAG, "Call : " + url);
        RestClient restclient = new RestClient("PetdoFacade");

        JSONObject jsonobj = restclient.getJsonbyHttpGet(url);
        JSONObject latest = jsonobj.getJSONObject("result");

        EpisodePojo epi = new EpisodePojo();
        epi.setNo(latest.getString("no"));
        epi.setName(latest.getString("name"));
        epi.setCredit(latest.getString("storycredit"));
        epi.setUrl1(latest.getString("url1"));
        epi.setUrl2(latest.getString("url2"));
        epi.setUrl3(latest.getString("url3"));
        epi.setUrl4(latest.getString("url4"));

        Log.i(TAG, "Name: "+epi.getName());

        return epi;

    }

    private static String getEpisodeUrl(String episodeno) {
        return String.format(API_PETBOAT_GET, episodeno);
    }

    private static String getListUrl(String episodeno, String count) {
        return String.format(API_PETBOAT_LIST, episodeno, count);
    }

    private static String getLatestUrl() {
        return String.format(API_PETBOAT_LATEST);
    }

}
