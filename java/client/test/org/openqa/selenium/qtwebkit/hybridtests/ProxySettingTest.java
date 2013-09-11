package org.openqa.selenium.qtwebkit.hybridtests;

import com.google.common.io.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.TemporaryFilesystem;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.openqa.selenium.testing.ProxyServer;
import org.openqa.selenium.testing.TestUtilities;
import org.openqa.selenium.testing.drivers.WebDriverBuilder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.openqa.selenium.remote.CapabilityType.PROXY;

public class ProxySettingTest extends JUnit4TestBase {

  private ProxyServer proxyServer;

  @Before
  public void newProxyInstance() {
    proxyServer = new ProxyServer();
  }

  @Before
  public void avoidRemote() {
    // TODO: Resolve why these tests don't work on the remote server
    assumeTrue(TestUtilities.isLocal());
  }

  @Before
  public void setUp() throws Exception {
    if (null != driver)
      JUnit4TestBase.removeDriver();
  }

  @After
  public void deleteProxyInstance() {
    if (proxyServer != null) {
      proxyServer.destroy();
    }
  }

  @Test
  public void canConfigureProxyWithRequiredCapability() throws InterruptedException {

    Proxy proxyToUse = proxyServer.asProxy();
    DesiredCapabilities requiredCaps = new DesiredCapabilities();
    requiredCaps.setCapability(PROXY, proxyToUse);

    WebDriver driver = new WebDriverBuilder().setRequiredCapabilities(requiredCaps).get();

    driver.get("http://www.diveintopython.net/toc/index.html");
    driver.quit();

    assertTrue("Proxy should have been called", proxyServer.hasBeenCalled("index.html"));
  }

  @Test
  public void requiredProxyCapabilityShouldHavePriority() {
    ProxyServer desiredProxyServer = new ProxyServer();
    Proxy desiredProxy = desiredProxyServer.asProxy();
    Proxy requiredProxy = proxyServer.asProxy();

    DesiredCapabilities desiredCaps = new DesiredCapabilities();
    desiredCaps.setCapability(PROXY, desiredProxy);
    DesiredCapabilities requiredCaps = new DesiredCapabilities();
    requiredCaps.setCapability(PROXY, requiredProxy);
    WebDriver driver = new WebDriverBuilder().setDesiredCapabilities(desiredCaps).
        setRequiredCapabilities(requiredCaps).get();
    driver.get("http://www.diveintopython.net/toc/index.html");
    driver.quit();

    assertFalse("Desired proxy should not have been called.",
                desiredProxyServer.hasBeenCalled("index.html"));
    assertTrue("Required proxy should have been called.",
               proxyServer.hasBeenCalled("index.html"));

    desiredProxyServer.destroy();
  }

}