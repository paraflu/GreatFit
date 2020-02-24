package com.dinodevs.greatfitwatchface.theme;

import android.content.Context;

import com.huami.watch.watchface.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class GtrTheme implements ITheme {

    public String background = null;

    private GtrTheme() {
    }

    static class Image {
        public int x;
        public int y;
        public int idx;

        public Image(int x, int y, int idx) {
            this.x = x;
            this.y = y;
            this.idx = idx;
        }
    }

    public static ITheme load(Context context, String path) throws IOException, JSONException {
        File json = new File(path);
        byte[] data = Util.assetToBytes(context, path);
        GtrTheme res = new GtrTheme();
        res.background = "gtr/0000.png";
        return res;
    }

    public String getBackground() {
        return this.background;
    }

    public String[] getBattery() {
        return new String[]{"gtr/0028.png", "gtr/0029.png", "gtr/0030.png", "gtr/0031.png", "gtr/0032.png", "gtr/0033.png", "gtr/0034.png",
                "gtr/0035.png", "gtr/0036.png", "gtr/0037.png", "gtr/0038.png",};
    }
}