package org.openqa.selenium.qtwebkit.NativeTests;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.openqa.selenium.testing.JavascriptEnabled;
import org.openqa.selenium.testing.TestUtilities;
import org.openqa.selenium.testing.drivers.WebDriverBuilder;

import static org.openqa.selenium.TestWaiter.waitFor;
import static org.openqa.selenium.WaitingConditions.windowHandleCountToBe;
import static org.openqa.selenium.testing.Ignore.Driver.*;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA_MOBILE;

/**
 * Created with IntelliJ IDEA.
 * User: andrii
 * Date: 4/5/13
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClickTest extends JUnit4TestBase {

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        WebDriverBuilder builder = new WebDriverBuilder();

        caps.setCapability("browserStartWindow", "ClickTestWidget");
        builder.setRequiredCapabilities(caps);
        driver = builder.get();
    }

    @After
    public void tearDown() throws Exception {
        driver.close();
    }

    @Test
    public void testCanClickOnAPushButton() {
        driver.findElement(By.id("pushBtn")).click();

        waitFor(WaitingConditions.pageTitleToBe(driver, "CLick Test Window"));
    }

    @Test
    public void testCanClickOnACheckBox() {
        driver.findElement(By.id("checkBox")).click();

        waitFor(WaitingConditions.pageTitleToBe(driver, "CLick Test Window"));
    }

    @Test
    public void testClickingOnButtonInScrollArea() {
        driver.findElement(By.id("btnOnScroll")).click();

        waitFor(WaitingConditions.pageTitleToBe(driver, "CLick Test Window"));
    }
}
