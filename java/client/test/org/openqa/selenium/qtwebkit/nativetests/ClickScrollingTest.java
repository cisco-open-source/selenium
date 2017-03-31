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


package org.openqa.selenium.qtwebkit.nativetests;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JUnit4TestBase;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.openqa.selenium.testing.Ignore.Driver.ANDROID;
import static org.openqa.selenium.testing.Ignore.Driver.HTMLUNIT;

@Ignore(value = {ANDROID, HTMLUNIT}, reason = "Android: Race condition when click returns, "
    + "the UI did not finish scrolling..\nHtmlUnit: Scrolling requires rendering")
public class ClickScrollingTest extends JUnit4TestBase {
    @Before
    public void setUp() throws Exception {
        driver.get("qtwidget://ClickScrollingTestWidget");
    }

  @Test
  public void testShouldScrollToClickOnAnElementHiddenByOverflow() {
    WebElement buttonHiddenByOverflow = driver.findElement(By.id("buttonHiddenByOverflow"));
    try {
      buttonHiddenByOverflow.click();
    } catch (MoveTargetOutOfBoundsException e) {
      fail("Should not be out of bounds: " + e.getMessage());
    }
  }

  @Test
  public void testShouldBeAbleToClickOnAnElementHiddenByOverflow() {
      WebElement buttonHiddenByOverflow = driver.findElement(By.id("buttonHiddenByOverflow"));
      buttonHiddenByOverflow.click();
      assertEquals("clicked", driver.findElement(By.id("buttonHiddenByOverflow")).getText());
  }

  @Test
  public void testShouldBeAbleToClickRadioButtonScrolledIntoView() {
    driver.findElement(By.id("visibleRadioButton")).click();
    // If we don't throw, we're good
  }
  
  @Test
  public void testShouldScrollOverflowElementsIfClickPointIsOutOfViewButElementIsInView() {
    driver.findElement(By.id("partiallyVisibleRadioButton")).click();
    assertEquals("clicked", driver.findElement(By.id("partiallyVisibleRadioButton")).getText());
  }

  @Test(expected = MoveTargetOutOfBoundsException.class)
  public void testShouldNotBeAbleToClickElementThatIsOutOfViewInANonScrollableWidget() {
      WebElement element = driver.findElement(By.id("outOfViewButton"));
      element.click();
  }
}
