package com.dinodevs.greatfitwatchface.theme;

import com.dinodevs.greatfitwatchface.theme.bin.Background;
import com.dinodevs.greatfitwatchface.theme.bin.Calories;
import com.dinodevs.greatfitwatchface.theme.bin.CaloriesGraph;
import com.dinodevs.greatfitwatchface.theme.bin.IText;
import com.dinodevs.greatfitwatchface.theme.bin.Pulse;
import com.dinodevs.greatfitwatchface.theme.bin.Scale;
import com.dinodevs.greatfitwatchface.theme.bin.Text;
import com.dinodevs.greatfitwatchface.theme.bin.Time;

public interface ITheme {
    Background getBackground();

    String[] getBattery();

    Text getBatterySpec();

    Scale getBatteryScale();

    String getTimeHand() throws Exception;

    Time getTime();

    String getAmPm(int amPm);

    String getImagePath(int imageIndex);

    Calories getCalories();

    Pulse getPulse();

    CaloriesGraph getCaloriesGraph();


//    static ITheme Load(String path) {
//    };
}
