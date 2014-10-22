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

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.touch.FlickAction;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.qtwebkit.quick_tests.interactions.touch.TouchTestBase;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WaitingConditions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.testing.JUnit4TestBase;

/**
 * Tests single tap actions on touch enabled devices.
 */
public class TouchInteractionTests extends TouchTestBase {

    private TouchActions getBuilder(WebDriver driver) {
        return new TouchActions(driver);
    }

    private void singleTapOnElement(String elementId) {
        WebElement toSingleTap = driver.findElement(By.id(elementId));
        Action singleTap = getBuilder(driver).singleTap(toSingleTap).build();
        singleTap.perform();
    }

    private void doubleTapOnElement(String elementId) {
        WebElement toDoubleTap = driver.findElement(By.id(elementId));
        Action doubleTap = getBuilder(driver).doubleTap(toDoubleTap).build();
        doubleTap.perform();
    }

    private void longTapOnElement(String elementId) {
        WebElement toLongTap = driver.findElement(By.id(elementId));
        Action longTap = getBuilder(driver).longPress(toLongTap).build();
        longTap.perform();
    }

    private void scrollElement(String elementId, int x, int y) {
        WebElement toScroll= driver.findElement(By.id(elementId));
        Action scroll = getBuilder(driver).scroll(toScroll, x, y).build();
        scroll.perform();
    }

    private void flickElement(String elementId, int xoffset, int yoffset, int speed) {
        WebElement toFlick = driver.findElement(By.id(elementId));
        Action flick = getBuilder(driver).flick(toFlick, xoffset, yoffset, speed).build();
        flick.perform();
    }

    private Point getLocationOnScreen(By locator) {
        WebElement element = driver.findElement(locator);
        return ((Locatable) element).getCoordinates().onScreen();
    }


    @Test
    public void testCanClickOnMulitouchArea() {
        driver.get(pages.touchTest);
        singleTapOnElement("touchArea");
        waitFor(WaitingConditions.elementTextToEqual(driver, By.id("resultLabel"), "PressedReleased"));
    }

    @Test
    public void testCanClickOnMouseArea() {
        driver.get(pages.touchTest);
        singleTapOnElement("mouseArea");
        waitFor(WaitingConditions.elementTextToEqual(driver, By.id("resultLabel"), "Clicked"));
    }

    @Test
    public void testCanDoubleClickOnMouseArea() {
        driver.get(pages.touchTest);
        doubleTapOnElement("mouseArea");
        waitFor(WaitingConditions.elementTextToEqual(driver, By.id("resultLabel"), "DoubleClicked"));
    }

    @Test
    public void testCanDoubleClickOnMultitouchArea() {
        driver.get(pages.touchTest);
        doubleTapOnElement("touchArea");
        waitFor(WaitingConditions.elementTextToEqual(driver, By.id("resultLabel"), "PressedReleasedPressedReleased"));
    }

    @Test
    public void testCanLongClick() {
        driver.get(pages.touchTest);
        longTapOnElement("mouseArea");
        waitFor(WaitingConditions.elementTextToEqual(driver, By.id("resultLabel"), "LongClicked"));
    }

    @Test
    public void testCanDownMoveUp() {
        driver.get(pages.touchTest);
        Action down = getBuilder(driver).down(100, 100).build();
        down.perform();
        Action move = getBuilder(driver).move(150, 150).build();
        move.perform();
        Action up = getBuilder(driver).up(250, 250).build();
        up.perform();

        assertThat(getLocationOnScreen(By.id("pressPoint")), is(new Point(90, 90)));
        assertThat(getLocationOnScreen(By.id("movePoint")), is(new Point(140, 140)));
        assertThat(getLocationOnScreen(By.id("releasePoint")), is(new Point(240, 240)));

    }

    @Test
    public void testScroll() {
        driver.get(pages.touchScrollTest);
        int prewY = getLocationOnScreen(By.id("item6")).getY();
        scrollElement("item6", 0, -200);
        int newY = getLocationOnScreen(By.id("item6")).getY();
        assertTrue("Got: " + newY, newY < prewY);
    }

    @Test
    public void testFlick() {
        driver.get(pages.touchScrollTest);
        flickElement("listView", 0, -200, FlickAction.SPEED_FAST);
        waitFor(WaitingConditions.elementTextToEqual(driver, By.id("resultLabel"), "Flicked"));

    }
}
