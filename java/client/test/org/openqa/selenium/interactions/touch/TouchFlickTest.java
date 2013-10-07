/*
Copyright 2012 Software Freedom Conservancy
Copyright 2007-2012 Selenium committers

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package org.openqa.selenium.interactions.touch;

import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
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

  @Ignore(QTWEBKIT)
  @NeedsFreshDriver
  @Test
  public void testCanFlickHorizontallyFromWebElement() {
    driver.get(pages.longContentPage);

    WebElement toFlick = driver.findElement(By.id("imagestart"));
    WebElement link = driver.findElement(By.id("link1"));
    int x = link.getLocation().x;
    // The element is located at the right of the page,
    // so it is not initially visible on the screen.
    assertTrue("Expected x > 1500, but got x = " + x, x > 1500);

    Action flick = getBuilder(driver).flick(toFlick, -1000, 0, FlickAction.SPEED_NORMAL)
        .build();
    flick.perform();

    x = link.getLocation().x;
    // After flicking, the element should now be visible on the screen.
    assertTrue("Expected x < 1500, but got x = " + x, x < 1500);
  }

  @Ignore(QTWEBKIT)
  @NeedsFreshDriver
  @Test
  public void testCanFlickHorizontallyFastFromWebElement() {
    driver.get(pages.longContentPage);

    WebElement toFlick = driver.findElement(By.id("imagestart"));
    WebElement link = driver.findElement(By.id("link2"));
    int x = link.getLocation().x;
    // The element is located at the right of the page,
    // so it is not initially visible on the screen.
    assertTrue(x > 3500);

    Action flick = getBuilder(driver).flick(toFlick, -400, 0, FlickAction.SPEED_FAST)
        .build();
    flick.perform();
    x = link.getLocation().x;
    // After flicking, the element should now be visible on the screen.
    assertTrue("Expected x < 3500, but got: " + x, x < 3500);
  }

  @Ignore(QTWEBKIT)
  @NeedsFreshDriver
  @Test
  public void testCanFlickHorizontally() {
    driver.get(pages.clicksPage);
    driver.get(pages.longContentPage);

    WebElement link = driver.findElement(By.id("link1"));
    int x = link.getLocation().x;
    // The element is located at the right of the page,
    // so it is not initially visible on the screen.
    assertTrue("Expected x > 2000, but got x = " + x, x > 1500);

    Action flick = getBuilder(driver).flick(1000, 0).build();
    flick.perform();
    x = link.getLocation().x;
    // After flicking, the element should now be visible on the screen.
    assertTrue("Expected x < 1500, but got x = " + x, x < 1500);
  }

  @Ignore(value = {ANDROID, QTWEBKIT},
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

    Action flick = getBuilder(driver).flick(1500, 0).build();
    flick.perform();
    x = link.getLocation().x;
    // After flicking, the element should now be visible on the screen.
    assertTrue("Got: " + x, x < 3000);
  }

  @Ignore(QTWEBKIT)
  @NeedsFreshDriver
  @Test
  public void testCanFlickVerticallyFromWebElement() {
    driver.get(pages.longContentPage);

    WebElement link = driver.findElement(By.id("link3"));
    int y = link.getLocation().y;
    // The element is located at the bottom of the page,
    // so it is not initially visible on the screen.
    assertTrue(y > 4200);

    WebElement toFlick = driver.findElement(By.id("imagestart"));
    Action flick = getBuilder(driver).flick(toFlick, 0, -600, FlickAction.SPEED_NORMAL)
        .build();
    flick.perform();
    y = link.getLocation().y;
    // After flicking, the element should now be visible on the screen.
    assertTrue("Expected y < 4000, but got: " + y, y < 4000);
  }

  @Ignore(QTWEBKIT)
  @NeedsFreshDriver
  @Test
  public void testCanFlickVerticallyFastFromWebElement() {
    driver.get(pages.longContentPage);

    WebElement link = driver.findElement(By.id("link4"));
    int y = link.getLocation().y;
    // The element is located at the bottom of the page,
    // so it is not initially visible on the screen.
    assertTrue(y > 8700);

    WebElement toFlick = driver.findElement(By.id("imagestart"));
    Action flick = getBuilder(driver).flick(toFlick, 0, -600, FlickAction.SPEED_FAST)
        .build();
    flick.perform();
    y = link.getLocation().y;
    // After flicking, the element should now be visible on the screen.
    assertTrue("Expected y < 8700, but got: " + y, y < 8700);
  }

  @Ignore(QTWEBKIT)
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
    y = link.getLocation().y;

    // After flicking, the element should now be visible on the screen.
    assertTrue("Got: " + y, y < 4200);
  }

  @Ignore(QTWEBKIT)
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
    y = link.getLocation().y;
    // After flicking, the element should now be visible on the screen.
    assertTrue("Got: " + y, y < 4000);
  }

    @NeedsFreshDriver
    @Test
    public void testCanFlickHorizontallyFromWebElementQt() {
        driver.get(pages.longContentPage);

        WebElement toFlick = driver.findElement(By.id("imagestart"));
        WebElement link = driver.findElement(By.id("link1"));
        int x = link.getLocation().x;
        // The element is located at the right of the page,
        // so it is not initially visible on the screen.
        assertTrue("Expected x > 1500, but got x = " + x, x > 1500);

        Action flick = getBuilder(driver).flick(toFlick, -1000, 0, FlickAction.SPEED_NORMAL)
                .build();
        flick.perform();

        x = ((Long)((JavascriptExecutor) driver).executeScript("return document.body.scrollLeft")).intValue();
        //If speed normal we scroll xoffset pixel
        assertTrue("Expected x == 1000, but got x = " + x, x == 1000);
    }

    @NeedsFreshDriver
    @Test
    public void testCanFlickHorizontallyFastFromWebElementQt() {
        driver.get(pages.longContentPage);

        WebElement toFlick = driver.findElement(By.id("imagestart"));
        WebElement link = driver.findElement(By.id("link2"));
        int x = link.getLocation().x;
        // The element is located at the right of the page,
        // so it is not initially visible on the screen.
        assertTrue(x > 3500);

        Action flick = getBuilder(driver).flick(toFlick, -400, 0, FlickAction.SPEED_FAST)
                .build();
        flick.perform();
        x = ((Long)((JavascriptExecutor) driver).executeScript("return document.body.scrollLeft")).intValue();
        //If speed fast we scroll 2*xoffset pixel
        assertTrue("Expected x == 800, but got: " + x, x == 800);
    }

    @NeedsFreshDriver
    @Test
    public void testCanFlickHorizontallyQt() {
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
    public void testCanFlickHorizontallyFastQt() {
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
    public void testCanFlickVerticallyFromWebElementQt() {
        driver.get(pages.longContentPage);

        WebElement link = driver.findElement(By.id("link3"));
        int y = link.getLocation().y;
        // The element is located at the bottom of the page,
        // so it is not initially visible on the screen.
        assertTrue(y > 4200);

        WebElement toFlick = driver.findElement(By.id("imagestart"));
        Action flick = getBuilder(driver).flick(toFlick, 0, -600, FlickAction.SPEED_NORMAL)
                .build();
        flick.perform();
        y = ((Long)((JavascriptExecutor) driver).executeScript("return document.body.scrollTop")).intValue();
        //If speed normal we scroll yoffset pixel
        assertTrue("Expected y == 600, but got: " + y, y == 600);
    }

    @NeedsFreshDriver
    @Test
    public void testCanFlickVerticallyFastFromWebElementQt() {
        driver.get(pages.longContentPage);

        WebElement link = driver.findElement(By.id("link4"));
        int y = link.getLocation().y;
        // The element is located at the bottom of the page,
        // so it is not initially visible on the screen.
        assertTrue(y > 8700);

        WebElement toFlick = driver.findElement(By.id("imagestart"));
        Action flick = getBuilder(driver).flick(toFlick, 0, -600, FlickAction.SPEED_FAST)
                .build();
        flick.perform();
        y = ((Long)((JavascriptExecutor) driver).executeScript("return document.body.scrollTop")).intValue();
        //If speed normal we scroll 2*yoffset pixel
        assertTrue("Expected y == 1200, but got: " + y, y == 1200);
    }

    @NeedsFreshDriver
    @Test
    public void testCanFlickVerticallyQt() {
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
    public void testCanFlickVerticallyFastQt() {
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