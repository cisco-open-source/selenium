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

package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.qtwebkit.QtWebDriverExecutor;
import org.openqa.selenium.qtwebkit.QtWebKitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.*;
import static org.openqa.selenium.WaitingConditions.*;
import static org.openqa.selenium.qtwebkit.visualizer_tests.WaitingConditions.*;

public class QtWebDriverJsTest extends QtWebDriverJsBaseTest {

  @Test
  public void checkWebPageUpdateOnSessionReuse() {
    page.setWebPage(pages.clicksPage);
    page.clickGet();

    String webDriverUrlPort = page.getWebDriverUrlPort();
    WebDriver originalDriver = driver;
    try {
      QtWebDriverExecutor webDriverExecutor = QtWebKitDriver.createDefaultExecutor();
      driver = new QtWebKitDriver(webDriverExecutor, DesiredCapabilities.qtwebkit());
      page = PageFactory.initElements(driver, QtWebDriverJsPage.class);
      page.setDriver(driver);
      page.setTargetDriver(targetDriver);
      page.setWebDriverUrl(webDriverUrlPort);

      driver.findElement(By.id("getButton")).click();

      // shall retrieve web page url from previous session according to MHA-879
      wait.until(page.webPageIs(pages.clicksPage));
    } finally {
      originalDriver.quit();
    }
  }

  @Test
  public void checkSourceScreenshotButtonsDisabling() {
    WebElement sourceButton = driver.findElement(By.id("sourceButton"));
    WebElement screenshotButton = driver.findElement(By.id("screenshotButton"));

    assertFalse(sourceButton.isEnabled());
    assertFalse(screenshotButton.isEnabled());

    page.setWebPage(pages.clicksPage);

    wait.until(elementToBeEnabled(sourceButton));
    wait.until(elementToBeEnabled(screenshotButton));

    page.setWebDriverUrl("");

    wait.until(elementToBeDisabled(sourceButton));
    wait.until(elementToBeDisabled(screenshotButton));
  }

  @Test
  public void canScreenshot() {
    Set<String> originalWindowHandles = driver.getWindowHandles();
    page.setWebPage(pages.clicksPage);
    page.clickScreenshotButton();

    String newWindow = wait.until(newWindowIsOpened(originalWindowHandles));
    driver.switchTo().window(newWindow);
    Dimension dimension = VisualizerUtils.getDimensionFromTitle(driver.getTitle());
    assertTrue("Screenshot has non zero dimension",
               dimension.getWidth() > 0 && dimension.getHeight() > 0);
  }

  @Test
  public void canLogDriver() {
    page.setWebPage(pages.clicksPage);
    page.clickGet();

    Set<String> originalWindowHandles = driver.getWindowHandles();
    page.clickLogsSelect("driver");
    String newWindow = wait.until(newWindowIsOpened(originalWindowHandles));
    driver.switchTo().window(newWindow);
    assertTrue(driver.getPageSource().contains("ALL"));
    assertTrue(driver.getPageSource().contains("INFO"));
  }

  @Test
  public void canLogBrowser() {
    page.setWebPage(pages.clicksPage);
    page.clickGet();

    ((JavascriptExecutor) targetDriver).executeScript("console.log('Fingerprint...');");

    Set<String> originalWindowHandles = driver.getWindowHandles();
    driver.switchTo().window(page.getWebDriverJsWindowHandle());
    page.clickLogsSelect("browser");
    String newWindow = wait.until(newWindowIsOpened(originalWindowHandles));
    driver.switchTo().window(newWindow);
    assertTrue(driver.getPageSource().contains("Fingerprint"));
  }

  @Test
  public void canFindElement() {
    page.setWebPage(pages.clicksPage);
    page.clickGet();

    String wdcByTagName = page.findElement("tagName", "h1");
    String wdcByXPath = page.findElement("xpath", "//h1");
    assertEquals(wdcByTagName, wdcByXPath);
  }

  @Test
  public void canGetFoundElementTagNameAndText() {
    page.setWebPage(pages.simpleTestPage);
    page.clickGet();
    page.findElement("xpath", "//h1");
    assertEquals(targetDriver.findElement(By.xpath("//h1")).getTagName(), page.getFoundElementTagName());
    assertEquals(targetDriver.findElement(By.xpath("//h1")).getText(), page.getFoundElementText());
  }

  @Test
  public void canGetLocation() {
    page.setWebPage(pages.clicksPage);
    page.clickGet();
    page.findElement("id", "normal");

    Point expectedLocation = targetDriver.findElement(By.id("normal")).getLocation();
    assertEquals(expectedLocation, page.getFoundElementLocation());
  }

