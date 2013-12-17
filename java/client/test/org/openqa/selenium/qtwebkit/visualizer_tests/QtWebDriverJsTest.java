package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.junit.Test;
import org.openqa.selenium.*;

import java.util.HashSet;
import java.util.Set;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.*;
import static org.openqa.selenium.TestWaiter.waitFor;
import static org.openqa.selenium.WaitingConditions.*;
import static org.openqa.selenium.qtwebkit.visualizer_tests.WaitingConditions.*;

public class QtWebDriverJsTest extends QtWebDriverJsBaseTest {

  @Test
  public void checkSourceScreenshotButtonsDisabling() {
    WebElement sourceButton = driver.findElement(By.id("sourceButton"));
    WebElement screenshotButton = driver.findElement(By.id("screenshotButton"));

    assertFalse(sourceButton.isEnabled());
    assertFalse(screenshotButton.isEnabled());

    page.setWebPage(pages.clicksPage);

    waitFor(elementToBeEnabled(sourceButton));
    waitFor(elementToBeEnabled(screenshotButton));

    page.setWebDriverUrl("");

    waitFor(elementToBeDisabled(sourceButton));
    waitFor(elementToBeDisabled(screenshotButton));
  }

  @Test
  public void canScreenshot() {
    Set<String> originalWindowHandles = driver.getWindowHandles();
    page.setWebPage(pages.clicksPage);
    page.clickScreenshotButton();

    String newWindow = waitFor(newWindowIsOpened(driver, originalWindowHandles), 20, SECONDS);
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
    String newWindow = waitFor(newWindowIsOpened(driver, originalWindowHandles));
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
    driver.switchTo().window(getWebDriverJsWindowHandle());
    page.clickLogsSelect("browser");
    String newWindow = waitFor(newWindowIsOpened(driver, originalWindowHandles));
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
  public void canListWindows() {
    page.setWebPage(pages.clicksPage);
    page.clickGet();

    Set<String> originalWindowHandles = targetDriver.getWindowHandles();
    targetDriver.findElement(By.id("new-window")).click();
    waitFor(newWindowIsOpened(targetDriver, originalWindowHandles));

    page.clickListWindowHandles();

    Set<String> actualWindowHandles = new HashSet<String>();
    for (WebElement option : page.getWindowListSelect().getOptions()) {
      actualWindowHandles.add(VisualizerUtils.trimString(option.getText(), "(active)").trim());
    }

    assertEquals(targetDriver.getWindowHandles(), actualWindowHandles);

    String currentActiveWindow = targetDriver.getWindowHandle();
    String expectedActiveWindow = VisualizerUtils.findNotEqualsIgnoreCase(targetDriver.getWindowHandles(), currentActiveWindow);
    page.getWindowListSelect().selectByValue(expectedActiveWindow);
    page.clickChooseWindow();
    waitFor(activeWindowToBe(targetDriver, expectedActiveWindow));
  }
}
