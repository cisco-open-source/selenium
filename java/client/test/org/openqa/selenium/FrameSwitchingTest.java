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

package org.openqa.selenium;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.openqa.selenium.support.ui.ExpectedConditions.frameToBeAvailableAndSwitchToIt;
import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.openqa.selenium.testing.Ignore.Driver.ALL;
import static org.openqa.selenium.testing.Ignore.Driver.ANDROID;
import static org.openqa.selenium.testing.Ignore.Driver.CHROME;
import static org.openqa.selenium.testing.Ignore.Driver.IE;
import static org.openqa.selenium.testing.Ignore.Driver.MARIONETTE;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA_MOBILE;
import static org.openqa.selenium.testing.Ignore.Driver.PHANTOMJS;
import static org.openqa.selenium.testing.Ignore.Driver.QTWEBKIT;
import static org.openqa.selenium.testing.Ignore.Driver.SAFARI;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.openqa.selenium.testing.JavascriptEnabled;
import org.openqa.selenium.testing.NeedsLocalEnvironment;

import java.util.Random;

public class FrameSwitchingTest extends JUnit4TestBase {

  @After
  public void tearDown() throws Exception {
    try {
      driver.switchTo().defaultContent();
    } catch (Exception e) {
      // May happen if the driver went away.
    }
  }

  // ----------------------------------------------------------------------------------------------
  //
  // Tests that WebDriver doesn't do anything fishy when it navigates to a page with frames.
  //
  // ----------------------------------------------------------------------------------------------
  @Test
  @Ignore(MARIONETTE)
  public void testShouldAlwaysFocusOnTheTopMostFrameAfterANavigationEvent() {
    driver.get(pages.framesetPage);
    driver.findElement(By.tagName("frameset")); // Test passes if this does not throw.
  }

  @Test
  public void testShouldNotAutomaticallySwitchFocusToAnIFrameWhenAPageContainingThemIsLoaded() {
    driver.get(pages.iframePage);
    driver.findElement(By.id("iframe_page_heading"));
  }

  @Test(timeout = 10000)
  @NeedsLocalEnvironment(reason = "it hangs at IE9 and event Test.timeout doesn't help")
  public void testShouldOpenPageWithBrokenFrameset() {
    driver.get(appServer.whereIs("framesetPage3.html"));

    WebElement frame1 = driver.findElement(By.id("first"));
    driver.switchTo().frame(frame1);

    driver.switchTo().defaultContent();

    WebElement frame2 = driver.findElement(By.id("second"));

    try {
      driver.switchTo().frame(frame2);
    } catch (WebDriverException e) {
      // IE9 can not switch to this broken frame - it has no window.
    }
  }

  // ----------------------------------------------------------------------------------------------
  //
  // Tests that WebDriver can switch to frames as expected.
  //
  // ----------------------------------------------------------------------------------------------
  @Test
  @Ignore(MARIONETTE)
  public void testShouldBeAbleToSwitchToAFrameByItsIndex() {
    driver.get(pages.framesetPage);
    driver.switchTo().frame(1);

    assertThat(driver.findElement(By.id("pageNumber")).getText(), equalTo("2"));
  }

  @Test
  public void testShouldBeAbleToSwitchToAnIframeByItsIndex() {
    driver.get(pages.iframePage);
    driver.switchTo().frame(0);

    assertThat(driver.findElement(By.name("id-name1")).getAttribute("value"), equalTo("name"));
  }

  @Test
  @Ignore(MARIONETTE)
  public void testShouldBeAbleToSwitchToAFrameByItsName() {
    driver.get(pages.framesetPage);
    driver.switchTo().frame("fourth");

    assertThat(driver.findElement(By.tagName("frame")).getAttribute("name"), equalTo("child1"));
  }

  @Test
  @Ignore(MARIONETTE)
  public void testShouldBeAbleToSwitchToAnIframeByItsName() {
    driver.get(pages.iframePage);
    driver.switchTo().frame("iframe1-name");

    assertThat(driver.findElement(By.name("id-name1")).getAttribute("value"), equalTo("name"));
  }

  @Test
  @Ignore(MARIONETTE)
  public void testShouldBeAbleToSwitchToAFrameByItsID() {
    driver.get(pages.framesetPage);
    driver.switchTo().frame("fifth");
    assertThat(driver.findElement(By.name("windowOne")).getText(), equalTo("Open new window"));
  }

  @Test
  public void testShouldBeAbleToSwitchToAnIframeByItsID() {
    driver.get(pages.iframePage);
    driver.switchTo().frame("iframe1");

    assertThat(driver.findElement(By.name("id-name1")).getAttribute("value"), equalTo("name"));
  }

