package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.junit.*;

import org.openqa.selenium.*;
import org.openqa.selenium.qtwebkit.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.openqa.selenium.testing.NeedsLocalEnvironment;

import java.net.URL;
import java.util.Set;
import java.util.concurrent.Callable;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.openqa.selenium.TestWaiter.waitFor;
import static org.openqa.selenium.WaitingConditions.*;

public class QtWebDriverVisualizerTest extends JUnit4TestBase {
  protected WebDriver driver2;
  private String wdIP = "http://localhost:9517";
  private String wd2IP = "http://localhost:9520";

  @Before
  public void createDriver() throws Exception {
    DesiredCapabilities capabilities = DesiredCapabilities.qtwebkit();

    System.setProperty(QtWebDriverService.QT_DRIVER_EXE_PROPERTY, wdIP);
    QtWebDriverExecutor webDriverExecutor = QtWebKitDriver.createDefaultExecutor();
    driver = new QtWebKitDriver(webDriverExecutor, capabilities);

    System.setProperty(QtWebDriverService.QT_DRIVER_EXE_PROPERTY, wd2IP);
    QtWebDriverExecutor webDriver2Executor = QtWebKitDriver.createDefaultExecutor();
    driver2 = new QtWebKitDriver(webDriver2Executor, capabilities);
  }

  @After
  public void theerDown() throws Exception {
    driver.quit();
    driver2.quit();
  }

  @NeedsLocalEnvironment
  @Test
  public void canOpenLinkAndTypeText() {
    driver.get(wd2IP + "/WebDriverJsDemo.html");

    WebElement webDriverUrlPort = driver.findElement(By.name("webDriverUrlPort"));
    webDriverUrlPort.clear();
    webDriverUrlPort.sendKeys(wd2IP);

    WebElement webPage = driver.findElement(By.name("webPage"));
    webPage.clear();
    webPage.sendKeys(pages.clicksPage);

    String currentHandle = driver.getWindowHandle();
    Set<String> currentWindowHandles = driver.getWindowHandles();

    driver.findElement(By.xpath("//input[@value='Source']")).click();
    waitFor(newWindowIsOpened(driver, currentWindowHandles));

    driver2.findElement(By.id("normal")).click();
    waitFor(pageTitleToBe(driver2, "XHTML Test Page"));

    driver.findElement(By.xpath("//input[@value='Source']")).click();
    waitFor(windowHandleCountToBe(driver, 2));

    Set<String> allWindowHandles = driver.getWindowHandles();
    assertEquals(2, allWindowHandles.size());
    String handle = (String) allWindowHandles.toArray()[1];

    if (currentHandle.equalsIgnoreCase(handle)) {
      handle = (String) allWindowHandles.toArray()[0];
    }

    driver.switchTo().window(handle);
    waitFor(windowToBeSwitchedToWithName(driver, handle));

    String typingText = "TheTypingText";
    String expectedText = "change" + typingText;

    waitFor(elementToExist(driver, "username"));
    WebElement inputField = driver.findElement(By.id("username"));

    inputField.click();
    waitFor(activeElementToBe(driver, inputField));

    inputField.sendKeys(typingText);
    inputField.click();

    WebElement inputField2 = driver2.findElement(By.id("username"));
    waitFor(elementValueToEqual(inputField2, expectedText));
    assertThat(inputField2.getAttribute("value"), equalTo(expectedText));
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
