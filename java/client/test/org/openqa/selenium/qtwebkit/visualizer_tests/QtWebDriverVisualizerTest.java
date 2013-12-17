package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.openqa.selenium.TestWaiter.waitFor;
import static org.openqa.selenium.WaitingConditions.*;
import static org.openqa.selenium.qtwebkit.visualizer_tests.WaitingConditions.activeElementToBe;

public class QtWebDriverVisualizerTest extends QtWebDriverJsBaseTest {

  private String visualizerWindowHandle;

  @Test
  public void canOpenLinkAndTypeText() {
    Set<String> originalWindowHandles = driver.getWindowHandles();
    page.setWebPage(pages.clicksPage);
    source();
    waitFor(newWindowIsOpened(driver, originalWindowHandles));

    targetDriver.findElement(By.id("normal")).click();
    waitFor(pageTitleToBe(targetDriver, "XHTML Test Page"));

    source();
    driver.switchTo().window(visualizerWindowHandle);
    waitFor(windowToBeSwitchedToWithName(driver, visualizerWindowHandle));

    String typingText = "TheTypingText";
    String expectedText = "change" + typingText;

    waitFor(elementToExist(driver, "username"));
    WebElement inputField = driver.findElement(By.id("username"));
    WebElement inputField2 = targetDriver.findElement(By.id("username"));

    inputField.click();
    waitFor(activeElementToBe(driver, inputField));
    waitFor(activeElementToBe(targetDriver, inputField2));

    inputField.sendKeys(typingText);

    waitFor(elementValueToEqual(inputField2, expectedText));
    assertThat(inputField2.getAttribute("value"), equalTo(expectedText));
  }

  @Test
  public void isClickOnLinkCorrect() {
    page.setWebPage(pages.clicksPage);
    source();

    driver.switchTo().window(visualizerWindowHandle);
    assertEquals(driver.getWindowHandle(), visualizerWindowHandle);

    String visualizerTitle = driver.getTitle();

    driver.findElement(By.id("normal")).click();
    waitFor(pageTitleToBe(targetDriver, "XHTML Test Page"));

    assertEquals("We do not proceed by links in visualizer", visualizerTitle, driver.getTitle());
  }

  private void source() {
    driver.findElement(By.xpath("//input[@value='Source']")).click();
    waitFor(windowHandleCountToBe(driver, 2));

    Set<String> allWindowHandles = driver.getWindowHandles();
    assertEquals(2, allWindowHandles.size());

    visualizerWindowHandle = VisualizerUtils.findNotEqualsIgnoreCase(allWindowHandles, getWebDriverJsWindowHandle());
    assertNotNull(visualizerWindowHandle);
  }
}
