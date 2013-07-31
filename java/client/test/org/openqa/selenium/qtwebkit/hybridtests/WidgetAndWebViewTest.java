package org.openqa.selenium.qtwebkit.hybridtests;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WaitingConditions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.TestWaiter.waitFor;

public class WidgetAndWebViewTest  extends JUnit4TestBase {

  @Before
  public void setUp() {
    driver.get("qtwidget://WidgetAndWebViewTestWindows");
    currentWindow = driver.getWindowHandle();
    inputURL = driver.findElement(By.id("inputURL"));
    button = driver.findElement(By.id("openViewButton"));
  }

  @Test
  public void testCanSwitchToNeededWebView() {
    inputURL.sendKeys(pages.colorPage);
    button.click();
    String enteredUrl = inputURL.getText();
    for (String winHandle : driver.getWindowHandles()) {
      if (!currentWindow.equals(winHandle)) {
        driver.switchTo().window(winHandle);
        waitFor(WaitingConditions.pageTitleToBe(driver, "Color Page"));
        break;
      }
    }
    assertEquals(enteredUrl, driver.getCurrentUrl());
    driver.close();
    driver.switchTo().window(currentWindow);
  }

  @Test
  public void testCanGetUrlFromWebView() {
    inputURL.sendKeys(pages.colorPage);
    button.click();
    for (String winHandle : driver.getWindowHandles()) {
      if (!currentWindow.equals(winHandle)) {
        driver.switchTo().window(winHandle);
        waitFor(windowUrlContains(driver, "colorPage.html"));
        break;
      }
    }
    driver.close();
    driver.switchTo().window(currentWindow);
    assertEquals("qtwidget://WidgetAndWebViewTestWindows", driver.getCurrentUrl());
  }

  @Test
  public void testCanGetTitleFromWebView() {
    inputURL.sendKeys(pages.colorPage);
    button.click();

    for (String winHandle : driver.getWindowHandles()) {
      if (!currentWindow.equals(winHandle)) {
        driver.switchTo().window(winHandle);
        waitFor(WaitingConditions.pageTitleToBe(driver, "Color Page"));
        break;
      }
    }
    driver.close();
    driver.switchTo().window(currentWindow);
    assertEquals("Test Widget", driver.getTitle());
  }

  @Test
  public void testCanOpenWebViewNoDownloadPage() {
    button.click();
    for (String winHandle : driver.getWindowHandles()) {
      if (!currentWindow.equals(winHandle)) {
        driver.switchTo().window(winHandle);
        break;
      }
    }
    assertEquals("about:blank", driver.getCurrentUrl());
    driver.close();
    driver.switchTo().window(currentWindow);
  }

  @Test
  public void testCanDownloadPageIntoNoParentView() {
    button.click();
    for (String winHandle : driver.getWindowHandles()) {
      if (!currentWindow.equals(winHandle)) {
        driver.switchTo().window(winHandle);
        break;
      }
    }
    driver.get(pages.colorPage);
    assertEquals("Color Page", driver.getTitle());
    driver.close();
    driver.switchTo().window(currentWindow);
  }

  @Test
  public void testCheckNumberOfWindows()  {
    assertEquals(1, driver.getWindowHandles().size());
    inputURL.sendKeys(pages.colorPage);
    button.click();
    assertEquals(2, driver.getWindowHandles().size());
    button.click();
    button.click();
    assertEquals(4, driver.getWindowHandles().size());
    for (String winHandle : driver.getWindowHandles()) {
      if (!currentWindow.equals(winHandle)) {
        driver.switchTo().window(winHandle);
        driver.close();
      }
    } driver.switchTo().window(currentWindow);
  }

  private String currentWindow;
  private WebElement inputURL;
  private WebElement button;


  private Callable<Boolean> windowUrlContains(final WebDriver webdriver, final String url) {
    return new Callable<Boolean>() {
      public Boolean call() throws Exception {
        String currentUrl = webdriver.getCurrentUrl();
        return currentUrl.contains(url);
      }
    };
  }

}
