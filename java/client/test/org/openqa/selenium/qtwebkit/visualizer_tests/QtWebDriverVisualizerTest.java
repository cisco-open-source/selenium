package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TestWaiter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.multitouch.MultiTouchActions;

import java.util.Set;
import java.util.concurrent.Callable;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.openqa.selenium.WaitingConditions.*;
import static org.openqa.selenium.qtwebkit.visualizer_tests.WaitingConditions.activeElementToBe;

public class QtWebDriverVisualizerTest extends QtWebDriverJsBaseTest {

  private static final long TIME_OUT = 20;

  public static <X> X waitFor(Callable<X> until) {
    return TestWaiter.waitFor(until, TIME_OUT, SECONDS);
  }

  private void rotateElement(String elementId, int angle) {
    WebElement toRotate = driver.findElement(By.id(elementId));
    Action rotate = new MultiTouchActions(driver).pinchRotate(toRotate, angle).build();
    rotate.perform();
  }

  private void zoomElement(String elementId, double scale) {
    WebElement toZoom = driver.findElement(By.id(elementId));
    Action zoom = new MultiTouchActions(driver).pinchZoom(toZoom, scale).build();
    zoom.perform();
  }

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

  @Test
  public void canPinchRotate() {
    page.setWebPage(pages.pinchTouchTest);
    driver.switchTo().window(page.clickSource());

    rotateElement("picture", 35);
    WebElement result = targetDriver.findElement(By.id("result_rotate"));
    waitFor(elementTextToEqual(result, "35"));
  }

  @Test
  public void canPinchRotateNegativeDegree() {
    page.setWebPage(pages.pinchTouchTest);
    driver.switchTo().window(page.clickSource());

    rotateElement("picture", -35);
    WebElement result = targetDriver.findElement(By.id("result_rotate"));
    waitFor(org.openqa.selenium.WaitingConditions.elementTextToEqual(result, "-35"));
  }

  @Test
  public void canPinchZoomOut() {
    page.setWebPage(pages.pinchTouchTest);
    driver.switchTo().window(page.clickSource());

    zoomElement("picture", 2.5);
    WebElement result = targetDriver.findElement(By.id("result_scale"));
    waitFor(org.openqa.selenium.WaitingConditions.elementTextToEqual(result, "2.5"));
  }

  @Test
  public void canPinchZoomIn() {
    page.setWebPage(pages.pinchTouchTest);
    driver.switchTo().window(page.clickSource());

    zoomElement("picture", 0.5);
    WebElement result = targetDriver.findElement(By.id("result_scale"));
    waitFor(org.openqa.selenium.WaitingConditions.elementTextToEqual(result, "0.5"));
  }
}
