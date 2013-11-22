package org.openqa.selenium.qtwebkit;

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
        rotateElement("picture", 75);
        WebElement result = driver.findElement(By.id("result_rotate"));
        waitFor(WaitingConditions.elementTextToEqual(result, "75"));
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
