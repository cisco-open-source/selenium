package org.openqa.selenium.qtwebkit.NativeTests;

import org.openqa.selenium.*;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.util.concurrent.Callable;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.openqa.selenium.testing.TestUtilities;
import org.openqa.selenium.testing.drivers.SauceDriver;
import org.openqa.selenium.testing.drivers.WebDriverBuilder;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.openqa.selenium.TestWaiter.waitFor;

/**
 * Created with IntelliJ IDEA.
 * User: andrii
 * Date: 4/1/13
 * Time: 1:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class WindowTest extends JUnit4TestBase {

    // private static Logger log = Logger.getLogger(WindowTest.class.getName());


    @Before
    public void createWebDriver()
    {
        DesiredCapabilities caps = new DesiredCapabilities();
        WebDriverBuilder builder = new WebDriverBuilder();

        caps.setCapability("browserStartWindow", "WindowTestWidget");
        builder.setRequiredCapabilities(caps);
        driver = builder.get();
    }

    @After
    public void closeDriver()
    {
        driver.close();
    }

    @Test
    public void testGetsTheSizeOfTheCurrentWindow() {
        Dimension size = driver.manage().window().getSize();
        // log.log(log.getLevel(), "Driver: " + driver);

        assertThat(size.width, is(greaterThan(0)));
        assertThat(size.height, is(greaterThan(0)));
    }

    @Test
    public void testSetsTheSizeOfTheCurrentWindow() {
        // resize relative to the initial size, since we don't know what it is
        changeSizeBy(-20, -20);
    }

    @Ignore     // Frames not implemented on native functionality
    @Test
    public void testSetsTheSizeOfTheCurrentWindowFromFrame() {
        driver.get(pages.framesetPage);
        driver.switchTo().frame("fourth");
        try {
            // resize relative to the initial size, since we don't know what it is
            changeSizeBy(-20, -20);
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    @Ignore     // Frames not implemented on native functionality
    @Test
    public void testSetsTheSizeOfTheCurrentWindowFromIframe() {
        driver.get(pages.iframePage);
        driver.switchTo().frame("iframe1-name");
        try {
            // resize relative to the initial size, since we don't know what it is
            changeSizeBy(-20, -20);
        } finally {
            driver.switchTo().defaultContent();
        }
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

            waitFor(xEqual(driver, targetPosition));
            waitFor(yEqual(driver, targetPosition));
        } finally {
            window.setSize(originalSize);
        }
    }

    @Test
    public void testCanMaximizeTheWindow() throws InterruptedException {
        if(SauceDriver.shouldUseSauce() && TestUtilities.getEffectivePlatform().is(Platform.LINUX)) {
            // This test requires a window manager on Linux, and Sauce currently doesn't have one.
            return;
        }

        changeSizeTo(new Dimension(275, 275));
        maximize();
    }

    @Ignore     // Frames not implemented on native functionality
    @Test
    public void testCanMaximizeTheWindowFromFrame() throws InterruptedException {
        if(SauceDriver.shouldUseSauce() && TestUtilities.getEffectivePlatform().is(Platform.LINUX)) {
            // This test requires a window manager on Linux, and Sauce currently doesn't have one.
            return;
        }

        driver.get(pages.framesetPage);
        changeSizeTo(new Dimension(275, 275));

        driver.switchTo().frame("fourth");
        try {
            maximize();
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    @Ignore     // Frames not implemented on native functionality
    @Test
    public void testCanMaximizeTheWindowFromIframe() throws InterruptedException {
        if(SauceDriver.shouldUseSauce() && TestUtilities.getEffectivePlatform().is(Platform.LINUX)) {
            // This test requires a window manager on Linux, and Sauce currently doesn't have one.
            return;
        }

        driver.get(pages.iframePage);
        changeSizeTo(new Dimension(275, 275));

        driver.switchTo().frame("iframe1-name");
        try {
            maximize();
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    private void changeSizeBy(int deltaX, int deltaY) {
        WebDriver.Window window = driver.manage().window();
        Dimension size = window.getSize();
        changeSizeTo(new Dimension(size.width + deltaX, size.height + deltaY));
    }

    private void changeSizeTo(Dimension targetSize) {
        WebDriver.Window window = driver.manage().window();

        window.setSize(targetSize);
        waitFor(windowHeightToEqual(driver,targetSize));
        waitFor(windowWidthToEqual(driver, targetSize));
    }

    private void maximize() {
        WebDriver.Window window = driver.manage().window();

        Dimension size = window.getSize();

        window.maximize();
        waitFor(windowWidthToBeGreaterThan(driver, size));
        waitFor(windowHeightToBeGreaterThan(driver, size));
    }

    private Callable<Boolean> windowWidthToEqual(final WebDriver driver, final Dimension size) {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                Dimension newSize = driver.manage().window().getSize();
                if(newSize.width == size.width) {
                    return true;
                }
                return null;
            }
        };
    }

    private Callable<Boolean> windowHeightToEqual(final WebDriver driver, final Dimension size) {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                Dimension newSize = driver.manage().window().getSize();
                if(newSize.height == size.height) {
                    return true;
                }

                return null;
            }
        };
    }

    private Callable<Boolean> windowWidthToBeGreaterThan(final WebDriver driver, final Dimension size) {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                Dimension newSize = driver.manage().window().getSize();
                // log.info("waiting for width, Current dimensions are " + newSize);
                if(newSize.width != size.width) {
                    return true;
                }

                return null;
            }
        };
    }

    private Callable<Boolean> windowHeightToBeGreaterThan(final WebDriver driver, final Dimension size) {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                Dimension newSize = driver.manage().window().getSize();
                // log.info("waiting for height, Current dimensions are " + newSize);
                if(newSize.height != size.height) {
                    return true;
                }

                return null;
            }
        };
    }
    private Callable<Boolean> xEqual(final WebDriver driver, final Point targetPosition) {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                Point newPosition = driver.manage().window().getPosition();
                if(newPosition.x == targetPosition.x) {
                    return true;
                }

                return null;
            }
        };
    }
    private Callable<Boolean> yEqual(final WebDriver driver, final Point targetPosition) {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                Point newPosition = driver.manage().window().getPosition();
                if(newPosition.y == targetPosition.y) {
                    return true;
                }

                return null;
            }
        };
    }
}
