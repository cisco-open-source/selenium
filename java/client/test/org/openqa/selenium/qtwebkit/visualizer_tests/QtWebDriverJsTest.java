package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    driver.findElement(By.id("screenshotButton")).click();

    waitFor(newWindowIsOpened(driver, originalWindowHandles), 20, SECONDS);
    Set<String> windowHandles = driver.getWindowHandles();
    windowHandles.removeAll(originalWindowHandles);

    driver.switchTo().window(windowHandles.iterator().next());
    Dimension dimension = VisualizerUtils.getDimensionFromTitle(driver.getTitle());
    assertTrue("Screenshot has non zero dimension",
               dimension.getWidth() > 0 && dimension.getHeight() > 0);
  }

  @Test
  public void canListWindows() {
    page.setWebPage(pages.clicksPage);
    page.clickGet();

    Set<String> currentWindowHandles = driver2.getWindowHandles();
    driver2.findElement(By.id("new-window")).click();
    waitFor(newWindowIsOpened(driver2, currentWindowHandles));

    page.clickListWindowHandles();

    Set<String> actualWindowHandles = new HashSet<String>();
    for (WebElement option : page.getWindowListSelect().getOptions()) {
      actualWindowHandles.add(option.getText());
    }

    assertEquals(driver2.getWindowHandles(), actualWindowHandles);

    String currentActiveWindow = driver2.getWindowHandle();
    String expectedActiveWindow = VisualizerUtils.findNotEqualsIgnoreCase(driver2.getWindowHandles(), currentActiveWindow);
    page.getWindowListSelect().selectByValue(expectedActiveWindow);
    page.clickChooseWindow();
    waitFor(activeWindowToBe(driver2, expectedActiveWindow));
  }
}
