package org.openqa.selenium.qtwebkit.touch;

import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.touch.FlickAction;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.interactions.touch.TouchTestBase;
import org.openqa.selenium.testing.Ignore;

import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.testing.Ignore.Driver.*;

/**
 * Tests the basic flick operations on touch enabled devices.
 */
@Ignore({OPERA, OPERA_MOBILE})
public class TouchFlickTest extends TouchTestBase {

    private TouchActions getBuilder(WebDriver driver) {
        return new TouchActions(driver);
    }

    @NeedsFreshDriver
    @Test
    public void testCanFlickHorizontally() {
        driver.get(pages.clicksPage);
        driver.get(pages.longContentPage);

        WebElement link = driver.findElement(By.id("link1"));
        int x = link.getLocation().x;
        // The element is located at the right of the page,
        // so it is not initially visible on the screen.
        assertTrue("Expected x == 1500, but got x = " + x, x > 1500);

        Action flick = getBuilder(driver).flick(200, 0).build();
        flick.perform();
        x = ((Long)((JavascriptExecutor) driver).executeScript("return document.body.scrollLeft")).intValue();
        //Scroll 3*xspeed pixel
        assertTrue("Expected x == 600, but got x = " + x, x == 600);
    }

    @Ignore(value = {ANDROID},
            reason = "Android flick's can result in different offsets")
    @NeedsFreshDriver
    @Test
    public void testCanFlickHorizontallyFast() {
        driver.get(pages.longContentPage);

        WebElement link = driver.findElement(By.id("link2"));
        int x = link.getLocation().x;
        // The element is located at the right of the page,
        // so it is not initially visible on the screen.
        assertTrue(x > 3500);

        Action flick = getBuilder(driver).flick(750, 0).build();
        flick.perform();
        x = ((Long)((JavascriptExecutor) driver).executeScript("return document.body.scrollLeft")).intValue();
        //Scroll 3*xspeed pixel
        assertTrue("Got: " + x, x == 2250);
    }

    @NeedsFreshDriver
    @Test
    public void testCanFlickVertically() {
        driver.get(pages.longContentPage);

        WebElement link = driver.findElement(By.id("link3"));
        int y = link.getLocation().y;
        // The element is located at the bottom of the page,
        // so it is not initially visible on the screen.
        assertTrue(y > 4200);

        Action flick = getBuilder(driver).flick(0, 750).build();
        flick.perform();
        y = ((Long)((JavascriptExecutor) driver).executeScript("return document.body.scrollTop")).intValue();

        //Scroll 3*yspeed pixel
        assertTrue("Got: " + y, y == 2250);
    }

    @NeedsFreshDriver
    @Test
    public void testCanFlickVerticallyFast() {
        driver.get(pages.longContentPage);

        WebElement link = driver.findElement(By.id("link4"));
        int y = link.getLocation().y;
        // The element is located at the bottom of the page,
        // so it is not initially visible on the screen.
        assertTrue(y > 8700);

        Action flick = getBuilder(driver).flick(0, 1500).build();
        flick.perform();
        y = ((Long)((JavascriptExecutor) driver).executeScript("return document.body.scrollTop")).intValue();
        //Scroll 3*yspeed pixel
        assertTrue("Got: " + y, y == 4500);
    }

}