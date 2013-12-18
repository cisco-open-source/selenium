package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.qtwebkit.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.testing.JUnit4TestBase;

public class QtWebDriverJsBaseTest extends JUnit4TestBase {

  protected QtWebDriverJsPage page;
  protected WebDriver targetDriver;
  private String webDriverJsWindowHandle;

  @Before
  public void createDriver() throws Exception {
    DesiredCapabilities capabilities = DesiredCapabilities.qtwebkit();

    QtWebDriverExecutor webDriverExecutor = QtWebKitDriver.createDefaultExecutor();
    driver = new QtWebKitDriver(webDriverExecutor, capabilities);
    page = PageFactory.initElements(driver, QtWebDriverJsPage.class);
    page.setDriver(driver);

    DriverService webDriver2Service = QtWebDriverService.createDefaultService();
    QtWebDriverExecutor webDriver2Executor = new QtWebDriverServiceExecutor(webDriver2Service);
    targetDriver = new QtWebKitDriver(webDriver2Executor, capabilities);
    page.setTargetDriver(targetDriver);

    String realWebDriverUrl = webDriver2Service.getUrl().toExternalForm();
    driver.get(realWebDriverUrl + "/WebDriverJsDemo.html");
    webDriverJsWindowHandle = driver.getWindowHandle();

    page.setWebDriverUrl(realWebDriverUrl);
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    targetDriver.quit();
  }

  public String getWebDriverJsWindowHandle() {
    return webDriverJsWindowHandle;
  }
}