  @Test
  @Ignore({OPERA, OPERA_MOBILE, MARIONETTE})
  public void testShouldBeAbleToSwitchToFrameWithNameContainingDot() {
    driver.get(pages.framesetPage);
    driver.switchTo().frame("sixth.iframe1");
    assertThat(driver.findElement(By.tagName("body")).getText(), containsString("Page number 3"));
  }

  @Test
  public void testShouldBeAbleToSwitchToAFrameUsingAPreviouslyLocatedWebElement() {
    driver.get(pages.framesetPage);
    WebElement frame = driver.findElement(By.tagName("frame"));
    driver.switchTo().frame(frame);

    assertThat(driver.findElement(By.id("pageNumber")).getText(), equalTo("1"));
  }

  @Test
  public void testShouldBeAbleToSwitchToAnIFrameUsingAPreviouslyLocatedWebElement() {
    driver.get(pages.iframePage);
    WebElement frame = driver.findElement(By.tagName("iframe"));
    driver.switchTo().frame(frame);

    WebElement element = driver.findElement(By.name("id-name1"));
    assertThat(element.getAttribute("value"), equalTo("name"));
  }

  @Test
  public void testShouldEnsureElementIsAFrameBeforeSwitching() {
    driver.get(pages.framesetPage);
    WebElement frame = driver.findElement(By.tagName("frameset"));

    try {
      driver.switchTo().frame(frame);
      fail();
    } catch (NoSuchFrameException expected) {
      // Do nothing.
    }
  }

  @Ignore({ANDROID, MARIONETTE})
  @Test
  public void testFrameSearchesShouldBeRelativeToTheCurrentlySelectedFrame() {
    driver.get(pages.framesetPage);

    driver.switchTo().frame("second");
    assertThat(driver.findElement(By.id("pageNumber")).getText(), equalTo("2"));

    try {
      driver.switchTo().frame("third");
      fail();
    } catch (NoSuchFrameException expected) {
      // Do nothing
    }

    driver.switchTo().defaultContent();
    driver.switchTo().frame("third");

    try {
      driver.switchTo().frame("second");
      fail();
    } catch (NoSuchFrameException expected) {
      // Do nothing
    }

    driver.switchTo().defaultContent();
    driver.switchTo().frame("second");
    assertThat(driver.findElement(By.id("pageNumber")).getText(), equalTo("2"));
  }

  @Ignore({ANDROID, OPERA, OPERA_MOBILE, MARIONETTE})
  @Test
  public void testShouldSelectChildFramesByChainedCalls() {
    driver.get(pages.framesetPage);

    driver.switchTo().frame("fourth").switchTo().frame("child2");
    assertThat(driver.findElement(By.id("pageNumber")).getText(), equalTo("11"));
  }

  @Ignore({ANDROID, MARIONETTE})
  @Test
  public void testShouldThrowFrameNotFoundExceptionLookingUpSubFramesWithSuperFrameNames() {
    driver.get(pages.framesetPage);
    driver.switchTo().frame("fourth");

    try {
      driver.switchTo().frame("second");
      fail("Expected NoSuchFrameException");
    } catch (NoSuchFrameException e) {
      // Expected
    }

  }

  @Test
  public void testShouldThrowAnExceptionWhenAFrameCannotBeFound() {
    driver.get(pages.xhtmlTestPage);

    try {
      driver.switchTo().frame("Nothing here");
      fail("Should not have been able to switch");
    } catch (NoSuchFrameException e) {
      // This is expected
    }
  }

  @Test
  public void testShouldThrowAnExceptionWhenAFrameCannotBeFoundByIndex() {
    driver.get(pages.xhtmlTestPage);

    try {
      driver.switchTo().frame(27);
      fail("Should not have been able to switch");
    } catch (NoSuchFrameException e) {
      // This is expected
    }
  }

  @Ignore({ANDROID, CHROME, IE, OPERA, OPERA_MOBILE, PHANTOMJS, QTWEBKIT, SAFARI, MARIONETTE})
  @Test
  public void testShouldBeAbleToSwitchToParentFrame() {
    driver.get(pages.framesetPage);

    driver.switchTo().frame("fourth").switchTo().parentFrame().switchTo().frame("first");
    assertThat(driver.findElement(By.id("pageNumber")).getText(), equalTo("1"));
  }

  @Ignore({ANDROID, CHROME, IE, OPERA, OPERA_MOBILE, PHANTOMJS, QTWEBKIT, SAFARI, MARIONETTE})
  @Test
  public void testShouldBeAbleToSwitchToParentFrameFromASecondLevelFrame() {
    driver.get(pages.framesetPage);

    driver.switchTo().frame("fourth").switchTo().frame("child1")
        .switchTo().parentFrame().switchTo().frame("child2");
    assertThat(driver.findElement(By.id("pageNumber")).getText(), equalTo("11"));
  }

