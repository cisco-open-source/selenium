package org.openqa.selenium.interactions.multitouch;

import org.openqa.selenium.MultiTouchScreen;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.internal.MultiTouchAction;
import org.openqa.selenium.internal.Locatable;

/**
 * Creates a pinch zoom gesture.
 */
public class PinchZoomAction extends MultiTouchAction implements Action {

    private double scale;

    public PinchZoomAction(MultiTouchScreen multiTouchScreen, Locatable locationProvider, double scale) {
        super(multiTouchScreen, locationProvider);
        this.scale = scale;
    }

    public void perform() {
        multiTouchScreen.pinchZoom(getActionLocation(), scale);
    }
}