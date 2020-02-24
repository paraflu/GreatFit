package com.dinodevs.greatfitwatchface.widget;

import android.app.Service;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.util.Size;

import com.dinodevs.greatfitwatchface.settings.LoadSettings;
import com.dinodevs.greatfitwatchface.theme.bin.Scale;
import com.dinodevs.greatfitwatchface.theme.bin.Text;
import com.huami.watch.watchface.util.Util;
import com.ingenic.iwds.slpt.view.arc.SlptPowerArcAnglePicView;
import com.ingenic.iwds.slpt.view.core.SlptBatteryView;
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout;
import com.ingenic.iwds.slpt.view.core.SlptPictureView;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;
import com.ingenic.iwds.slpt.view.sport.SlptPowerNumView;
import com.ingenic.iwds.slpt.view.utils.SimpleFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dinodevs.greatfitwatchface.data.Battery;
import com.dinodevs.greatfitwatchface.data.DataType;
import com.dinodevs.greatfitwatchface.resource.ResourceManager;

public class BatteryWidget extends AbstractWidget {
    private Battery batteryData;
    private final static String TAG = "VergeIT-LOG";
    //    private Paint batteryPaint;
    private Paint ring;
    //
    private Float batterySweepAngle = 0f;
    private Integer angleLength;
    //
    private Bitmap batteryIcon;
    //    private Bitmap icon;
//
    private Integer tempBattery = 0;

    private LoadSettings settings;
    private Service mService;

    // Constructor
    public BatteryWidget(LoadSettings settings) {
        this.settings = settings;

        if (settings.theme.getBatteryScale() == null) {
            return;
        }
//        if (settings.batteryProgClockwise == 1) {
//            this.angleLength = (settings.batteryProgEndAngle < settings.batteryProgStartAngle) ?
//                    360 - (settings.theme.getBatteryScale().getStartAngle() - settings.theme.getBatteryScale().getEndAngle()) :
//                    settings.theme.getBatteryScale().getEndAngle() - settings.theme.getBatteryScale().getStartAngle();
//        } else {
//            this.angleLength = (settings.theme.getBatteryScale().getEndAngle() > settings.theme.getBatteryScale().getStartAngle()) ?
//                    360 - (settings.theme.getBatteryScale().getStartAngle() - settings.theme.getBatteryScale().getEndAngle()) :
//                    settings.theme.getBatteryScale().getEndAngle() - settings.theme.getBatteryScale().getStartAngle();
//        }

        Scale batteryScale = settings.theme.getBatteryScale();

        this.angleLength = batteryScale.getStartAngle() > batteryScale.getEndAngle() ? batteryScale.getStartAngle() - batteryScale.getEndAngle()
                : batteryScale.getEndAngle() - batteryScale.getStartAngle();
    }

    // Screen-on init (runs once)
    public void init(Service service) {
        this.mService = service;

        this.batteryIcon = Util.decodeImage(mService.getResources(), this.settings.theme.getBattery()[0]);

        // Battery percent element
//        if(settings.battery_percent>0){
//            if(settings.battery_percentIcon){
//                this.icon = Util.decodeImage(mService.getResources(),"icons/"+settings.is_white_bg+"battery.png");
//            }
//
//            this.batteryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//            this.batteryPaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), settings.font));
//            this.batteryPaint.setTextSize(settings.battery_percentFontSize);
//            this.batteryPaint.setColor(settings.battery_percentColor);
//            this.batteryPaint.setTextAlign( (settings.battery_percentAlignLeft) ? Paint.Align.LEFT : Paint.Align.CENTER );
//        }
//
//        // Battery as images
//        if(settings.batteryProg>0 && settings.batteryProgType==1) {
//            this.batteryIcon = Util.decodeImage(mService.getResources(),"battery/battery0.png");
//        }
//
        // Progress Bar Circle
        if (settings.theme.getBatteryScale() != null) {
            this.ring = new Paint(Paint.ANTI_ALIAS_FLAG);
            this.ring.setStrokeCap(Paint.Cap.ROUND);
            this.ring.setStyle(Paint.Style.STROKE);
            this.ring.setStrokeWidth(settings.theme.getBatteryScale().getWidth());
        }
    }

    // Value updater
    public void onDataUpdate(DataType type, Object value) {
        // Battery class
        this.batteryData = (Battery) value;

        if (this.batteryData == null) {
            return;
        }

        // Bar angle
        Log.d(TAG, String.format("settings.batteryProg > %d && settings.batteryProgType == %d", settings.batteryProg, settings.batteryProgType));
        if (settings.batteryProg > 0 && settings.batteryProgType == 0) {
            Log.d(TAG, String.format("angle %d", this.angleLength));
            this.batterySweepAngle = this.angleLength * (this.batteryData.getLevel() / (float) this.batteryData.getScale());
        }
//
//        // Battery Image
//        if( this.tempBattery == this.batteryData.getLevel()/10 || !(settings.batteryProg>0 && settings.batteryProgType==1)){
//            return;
//        }
        int batterySteps = this.settings.theme.getBattery().length;
        this.tempBattery = this.batteryData.getLevel() / batterySteps;

//        this.batteryIcon = Util.decodeImage(mService.getResources(), this.settings.theme.getBattery()[this.tempBattery]);
    }

