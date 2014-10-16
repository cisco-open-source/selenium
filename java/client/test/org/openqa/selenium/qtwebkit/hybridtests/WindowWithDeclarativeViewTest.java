/****************************************************************************
**
** Copyright © 1992-2014 Cisco and/or its affiliates. All rights reserved.
** All rights reserved.
** 
** $CISCO_BEGIN_LICENSE:APACHE$
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
** http://www.apache.org/licenses/LICENSE-2.0
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
**
** $CISCO_END_LICENSE$
**
****************************************************************************/

package org.openqa.selenium.qtwebkit.hybridtests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.util.concurrent.Callable;

import static org.junit.Assert.fail;
import static org.openqa.selenium.TestWaiter.waitFor;

public class WindowWithDeclarativeViewTest extends JUnit4TestBase {

  @Before
  public void setUp() throws Exception {
    driver.get("qtwidget://WindowWithDeclarativeViewTestWidget");
    inputUrl = driver.findElement(By.id("inputURL"));
    btnLoad = driver.findElement(By.id("loadButton"));
    currentWindow = driver.getWindowHandle();
  }

  @After
  public void cleanUp() {
    driver.switchTo().window(currentWindow);
  }

  @Test
  public void testCanGetUrlFromInternalDeclarativeView() {
    inputUrl.sendKeys(appServer.whereIs("quick1/FindingTest.qml"));
    btnLoad.click();
    for (String winHandle : driver.getWindowHandles()) {
      if (!currentWindow.equals(winHandle)) {
        driver.switchTo().window(winHandle);
        waitFor(windowUrlContains(driver, "FindingTest.qml"));
        break;
      }
    }
  }

  @Test
  public void testCanNotLoadNotExistIntoDeclarativeView() {
    inputUrl.sendKeys(appServer.whereIs("quick1/NoExist.qml"));
    btnLoad.click();
    WebElement label = driver.findElement(By.id("labelStatus"));
    waitFor(labelContains(label, "Error"));
  }

  @Test
  public void testCanNotLoadWidgetIntoDeclarativeView() {
    inputUrl.sendKeys("qtwidget://FindingTestWidget");
    btnLoad.click();
    WebElement label = driver.findElement(By.id("labelStatus"));
    waitFor(labelContains(label, "Error"));
  }

  @Test
  public void testCanNotResizeInternalView() {
    for (String winHandle : driver.getWindowHandles()) {
      if (!currentWindow.equals(winHandle)) {
        driver.switchTo().window(winHandle);
        break;
      }
    }

    WebDriver.Window window = driver.manage().window();
    try {
      window.setSize(new Dimension(200, 200));
      fail("Should not have succeeded");
    } catch (RuntimeException e) {
      //expected
    }
  }

  @Test
  public void testCanNotCloseInternalView() {
    for (String winHandle : driver.getWindowHandles()) {
      if (!currentWindow.equals(winHandle)) {
        driver.switchTo().window(winHandle);
        break;
      }
    }

    try {
      driver.close();
      fail("Should not have succeeded");
    } catch (RuntimeException e) {
      //expected
    }
  }

  @Test
  public void testCanNotMoveInternalView() {
    for (String winHandle : driver.getWindowHandles()) {
      if (!currentWindow.equals(winHandle)) {
        driver.switchTo().window(winHandle);
        break;
      }
    }

    WebDriver.Window window = driver.manage().window();
    try {
      window.setPosition(new Point(50, 50));
      fail("Should not have succeeded");
    } catch (RuntimeException e) {
      //expected
    }
  }

  @Test
  public void testCanNotMaximizeInternalView() {
    for (String winHandle : driver.getWindowHandles()) {
      if (!currentWindow.equals(winHandle)) {
        driver.switchTo().window(winHandle);
        break;
      }
    }

    WebDriver.Window window = driver.manage().window();
    try {
      window.maximize();
      fail("Should not have succeeded");
    } catch (RuntimeException e) {
      // expected
    }
  }

  private WebElement inputUrl;
  private WebElement btnLoad;
  private String currentWindow;

  private Callable<Boolean> windowUrlContains(final WebDriver webdriver, final String url) {
    return new Callable<Boolean>() {
      public Boolean call() throws Exception {
        String currentUrl = webdriver.getCurrentUrl();
        return currentUrl.contains(url);
      }
    };
  }

  private Callable<Boolean> labelContains(final WebElement elem, final String text) {
    return new Callable<Boolean>() {
      public Boolean call() throws Exception {
        String current = elem.getText();
        return current.contains(text);
      }
    };
  }

}
