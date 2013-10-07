package org.openqa.selenium.qtwebkit.quick_tests.interactions.touch;


import org.openqa.selenium.By;
import org.openqa.selenium.WaitingConditions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.multitouch.MultiTouchActions;
import org.openqa.selenium.testing.JUnit4TestBase;

import org.junit.Test;

import static org.openqa.selenium.TestWaiter.waitFor;

public class MultiTouchTests extends JUnit4TestBase {
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
    public void testCanRotateElement() {
        driver.get(pages.multiTouchTest);
        rotateElement("pinchItem", 80);
        waitFor(WaitingConditions.elementTextToEqual(driver, By.id("rotationLbl"), "80"));
    }

    @Test
    public void testCanZoomOutElement() {
        driver.get(pages.multiTouchTest);
        zoomElement("pinchItem", 12.5);
        waitFor(WaitingConditions.elementTextToEqual(driver, By.id("zoomLbl"), "12.5"));
    }

    @Test
    public void testCanZoomInElement() {
        driver.get(pages.multiTouchTest);
        zoomElement("pinchItem", 0.3);
        waitFor(WaitingConditions.elementTextToEqual(driver, By.id("zoomLbl"), "0.3"));
    }
}