    // Register update listeners
    public List<DataType> getDataTypes() {
        return Collections.singletonList(DataType.BATTERY);
    }

    private void drawText(Canvas canvas, int value, String[] digits, Text spec) {
        int width = spec.getBottomRightX() - spec.getTopLeftX();
        int height = spec.getBottomRightY() - spec.getTopLeftY();
        int spacing = Math.round(width / digits.length) + spec.getSpacing();

        int x = spec.getTopLeftX();
        int y = spec.getTopLeftY();

        Bitmap bmp = Util.decodeImage(mService.getResources(), this.settings.theme.getBattery()[0]);
        Size imageSize = new Size(bmp.getWidth(), bmp.getHeight());

        if (spec.getAlignment().equals("Center")) {
            x += imageSize.getWidth() / 2 + width / 2 - (value / 10 * spacing);
        }

        String text = String.format("%d", value);
        Log.d(TAG, String.format("draw value %s", text));
        for (int i = 0; i < text.toCharArray().length; i++) {
            char charToPrint = text.toCharArray()[i];
            int va = charToPrint - '0';
            Log.d(TAG, String.format("draw char (x: %d, y: %d) %d - %c > bmp %s", x, y, i, charToPrint, this.settings.theme.getBattery()[va]));
            Bitmap charBmp = Util.decodeImage(mService.getResources(), this.settings.theme.getBattery()[va]);
            canvas.drawBitmap(charBmp, x, y, settings.mGPaint);
            x += charBmp.getWidth() + spacing;
        }
        Log.d(TAG, "complete");
    }

