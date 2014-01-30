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

    String targetWebDriverUrl = webDriver2Service.getUrl().toExternalForm();
    page.setWebDriverUrl(targetWebDriverUrl);
  }

  @After
  public void tearDown() throws Exception {
    if (driver != null) {
      driver.quit();
      driver = null;
    }
    if (targetDriver != null) {
      targetDriver.quit();
      targetDriver = null;
    }
  }
}
