/*
Copyright 2012 Selenium committers
Copyright 2012 Software Freedom Conservancy

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

package org.openqa.selenium;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.openqa.selenium.testing.Ignore.Driver.ANDROID;
import static org.openqa.selenium.testing.Ignore.Driver.IE;
import static org.openqa.selenium.testing.Ignore.Driver.IPHONE;
import static org.openqa.selenium.testing.Ignore.Driver.MARIONETTE;
import static org.openqa.selenium.testing.Ignore.Driver.PHANTOMJS;
import static org.openqa.selenium.testing.Ignore.Driver.SAFARI;
import static org.openqa.selenium.testing.Ignore.Driver.QTWEBKIT;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.openqa.selenium.testing.JavascriptEnabled;
import org.openqa.selenium.testing.NeedsLocalEnvironment;

import java.util.List;

@NeedsLocalEnvironment(reason =
    "Executing these tests over the wire doesn't work, because they relies on 100ms-specific timing")
public class ImplicitWaitTest extends JUnit4TestBase {

  @Before
  public void setUp() throws Exception {
    driver.manage().timeouts().implicitlyWait(0, MILLISECONDS);
  }

  @After
  public void tearDown() throws Exception {
    driver.manage().timeouts().implicitlyWait(0, MILLISECONDS);
  }

  @Test
  @JavascriptEnabled
  public void testShouldImplicitlyWaitForASingleElement() {
    driver.get(pages.dynamicPage);
    WebElement add = driver.findElement(By.id("adder"));

    driver.manage().timeouts().implicitlyWait(3000, MILLISECONDS);

    add.click();
    driver.findElement(By.id("box0")); // All is well if this doesn't throw.
  }

  @Test
  @JavascriptEnabled
  public void testShouldStillFailToFindAnElementWhenImplicitWaitsAreEnabled() {
    driver.get(pages.dynamicPage);
    driver.manage().timeouts().implicitlyWait(500, MILLISECONDS);
    try {
      driver.findElement(By.id("box0"));
      fail("Expected to throw.");
    } catch (NoSuchElementException expected) {
    }
  }

  @Test
  @JavascriptEnabled
  public void testShouldReturnAfterFirstAttemptToFindOneAfterDisablingImplicitWaits() {
    driver.get(pages.dynamicPage);
    driver.manage().timeouts().implicitlyWait(3000, MILLISECONDS);
    driver.manage().timeouts().implicitlyWait(0, MILLISECONDS);
    try {
      driver.findElement(By.id("box0"));
      fail("Expected to throw.");
    } catch (NoSuchElementException expected) {
    }
  }

  @Test
  @JavascriptEnabled
  @Ignore(MARIONETTE)
  public void testShouldImplicitlyWaitUntilAtLeastOneElementIsFoundWhenSearchingForMany() {
    driver.get(pages.dynamicPage);
    WebElement add = driver.findElement(By.id("adder"));

    driver.manage().timeouts().implicitlyWait(2000, MILLISECONDS);
    add.click();
    add.click();

    List<WebElement> elements = driver.findElements(By.className("redbox"));
    assertFalse(elements.isEmpty());
  }

  @Test
  @JavascriptEnabled
  public void testShouldStillFailToFindElementsWhenImplicitWaitsAreEnabled() {
    driver.get(pages.dynamicPage);
    driver.manage().timeouts().implicitlyWait(500, MILLISECONDS);
    List<WebElement> elements = driver.findElements(By.className("redbox"));
    assertTrue(elements.isEmpty());
  }

  @Test
  @JavascriptEnabled
  public void testShouldStillFailToFindElementsByIdWhenImplicitWaitsAreEnabled() {
    driver.get(pages.dynamicPage);
    driver.manage().timeouts().implicitlyWait(500, MILLISECONDS);
    List<WebElement> elements = driver.findElements(By.id("redbox"));
    assertTrue(elements.toString(), elements.isEmpty());
  }

  @Test
  @JavascriptEnabled
  public void testShouldReturnAfterFirstAttemptToFindManyAfterDisablingImplicitWaits() {
    driver.get(pages.dynamicPage);
    WebElement add = driver.findElement(By.id("adder"));

    driver.manage().timeouts().implicitlyWait(1100, MILLISECONDS);
    driver.manage().timeouts().implicitlyWait(0, MILLISECONDS);
    add.click();

    List<WebElement> elements = driver.findElements(By.className("redbox"));
    assertTrue(elements.isEmpty());
  }

  @Test
  @JavascriptEnabled
  @Ignore({ANDROID, IE, IPHONE, PHANTOMJS, SAFARI, MARIONETTE, QTWEBKIT})
  public void testShouldImplicitlyWaitForAnElementToBeVisibleBeforeInteracting() {
    driver.get(pages.dynamicPage);

    WebElement reveal = driver.findElement(By.id("reveal"));
    WebElement revealed = driver.findElement(By.id("revealed"));
    driver.manage().timeouts().implicitlyWait(5000, MILLISECONDS);

    assertFalse("revealed should not be visible", revealed.isDisplayed());
    reveal.click();

    try {
      revealed.sendKeys("hello world");
      // This is what we want
    } catch (ElementNotVisibleException e) {
      fail("Element should have been visible");
    }
  }
}
