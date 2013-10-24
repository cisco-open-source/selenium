package org.openqa.selenium.qtwebkit.hybridtests;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.qtwebkit.QtWebDriverExecutor;
import org.openqa.selenium.qtwebkit.QtWebKitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class CustomCapabilitiesQt5Test extends JUnit4TestBase {

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

    @BeforeClass
    public static void cleanUpExistingDriver() {
        JUnit4TestBase.removeDriver();
    }

    @Test
    public void testShouldBeAbleToRunWithRequiredHybridViewTypesCapabilities() {
        Map<String, Object> types = new HashMap<String, Object>();
        types.put("html", true);
        types.put("widget", true);
        types.put("qml2", true);
        requiredCapabilities = new DesiredCapabilities();
        requiredCapabilities.setCapability(QtWebKitDriver.HYBRID, types);
        try {
            driver = CreateWebDriver();
            driver.get(pages.colorPage);
        } catch (RuntimeException e) {
            fail("Should not to be here ...");
        }
    }

    @Test
    public void testShouldNotBeAbleToRunWithRequiredHybridViewTypesCapabilitiesWhichDidntSet() {
        Map<String, Object> types = new HashMap<String, Object>();
        types.put("html", true);
        types.put("abcxy", true);
        requiredCapabilities = new DesiredCapabilities();
        requiredCapabilities.setCapability(QtWebKitDriver.HYBRID, types);
        try {
            driver = CreateWebDriver();
            driver.get(pages.rectanglesPage);
            fail("Should not start session ...");
        } catch (RuntimeException e) {
            // expected
            assertThat(e, is(instanceOf(WebDriverException.class)));
        }
    }

    @Test
    public void testShouldBeAbleToRunWithDesiredHybridCapabilitiesWhichDidntSet() {
        Map<String, Object> types = new HashMap<String, Object>();
        types.put("html", true);
        types.put("qml2", true);
        types.put("abcdx", true);
        types.put("qtVersion", "4.8.2");
        desiredCapabilities.setCapability(QtWebKitDriver.HYBRID, types);
        try {
            driver = CreateWebDriver();
            driver.get(pages.rectanglesPage);
        } catch (RuntimeException e) {
            fail("Should not to be here ...");
        }
    }

    @Test
    public void testShouldNotBeAbleToRunWithRequiredHybridTypeThatSetFalse() {
        Map<String, Object> types = new HashMap<String, Object>();
        types.put("html", false);
        types.put("qml2", false);
        requiredCapabilities = new DesiredCapabilities();
        requiredCapabilities.setCapability(QtWebKitDriver.HYBRID, types);
        try {
            driver = CreateWebDriver();
            fail("Should not have succeeded");
        }  catch (RuntimeException e) {
            // expected
            assertThat(e, is(instanceOf(WebDriverException.class)));
        }
    }

    @Test
    public void testShouldBeAbleToRunWithDesiredHybridTypeThatSetFalse() {
        Map<String, Object> types = new HashMap<String, Object>();
        types.put("html", false);
        types.put("widget", false);
        desiredCapabilities.setCapability(QtWebKitDriver.HYBRID, types);
        try {
            driver = CreateWebDriver();
        }  catch (RuntimeException e) {
            // expected
            fail("Should not to be here ... ");
        }
    }

    @Test
    public void testDesiredQtVersion() {
        Map<String, Object> types = new HashMap<String, Object>();
        types.put("html", true);
        types.put("qtVersion", "4.5.0");
        desiredCapabilities.setCapability(QtWebKitDriver.HYBRID, types);
        try {
            driver = CreateWebDriver();
        }  catch (RuntimeException e) {
            fail("Should not to be here ... ");
        }
    }


    @Test
    public void testRequiredQtVersion() {
        Map<String, Object> types = new HashMap<String, Object>();
        types.put("html", true);
        types.put("qtVersion", "5.1.0");
        requiredCapabilities = new DesiredCapabilities();
        requiredCapabilities.setCapability(QtWebKitDriver.HYBRID, types);
        try {
            driver = CreateWebDriver();
        }  catch (RuntimeException e) {
            fail("Should not to be here ... ");
        }
    }

    @Test
    public void testRequiredQtVersionShouldHasHighPriority() {
        Map<String, Object> destypes = new HashMap<String, Object>();
        destypes.put("html", true);
        destypes.put("qtVersion", "4.5.0");
        desiredCapabilities.setCapability(QtWebKitDriver.HYBRID, destypes);
        Map<String, Object> reqtypes = new HashMap<String, Object>();
        reqtypes.put("html", true);
        reqtypes.put("qtVersion", "5.1.0");
        requiredCapabilities = new DesiredCapabilities();
        requiredCapabilities.setCapability(QtWebKitDriver.HYBRID, reqtypes);
        try {
            driver = CreateWebDriver();
        }  catch (RuntimeException e) {
            fail("Should not to be here ... ");
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