  @Ignore({ANDROID, CHROME, IE, OPERA, OPERA_MOBILE, PHANTOMJS, QTWEBKIT, SAFARI, MARIONETTE})
  @Test
  public void testSwitchingToParentFrameFromDefaultContextIsNoOp() {
    driver.get(pages.xhtmlTestPage);
    driver.switchTo().parentFrame();
    assertEquals(driver.getTitle(), "XHTML Test Page");
  }

  @Ignore({ANDROID, CHROME, IE, OPERA, OPERA_MOBILE, PHANTOMJS, QTWEBKIT, SAFARI, MARIONETTE})
  @Test
  public void testShouldBeAbleToSwitchToParentFromAnIframe() {
    driver.get(pages.iframePage);
    driver.switchTo().frame(0);

    driver.switchTo().parentFrame();
    driver.findElement(By.id("iframe_page_heading"));
  }

  // ----------------------------------------------------------------------------------------------
  //
  // General frame handling behavior tests
  //
  // ----------------------------------------------------------------------------------------------

  @Ignore({ANDROID, MARIONETTE})
  @Test
  public void testShouldContinueToReferToTheSameFrameOnceItHasBeenSelected() {
    driver.get(pages.framesetPage);

    driver.switchTo().frame(2);
    WebElement checkbox = driver.findElement(By.xpath("//input[@name='checky']"));
    checkbox.click();
    checkbox.submit();

    // TODO(simon): this should not be needed, and is only here because IE's submit returns too
    // soon.

    wait.until(WaitingConditions.elementTextToEqual(By.xpath("//p"), "Success!"));
  }

  @Ignore(value = {ANDROID, OPERA, OPERA_MOBILE, MARIONETTE},
          reason = "Android does not detect that the select frame has disappeared")
  @Test
  public void testShouldFocusOnTheReplacementWhenAFrameFollowsALinkToA_TopTargetedPage()
      throws Exception {
    driver.get(pages.framesetPage);

    driver.switchTo().frame(0);
    driver.findElement(By.linkText("top")).click();

    String expectedTitle = "XHTML Test Page";

    wait.until(titleIs(expectedTitle));
    wait.until(presenceOfElementLocated(By.id("only-exists-on-xhtmltest")));
  }

  @Ignore(ANDROID)
  @Test
  public void testShouldAllowAUserToSwitchFromAnIframeBackToTheMainContentOfThePage() {
    driver.get(pages.iframePage);
    driver.switchTo().frame(0);

    try {
      driver.switchTo().defaultContent();
      driver.findElement(By.id("iframe_page_heading"));
    } catch (Exception e) {
      fail("Should have switched back to main content");
    }
  }

  @Ignore(ANDROID)
  @Test
  public void testShouldAllowTheUserToSwitchToAnIFrameAndRemainFocusedOnIt() {
    driver.get(pages.iframePage);
    driver.switchTo().frame(0);

    driver.findElement(By.id("submitButton")).click();

    assertThat(getTextOfGreetingElement(), equalTo("Success!"));
  }

  public String getTextOfGreetingElement() {
    return wait.until(presenceOfElementLocated(By.id("greeting"))).getText();
  }

  @Ignore({OPERA, ANDROID, OPERA_MOBILE, MARIONETTE})
  @Test
  public void testShouldBeAbleToClickInAFrame() {
    driver.get(pages.framesetPage);
    driver.switchTo().frame("third");

    // This should replace frame "third" ...
    driver.findElement(By.id("submitButton")).click();
    // driver should still be focused on frame "third" ...
    assertThat(getTextOfGreetingElement(), equalTo("Success!"));
    // Make sure it was really frame "third" which was replaced ...
    driver.switchTo().defaultContent().switchTo().frame("third");
    assertThat(getTextOfGreetingElement(), equalTo("Success!"));
  }

  // See https://code.google.com/p/selenium/issues/detail?id=5237
  @Ignore({OPERA, ANDROID, OPERA_MOBILE})
  @JavascriptEnabled
  @Test
  public void testShouldBeAbleToClickInAFrameThatRewritesTopWindowLocation() {
    driver.get(appServer.whereIs("click_tests/issue5237.html"));
    driver.switchTo().frame("search");
    driver.findElement(By.id("submit")).click();
    driver.switchTo().defaultContent();
    wait.until(titleIs("Target page for issue 5237"));
  }

  @Ignore({OPERA, ANDROID, OPERA_MOBILE, MARIONETTE})
  @Test
  public void testShouldBeAbleToClickInASubFrame() {
    driver.get(pages.framesetPage);
    driver.switchTo().frame("sixth")
        .switchTo().frame("iframe1");

    // This should replace frame "iframe1" inside frame "sixth" ...
    driver.findElement(By.id("submitButton")).click();
    // driver should still be focused on frame "iframe1" inside frame "sixth" ...
    assertThat(getTextOfGreetingElement(), equalTo("Success!"));
    // Make sure it was really frame "iframe1" inside frame "sixth" which was replaced ...
    driver.switchTo().defaultContent()
        .switchTo().frame("sixth")
        .switchTo().frame("iframe1");
    assertThat(driver.findElement(By.id("greeting")).getText(), equalTo("Success!"));
  }

