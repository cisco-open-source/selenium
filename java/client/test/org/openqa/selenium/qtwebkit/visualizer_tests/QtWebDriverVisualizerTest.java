package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.openqa.selenium.TestWaiter.waitFor;
import static org.openqa.selenium.WaitingConditions.*;
import static org.openqa.selenium.qtwebkit.visualizer_tests.WaitingConditions.activeElementToBe;

public class QtWebDriverVisualizerTest extends QtWebDriverJsBaseTest {

  @Test
  public void canOpenLinkAndTypeText() {
    Set<String> originalWindowHandles = driver.getWindowHandles();
    page.setWebPage(pages.clicksPage);
    page.clickSource();
    waitFor(newWindowIsOpened(driver, originalWindowHandles));

    targetDriver.findElement(By.id("normal")).click();
    waitFor(pageTitleToBe(targetDriver, "XHTML Test Page"));

    driver.switchTo().window(page.clickSource());

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
    driver.switchTo().window(page.clickSource());

    String visualizerTitle = driver.getTitle();

    driver.findElement(By.id("normal")).click();
    waitFor(pageTitleToBe(targetDriver, "XHTML Test Page"));

    assertEquals("We do not proceed by links in visualizer", visualizerTitle, driver.getTitle());
  }
}
