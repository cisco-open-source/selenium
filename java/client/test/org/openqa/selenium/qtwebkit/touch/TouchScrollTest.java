package org.openqa.selenium.qtwebkit.touch;

import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.interactions.touch.TouchTestBase;
import org.openqa.selenium.testing.Ignore;

import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA_MOBILE;
import static org.openqa.selenium.testing.Ignore.Driver.QTWEBKIT;

/**
 * Tests the basic scroll operations on touch enabled devices..
 */
@Ignore({OPERA, OPERA_MOBILE})
public class TouchScrollTest extends TouchTestBase {

    private TouchActions getBuilder(WebDriver driver) {
        return new TouchActions(driver);
    }

    @NeedsFreshDriver
    @Test
    public void testCanScrollVerticallyFromWebElement() {
        driver.get(pages.longContentPage);

        WebElement link = driver.findElement(By.id("link3"));
        int y = link.getLocation().y;
        // The element is located at the right of the page,
        // so it is not initially visible on the screen.
        assertTrue("Expected y > 4200, but got y = " + y, y > 4200);

        WebElement toScroll = driver.findElement(By.id("imagestart"));
        Action scroll = getBuilder(driver).scroll(toScroll, 0, -800).build();
        scroll.perform();

        y = ((Long)((JavascriptExecutor) driver).executeScript("return document.body.scrollTop")).intValue();
        // Scroll position should change
        assertTrue("Expected y == 1000, but got y = " + y, y == 800);
    }

    @NeedsFreshDriver
    @Test
    public void testCanScrollHorizontallyFromWebElement() {
        driver.get(pages.longContentPage);

        WebElement link = driver.findElement(By.id("link1"));
        int x = link.getLocation().x;
        // The element is located at the right of the page,
        // so it is not initially visible on the screen.
        assertTrue("Expected x > 1500, but got x = " + x, x > 1500);

        WebElement toScroll = driver.findElement(By.id("imagestart"));
        Action scroll = getBuilder(driver).scroll(toScroll, -1000, 0).build();
        scroll.perform();

        x = ((Long)((JavascriptExecutor) driver).executeScript("return document.body.scrollLeft")).intValue();
        // Scroll position should change
        assertTrue("Expected x == 1000, but got x = " + x, x == 1000);
    }

    @NeedsFreshDriver
    @Test
    public void testCanScrollVertically() {
        driver.get(pages.longContentPage);

        WebElement link = driver.findElement(By.id("link3"));
        int y = link.getLocation().y;
        // The element is located at the right of the page,
        // so it is not initially visible on the screen.
        assertTrue(y > 4200);

        Action scrollDown = getBuilder(driver).scroll(0, 800).build();
        scrollDown.perform();

        y = ((Long)((JavascriptExecutor) driver).executeScript("return document.body.scrollTop")).intValue();
        // Scroll position should change
        assertTrue(y == 800);
    }

    @NeedsFreshDriver
    @Test
    public void testCanScrollHorizontally() {
        driver.get(pages.longContentPage);

        WebElement link = driver.findElement(By.id("link1"));
        int x = link.getLocation().x;
        // The element is located at the right of the page,
        // so it is not initially visible on the screen.
        assertTrue("Expected x > 1500, but got x = " + x, x > 1500);

        Action scrollDown = getBuilder(driver).scroll(400, 0).build();
        scrollDown.perform();

        x = ((Long)((JavascriptExecutor) driver).executeScript("return document.body.scrollLeft")).intValue();
        // Scroll position should change
        assertTrue("Expected x == 400, but got x = " + x, x == 400);
    }

}
