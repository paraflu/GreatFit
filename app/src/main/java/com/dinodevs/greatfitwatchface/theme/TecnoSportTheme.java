
package com.dinodevs.greatfitwatchface.theme;

import android.content.Context;

import com.dinodevs.greatfitwatchface.theme.bin.AmPm;
import com.dinodevs.greatfitwatchface.theme.bin.Background;
import com.dinodevs.greatfitwatchface.theme.bin.Calories;
import com.dinodevs.greatfitwatchface.theme.bin.CaloriesGraph;
import com.dinodevs.greatfitwatchface.theme.bin.Circle;
import com.dinodevs.greatfitwatchface.theme.bin.Hours;
import com.dinodevs.greatfitwatchface.theme.bin.Image;
import com.dinodevs.greatfitwatchface.theme.bin.Minutes;
import com.dinodevs.greatfitwatchface.theme.bin.Ones;
import com.dinodevs.greatfitwatchface.theme.bin.Pulse;
import com.dinodevs.greatfitwatchface.theme.bin.Scale;
import com.dinodevs.greatfitwatchface.theme.bin.Step;
import com.dinodevs.greatfitwatchface.theme.bin.Steps;
import com.dinodevs.greatfitwatchface.theme.bin.StepsProgress;
import com.dinodevs.greatfitwatchface.theme.bin.Tens;
import com.dinodevs.greatfitwatchface.theme.bin.Text;
import com.dinodevs.greatfitwatchface.theme.bin.Time;
import com.huami.watch.watchface.util.Util;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

public class TecnoSportTheme implements ITheme {


    private TecnoSportTheme() {
    }


    public static ITheme load(Context context, String path) throws IOException, JSONException {
        File json = new File(path);
        byte[] data = Util.assetToBytes(context, path);
        return new TecnoSportTheme();
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
        return new String[]{"tecno_sport/0028.png", "tecno_sport/0029.png", "tecno_sport/0030.png", "tecno_sport/0031.png", "tecno_sport/0032.png", "tecno_sport/0033.png", "tecno_sport/0034.png",
                "tecno_sport/0035.png", "tecno_sport/0036.png", "tecno_sport/0037.png", "tecno_sport/0038.png",};
    }

    public Text getBatterySpec() {
        /**
         *   "TopLeftX": 205,
         *       "TopLeftY": 74,
         *       "BottomRightX": 238,
         *       "BottomRightY": 85,
         *       "Alignment": "TopRight",
         *       "Spacing": -2,
         *       "ImageIndex": 70,
         *       "ImagesCount": 10
         */
        Text res = new Text();
        res.setTopLeftX(205);
        res.setTopLeftY(74);
        res.setBottomRightX(238);
        res.setBottomRightY(85);
        res.setAlignment("TopRight");
        res.setSpacing(-2);
        res.setImageIndex(70);
        res.setImagesCount(10);
        return res;
    }

    public Scale getBatteryScale() {
        /*
       "CenterX": 158,
      "CenterY": 159,
      "RadiusX": 141,
      "RadiusY": 141,
      "StartAngle": 221,
      "EndAngle": 48,
      "Width": 20,
      "Color": "0x0000000000FF0000",
      "Flatness": 0
      */
        Scale res = new Scale();
        res.setCenterX(158);
        res.setCenterY(159);
        res.setRadiusX(141);
        res.setRadiusY(141);
        res.setStartAngle(221);
        res.setEndAngle(48);
        res.setWidth(20);
        res.setColor("0x0000000000FF0000");
        res.setFlatness(0);
        return res;
    }

