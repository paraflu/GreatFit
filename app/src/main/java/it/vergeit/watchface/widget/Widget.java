package it.vergeit.watchface.widget;

import android.app.Service;
import android.graphics.Canvas;

import it.vergeit.watchface.data.MultipleWatchDataListener;


public interface Widget extends MultipleWatchDataListener, HasSlptViewComponent {

    int getX();

    int getY();

    void setX(int x);

    void setY(int y);

    void init(Service service);

    void draw(Canvas canvas, float width, float height, float centerX, float centerY);
}
