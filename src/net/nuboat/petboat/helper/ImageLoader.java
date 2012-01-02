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

    public static Bitmap getBitmapFromURL(String src) throws Exception {

        if (cache.containsKey(src))
            return cache.get(src);

        Bitmap bit = getBitmap(src);

        if (bit == null)
            throw new Exception("Cannot load anything");

        cache.put(src, bit);

        return bit;

    }

    private static Bitmap getBitmap(String src) {
        Bitmap bit = null;

        for (int i=0; i<3; i++) {
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bit = BitmapFactory.decodeStream(input);

                return bit;

            } catch(Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }

        }
        return null;

    }

}
