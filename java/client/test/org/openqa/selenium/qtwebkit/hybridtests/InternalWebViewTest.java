package org.openqa.selenium.qtwebkit.hybridtests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WaitingConditions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.util.concurrent.Callable;

import static org.junit.Assert.fail;
import static org.openqa.selenium.TestWaiter.waitFor;

public class InternalWebViewTest extends JUnit4TestBase {

  @Before
  public void setUp() throws Exception {
    driver.get("qtwidget://WindowWithEmbeddedViewTestWidget");
    inputUrl = driver.findElement(By.id("inputURL"));
    btnLoad = driver.findElement(By.id("loadButton"));
    currentWindow = driver.getWindowHandle();
  }

  @After
  public void cleanUp() {
    driver.switchTo().window(currentWindow);
  }

  @Test
  public void testCanGetUrlFromInternalWebView() {
    inputUrl.sendKeys(pages.colorPage);
    btnLoad.click();
    for (String winHandle : driver.getWindowHandles()) {
      if (!currentWindow.equals(winHandle)) {
        driver.switchTo().window(winHandle);
        waitFor(windowUrlContains(driver, "colorPage.html"));
        break;
      }
    }
  }

  @Test
  public void testCanGetTitleFromInternalWebView() {
    inputUrl.sendKeys(pages.colorPage);
    btnLoad.click();

    for (String winHandle : driver.getWindowHandles()) {
      if (!currentWindow.equals(winHandle)) {
        driver.switchTo().window(winHandle);
        waitFor(WaitingConditions.pageTitleToBe(driver, "Color Page"));
        break;
      }
    }
  }

  @Test
  public void testCanNotLoadNotExistIntoWebView() {
    inputUrl.sendKeys(pages.notExist);
    btnLoad.click();
    WebElement label = driver.findElement(By.id("labelTitle"));
    waitFor(labelContains(label, "Not Found"));
  }

  @Test
  public void testCanNotLoadWidgetIntoWebView() {
    inputUrl.sendKeys("qtwidget://FindingTestWidget");
    btnLoad.click();
    WebElement label = driver.findElement(By.id("labelTitle"));
    waitFor(labelNoContains(label, "Here Will Be HTML Title"));
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

  private Callable<Boolean> labelNoContains(final WebElement elem, final String text) {
    return new Callable<Boolean>() {
      public Boolean call() throws Exception {
        String current = elem.getText();
        return !current.contains(text);
      }
    };
  }

}
