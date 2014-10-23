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

package org.openqa.selenium.qtwebkit.quick_tests;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.Before;
import org.openqa.selenium.testing.TestUtilities;
import org.openqa.selenium.testing.drivers.SauceDriver;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.openqa.selenium.testing.Ignore.Driver.QTWEBKIT;

public class WindowTest extends JUnit4TestBase {

    private static Logger log = Logger.getLogger(WindowTest.class.getName());

    @Before
    public void setUp()
    {
        driver.get(pages.windowTest);
    }

    @Test
    public void testGetsTheSizeOfTheCurrentWindow() {
        Dimension size = driver.manage().window().getSize();
        if(log != null && log.getLevel() != null)
            log.log(log.getLevel(), "Driver: " + driver);

        assertThat(size.width, is(greaterThan(0)));
        assertThat(size.height, is(greaterThan(0)));
    }

    @Test
    public void testSetsTheSizeOfTheCurrentWindow() {
        // resize relative to the initial size, since we don't know what it is
        changeSizeBy(-20, -20);
    }

    @Test
    public void testGetsThePositionOfTheCurrentWindow() {
        Point position = driver.manage().window().getPosition();

        assertThat(position.x, is(greaterThanOrEqualTo(0)));
        assertThat(position.y, is(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testSetsThePositionOfTheCurrentWindow() throws InterruptedException {
        WebDriver.Window window = driver.manage().window();
        Point position = window.getPosition();
        Dimension originalSize = window.getSize();

        try {
            // Some Linux window managers start taking liberties wrt window positions when moving the window
            // off-screen. Therefore, try to stay on-screen. Hopefully you have more than 210 px,
            // or this may fail.
            window.setSize(new Dimension(200, 200));
            Point targetPosition = new Point(position.x + 10, position.y + 10);
            window.setPosition(targetPosition);

            wait.until(xEqual(targetPosition));
            wait.until(yEqual(targetPosition));
        } finally {
            window.setSize(originalSize);
        }
    }

    @Test
    @Ignore(QTWEBKIT)
    public void testCanMaximizeTheWindow() throws InterruptedException {
        if(SauceDriver.shouldUseSauce() && TestUtilities.getEffectivePlatform().is(Platform.LINUX)) {
            // This test requires a window manager on Linux, and Sauce currently doesn't have one.
            return;
        }

        changeSizeTo(new Dimension(275, 275));
        maximize();
    }

    private void changeSizeBy(int deltaX, int deltaY) {
        WebDriver.Window window = driver.manage().window();
        Dimension size = window.getSize();
        changeSizeTo(new Dimension(size.width + deltaX, size.height + deltaY));
    }

    private void changeSizeTo(Dimension targetSize) {
        WebDriver.Window window = driver.manage().window();

        window.setSize(targetSize);
        wait.until(windowHeightToEqual(targetSize));
        wait.until(windowWidthToEqual(targetSize));
    }

    private void maximize() {
        WebDriver.Window window = driver.manage().window();

        Dimension size = window.getSize();

        window.maximize();
        wait.until(windowWidthToBeGreaterThan(size));
        wait.until(windowHeightToBeGreaterThan(size));
    }

    private ExpectedCondition<Boolean> windowWidthToEqual(final Dimension size) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                Dimension newSize = driver.manage().window().getSize();
                if(newSize.width == size.width) {
                    return true;
                }
                return null;
            }
        };
    }

    private ExpectedCondition<Boolean> windowHeightToEqual(final Dimension size) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                Dimension newSize = driver.manage().window().getSize();
                if(newSize.height == size.height) {
                    return true;
                }

                return null;
            }
        };
    }

    private ExpectedCondition<Boolean> windowWidthToBeGreaterThan(final Dimension size) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                Dimension newSize = driver.manage().window().getSize();
                log.info("waiting for width, Current dimensions are " + newSize);
                if(newSize.width != size.width) {
                    return true;
                }

                return null;
            }
        };
    }

    private ExpectedCondition<Boolean> windowHeightToBeGreaterThan(final Dimension size) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                Dimension newSize = driver.manage().window().getSize();
                log.info("waiting for height, Current dimensions are " + newSize);
                if(newSize.height != size.height) {
                    return true;
                }

                return null;
            }
        };
    }
    private ExpectedCondition<Boolean> xEqual(final Point targetPosition) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                Point newPosition = driver.manage().window().getPosition();
                if(newPosition.x == targetPosition.x) {
                    return true;
                }

                return null;
            }
        };
    }
    private ExpectedCondition<Boolean> yEqual(final Point targetPosition) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                Point newPosition = driver.manage().window().getPosition();
                if(newPosition.y == targetPosition.y) {
                    return true;
                }

                return null;
            }
        };
    }
}
