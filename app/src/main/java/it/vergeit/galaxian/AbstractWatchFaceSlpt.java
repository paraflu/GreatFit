package it.vergeit.galaxian;

import com.huami.watch.watchface.AbstractSlptClock;

import java.util.Arrays;
import java.util.LinkedList;

import it.vergeit.galaxian.widget.ClockWidget;
import it.vergeit.galaxian.widget.Widget;

/**
 * Splt version of
 */

public abstract class AbstractWatchFaceSlpt extends AbstractSlptClock {

    public ClockWidget clock;
    final LinkedList<Widget> widgets = new LinkedList<>();

    protected AbstractWatchFaceSlpt(final ClockWidget clock, final Widget... widgets) {
        this.clock = clock;
        this.widgets.addAll(Arrays.asList(widgets));
    }

    protected AbstractWatchFaceSlpt() {

    }
}
