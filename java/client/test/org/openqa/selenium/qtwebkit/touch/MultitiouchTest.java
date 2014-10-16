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

package org.openqa.selenium.qtwebkit.touch;

import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.TestWaiter.waitFor;
import org.junit.AfterClass;

import org.junit.Test;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assume.assumeThat;
import org.junit.Before;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.HasMultiTouchScreen;
import org.openqa.selenium.interactions.multitouch.MultiTouchActions;
import org.openqa.selenium.testing.JUnit4TestBase;


public class MultitiouchTest extends JUnit4TestBase {

    @Before
    public void checkHasMultiTouchScreen() {
        assumeThat(driver, instanceOf(HasMultiTouchScreen.class));
    }

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
    public void testCanRotate() {
        driver.get(pages.pinchTouchTest);
        rotateElement("picture", 35);
        WebElement result = driver.findElement(By.id("result_rotate"));
        waitFor(WaitingConditions.elementTextToEqual(result, "35"));
    }

    @Test
    public void testCanRotateNegativeDegree() {
        driver.get(pages.pinchTouchTest);
        rotateElement("picture", -35);
        WebElement result = driver.findElement(By.id("result_rotate"));
        waitFor(WaitingConditions.elementTextToEqual(result, "-35"));
    }

    @Test
    public void testCanZoomOut() {
        driver.get(pages.pinchTouchTest);
        zoomElement("picture", 2.5);
        WebElement result = driver.findElement(By.id("result_scale"));
        waitFor(WaitingConditions.elementTextToEqual(result, "2.5"));
    }

    @Test
    public void testCanZoomIn() {
        driver.get(pages.pinchTouchTest);
        zoomElement("picture", 0.4);
        WebElement result = driver.findElement(By.id("result_scale"));
        waitFor(WaitingConditions.elementTextToEqual(result, "0.4"));
    }

    @AfterClass
    public static void cleanUpDriver() {
        JUnit4TestBase.removeDriver();
    }
}
