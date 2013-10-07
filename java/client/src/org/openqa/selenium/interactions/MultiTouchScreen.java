package org.openqa.selenium.interactions;

import org.openqa.selenium.interactions.internal.Coordinates;

/**
 * Interface to use for testing multitouch event
 *
 */
public interface MultiTouchScreen {
    /**
     * Allows the execution of pinch zoom action.
     *
     * @param where The coordinate of the element to pinchZoom on
     * @param scale scale factor
     */
    void pinchZoom(Coordinates where, double scale)   ;
    /**
     *Allows the execution of pinch rotate action.
     *
     * @param where The coordinate of the element to pinchRotate on
     * @param angle angle to rotate
     */
    void pinchRotate(Coordinates where, int angle);
}
