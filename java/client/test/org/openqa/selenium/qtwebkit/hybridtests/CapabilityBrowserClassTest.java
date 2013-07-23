package org.openqa.selenium.qtwebkit.hybridtests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CapabilityBrowserClassTest extends JUnit4TestBase {

  @Before
  public void createDriver() {
    capabilities = DesiredCapabilities.qtwebkit();
  }

  @After
  public void cleanUp() {
    if (null == driver)
      return;
    driver.quit();
  }

  @Test
  public void testCanStartWidgetWithDesiredCapabilities() {
    capabilities.setCapability("browserClass", "XPathElementFindingTestWidget");
    driver = CreateWebDriver();
    assertEquals(capabilities.getCapability("browserClass"), "XPathElementFindingTestWidget");
  }

  @Test
  public void testCanStartNeededWidgetWithDesiredCapabilities() {
    capabilities.setCapability("browserClass", "XPathElementFindingTestWidget");
    driver = CreateWebDriver();
    assertEquals(driver.getTitle(), "Application Window");
  }

  @Test
  public void testCheckNotSetDesiredCapabilities() {
    capabilities.setCapability("browserStartWindow", "XPathElementFindingTestWidget");
    driver = CreateWebDriver();
    assertEquals(capabilities.getCapability("browserClass"), null);
  }

  @Test
  public void testCheckNumberWindowStartedWithDesiredCapabilities() {
    capabilities.setCapability("browserStartWindow", "XPathElementFindingTestWidget");
    driver = CreateWebDriver();
    assertNotNull(driver.getWindowHandles().size());
    assertEquals(driver.getWindowHandles().size(), 1);
  }

  @Test
  public void testCanNotStarNoExistingWidget() {
    capabilities.setCapability("browserClass", "NoExistingWidget");
    try {
      driver = CreateWebDriver();
      assertEquals(driver.getWindowHandles().size(), 0);
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
  }
  private DesiredCapabilities capabilities;

  private WebDriver CreateWebDriver() throws RuntimeException {
    URL hostURL;
    try {
      hostURL = new URL("http://localhost:9517");
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return null;
    }
    return new RemoteWebDriver(hostURL, capabilities);
  }

}
