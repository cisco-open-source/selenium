package org.openqa.selenium.qtwebkit.quick1_tests;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.openqa.selenium.*;
import org.openqa.selenium.testing.JUnit4TestBase;

import static org.openqa.selenium.TestWaiter.waitFor;

public class ClickTest extends JUnit4TestBase {


    @Before
    public void setUp() throws Exception {
        driver.get(appServer.whereIs("quick1/ClickTest.qml"));
    }

    @Test
    public void testCanClickOnAPushButton() {
        driver.findElement(By.id("button1")).click();

        waitFor(WaitingConditions.elementTextToEqual(driver, By.id("clickDisplay"), "clicked button1"));
    }

    @Test
    public void testCanClickOnMouseArea() {
        driver.findElement(By.id("mouseHotSpotArea2")).click();

        waitFor(WaitingConditions.elementTextToEqual(driver, By.id("clickDisplay"), "clicked area 2"));
    }
}
