/****************************************************************************
**
** Copyright © 1992-2014 Cisco and/or its affiliates. All rights reserved.
** All rights reserved.
** 
** $CISCO_BEGIN_LICENSE:APACHE$
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
** http://www.apache.org/licenses/LICENSE-2.0
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
**
** $CISCO_END_LICENSE$
**
****************************************************************************/

package org.openqa.selenium.interactions.multitouch;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.HasMultiTouchScreen;
import org.openqa.selenium.interactions.MultiTouchScreen;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.internal.Locatable;

/**
 * Implements actions for multitouch enabled devices, reusing the available composite and builder design
 * patterns from Actions.
 */
public class MultiTouchActions extends TouchActions {
    protected MultiTouchScreen multiTouchScreen;

    public MultiTouchActions(WebDriver driver) {
        this(((HasInputDevices) driver).getKeyboard(),
                ((HasMultiTouchScreen) driver).getMultiTouch());
    }

    public MultiTouchActions(Keyboard keyboard, MultiTouchScreen multiTouchScreen) {
        super(keyboard, multiTouchScreen);
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
