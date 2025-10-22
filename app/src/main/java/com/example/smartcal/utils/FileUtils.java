package com.example.smartcal.utils;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    public static String loadJSONFromAsset(Context ctx, String filename) {
        try {
            InputStream is = ctx.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return "{}";
        }
    }
}
