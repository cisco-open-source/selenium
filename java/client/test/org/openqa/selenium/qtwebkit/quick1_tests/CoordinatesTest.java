package org.openqa.selenium.qtwebkit.quick1_tests;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.testing.JUnit4TestBase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;


public class CoordinatesTest extends JUnit4TestBase {

    @Before
    public void setUpWebDriver()
    {
        driver.get(appServer.whereIs("quick1/CoordinatesTest.qml"));
    }

    @Test
    public void testShouldGetCoordinatesOfAnElementInViewPort() {
        assertThat(getLocationOnScreen(By.id("mainRectangle")), is(new Point(10, 10)));
    }

    @Test
    public void testShouldGetCoordinatesOfAnZeroSizeElement() {
        assertThat(getLocationOnScreen(By.id("zeroSizedElement")), is(new Point(8, 8)));
    }

    @Test
    public void testShouldGetCoordinatesOfATransparentElement() {
        assertThat(getLocationOnScreen(By.id("transparentRect")), is(new Point(10, 120)));
    }

    @Test
    public void testShouldGetCoordinatesOfAHiddenElement() {
        assertThat(getLocationOnScreen(By.id("hiddenRect")), is(new Point(120, 10)));
    }

    @Test
    public void testShouldGetCoordinatesOfAnInvisibleElement() {
        assertThat(getLocationOnScreen(By.id("invisibleItem")), is(new Point(3, 3)));
    }

    private Point getLocationOnScreen(By locator) {
        WebElement element = driver.findElement(locator);
        return ((Locatable) element).getCoordinates().onScreen();
    }
}