  @Ignore(ANDROID)
  @Test
  public void testShouldBeAbleToFindElementsInIframesByXPath() {
    driver.get(pages.iframePage);

    driver.switchTo().frame("iframe1");

    WebElement element = driver.findElement(By.xpath("//*[@id = 'changeme']"));

    assertNotNull(element);
  }

  @Ignore({ANDROID, MARIONETTE})
  @Test
  public void testGetCurrentUrl() {
    driver.get(pages.framesetPage);

    driver.switchTo().frame("second");

    String url = appServer.whereIs("page/2");
    assertThat(driver.getCurrentUrl(), equalTo(url + "?title=Fish"));

    url = appServer.whereIs("iframes.html");
    driver.get(pages.iframePage);
    assertThat(driver.getCurrentUrl(), equalTo(url));

    url = appServer.whereIs("formPage.html");
    driver.switchTo().frame("iframe1");
    assertThat(driver.getCurrentUrl(), equalTo(url));
  }

  @Ignore(value = {ANDROID, OPERA, OPERA_MOBILE, PHANTOMJS})
  @JavascriptEnabled
  @Test
  public void testShouldBeAbleToSwitchToTheTopIfTheFrameIsDeletedFromUnderUs() {
    driver.get(appServer.whereIs("frame_switching_tests/deletingFrame.html"));

    driver.switchTo().frame("iframe1");

    WebElement killIframe = driver.findElement(By.id("killIframe"));
    killIframe.click();
    driver.switchTo().defaultContent();

    assertFrameNotPresent("iframe1");

    WebElement addIFrame = driver.findElement(By.id("addBackFrame"));
    addIFrame.click();
    wait.until(presenceOfElementLocated(By.id("iframe1")));

    driver.switchTo().frame("iframe1");

    try {
      wait.until(presenceOfElementLocated(By.id("success")));
    } catch (WebDriverException web) {
      fail("Could not find element after switching frame");
    }
  }

  @Ignore(ALL)
  @JavascriptEnabled
  @Test
  public void testShouldNotBeAbleToDoAnythingTheFrameIsDeletedFromUnderUs() {
    driver.get(appServer.whereIs("frame_switching_tests/deletingFrame.html"));

    driver.switchTo().frame("iframe1");

    WebElement killIframe = driver.findElement(By.id("killIframe"));
    killIframe.click();

    try {
      driver.findElement(By.id("killIframe")).click();
      fail("NoSuchFrameException should be thrown");
    } catch (NoSuchFrameException expected) {
    }
  }

  @Test
  @Ignore(MARIONETTE)
  public void testShouldReturnWindowTitleInAFrameset() {
    driver.get(pages.framesetPage);
    driver.switchTo().frame("third");
    assertEquals("Unique title", driver.getTitle());
  }

  @JavascriptEnabled
  @Test
  @Ignore(MARIONETTE)
  public void testJavaScriptShouldExecuteInTheContextOfTheCurrentFrame() {
    JavascriptExecutor executor = (JavascriptExecutor) driver;

    driver.get(pages.framesetPage);
    assertTrue((Boolean) executor.executeScript("return window == window.top"));
    driver.switchTo().frame("third");
    assertTrue((Boolean) executor.executeScript("return window != window.top"));
  }

  @Test
  public void testShouldNotSwitchMagicallyToTheTopWindow() {
    String baseUrl = appServer.whereIs("frame_switching_tests/");
    driver.get(baseUrl + "bug4876.html");
    driver.switchTo().frame(0);
    wait.until(presenceOfElementLocated(By.id("inputText")));

    for (int i = 0; i < 20; i++) {
      try {
        WebElement input = wait.until(presenceOfElementLocated(By.id("inputText")));
        WebElement submit = wait.until(presenceOfElementLocated(By.id("submitButton")));
        input.clear();
        input.sendKeys("rand" + new Random().nextInt());
        submit.click();
      } finally {
        String url = driver.getCurrentUrl();
        // IE6 and Chrome add "?"-symbol to the end of the URL
        if (url.endsWith("?")) {
          url = url.substring(0, url.length()-1);
        }
        assertEquals(baseUrl + "bug4876_iframe.html", url);
      }
    }
  }

  private void assertFrameNotPresent(String locator) {
    driver.switchTo().defaultContent();
    wait.until(not(frameToBeAvailableAndSwitchToIt(locator)));
    driver.switchTo().defaultContent();
  }
}
