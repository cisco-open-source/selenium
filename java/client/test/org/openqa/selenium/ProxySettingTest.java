/*
Copyright 2012 Selenium committers
Copyright 2012 Software Freedom Conservancy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.openqa.selenium;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.remote.CapabilityType.PROXY;
import static org.openqa.selenium.testing.Ignore.Driver.ANDROID;
import static org.openqa.selenium.testing.Ignore.Driver.CHROME;
import static org.openqa.selenium.testing.Ignore.Driver.HTMLUNIT;
import static org.openqa.selenium.testing.Ignore.Driver.IE;
import static org.openqa.selenium.testing.Ignore.Driver.IPHONE;
import static org.openqa.selenium.testing.Ignore.Driver.MARIONETTE;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA_MOBILE;
import static org.openqa.selenium.testing.Ignore.Driver.PHANTOMJS;
import static org.openqa.selenium.testing.Ignore.Driver.REMOTE;
import static org.openqa.selenium.testing.Ignore.Driver.SAFARI;
import static org.openqa.selenium.testing.Ignore.Driver.QTWEBKIT;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.net.HostAndPort;
import com.google.common.net.HttpHeaders;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.openqa.selenium.testing.NeedsLocalEnvironment;
import org.openqa.selenium.testing.ProxyServer;
import org.openqa.selenium.testing.drivers.WebDriverBuilder;
import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Ignore(MARIONETTE)
public class ProxySettingTest extends JUnit4TestBase {

  @Rule
  public ErrorCollector errorCollector = new ErrorCollector();

  private final List<Callable<Object>> tearDowns = Lists.newLinkedList();

  private ProxyServer proxyServer;

  @Before
  public void newProxyInstance() {
    proxyServer = new ProxyServer();
    registerProxyTeardown(proxyServer);
  }

  @After
  public void tearDown() {
    for (Callable<Object> tearDown : tearDowns) {
      errorCollector.checkSucceeds(tearDown);
    }
  }

  @Ignore(value = {ANDROID, IPHONE, OPERA_MOBILE, PHANTOMJS, SAFARI, QTWEBKIT},
          reason = "Android/Iphone/PhantomJS - not tested,"
                   + "Opera mobile/Safari - not implemented, "
                   + "test for QTWEBKIT implemented in qtwebkit/hybridtests")
  @NeedsLocalEnvironment
  @Test
  public void canConfigureManualHttpProxy() {
    Proxy proxyToUse = proxyServer.asProxy();
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability(PROXY, proxyToUse);

    WebDriver driver = new WebDriverBuilder().setDesiredCapabilities(caps).get();
    registerDriverTeardown(driver);

    driver.get(pages.simpleTestPage);
    assertTrue("Proxy should have been called", proxyServer.hasBeenCalled("simpleTest.html"));
  }

  @Ignore(value = {ANDROID, IPHONE, OPERA_MOBILE, PHANTOMJS, SAFARI, HTMLUNIT, QTWEBKIT},
          reason = "Android/Iphone/PhantomJS - not tested,"
                   + "Opera mobile/Safari - not implemented"
                   + "QtWebKit: WebDriver doesn't support proxy configuring by PAC")
  @NeedsLocalEnvironment
  @Test
  public void canConfigureProxyThroughPACFile() {
    WebServer helloServer = createSimpleHttpServer(
        "<!DOCTYPE html><title>Hello</title><h3>Hello, world!</h3>");
    WebServer pacFileServer = createPacfileServer(Joiner.on('\n').join(
        "function FindProxyForURL(url, host) {",
        "  return 'PROXY " + getHostAndPort(helloServer) + "';",
        "}"));

    Proxy proxy = new Proxy();
    proxy.setProxyAutoconfigUrl("http://" + getHostAndPort(pacFileServer) + "/proxy.pac");

    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability(PROXY, proxy);

    WebDriver driver = new WebDriverBuilder().setDesiredCapabilities(caps).get();
    registerDriverTeardown(driver);

    driver.get(pages.mouseOverPage);
    assertEquals("Should follow proxy to another server",
        "Hello, world!", driver.findElement(By.tagName("h3")).getText());
  }

  @Ignore(value = {ANDROID, IPHONE, OPERA_MOBILE, PHANTOMJS, SAFARI, HTMLUNIT, QTWEBKIT},
          reason = "Android/Iphone/PhantomJS - not tested,"
                   + "Opera mobile/Safari - not implemented"
                   + "QtWebkit: WebDriver doesn't support proxy configuring by PAC")
  @NeedsLocalEnvironment
  @Test
  public void canUsePACThatOnlyProxiesCertainHosts() throws Exception {
    WebServer helloServer = createSimpleHttpServer(
        "<!DOCTYPE html><title>Hello</title><h3>Hello, world!</h3>");
    WebServer goodbyeServer = createSimpleHttpServer(
        "<!DOCTYPE html><title>Goodbye</title><h3>Goodbye, world!</h3>");
    WebServer pacFileServer = createPacfileServer(Joiner.on('\n').join(
        "function FindProxyForURL(url, host) {",
        "  if (url.indexOf('" + getHostAndPort(helloServer) + "') != -1) {",
        "    return 'PROXY " + getHostAndPort(goodbyeServer) + "';",
        "  }",
        "  return 'DIRECT';",
        "}"));

    Proxy proxy = new Proxy();
    proxy.setProxyAutoconfigUrl("http://" + getHostAndPort(pacFileServer) + "/proxy.pac");

    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability(PROXY, proxy);

    WebDriver driver = new WebDriverBuilder().setDesiredCapabilities(caps).get();
    registerDriverTeardown(driver);

    driver.get("http://" + getHostAndPort(helloServer));
    assertEquals("Should follow proxy to another server",
        "Goodbye, world!", driver.findElement(By.tagName("h3")).getText());

    driver.get(pages.simpleTestPage);
    assertEquals("Proxy should have permitted direct access to host",
        "Heading", driver.findElement(By.tagName("h1")).getText());
  }

  //Required Capabilities not supported
  @Ignore({ANDROID, CHROME, HTMLUNIT, IE, IPHONE, OPERA, OPERA_MOBILE, PHANTOMJS, QTWEBKIT, REMOTE, SAFARI})
  @NeedsLocalEnvironment
  @Test
  public void canConfigureProxyWithRequiredCapability() {
    Proxy proxyToUse = proxyServer.asProxy();
    DesiredCapabilities requiredCaps = new DesiredCapabilities();
    requiredCaps.setCapability(PROXY, proxyToUse);

    WebDriver driver = new WebDriverBuilder().setRequiredCapabilities(requiredCaps).get();
    registerDriverTeardown(driver);

    driver.get(pages.simpleTestPage);
    assertTrue("Proxy should have been called", proxyServer.hasBeenCalled("simpleTest.html"));
  }

  //Required Capabilities not supported
  @Ignore({ANDROID, CHROME, HTMLUNIT, IE, IPHONE, OPERA, OPERA_MOBILE, PHANTOMJS, QTWEBKIT, REMOTE, SAFARI})
  @NeedsLocalEnvironment
  @Test
  public void requiredProxyCapabilityShouldHavePriority() {
    ProxyServer desiredProxyServer = new ProxyServer();
    registerProxyTeardown(desiredProxyServer);

    Proxy desiredProxy = desiredProxyServer.asProxy();
    Proxy requiredProxy = proxyServer.asProxy();

    DesiredCapabilities desiredCaps = new DesiredCapabilities();
    desiredCaps.setCapability(PROXY, desiredProxy);
    DesiredCapabilities requiredCaps = new DesiredCapabilities();
    requiredCaps.setCapability(PROXY, requiredProxy);

    WebDriver driver = new WebDriverBuilder().setDesiredCapabilities(desiredCaps).
        setRequiredCapabilities(requiredCaps).get();
    registerDriverTeardown(driver);

    driver.get(pages.simpleTestPage);

    assertFalse("Desired proxy should not have been called.",
                desiredProxyServer.hasBeenCalled("simpleTest.html"));
    assertTrue("Required proxy should have been called.",
               proxyServer.hasBeenCalled("simpleTest.html"));
  }

  private void registerDriverTeardown(final WebDriver driver) {
    tearDowns.add(new Callable<Object>() {
      @Override
      public Object call() {
        driver.quit();
        return null;
      }
    });
  }

  private void registerProxyTeardown(final ProxyServer proxy) {
    tearDowns.add(new Callable<Object>() {
      @Override
      public Object call() {
        proxy.destroy();
        return null;
      }
    });
  }

  private WebServer createSimpleHttpServer(final String responseHtml) {
    return createServer(new HttpHandler() {
      @Override
      public void handleHttpRequest(
          HttpRequest request, HttpResponse response, HttpControl control) {
        response.charset(Charsets.UTF_8)
            .header(HttpHeaders.CONTENT_TYPE, "text/html")
            .content(responseHtml)
            .end();
      }
    });
  }

  private WebServer createPacfileServer(final String pacFileContents) {
    return createServer(new HttpHandler() {
      @Override
      public void handleHttpRequest(
          HttpRequest request, HttpResponse response, HttpControl control) {
        response.charset(Charsets.US_ASCII)
            .header(HttpHeaders.CONTENT_TYPE, "application/x-javascript-config")
            .content(pacFileContents)
            .end();
      }
    });
  }

  private WebServer createServer(HttpHandler handler) {
    int port = PortProber.findFreePort();
    final WebServer server = WebServers.createWebServer(newFixedThreadPool(5), port);
    server.add(handler);

    tearDowns.add(new Callable<Object>() {
      @Override
      public Object call() {
        server.stop();
        return null;
      }
    });

    try {
      server.start().get(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Interrupted waiting for server to start", e);
    } catch (ExecutionException e) {
      throw new RuntimeException("Server failed to start", e);
    } catch (java.util.concurrent.TimeoutException e) {
      throw new TimeoutException("Timed out waiting for the server to start", e);
    }
    return server;
  }

  private static HostAndPort getHostAndPort(WebServer server) {
    String host = Objects.firstNonNull(System.getenv("HOSTNAME"), "localhost");
    return HostAndPort.fromParts(host, server.getPort());
  }
}