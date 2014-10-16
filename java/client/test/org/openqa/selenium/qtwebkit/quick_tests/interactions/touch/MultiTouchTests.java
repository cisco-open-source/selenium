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

package org.openqa.selenium.qtwebkit.quick_tests.interactions.touch;


import org.openqa.selenium.By;
import org.openqa.selenium.WaitingConditions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.multitouch.MultiTouchActions;
import org.openqa.selenium.testing.JUnit4TestBase;

import org.junit.Test;

import static org.openqa.selenium.TestWaiter.waitFor;

public class MultiTouchTests extends JUnit4TestBase {
    private MultiTouchActions getBuilder(WebDriver driver) {
        return new MultiTouchActions(driver);
    }

    private void rotateElement(String elementId, int angle) {
        WebElement toRotate = driver.findElement(By.id(elementId));
        Action rotate = getBuilder(driver).pinchRotate(toRotate, angle).build();
        rotate.perform();
    }

    private void zoomElement(String elementId, double scale) {
        WebElement toZoom = driver.findElement(By.id(elementId));
        Action zoom = getBuilder(driver).pinchZoom(toZoom, scale).build();
        zoom.perform();
    }

    @Test
    public void testCanRotateElement() {
        driver.get(pages.multiTouchTest);
        rotateElement("pinchItem", 80);
        waitFor(WaitingConditions.elementTextToEqual(driver, By.id("rotationLbl"), "80"));
    }

    @Test
    public void testCanZoomOutElement() {
        driver.get(pages.multiTouchTest);
        zoomElement("pinchItem", 12.5);
        waitFor(WaitingConditions.elementTextToEqual(driver, By.id("zoomLbl"), "12.5"));
    }

    @Test
    public void testCanZoomInElement() {
        driver.get(pages.multiTouchTest);
        zoomElement("pinchItem", 0.3);
        waitFor(WaitingConditions.elementTextToEqual(driver, By.id("zoomLbl"), "0.3"));
    }
}
