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

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.openqa.selenium.testing.Ignore.Driver.ANDROID;
import static org.openqa.selenium.testing.Ignore.Driver.CHROME;
import static org.openqa.selenium.testing.Ignore.Driver.HTMLUNIT;
import static org.openqa.selenium.testing.Ignore.Driver.IE;
import static org.openqa.selenium.testing.Ignore.Driver.IPHONE;
import static org.openqa.selenium.testing.Ignore.Driver.MARIONETTE;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA;
import static org.openqa.selenium.testing.Ignore.Driver.PHANTOMJS;
import static org.openqa.selenium.testing.Ignore.Driver.SAFARI;
import static org.openqa.selenium.testing.Ignore.Driver.QTWEBKIT;

import org.junit.Test;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.openqa.selenium.testing.JavascriptEnabled;

@Ignore(value = {ANDROID, HTMLUNIT}, reason = "Android: Race condition when click returns, "
    + "the UI did not finish scrolling..\nHtmlUnit: Scrolling requires rendering")
public class ClickScrollingTest extends JUnit4TestBase {
  @JavascriptEnabled
  @Test
  public void testClickingOnAnchorScrollsPage() {
    String scrollScript = "";
    scrollScript += "var pageY;";
    scrollScript += "if (typeof(window.pageYOffset) == 'number') {";
    scrollScript += "  pageY = window.pageYOffset;";
    scrollScript += "} else {";
    scrollScript += "  pageY = document.documentElement.scrollTop;";
    scrollScript += "}";
    scrollScript += "return pageY;";

    driver.get(pages.macbethPage);

    driver.findElement(By.partialLinkText("last speech")).click();

    long yOffset = (Long) ((JavascriptExecutor) driver)
        .executeScript(scrollScript);

    // Focusing on to click, but not actually following,
    // the link will scroll it in to view, which is a few pixels further than 0
    assertThat("Did not scroll", yOffset, is(greaterThan(300L)));
  }

  @Test
  public void testShouldScrollToClickOnAnElementHiddenByOverflow() {
    String url = appServer.whereIs("click_out_of_bounds_overflow.html");
    driver.get(url);

    WebElement link = driver.findElement(By.id("link"));
    try {
      link.click();
    } catch (MoveTargetOutOfBoundsException e) {
      fail("Should not be out of bounds: " + e.getMessage());
    }
  }

  @Test
  @Ignore(MARIONETTE)
  public void testShouldBeAbleToClickOnAnElementHiddenByOverflow() {
    driver.get(appServer.whereIs("scroll.html"));

    WebElement link = driver.findElement(By.id("line8"));
    // This used to throw a MoveTargetOutOfBoundsException - we don't expect it to
    link.click();
    assertEquals("line8", driver.findElement(By.id("clicked")).getText());
  }

  @JavascriptEnabled
  @Ignore(value = {CHROME, IPHONE, QTWEBKIT},
          reason = "Chrome: failed, iPhone: untested, Firefox: failed with native events")
  @Test
  public void testShouldBeAbleToClickOnAnElementHiddenByDoubleOverflow() {
    driver.get(appServer.whereIs("scrolling_tests/page_with_double_overflow_auto.html"));

    driver.findElement(By.id("link")).click();
    wait.until(titleIs("Clicked Successfully!"));
  }

  @JavascriptEnabled
  @Ignore(value = {IPHONE, SAFARI, MARIONETTE}, reason = "Safari: failed, iPhone: untested")
  @Test
  public void testShouldBeAbleToClickOnAnElementHiddenByYOverflow() {
    driver.get(appServer.whereIs("scrolling_tests/page_with_y_overflow_auto.html"));

    driver.findElement(By.id("link")).click();
    wait.until(titleIs("Clicked Successfully!"));
  }

  @JavascriptEnabled
  @Ignore({OPERA})
  @Test
  public void testShouldNotScrollOverflowElementsWhichAreVisible() {
    driver.get(appServer.whereIs("scroll2.html"));
    WebElement list = driver.findElement(By.tagName("ul"));
    WebElement item = list.findElement(By.id("desired"));
    item.click();
    long yOffset =
        (Long)((JavascriptExecutor)driver).executeScript("return arguments[0].scrollTop;", list);
    assertEquals("Should not have scrolled", 0, yOffset);
  }

  @JavascriptEnabled
  @Ignore(value = {CHROME, IPHONE, PHANTOMJS, SAFARI, MARIONETTE, QTWEBKIT},
      reason = "Safari: button1 is scrolled to the bottom edge of the view, " +
          "so additonal scrolling is still required for button2")
  @Test
  public void testShouldNotScrollIfAlreadyScrolledAndElementIsInView() {
    driver.get(appServer.whereIs("scroll3.html"));
    driver.findElement(By.id("button1")).click();
    long scrollTop = getScrollTop();
    driver.findElement(By.id("button2")).click();
    assertEquals(scrollTop, getScrollTop());
  }

  @Test
  @Ignore(MARIONETTE)
  public void testShouldBeAbleToClickRadioButtonScrolledIntoView() {
    driver.get(appServer.whereIs("scroll4.html"));
    driver.findElement(By.id("radio")).click();
    // If we don't throw, we're good
  }
  
