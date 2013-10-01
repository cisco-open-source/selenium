package org.openqa.selenium.interactions.multitouch;

import org.openqa.selenium.MultiTouchScreen;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.internal.MultiTouchAction;
import org.openqa.selenium.internal.Locatable;

/**
 * Creates a pinch rotate gesture.
 */
public class PinchRotateAction extends MultiTouchAction implements Action {

    private int angle;

    public PinchRotateAction(MultiTouchScreen multiTouchScreen, Locatable locationProvider, int angle) {
        super(multiTouchScreen, locationProvider);
        this.angle = angle;
    }

    public void perform() {
        multiTouchScreen.pinchRotate(getActionLocation(), angle);
    }
}