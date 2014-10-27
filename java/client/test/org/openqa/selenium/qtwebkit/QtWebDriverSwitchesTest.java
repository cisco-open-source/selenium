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

package org.openqa.selenium.qtwebkit;

import org.junit.*;

import java.util.Arrays;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.openqa.selenium.testing.NeedsLocalEnvironment;
import org.openqa.selenium.testing.Ignore;
import static org.openqa.selenium.testing.Ignore.Driver.QTWEBKIT;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;

public class QtWebDriverSwitchesTest extends JUnit4TestBase {

    @Before
    public void createDriver() throws Exception {
    }

    @After
    public void theerDown() throws Exception {
        driver.quit();
    }

    @NeedsLocalEnvironment
    @Test
    public void canStartWebDriverWithPos() {
        URL hostURL;
        Point point = new Point(80, 60);
        String arg = "" + point.getX() + ", " + point.getY();
        DesiredCapabilities capabilities = DesiredCapabilities.qtwebkit();
        capabilities.setCapability("windowposition", arg);

        driver = new QtWebKitDriver(QtWebKitDriver.createDefaultExecutor(), capabilities);
        wait = new WebDriverWait(driver, 30);

        driver.get(pages.colorPage);
        WebDriver.Window window = driver.manage().window();
        Point pnt = window.getPosition();
        assertEquals(80, pnt.getX());
        assertEquals(60, pnt.getY());


        wait.until(xEqual(point));
        wait.until(yEqual(point));

        sleep(2);

    }

    @NeedsLocalEnvironment
    @Test
    public void canStartWebDriverWithSize() {
        URL hostURL;
        Dimension size = new Dimension(600, 400);
        String arg = "" + size.getWidth() + ", " + size.getHeight();
        DesiredCapabilities capabilities = DesiredCapabilities.qtwebkit();
        capabilities.setCapability("windowsize", arg);

        driver = new QtWebKitDriver(QtWebKitDriver.createDefaultExecutor(), capabilities);
        wait = new WebDriverWait(driver, 30);

        driver.get(pages.colorPage);

        WebDriver.Window window = driver.manage().window();
        Dimension sz = window.getSize();
        assertEquals(400, sz.getHeight());
        assertEquals(600, sz.getWidth());
        wait.until(windowHeightToEqual(size));
        wait.until(windowWidthToEqual(size));

        sleep(2);

    }

    @NeedsLocalEnvironment
    @Test
    public void canStartWebDriverWithPosAndSize() {
        URL hostURL;
        Point point = new Point(80, 60);
        String arg_pos = "" + point.getX() + ", " + point.getY();
        DesiredCapabilities capabilities = DesiredCapabilities.qtwebkit();
        capabilities.setCapability("windowposition", arg_pos);

        Dimension size = new Dimension(640, 480);
        String arg_sz = "" + size.getWidth() + ", " + size.getHeight();
        capabilities.setCapability("windowsize", arg_sz);

        driver = new QtWebKitDriver(QtWebKitDriver.createDefaultExecutor(), capabilities);
        wait = new WebDriverWait(driver, 30);

        driver.get(pages.colorPage);
        WebDriver.Window window = driver.manage().window();
        Point pnt = window.getPosition();
        assertEquals(80, pnt.getX());
        assertEquals(60, pnt.getY());

        wait.until(xEqual(point));
        wait.until(yEqual(point));

        Dimension sz = window.getSize();
        assertEquals(640, sz.getWidth());
        assertEquals(480, sz.getHeight());
        wait.until(windowHeightToEqual(size));
        wait.until(windowWidthToEqual(size));

        sleep(2);

    }

    @NeedsLocalEnvironment
    @Test
    public void canStartWebDriverMaximized() {
        URL hostURL;
        DesiredCapabilities capabilities = DesiredCapabilities.qtwebkit();
        capabilities.setCapability("maximize", true);

        driver = new QtWebKitDriver(QtWebKitDriver.createDefaultExecutor(), capabilities);
        wait = new WebDriverWait(driver, 30);

        driver.get(pages.colorPage);

        Dimension maximizedSize = driver.manage().window().getSize();

        driver.manage().window().setSize(new Dimension(10, 10));
        driver.manage().window().maximize();

        wait.until(windowHeightToEqual(maximizedSize));
        wait.until(windowWidthToEqual(maximizedSize));

        sleep(2);

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

    private void sleep(int sec) {
        try {
            Thread.sleep(sec*1000);
        } catch (InterruptedException ie) {
            //Handle exception
        }
    }
}
