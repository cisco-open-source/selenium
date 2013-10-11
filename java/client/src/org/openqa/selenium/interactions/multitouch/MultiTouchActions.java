package org.openqa.selenium.interactions.multitouch;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.HasMultiTouchScreen;
import org.openqa.selenium.interactions.MultiTouchScreen;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.internal.Locatable;

/**
 * Implements actions for multitouch enabled devices, reusing the available composite and builder design
 * patterns from Actions.
 */
public class MultiTouchActions extends Actions {
    protected MultiTouchScreen multiTouchScreen;

    public MultiTouchActions(WebDriver driver) {
        this(((HasInputDevices) driver).getKeyboard(),
                ((HasMultiTouchScreen) driver).getMultiTouch());
    }

    public MultiTouchActions(Keyboard keyboard, MultiTouchScreen multiTouchScreen) {
        super(keyboard);
        this.multiTouchScreen = multiTouchScreen;
    }

    public MultiTouchActions pinchZoom(WebElement onElement, double scale) {
        action.addAction(new PinchZoomAction(multiTouchScreen, (Locatable) onElement, scale));
        return this;
    }

    public MultiTouchActions pinchRotate(WebElement onElement, int angle) {
        action.addAction(new PinchRotateAction(multiTouchScreen, (Locatable) onElement, angle));
        return this;
    }


}