  @Ignore(value = {IE, MARIONETTE}, reason = "IE has special overflow handling")
  @Test
  public void testShouldScrollOverflowElementsIfClickPointIsOutOfViewButElementIsInView() {
    driver.get(appServer.whereIs("scroll5.html"));
    driver.findElement(By.id("inner")).click();
    assertEquals("clicked", driver.findElement(By.id("clicked")).getText());
  }

  @Test
  @Ignore(value = {OPERA, IPHONE, SAFARI, MARIONETTE},
          reason = "Opera: fails, others: not tested")
  public void testShouldBeAbleToClickElementInAFrameThatIsOutOfView() {
    try {
      driver.get(appServer.whereIs("scrolling_tests/page_with_frame_out_of_view.html"));
      driver.switchTo().frame("frame");
      WebElement element = driver.findElement(By.name("checkbox"));
      element.click();
      assertTrue(element.isSelected());
    } finally {
      driver.switchTo().defaultContent();
    }
  }
  
  @Test
  @Ignore(value = {OPERA, IPHONE, SAFARI, MARIONETTE},
          reason = "Opera: fails, others: not tested")
  public void testShouldBeAbleToClickElementThatIsOutOfViewInAFrame() {
    try {
      driver.get(appServer.whereIs("scrolling_tests/page_with_scrolling_frame.html"));
      driver.switchTo().frame("scrolling_frame");
      WebElement element = driver.findElement(By.name("scroll_checkbox"));
      element.click();
      assertTrue(element.isSelected());
    } finally {
      driver.switchTo().defaultContent();
    }
  }
  
  @Test(expected = MoveTargetOutOfBoundsException.class)
  @Ignore(reason = "All tested browses scroll non-scrollable frames")
  public void testShouldNotBeAbleToClickElementThatIsOutOfViewInANonScrollableFrame() {
    try {
      driver.get(appServer.whereIs("scrolling_tests/page_with_non_scrolling_frame.html"));
      driver.switchTo().frame("scrolling_frame");
      WebElement element = driver.findElement(By.name("scroll_checkbox"));
      element.click();
    } finally {
      driver.switchTo().defaultContent();
    }
  }
  
  @Test
  @Ignore(value = {OPERA, IPHONE, SAFARI, MARIONETTE},
          reason = "Opera: fails, others: not tested")
  public void testShouldBeAbleToClickElementThatIsOutOfViewInAFrameThatIsOutOfView() {
    try {
      driver.get(appServer.whereIs("scrolling_tests/page_with_scrolling_frame_out_of_view.html"));
      driver.switchTo().frame("scrolling_frame");
      WebElement element = driver.findElement(By.name("scroll_checkbox"));
      element.click();
      assertTrue(element.isSelected());
    } finally {
      driver.switchTo().defaultContent();
    }
  }
  
  @Test
  @Ignore(value = {OPERA, IPHONE, SAFARI, MARIONETTE},
          reason = "Opera: fails, others: not tested")
  public void testShouldBeAbleToClickElementThatIsOutOfViewInANestedFrame() {
    try {
      driver.get(appServer.whereIs("scrolling_tests/page_with_nested_scrolling_frames.html"));
      driver.switchTo().frame("scrolling_frame");
      driver.switchTo().frame("nested_scrolling_frame");
      WebElement element = driver.findElement(By.name("scroll_checkbox"));
      element.click();
      assertTrue(element.isSelected());
    } finally {
      driver.switchTo().defaultContent();
    }
  }
  
  @Test
  @Ignore(value = {OPERA, IPHONE, SAFARI, MARIONETTE},
          reason = "Opera: fails, others: not tested")
  public void testShouldBeAbleToClickElementThatIsOutOfViewInANestedFrameThatIsOutOfView() {
    try {
      driver.get(appServer.whereIs("scrolling_tests/page_with_nested_scrolling_frames_out_of_view.html"));
      driver.switchTo().frame("scrolling_frame");
      driver.switchTo().frame("nested_scrolling_frame");
      WebElement element = driver.findElement(By.name("scroll_checkbox"));
      element.click();
      assertTrue(element.isSelected());
    } finally {
      driver.switchTo().defaultContent();
    }
  }

  @JavascriptEnabled
  @Test
  public void testShouldNotScrollWhenGettingElementSize() {
    driver.get(appServer.whereIs("scroll3.html"));
    long scrollTop = getScrollTop();
    driver.findElement(By.id("button1")).getSize();
    assertEquals(scrollTop, getScrollTop());
  }

  private long getScrollTop() {
    return (Long)((JavascriptExecutor)driver).executeScript("return document.body.scrollTop;");
  }

  @Test
  @Ignore(value = {OPERA, IPHONE, SAFARI, MARIONETTE},
          reason = "Not tested")
  public void testShouldBeAbleToClickElementInATallFrame() {
    try {
      driver.get(appServer.whereIs("scrolling_tests/page_with_tall_frame.html"));
      driver.switchTo().frame("tall_frame");
      WebElement element = driver.findElement(By.name("checkbox"));
      element.click();
      assertTrue(element.isSelected());
    } finally {
      driver.switchTo().defaultContent();
    }
  }

}