  @Test
  public void canGetSize() {
    page.setWebPage(pages.clicksPage);
    page.clickGet();
    page.findElement("id", "normal");

    Dimension expectedDimension = targetDriver.findElement(By.id("normal")).getSize();
    assertEquals(expectedDimension, page.getFoundElementSize());
  }

  @Test
  public void canClickAndCheckFoundElementSelected() {
    page.setWebPage(pages.simpleTestPage);
    page.clickGet();
    page.findElement("id", "checkbox1");

    assertFalse(page.isFoundElementSelected());
    page.clickElementClick();
    assertTrue(page.isFoundElementSelected());
  }

  @Test
  public void canCheckFoundElementEnabled() {
    page.setWebPage(pages.readOnlyPage);
    page.clickGet();

    page.findElement("id", "textInputnotenabled");
    assertFalse(page.isFoundElementEnabled());

    page.findElement("id", "writableTextInput");
    assertTrue(page.isFoundElementEnabled());
  }

  @Test
  public void canCheckFoundElementDisplayed() {
    page.setWebPage(pages.tables);
    page.clickGet();

    page.findElement("xpath", "//body");
    assertTrue(page.isFoundElementDisplayed());

    page.findElement("xpath", "//div[contains(@style, 'display: none;')]");
    assertFalse(page.isFoundElementDisplayed());
  }

  private ExpectedCondition<String> keyPressed(final String key) {
    return new ExpectedCondition<String>() {
      @Override
      public String apply(WebDriver driver) {
        String actualValue = getActualValue();

        if (key.equals(actualValue)) {
          return actualValue;
        }

        return null;
      }

      private String getActualValue() {
        return targetDriver.findElement(By.tagName("body")).getText();
      }

      @Override
      public String toString() {
        return "expected actual value '" + getActualValue() + "' is equal to '" + key + "'";
      }
    };
  }

  @Test
  public void canKeyPress() {
    String[] keys = {
        "Esc", "`", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=", "Bksp",
        "\u21e5 Tab", "q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "[", "]", "\\",
        "a", "s", "d", "f", "g", "h", "j", "k", "l", ";", "Enter",
        "z", "x", "c", "v", "b", "n", "m", ",", ".", "/",
        "\u21d0", "\u21d1", "\u21d3", "\u21d2"};

/*
    String[] shiftKeys = {
        "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "+",
        "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "{", "}",
        "A", "S", "D", "F", "G", "H", "J", "K", "L", ":", "\"",
        "Z", "X", "C", "V", "B", "N", "M", "<", ">", "?"
    };
*/

    page.setWebPage(pages.webdriverjsKeypressPage);
    page.clickGet();

    for (String key : keys) {
      page.keyPress(key);
      wait.until(keyPressed(key));
    }

//    page.keyPress("Shift");
//    for (String key : shiftKeys) {
//      page.keyPress(key);
//      wait.until(keyPressed(key));
//    }
  }

  @Test
  public void canListAndChooseWindows() {
    page.setWebPage(pages.clicksPage);
    page.clickGet();

    Set<String> originalWindowHandles = targetDriver.getWindowHandles();
    targetDriver.findElement(By.id("new-window")).click();
    targetWait.until(newWindowIsOpened(originalWindowHandles));

    page.clickListWindowHandles();

    Set<String> actualWindowHandles = new HashSet<String>();
    for (WebElement option : page.getWindowListSelect().getOptions()) {
      actualWindowHandles.add(option.getText().replace("(active)", "").trim());
    }

    assertEquals(targetDriver.getWindowHandles(), actualWindowHandles);

    String currentActiveWindow = targetDriver.getWindowHandle();
    String expectedActiveWindow = VisualizerUtils.findNotEqualsIgnoreCase(targetDriver.getWindowHandles(), currentActiveWindow);
    page.getWindowListSelect().selectByValue(expectedActiveWindow);
    page.clickChooseWindow();
    targetWait.until(activeWindowToBe(expectedActiveWindow));

    // Upon choose window, input field 'Web page' shall be update, MHA-879
    wait.until(page.webPageIs(pages.xhtmlTestPage));
  }

  @Test
  public void canSetWindowSize() {
    page.setWebPage(pages.clicksPage);
    page.clickGet();
    final Dimension dimension = new Dimension(200, 100);
    page.setWindowSize(dimension);
    wait.until(new ExpectedCondition<Dimension>() {
      @Override
      public Dimension apply(WebDriver driver) {
        return dimension.equals(targetDriver.manage().window().getSize()) ? dimension : null;
      }
    });
  }
}
