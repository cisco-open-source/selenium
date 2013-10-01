package org.openqa.selenium.interactions.internal;

import org.openqa.selenium.MultiTouchScreen;
import org.openqa.selenium.interactions.internal.DisplayAction;
import org.openqa.selenium.internal.Locatable;

/**
 * Base class for all multitouch screen-related actions
 */
public class MultiTouchAction extends DisplayAction {

    protected final MultiTouchScreen multiTouchScreen;

    public MultiTouchAction(MultiTouchScreen touchScreen, Locatable locationProvider) {
        super(locationProvider);
        this.multiTouchScreen = touchScreen;
    }
}