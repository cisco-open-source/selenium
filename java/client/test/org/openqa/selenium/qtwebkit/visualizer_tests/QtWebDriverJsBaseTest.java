package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.qtwebkit.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.testing.JUnit4TestBase;

public class QtWebDriverJsBaseTest extends JUnit4TestBase {

  protected WebDriver driver2;
  private String webDriverJsWindowHandle;

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

  public void setWebDriverUrl(String url) {
    WebElement input = driver.findElement(By.name("webDriverUrlPort"));
    input.clear();
    input.sendKeys(url);
  }

  public void setWebPage(String webPage) {
    WebElement input = driver.findElement(By.name("webPage"));
    input.clear();
    input.sendKeys(webPage);
  }

  public String getWebDriverJsWindowHandle() {
    return webDriverJsWindowHandle;
  }
}
