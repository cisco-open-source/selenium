package org.openqa.selenium.qtwebkit.nativetests;

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
        driver.get("ClickTestWidget");
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