    public Time getTime() {
        Time res = new Time();
        Hours hours = new Hours();
        Tens tens = new Tens();
        tens.setX(98);
        tens.setY(126);
        tens.setImageIndex(1);
        tens.setImagesCount(10);

        Ones ones = new Ones();
        ones.setX(131);
        ones.setY(126);
        ones.setImageIndex(1);
        ones.setImagesCount(10);
        hours.setTens(tens);
        hours.setOnes(ones);

        tens = new Tens();
        tens.setX(180);
        tens.setY(126);
        tens.setImageIndex(1);
        tens.setImagesCount(10);

        ones = new Ones();
        ones.setX(213);
        ones.setY(126);
        ones.setImageIndex(1);
        ones.setImagesCount(10);
        Minutes minutes = new Minutes();
        minutes.setTens(tens);
        minutes.setOnes(ones);

        res.setHours(hours);
        res.setMinutes(minutes);
//        AmPm amPm = new AmPm();
//        amPm.setX(124);
//        amPm.setY(300);
//        amPm.setImageIndexAMCN(43);
//        amPm.setImageIndexPMCN(44);
//        amPm.setImageIndexAMEN(43);
//        amPm.setImageIndexPMEN(44);
//        res.setAmPm(amPm);
        return res;
    }

    public String getTimeHand() throws Exception {
        throw new Exception("Not implemented");
    }

    public String getImagePath(int idx) {
        return String.format("tecno_sport/%04d.png", idx);
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
        /**
         * "CenterX": 85,
         *       "CenterY": 95,
         *       "RadiusX": 39,
         *       "RadiusY": 39,
         *       "StartAngle": 277,
         *       "EndAngle": 444,
         *       "Width": 12,
         *       "Color": "0x000000000000E3FE",
         *       "Flatness": 0
         */
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

    public Pulse getPulse() {
        /**
         *
         "TopLeftX": 134,
         "TopLeftY": 53,
         "BottomRightX": 185,
         "BottomRightY": 74,
         "Alignment": "Center",
         "Spacing": -7,
         "ImageIndex": 28,
         "ImagesCount": 10
         */
        Pulse res = new Pulse();
        res.setTopLeftX(132);
        res.setTopLeftY(53);
        res.setBottomRightX(185);
        res.setBottomRightY(74);
        res.setAlignment("Center");
        res.setSpacing(-7);
        res.setImageIndex(28);
        res.setImagesCount(10);
        return res;
    }

    public CaloriesGraph getCaloriesGraph() {
        CaloriesGraph res = new CaloriesGraph();
        Circle circle = new Circle();
        /**
         * CaloriesGraph
         * "Circle": {
         *         "CenterX": 160,
         *         "CenterY": 162,
         *         "RadiusX": 144,
         *         "RadiusY": 148,
         *         "StartAngle": 142,
         *         "EndAngle": 64,
         *         "Width": 23,
         *         "Color": "0x0000000000FE3E02",
         *         "Flatness": 180
         *       }
         */
        circle.setCenterX(160);
        circle.setCenterY(162);
        circle.setRadiusX(144);
        circle.setRadiusY(148);
        circle.setStartAngle(142);
        circle.setEndAngle(64);
        circle.setWidth(23);
        circle.setColor("0x0000000000FE3E02");
        circle.setFlatness(180);

        res.setCircle(circle);

        return res;
    }

    public Steps getStepWidget() {
        Steps res = new Steps();
        /**
         "TopLeftX": 40,
         "TopLeftY": 102,
         "BottomRightX": 123,
         "BottomRightY": 123,
         "Alignment": "Center",
         "Spacing": -7,
         "ImageIndex": 28,
         "ImagesCount": 10
         */
        Step step = new Step();
        step.setTopLeftX(40);
        step.setTopLeftY(102);
        step.setBottomRightX(123);
        step.setBottomRightY(123);
        step.setAlignment("Center");
        step.setSpacing(-7);
        step.setImageIndex(28);
        step.setImagesCount(10);
        res.setStep(step);
        return res;
    }

    public StepsProgress getStepProgress() {
        StepsProgress res = new StepsProgress();
        Circle circle = new Circle();
        /***
         "CenterX": 85,
         "CenterY": 95,
         "RadiusX": 39,
         "RadiusY": 39,
         "StartAngle": 277,
         "EndAngle": 444,
         "Width": 12,
         "Color": "0x000000000000E3FE",
         "Flatness": 0
         */
        circle.setCenterX(85);
        circle.setCenterY(95);
        circle.setRadiusX(39);
        circle.setRadiusX(39);
        circle.setStartAngle(277);
        circle.setEndAngle(444);
        circle.setWidth(12);
        circle.setColor("0x000000000000E3FE");
        circle.setFlatness(0);
        res.setCircle(circle);
        return res;
    }
}
