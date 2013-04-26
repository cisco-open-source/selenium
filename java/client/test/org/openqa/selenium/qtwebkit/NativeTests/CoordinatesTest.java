package org.openqa.selenium.qtwebkit.NativeTests;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JUnit4TestBase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.openqa.selenium.testing.Ignore.Driver.IE;

/**
 * Created with IntelliJ IDEA.
 * User: andrii
 * Date: 4/25/13
 * Time: 1:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoordinatesTest extends JUnit4TestBase {

    @Before
    public void setUpWebDriver()
    {
        driver.get("CoordinatesTestWidget");
    }

    @Test
    public void testShouldGetCoordinatesOfAnElementInViewPort() {
        assertThat(getLocationOnScreen(By.id("scrollArea")), is(new Point(11, 11)));
    }

    @Test
    public void testShouldGetCoordinatesOfAnZeroSizeElement() {
        assertThat(getLocationOnScreen(By.id("zeroSizeLabel")), is(new Point(21, 21)));
    }

    @Test
    public void testShouldGetCoordinatesOfATransparentElement() {
        assertThat(getLocationOnScreen(By.id("transparentLabel")), is(new Point(21, 24)));
    }

    @Test
    public void testShouldGetCoordinatesOfAHiddenElement() {
        assertNotNull(getLocationOnScreen(By.id("hiddenLabel")));
    }

    @Test
    public void testShouldGetCoordinatesOfAnInvisibleElement() {
        assertNotNull(getLocationOnScreen(By.id("unvisibleLabel")));
    }

    private Point getLocationOnScreen(By locator) {
        WebElement element = driver.findElement(locator);
        return ((Locatable) element).getCoordinates().getLocationOnScreen();
    }
}
