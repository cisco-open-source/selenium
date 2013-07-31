package org.openqa.selenium.qtwebkit.hybridtests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.qtwebkit.QtWebDriverExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class CapabilitiesTest extends JUnit4TestBase {

  @Before
  public void createDriver() {
    desiredCapabilities = DesiredCapabilities.qtwebkit();
    requiredCapabilities = null;
  }

  @After
  public void cleanUp() {
    if (null == driver)
      return;
    driver.quit();
  }

  @Test
  public void testCanStartNeededWidgetWithDesiredCapabilities() {
    desiredCapabilities.setCapability("browserClass", "XPathElementFindingTestWidget");
    driver = CreateWebDriver();
    assertEquals(driver.getTitle(), "Application Window");
  }

  @Test
  public void testCanStartNeededWidgetWithRequiredCapabilities() {
    requiredCapabilities = new DesiredCapabilities();
    requiredCapabilities.setCapability("browserClass", "XPathElementFindingTestWidget");
    driver = CreateWebDriver();
    assertEquals(driver.getTitle(), "Application Window");
  }


  @Test
  public void testCheckNotSetDesiredCapabilities() {
    desiredCapabilities.setCapability("browserStartWindow", "XPathElementFindingTestWidget");
    driver = CreateWebDriver();
    assertNull(desiredCapabilities.getCapability("browserClass"));
  }

  @Test
  public void testIgnoreDesiredCapabilitiesBrowserStartWindowIfWindowNotFound() {
    desiredCapabilities.setCapability("browserStartWindow", "XPathElementFindingTestWidget");
    driver = CreateWebDriver();
    assertNotNull(driver.getWindowHandles().size());
    assertEquals(driver.getTitle(), "");
  }

  @Test
  public void testCanNotStartedWithRequiredCapabilitiesBrowserStartWindowIfWindowNotFound() {
    requiredCapabilities = new DesiredCapabilities();
    requiredCapabilities.setCapability("browserStartWindow", "FindingTestWidget");
    try {
    driver = CreateWebDriver();
      fail("Can't start session if windows not found");
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testCanNotStartWithRequiredCapabilitiesBrowserStartWindowIfNotOpenedWindowYet() {
    requiredCapabilities = new DesiredCapabilities();
    requiredCapabilities.setCapability("browserStartWindow", "*");
    try {
      driver = CreateWebDriver();
      fail("Can't start session if windows not found");
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testMustStartWindowWithRequiredCapabilitiesIfSetDesiredToo() {
    requiredCapabilities = new DesiredCapabilities();
    requiredCapabilities.setCapability("browserClass", "XPathElementFindingTestWidget");
    desiredCapabilities.setCapability("browserClass", "FindingTestWidget");
    driver = CreateWebDriver();
    assertEquals(driver.getTitle(), "Application Window");
  }

  @Test
  public void testCanNotStarNoExistingWidget() {
    desiredCapabilities.setCapability("browserClass", "NoExistingWidget");
    try {
      driver = CreateWebDriver();
      fail("Can't start not existing widget");
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
  }

  private DesiredCapabilities desiredCapabilities;
  private DesiredCapabilities requiredCapabilities;

  private WebDriver CreateWebDriver() throws RuntimeException {
    URL hostURL;
    try {
      hostURL = new URL("http://localhost:9517");
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return null;
    }
    QtWebDriverExecutor executor = new QtWebDriverExecutor(hostURL);
    return new RemoteWebDriver(executor, desiredCapabilities, requiredCapabilities);
  }

}
