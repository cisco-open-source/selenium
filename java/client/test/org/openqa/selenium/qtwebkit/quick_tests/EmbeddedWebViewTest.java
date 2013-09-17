package org.openqa.selenium.qtwebkit.quick_tests;

import org.openqa.selenium.*;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.util.Set;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.Before;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.openqa.selenium.TestWaiter.waitFor;

public class EmbeddedWebViewTest extends JUnit4TestBase {

    private static Logger log = Logger.getLogger(EmbeddedWebViewTest.class.getName());

    @Before
    public void setUp()
    {
        driver.get(appServer.whereIs("quick1/WebviewTest.qml"));
    }

    @Test
    public void testEmbeddedWebViewEnumerated() {
        Set<String> allWindowHandles = driver.getWindowHandles();
        assertEquals(2, allWindowHandles.size());

        for (String handle : allWindowHandles) {
            driver.switchTo().window(handle);
        }
    }

}
