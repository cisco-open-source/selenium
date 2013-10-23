package org.openqa.grid.e2e;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.e2e.utils.GridTestHelper;
import org.openqa.grid.e2e.utils.RegistryTestHelper;
import org.openqa.grid.internal.utils.SelfRegisteringRemote;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.environment.GlobalTestEnvironment;
import org.openqa.selenium.environment.InProcessTestEnvironment;
import org.openqa.selenium.environment.webserver.AppServer;
import org.openqa.selenium.qtwebkit.QtWebKitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.environment.Pages;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.grid.web.Hub;
import org.openqa.grid.internal.utils.GridHubConfiguration;

public class GridQtWebDriverTest {

    private static Hub hub;

    @BeforeClass
    public static void prepare() throws Exception {

        GridHubConfiguration hubConfiguration = new GridHubConfiguration();
        hubConfiguration.loadFromJSON("qtjsons/qthub.json");
        hub = GridTestHelper.getHub(hubConfiguration);

        RegistrationRequest req = RegistrationRequest.build("-role", "node",
                                      "-nodeConfig", "qtjsons/qtnode1.json",
                                      "-DRemoteIP=http://127.0.0.1:9517");
        SelfRegisteringRemote remote = new SelfRegisteringRemote(req);

        remote.startRemoteServer();

        remote.getConfiguration().put(RegistrationRequest.TIME_OUT, -1);
        remote.sendRegistrationRequest();
        RegistryTestHelper.waitForNode(hub.getRegistry(), 1);
    }

    @AfterClass
    public static void stop() throws Exception {
        if (hub != null) {
            hub.stop();
        }
    }

    @Before
    public void initCaps() {
        desiredCapabilities = DesiredCapabilities.qtwebkit();
    }

    @Before
    public void prepareEnvironment() throws Exception {
        environment = GlobalTestEnvironment.get(InProcessTestEnvironment.class);
        appServer = environment.getAppServer();
        pages = environment.getTestContent();
    }

    @After
    public void cleanDrivers() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testShouldBeAbleToRunWithCapabilitiesThatExactMatchOnGrid() {
        Map<String, Object> types = new HashMap<String, Object>();
        types.put("html", true);
        types.put("qml", true);
        types.put("qtVersion", "4.8.2");
        desiredCapabilities.setCapability(QtWebKitDriver.HYBRID, types);
        try {
            driver = new RemoteWebDriver(new URL(hub.getUrl() + "/wd/hub"), desiredCapabilities);
            driver.get(pages.colorPage);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
            fail("Should not to be here ...");
        }
    }

    @Test
    public void testShouldBeAbleToRunWithNoHybridCapabilitiesOnGrid() {
        Map<String, Object> types = new HashMap<String, Object>();
        types.put("html", true);
        types.put("qml", true);
        types.put("qtVersion", "4.8.2");
        desiredCapabilities.setCapability(QtWebKitDriver.HYBRID, types);
        desiredCapabilities.setCapability("FakeCapabilities", true);
        try {
            driver = new RemoteWebDriver(new URL(hub.getUrl() + "/wd/hub"), desiredCapabilities);
            driver.get(pages.rectanglesPage);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
            fail("Should not to be here ...");
        }
    }

    @Test
    public void testShouldNotBeAbleToRunWithHybridCapabilitiesDontSetOnGrid() {
        Map<String, Object> types = new HashMap<String, Object>();
        types.put("html", true);
        types.put("qml", true);
        types.put("qtVersion", "4.8.2");
        types.put("fake", true);
        desiredCapabilities.setCapability(QtWebKitDriver.HYBRID, types);
        try {
            driver = new RemoteWebDriver(new URL(hub.getUrl() + "/wd/hub"), desiredCapabilities);
            driver.get(pages.rectanglesPage);
            fail("Should not to be here ...");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            // expected
            assertThat(e, is(instanceOf(WebDriverException.class)));
        }
    }

    @Test
    public void testShouldNotBeAbleToRunWithQtVersionThatDoNotMatchOnGrid() {
        Map<String, Object> types = new HashMap<String, Object>();
        types.put("html", true);
        types.put("qml", true);
        types.put("qtVersion", "5.1.0");
        desiredCapabilities.setCapability(QtWebKitDriver.HYBRID, types);
        try {
            driver = new RemoteWebDriver(new URL(hub.getUrl() + "/wd/hub"), desiredCapabilities);
            driver.get(pages.colorPage);
            fail("Should not have succeeded");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }  catch (RuntimeException e) {
            // expected
            assertThat(e, is(instanceOf(WebDriverException.class)));
        }
    }

    @Test
    public void testShouldNotBeAbleToRunWithHybridTypeThatDoNotMatchOnGrid() {
        Map<String, Object> types = new HashMap<String, Object>();
        types.put("html", true);
        types.put("qml", true);
        types.put("widget", true);
        types.put("qtVersion", "4.8");
        desiredCapabilities.setCapability(QtWebKitDriver.HYBRID, types);
        try {
            driver = new RemoteWebDriver(new URL(hub.getUrl() + "/wd/hub"), desiredCapabilities);
            fail("Should not have succeeded");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }  catch (RuntimeException e) {
            // expected
            assertThat(e, is(instanceOf(WebDriverException.class)));
        }
    }

    @Test
    public void testShouldNotBeAbleToRunWithHybridTypeThatSetFalseAndDoNotMatchOnGrid() {
        Map<String, Object> types = new HashMap<String, Object>();
        types.put("html", true);
        types.put("qml", false);
        types.put("qtVersion", "4.8");
        desiredCapabilities.setCapability(QtWebKitDriver.HYBRID, types);
        try {
            driver = new RemoteWebDriver(new URL(hub.getUrl() + "/wd/hub"), desiredCapabilities);
            fail("Should not have succeeded");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }  catch (RuntimeException e) {
            // expected
            assertThat(e, is(instanceOf(WebDriverException.class)));
        }
    }

    @Test
    public void testShouldBeAbleToRunWithQtVersionThatNoBestMatchOnGrid() {
        Map<String, Object> types = new HashMap<String, Object>();
        types.put("html", true);
        types.put("qml", true);
        types.put("qtVersion", "4.8.5");
        desiredCapabilities.setCapability(QtWebKitDriver.HYBRID, types);
        try {
            driver = new RemoteWebDriver(new URL(hub.getUrl() + "/wd/hub"), desiredCapabilities);
            driver.get(pages.html5Page);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }  catch (RuntimeException e) {
            e.printStackTrace();
            fail("Should not to be here ...");
        }
    }

    private WebDriver driver;
    private DesiredCapabilities desiredCapabilities;

    private InProcessTestEnvironment environment;
    private AppServer appServer;
    private Pages pages;

}