    // Draw screen-on
    public void draw(Canvas canvas, float width, float height, float centerX, float centerY) {
        if (this.batteryData == null) {
            return;
        }
        try {
            this.drawText(canvas, this.batteryData.getLevel(), this.settings.theme.getBattery(), this.settings.theme.getBatterySpec());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        // Battery % widget
//        if(settings.battery_percent>0){
//            if(settings.battery_percentIcon){
//                canvas.drawBitmap(this.icon, settings.battery_percentIconLeft, settings.battery_percentIconTop, settings.mGPaint);
//            }
//
//            String text = Integer.toString(this.batteryData.getLevel() * 100 / this.batteryData.getScale())+"%";
//            canvas.drawText(text, settings.battery_percentLeft, settings.battery_percentTop, batteryPaint);
//        }

        // Battery Progress Image
        if (settings.batteryProg > 0 && settings.batteryProgType == 1) {
            canvas.drawBitmap(this.batteryIcon,
                    settings.theme.getBatterySpec().getTopLeftX(),
                    settings.theme.getBatterySpec().getTopLeftX(),
                    settings.mGPaint);
        }

        // Battery bar
//        if (settings.batteryProg > 0 && settings.batteryProgType == 0) {
        if (settings.theme.getBatteryScale() != null) {
            int count = canvas.save();

            // Rotate canvas to 0 degrees = 12 o'clock
            canvas.rotate(-90, centerX, centerY);

            // Define circle
            Scale scale = settings.theme.getBatteryScale();
            float radius = scale.getRadiusX() /*- scale.getWidth()*/;
            RectF oval = new RectF(scale.getCenterX() - radius, scale.getCenterY() - radius,
                    scale.getCenterX() + radius,
                    scale.getCenterY() + radius);

            // Background
            Log.d(TAG, String.format("batteryProgBgBool %d", settings.batteryProgBgBool ? 1 : 0));
            Log.d(TAG, String.format("getStartAngle: %d angleLength: %d", scale.getStartAngle(), this.angleLength));
            this.ring.setColor(Color.parseColor(String.format("#%s", scale.getColor().substring(12))));
//            canvas.drawArc(oval, scale.getStartAngle(), this.angleLength, false, ring);

            // this.ring.setColor(settings.colorCodes[settings.batteryProgColorIndex]); progressione colore
            canvas.drawArc(oval, scale.getStartAngle(), this.batterySweepAngle, false, ring);

            canvas.restoreToCount(count);
        }
    }

    // Screen-off (SLPT)
    public List<SlptViewComponent> buildSlptViewComponent(Service service) {
        return buildSlptViewComponent(service, false);
    }

    // Screen-off (SLPT) - Better screen quality
    public List<SlptViewComponent> buildSlptViewComponent(Service service, boolean better_resolution) {
        better_resolution = better_resolution && settings.better_resolution_when_raising_hand;
        List<SlptViewComponent> slpt_objects = new ArrayList<>();

        // Do not show in SLPT (but show on raise of hand)
        boolean show_all = (!settings.clock_only_slpt || better_resolution);
        if (!show_all)
            return slpt_objects;

        int tmp_left;

//        // Show battery
//        if(settings.battery_percent>0){
//            // Show or Not icon
//            if (settings.battery_percentIcon) {
//                SlptPictureView battery_percentIcon = new SlptPictureView();
//                battery_percentIcon.setImagePicture( SimpleFile.readFileFromAssets(service, ( (better_resolution)?"":"slpt_" )+"icons/"+settings.is_white_bg+"battery.png") );
//                battery_percentIcon.setStart(
//                        (int) settings.battery_percentIconLeft,
//                        (int) settings.battery_percentIconTop
//                );
//                slpt_objects.add(battery_percentIcon);
//            }
//
//            SlptLinearLayout power = new SlptLinearLayout();
//            SlptPictureView percentage = new SlptPictureView();
//            percentage.setStringPicture("%");
//            power.add(new SlptPowerNumView());
//            power.add(percentage);
//            power.setTextAttrForAll(
//                    settings.battery_percentFontSize,
//                    settings.battery_percentColor,
//                    ResourceManager.getTypeFace(service.getResources(), settings.font)
//            );
//            // Position based on screen on
//            power.alignX = 2;
//            power.alignY = 0;
//            tmp_left = (int) settings.battery_percentLeft;
//            if(!settings.battery_percentAlignLeft) {
//                // If text is centered, set rectangle
//                power.setRect(
//                        (int) (2 * tmp_left + 640),
//                        (int) (((float)settings.font_ratio/100)*settings.battery_percentFontSize)
//                );
//                tmp_left = -320;
//            }
//            power.setStart(
//                    tmp_left,
//                    (int) (settings.battery_percentTop-((float)settings.font_ratio/100)*settings.battery_percentFontSize)
//            );
//            slpt_objects.add(power);
//        }

        // Battery as images
        if (settings.batteryProg > 0 && settings.batteryProgType == 1) {
            int battery_steps = 11;
            byte[][] arrayOfByte = new byte[battery_steps][];
            for (int i = 0; i < arrayOfByte.length; i++) {
                arrayOfByte[i] = SimpleFile.readFileFromAssets(service, this.settings.theme.getBattery()[i]);
            }
            SlptBatteryView localSlptBatteryView = new SlptBatteryView(battery_steps);
            localSlptBatteryView.setImagePictureArray(arrayOfByte);
            localSlptBatteryView.setStart((int) settings.batteryProgLeft, (int) settings.batteryProgTop);
            slpt_objects.add(localSlptBatteryView);
        }

//        // Battery bar
//        if(settings.batteryProg>0 && settings.batteryProgType==0){
//            // Draw background image
//            if(settings.batteryProgBgBool) {
//                SlptPictureView ring_background = new SlptPictureView();
//                ring_background.setImagePicture(SimpleFile.readFileFromAssets(service, ((settings.isVerge())?"verge_":( (better_resolution)?"":"slpt_" ))+"circles/ring1_bg.png"));
//                ring_background.setStart((int) (settings.batteryProgLeft-settings.batteryProgRadius), (int) (settings.batteryProgTop-settings.batteryProgRadius));
//                slpt_objects.add(ring_background);
//            }
//
//            SlptPowerArcAnglePicView localSlptPowerArcAnglePicView = new SlptPowerArcAnglePicView();
//            localSlptPowerArcAnglePicView.setImagePicture(SimpleFile.readFileFromAssets(service, ((settings.isVerge())?"verge_":( (better_resolution)?"":"slpt_" ))+settings.batteryProgSlptImage));
//            localSlptPowerArcAnglePicView.setStart((int) (settings.batteryProgLeft-settings.batteryProgRadius), (int) (settings.batteryProgTop-settings.batteryProgRadius));
//            localSlptPowerArcAnglePicView.start_angle = (settings.batteryProgClockwise==1)? settings.batteryProgStartAngle : settings.batteryProgEndAngle;
//            localSlptPowerArcAnglePicView.len_angle = 0;
//            localSlptPowerArcAnglePicView.full_angle = (settings.batteryProgClockwise==1)? this.angleLength : -this.angleLength;
//            localSlptPowerArcAnglePicView.draw_clockwise = settings.batteryProgClockwise;
//            slpt_objects.add(localSlptPowerArcAnglePicView);
//        }

        return slpt_objects;
    }
}
