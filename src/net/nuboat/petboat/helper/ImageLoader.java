/*
 * ImageLoader.java
 * © 2011 Chalkboard Pte Ltd. All rights reserved. Chalkboard is a registered trademark of Chalkboard Pte Ltd.
 */
package net.nuboat.petboat.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author  Peerapat Asoktummarungsri
 * @email   nuboat@gmail.com
 * @twitter @nuboat
 */
public class ImageLoader {

    private static final String TAG = "ImageLoader";

    private static Map<String, Bitmap> cache = new HashMap<String, Bitmap>();

    private ImageLoader() {}

    public static Bitmap getBitmapFromURL(String src) {
        Bitmap bit = null;

        try {
            if (cache.containsKey(src) )
                bit = cache.get(src);
            else {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bit = BitmapFactory.decodeStream(input);

                //int width=48;
                //int height=48;

                //bit = Bitmap.createScaledBitmap(b, width, height, true);
                cache.put(src, bit);
            }
        } catch(Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }

        return bit;
    }


}