/*
Copyright 2012-2013 Software Freedom Conservancy
Copyright 2007-2013 Selenium committers

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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;
import static org.openqa.selenium.WaitingConditions.newWindowIsOpened;
import static org.openqa.selenium.WaitingConditions.windowHandleCountToBe;
import static org.openqa.selenium.WaitingConditions.windowHandleCountToBeGreaterThan;
import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;
import static org.openqa.selenium.testing.Ignore.Driver.IE;
import static org.openqa.selenium.testing.Ignore.Driver.IPHONE;
import static org.openqa.selenium.testing.Ignore.Driver.MARIONETTE;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA_MOBILE;
import static org.openqa.selenium.testing.Ignore.Driver.REMOTE;
import static org.openqa.selenium.testing.Ignore.Driver.QTWEBKIT;

import com.google.common.collect.Sets;

import org.junit.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.openqa.selenium.testing.JavascriptEnabled;
import org.openqa.selenium.testing.TestUtilities;
import org.openqa.selenium.testing.drivers.Browser;

import java.util.Set;

@Ignore(value = {IPHONE}, reason = "The iPhone only supports one window")
public class WindowSwitchingTest extends JUnit4TestBase {

  @Ignore({OPERA_MOBILE, MARIONETTE})
  @Test
  public void testShouldSwitchFocusToANewWindowWhenItIsOpenedAndNotStopFutureOperations() {
    assumeFalse(Browser.detect() == Browser.opera &&
                TestUtilities.getEffectivePlatform().is(Platform.WINDOWS));

    driver.get(pages.xhtmlTestPage);
    String current = driver.getWindowHandle();
    Set<String> currentWindowHandles = driver.getWindowHandles();

    driver.findElement(By.linkText("Open new window")).click();

    wait.until(newWindowIsOpened(currentWindowHandles));

    assertThat(driver.getTitle(), equalTo("XHTML Test Page"));

    driver.switchTo().window("result");
    assertThat(driver.getTitle(), equalTo("We Arrive Here"));

    driver.get(pages.iframePage);
    final String handle = driver.getWindowHandle();
    driver.findElement(By.id("iframe_page_heading"));
    driver.switchTo().frame("iframe1");
    assertThat(driver.getWindowHandle(), equalTo(handle));

    driver.close();
    driver.switchTo().window(current);
  }

  @Test
  public void testShouldThrowNoSuchWindowException() {
    driver.get(pages.xhtmlTestPage);
    String current = driver.getWindowHandle();

    try {
      driver.switchTo().window("invalid name");
      fail("NoSuchWindowException expected");
    } catch (NoSuchWindowException e) {
      // Expected.
    }

    driver.switchTo().window(current);
  }

  @Ignore({OPERA, OPERA_MOBILE, MARIONETTE, QTWEBKIT})
  @Test
  public void testShouldThrowNoSuchWindowExceptionOnAnAttemptToGetItsHandle() {
    driver.get(pages.xhtmlTestPage);
    String current = driver.getWindowHandle();
    Set<String> currentWindowHandles = driver.getWindowHandles();

    driver.findElement(By.linkText("Open new window")).click();

    wait.until(newWindowIsOpened(currentWindowHandles));

    driver.switchTo().window("result");
    driver.close();

    try {
      driver.getWindowHandle();
      fail("NoSuchWindowException expected");
    } catch (NoSuchWindowException e) {
      // Expected.
    } finally {
      driver.switchTo().window(current);
    }
  }

  @Ignore({OPERA, OPERA_MOBILE, MARIONETTE})
  @Test
  public void testShouldThrowNoSuchWindowExceptionOnAnyOperationIfAWindowIsClosed() {
    driver.get(pages.xhtmlTestPage);
    String current = driver.getWindowHandle();
    Set<String> currentWindowHandles = driver.getWindowHandles();

    driver.findElement(By.linkText("Open new window")).click();

    wait.until(newWindowIsOpened(currentWindowHandles));

    driver.switchTo().window("result");
    driver.close();

    try {
      try {
        driver.getTitle();
        fail("NoSuchWindowException expected");
      } catch (NoSuchWindowException e) {
        // Expected.
      }

      try {
        driver.findElement(By.tagName("body"));
        fail("NoSuchWindowException expected");
      } catch (NoSuchWindowException e) {
        // Expected.
      }
    } finally {
      driver.switchTo().window(current);
    }
  }

  @Ignore({OPERA, OPERA_MOBILE, MARIONETTE})
  @Test
  public void testShouldThrowNoSuchWindowExceptionOnAnyElementOperationIfAWindowIsClosed() {
    driver.get(pages.xhtmlTestPage);
    String current = driver.getWindowHandle();
    Set<String> currentWindowHandles = driver.getWindowHandles();

    driver.findElement(By.linkText("Open new window")).click();

    wait.until(newWindowIsOpened(currentWindowHandles));

    driver.switchTo().window("result");
    WebElement body = driver.findElement(By.tagName("body"));
    driver.close();

    try {
      body.getText();
      fail("NoSuchWindowException expected");
    } catch (NoSuchWindowException e) {
      // Expected.
    } finally {
      driver.switchTo().window(current);
    }
  }

  @NeedsFreshDriver
  @NoDriverAfterTest
  @Ignore({IE, REMOTE})
  @Test
  public void testShouldBeAbleToIterateOverAllOpenWindows() {
    driver.get(pages.xhtmlTestPage);
    driver.findElement(By.name("windowOne")).click();
    driver.findElement(By.name("windowTwo")).click();

    wait.until(windowHandleCountToBeGreaterThan(2));

    Set<String> allWindowHandles = driver.getWindowHandles();

    // There should be three windows. We should also see each of the window titles at least once.
    Set<String> seenHandles = Sets.newHashSet();
    for (String handle : allWindowHandles) {
      assertFalse(seenHandles.contains(handle));
      driver.switchTo().window(handle);
      seenHandles.add(handle);
    }

    assertEquals(3, allWindowHandles.size());
  }

  @JavascriptEnabled
  @Test
  @Ignore(MARIONETTE)
  public void testClickingOnAButtonThatClosesAnOpenWindowDoesNotCauseTheBrowserToHang() {
    assumeFalse(Browser.detect() == Browser.opera &&
                TestUtilities.getEffectivePlatform().is(Platform.WINDOWS));

    driver.get(pages.xhtmlTestPage);
    Boolean isIEDriver = TestUtilities.isInternetExplorer(driver);
    Boolean isIE6 = TestUtilities.isIe6(driver);
    String currentHandle = driver.getWindowHandle();
    Set<String> currentWindowHandles = driver.getWindowHandles();

    driver.findElement(By.name("windowThree")).click();

    wait.until(newWindowIsOpened(currentWindowHandles));

    driver.switchTo().window("result");

    try {
      wait.until(ExpectedConditions.presenceOfElementLocated(By.id("close")));
      driver.findElement(By.id("close")).click();

      if (isIEDriver && !isIE6) {
        Alert alert = wait.until(alertIsPresent());
        alert.accept();
      }

      // If we make it this far, we're all good.
    } finally {
      driver.switchTo().window(currentHandle);
      driver.findElement(By.id("linkId"));
    }
  }

  @JavascriptEnabled
  @Test
  @Ignore(MARIONETTE)
  public void testCanCallGetWindowHandlesAfterClosingAWindow() {
    assumeFalse(Browser.detect() == Browser.opera &&
                TestUtilities.getEffectivePlatform().is(Platform.WINDOWS));

    driver.get(pages.xhtmlTestPage);

    Boolean isIEDriver = TestUtilities.isInternetExplorer(driver);
    Boolean isIE6 = TestUtilities.isIe6(driver);
    String currentHandle = driver.getWindowHandle();
    Set<String> currentWindowHandles = driver.getWindowHandles();

    driver.findElement(By.name("windowThree")).click();

    wait.until(newWindowIsOpened(currentWindowHandles));

    driver.switchTo().window("result");
    int allWindowHandles = driver.getWindowHandles().size();

    try {
      wait.until(ExpectedConditions.presenceOfElementLocated(By.id("close"))).click();

      if (isIEDriver && !isIE6) {
        Alert alert = wait.until(alertIsPresent());
        alert.accept();
      }

      Set<String> allHandles = wait.until(windowHandleCountToBe(allWindowHandles - 1));

      assertEquals(currentWindowHandles.size(), allHandles.size());
    } finally {
      driver.switchTo().window(currentHandle);
    }
  }

  @Test
  public void testCanObtainAWindowHandle() {
    driver.get(pages.xhtmlTestPage);

    String currentHandle = driver.getWindowHandle();

    assertNotNull(currentHandle);
  }

  @Test
  public void testFailingToSwitchToAWindowLeavesTheCurrentWindowAsIs() {
    driver.get(pages.xhtmlTestPage);
    String current = driver.getWindowHandle();

    try {
      driver.switchTo().window("i will never exist");
      fail("Should not be ablt to change to a non-existant window");
    } catch (NoSuchWindowException e) {
      // expected
    }

    String newHandle = driver.getWindowHandle();

    assertEquals(current, newHandle);
  }

  @NeedsFreshDriver
  @NoDriverAfterTest
  @Ignore(value = {OPERA_MOBILE})
  @Test
  public void testCanCloseWindowWhenMultipleWindowsAreOpen() {
    driver.get(pages.xhtmlTestPage);
    Set<String> currentWindowHandles = driver.getWindowHandles();

    driver.findElement(By.name("windowOne")).click();

    wait.until(newWindowIsOpened(currentWindowHandles));

    Set<String> allWindowHandles = driver.getWindowHandles();

    // There should be two windows. We should also see each of the window titles at least once.
    assertEquals(2, allWindowHandles.size());
    String handle1 = (String) allWindowHandles.toArray()[1];
    driver.switchTo().window(handle1);
    driver.close();
    allWindowHandles = driver.getWindowHandles();
    assertEquals(1, allWindowHandles.size());
  }

  @NeedsFreshDriver
  @NoDriverAfterTest
  @Ignore(value = {OPERA_MOBILE})
  @Test
  public void testCanCloseWindowAndSwitchBackToMainWindow() {
    driver.get(pages.xhtmlTestPage);
    Set<String> currentWindowHandles = driver.getWindowHandles();

    driver.findElement(By.name("windowOne")).click();

    wait.until(newWindowIsOpened(currentWindowHandles));

    Set<String> allWindowHandles = driver.getWindowHandles();

    // There should be two windows. We should also see each of the window titles at least once.
    assertEquals(2, allWindowHandles.size());
    String mainHandle = (String) allWindowHandles.toArray()[0];
    String handle1 = (String) allWindowHandles.toArray()[1];
    driver.switchTo().window(handle1);
    driver.close();
    driver.switchTo().window(mainHandle);

    String newHandle = driver.getWindowHandle();
    assertEquals(mainHandle, newHandle);
  }

  @NeedsFreshDriver
  @NoDriverAfterTest
  @Test
  public void testClosingOnlyWindowShouldNotCauseTheBrowserToHang() {
    driver.get(pages.xhtmlTestPage);
    driver.close();
  }

  @NeedsFreshDriver
  @NoDriverAfterTest
  @Test
  @Ignore(MARIONETTE)
  public void testShouldFocusOnTheTopMostFrameAfterSwitchingToAWindow() {
    driver.get(appServer.whereIs("window_switching_tests/page_with_frame.html"));

    Set<String> currentWindowHandles = driver.getWindowHandles();
    String mainWindow = driver.getWindowHandle();

    driver.findElement(By.id("a-link-that-opens-a-new-window")).click();
    wait.until(newWindowIsOpened(currentWindowHandles));

    driver.switchTo().frame("myframe");

    driver.switchTo().window("newWindow");
    driver.close();
    driver.switchTo().window(mainWindow);

    driver.findElement(By.name("myframe"));
  }

}
