package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.TestWaiter.waitFor;
import static org.openqa.selenium.WaitingConditions.newWindowIsOpened;
import static org.openqa.selenium.qtwebkit.visualizer_tests.WaitingConditions.elementAttributeToEqual;

public class QtWebDriverJsTest extends QtWebDriverJsBaseTest {

  @Test
  public void checkSourceScreenshotButtonsDisabling() {
    WebElement sourceButton = driver.findElement(By.id("sourceButton"));
    WebElement screenshotButton = driver.findElement(By.id("screenshotButton"));

    assertEquals("true", sourceButton.getAttribute("disabled"));
    assertEquals("true", screenshotButton.getAttribute("disabled"));

    setWebPage(pages.clicksPage);

    assertNull(sourceButton.getAttribute("disabled"));
    assertNull(screenshotButton.getAttribute("disabled"));

    setWebDriverUrl("");

    waitFor(elementAttributeToEqual(sourceButton, "disabled", "true"));
    waitFor(elementAttributeToEqual(screenshotButton, "disabled", "true"));
  }

  @Test
  public void canScreenshot() {
    Set<String> originalWindowHandles = driver.getWindowHandles();
    setWebPage(pages.clicksPage);
    driver.findElement(By.id("screenshotButton")).click();

    waitFor(newWindowIsOpened(driver, originalWindowHandles));
    Set<String> windowHandles = driver.getWindowHandles();
    windowHandles.removeAll(originalWindowHandles);

    driver.switchTo().window(windowHandles.iterator().next());
    Dimension dimension = getDimensionFromTitle(driver.getTitle());
    assertTrue("Screenshot has non zero dimension",
               dimension.getWidth() > 0 && dimension.getHeight() > 0);
  }

  /**
   * Get dimension from title like 'be2a1340-3141-4d5b-a829-8d77cc57add4 (800x512 pixels)'
   */
  private static Dimension getDimensionFromTitle(String title) {
    Matcher m = Pattern.compile(".*\\((?<width>\\d+)x(?<height>\\d+) pixels\\)").matcher(title);
    if (!m.matches())
      return null;
    return new Dimension(Integer.valueOf(m.group("width")), Integer.valueOf(m.group("height")));
  }
}
