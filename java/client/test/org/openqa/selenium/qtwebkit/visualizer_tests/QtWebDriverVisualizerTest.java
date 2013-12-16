package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.qtwebkit.QtWebDriverExecutor;
import org.openqa.selenium.qtwebkit.QtWebDriverService;
import org.openqa.selenium.qtwebkit.QtWebDriverServiceExecutor;
import org.openqa.selenium.qtwebkit.QtWebKitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.TestWaiter.waitFor;
import static org.openqa.selenium.WaitingConditions.elementToExist;
import static org.openqa.selenium.WaitingConditions.elementValueToEqual;
import static org.openqa.selenium.WaitingConditions.newWindowIsOpened;
import static org.openqa.selenium.WaitingConditions.pageTitleToBe;
import static org.openqa.selenium.WaitingConditions.windowHandleCountToBe;
import static org.openqa.selenium.WaitingConditions.windowToBeSwitchedToWithName;

public class QtWebDriverVisualizerTest extends JUnit4TestBase {
  protected WebDriver driver2;
  private String webDriverJsWindowHandle;
  private String visualizerWindowHandle;

  @Before
  public void createDriver() throws Exception {
    DesiredCapabilities capabilities = DesiredCapabilities.qtwebkit();

    QtWebDriverExecutor webDriverExecutor = QtWebKitDriver.createDefaultExecutor();
    driver = new QtWebKitDriver(webDriverExecutor, capabilities);

    DriverService webDriver2Service = QtWebDriverService.createDefaultService();
    QtWebDriverExecutor webDriver2Executor = new QtWebDriverServiceExecutor(webDriver2Service);
    driver2 = new QtWebKitDriver(webDriver2Executor, capabilities);

    String realWebDriverUrl = webDriver2Service.getUrl().toExternalForm();
    driver.get(realWebDriverUrl + "/WebDriverJsDemo.html");
    webDriverJsWindowHandle = driver.getWindowHandle();

    setWebDriverUrl(realWebDriverUrl);
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    driver2.quit();
  }

  @Test
  public void canOpenLinkAndTypeText() {
    Set<String> currentWindowHandles = driver.getWindowHandles();
    setWebPage(pages.clicksPage);
    source();
    waitFor(newWindowIsOpened(driver, currentWindowHandles));

    driver2.findElement(By.id("normal")).click();
    waitFor(pageTitleToBe(driver2, "XHTML Test Page"));

    source();
    driver.switchTo().window(visualizerWindowHandle);
    waitFor(windowToBeSwitchedToWithName(driver, visualizerWindowHandle));

    String typingText = "TheTypingText";
    String expectedText = "change" + typingText;

    waitFor(elementToExist(driver, "username"));
    WebElement inputField = driver.findElement(By.id("username"));
    WebElement inputField2 = driver2.findElement(By.id("username"));

    inputField.click();
    waitFor(activeElementToBe(driver, inputField));
    waitFor(activeElementToBe(driver2, inputField2));

    inputField.sendKeys(typingText);

    waitFor(elementValueToEqual(inputField2, expectedText));
    assertThat(inputField2.getAttribute("value"), equalTo(expectedText));
  }

  @Test
  public void isClickOnLinkCorrect() {
    setWebPage(pages.clicksPage);
    source();

    driver.switchTo().window(visualizerWindowHandle);
    assertEquals(driver.getWindowHandle(), visualizerWindowHandle);

    String visualizerTitle = driver.getTitle();

    driver.findElement(By.id("normal")).click();
    waitFor(pageTitleToBe(driver2, "XHTML Test Page"));

    assertEquals("We do not proceed by links in visualizer", visualizerTitle, driver.getTitle());
  }

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
    assertTrue("Screenshot has non zero dimension", dimension.getWidth() > 0 && dimension.getHeight() > 0);
  }

  private void setWebDriverUrl(String url) {
    WebElement input = driver.findElement(By.name("webDriverUrlPort"));
    input.clear();
    input.sendKeys(url);
  }

  private void setWebPage(String webPage) {
    WebElement input = driver.findElement(By.name("webPage"));
    input.clear();
    input.sendKeys(webPage);
  }

  private void source() {
    driver.findElement(By.xpath("//input[@value='Source']")).click();
    waitFor(windowHandleCountToBe(driver, 2));

    Set<String> allWindowHandles = driver.getWindowHandles();
    assertEquals(2, allWindowHandles.size());

    visualizerWindowHandle = null;
    for (String handle : allWindowHandles) {
      if (!handle.equalsIgnoreCase(webDriverJsWindowHandle)) {
        visualizerWindowHandle = handle;
      }
    }
    assertNotNull(visualizerWindowHandle);
  }

  /**
   * Get dimension from title like 'be2a1340-3141-4d5b-a829-8d77cc57add4 (800x512 pixels)'
   */
  public static Dimension getDimensionFromTitle(String title) {
    Matcher m = Pattern.compile(".*\\((?<width>\\d+)x(?<height>\\d+) pixels\\)").matcher(title);
    if (!m.matches())
      return null;
    return new Dimension(Integer.valueOf(m.group("width")), Integer.valueOf(m.group("height")));
  }

  public static Callable<String> elementAttributeToEqual(
      final WebElement element, final String attributeName, final String expectedValue) {
    if (expectedValue == null)
      throw new IllegalArgumentException("expectedValue");

    return new Callable<String>() {

      @Override
      public String call() throws Exception {
        String actualValue = element.getAttribute(attributeName);

        if (expectedValue.equals(actualValue)) {
          return actualValue;
        }

        return null;
      }

      @Override
      public String toString() {
        return "element " + element + " attribute '" + attributeName + "' to equal '" + expectedValue + "'";
      }
    };
  }

  public static Callable<WebElement> activeElementToBe(
      final WebDriver driver, final WebElement expectedActiveElement) {
    return new Callable<WebElement>() {

      public WebElement call() throws Exception {
        WebElement activeElement = driver.switchTo().activeElement();

        if (expectedActiveElement.equals(activeElement)) {
          return activeElement;
        }

        return null;
      }

      @Override
      public String toString() {
        return "active element to be: " + expectedActiveElement;
      }
    };
  }
}
