package com.dinodevs.greatfitwatchface.theme;

import android.content.Context;

import com.dinodevs.greatfitwatchface.theme.bin.AmPm;
import com.dinodevs.greatfitwatchface.theme.bin.Background;
import com.dinodevs.greatfitwatchface.theme.bin.Calories;
import com.dinodevs.greatfitwatchface.theme.bin.Hours;
import com.dinodevs.greatfitwatchface.theme.bin.Image;
import com.dinodevs.greatfitwatchface.theme.bin.Minutes;
import com.dinodevs.greatfitwatchface.theme.bin.Ones;
import com.dinodevs.greatfitwatchface.theme.bin.Scale;
import com.dinodevs.greatfitwatchface.theme.bin.Tens;
import com.dinodevs.greatfitwatchface.theme.bin.Text;
import com.dinodevs.greatfitwatchface.theme.bin.Time;
import com.huami.watch.watchface.util.Util;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

public class GtrTheme implements ITheme {


    private GtrTheme() {
    }


    public static ITheme load(Context context, String path) throws IOException, JSONException {
        File json = new File(path);
        byte[] data = Util.assetToBytes(context, path);
        return new GtrTheme();
    }

    public Background getBackground() {
        Background bck = new Background();
        Image img = new Image();
        img.setX(0);
        img.setY(0);
        img.setImageIndex(0);
        bck.setImage(img);
        return bck;
    }

    public String[] getBattery() {
        return new String[]{"gtr/0028.png", "gtr/0029.png", "gtr/0030.png", "gtr/0031.png", "gtr/0032.png", "gtr/0033.png", "gtr/0034.png",
                "gtr/0035.png", "gtr/0036.png", "gtr/0037.png", "gtr/0038.png",};
    }

    public Text getBatterySpec() {
        Text res = new Text();
        res.setTopLeftX(138);
        res.setTopLeftY(36);
        res.setBottomRightX(180);
        res.setBottomRightY(57);
        res.setAlignment("Center");
        res.setSpacing(0);
        res.setImageIndex(28);
        res.setImagesCount(10);
        return res;
    }

    public Scale getBatteryScale() {
        /*"CenterX": 160,
      "CenterY": 160,
      "RadiusX": 145,
      "RadiusY": 145,
      "StartAngle": 318,
      "EndAngle": 402,
      "Width": 20,
      "Color": "0x000000000000FE00",
      "Flatness": 0*/
        Scale res = new Scale();
        res.setCenterX(160);
        res.setCenterY(160);
        res.setRadiusX(145);
        res.setRadiusY(145);
        res.setStartAngle(318);
        res.setEndAngle(402);
        res.setWidth(20);
        res.setColor("0x000000000000FE00");
        res.setFlatness(0);
        return res;
    }

    public Time getTime() {
        Time res = new Time();
        Hours hours = new Hours();
        Tens tens = new Tens();
        tens.setX(109);
        tens.setY(78);
        tens.setImageIndex(8);
        tens.setImagesCount(10);

        Ones ones = new Ones();
        ones.setX(162);
        ones.setY(78);
        ones.setImageIndex(8);
        ones.setImagesCount(10);
        hours.setTens(tens);
        hours.setOnes(ones);

        tens = new Tens();
        tens.setX(109);
        tens.setY(169);
        tens.setImageIndex(18);
        tens.setImagesCount(10);

        ones = new Ones();
        ones.setX(162);
        ones.setY(169);
        ones.setImageIndex(18);
        ones.setImagesCount(10);
        Minutes minutes = new Minutes();
        minutes.setTens(tens);
        minutes.setOnes(ones);

        res.setHours(hours);
        res.setMinutes(minutes);
        AmPm amPm = new AmPm();
        amPm.setX(124);
        amPm.setY(300);
        amPm.setImageIndexAMCN(43);
        amPm.setImageIndexPMCN(44);
        amPm.setImageIndexAMEN(43);
        amPm.setImageIndexPMEN(44);
        res.setAmPm(amPm);
        return res;
    }

    public String getTimeHand() throws Exception {
        throw new Exception("Not implemented");
    }

    public String getImagePath(int idx) {
        return String.format("gtr/%04d.png", idx);
    }
    public String getAmPm(int amPm) {
        if (amPm == 0) {
            return getImagePath(getTime().getAmPm().getImageIndexAMEN());
        } else {
            return getImagePath(getTime().getAmPm().getImageIndexPMEN());
        }
    }

    public Calories getCalories() {
        Calories res = new Calories();
        res.setTopLeftX(223);
        res.setTopLeftY(148);
        res.setBottomRightX(286);
        res.setBottomRightY(168);
        res.setAlignment("Center");
        res.setSpacing(0);
        res.setImageIndex(28);
        res.setImagesCount(10);

        return res;
    }
}