package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
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

    WebElement webDriverUrlPort = driver.findElement(By.name("webDriverUrlPort"));
    webDriverUrlPort.clear();
    webDriverUrlPort.sendKeys(realWebDriverUrl);
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

  private void setWebPage(String webPage) {
    WebElement webPageUrl = driver.findElement(By.name("webPage"));
    webPageUrl.clear();
    webPageUrl.sendKeys(webPage);
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
