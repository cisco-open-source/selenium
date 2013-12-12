package org.openqa.selenium.qtwebkit.hybridtests;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.qtwebkit.QtWebKitDriver;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.qtwebkit.QtWebDriverExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.testing.JUnit4TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.openqa.selenium.Platform.WINDOWS;
import static org.openqa.selenium.Platform.LINUX;
import static org.openqa.selenium.Platform.UNIX;
import static org.openqa.selenium.Platform.MAC;
import static org.openqa.selenium.testing.Ignore.Driver.QTWEBKIT;

public class CapabilitiesTest extends JUnit4TestBase {

    @Before
    public void createDriver() {
        desiredCapabilities = DesiredCapabilities.qtwebkit();
        requiredCapabilities = null;
    }

    @After
    public void cleanUp() {
        if (null == driver) {
            return;
        }
        driver.quit();
    }

    @BeforeClass
    public static void cleanUpExistingDriver() {
        JUnit4TestBase.removeDriver();
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
    public void testCanNotStartWithRequiredCapabilitiesBrowserStartWindowIfWindowNotFound() {
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
    public void testCanNotStartNoExistingWidget() {
        desiredCapabilities.setCapability("browserClass", "NoExistingWidget");
        try {
            driver = CreateWebDriver();
            fail("Can't start not existing widget");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRequiredBrowserName() {
        requiredCapabilities = DesiredCapabilities.qtwebkit();
        try {
            driver = CreateWebDriver();
            driver.get(pages.rectanglesPage);
        } catch (RuntimeException e) {
            e.printStackTrace();
            fail("Should not to be here ...");
        }
    }

    @Ignore(value = {QTWEBKIT}, platforms = {UNIX, MAC, WINDOWS})
    @Test
    public void testRequiredPlatform() {
        requiredCapabilities = new DesiredCapabilities();
        requiredCapabilities.setPlatform(LINUX);
        try {
            driver = CreateWebDriver();
            driver.get(pages.rectanglesPage);
        } catch (RuntimeException e) {
            e.printStackTrace();
            fail("Should not to be here ...");
        }
    }

    @Test
    public void testRetainedSize() {
        desiredCapabilities.setCapability("windowsize", "1000, 600");
        desiredCapabilities.setCapability("browserClass", "TypingTestWidget");
        try {
            driver = CreateWebDriver();
            assertEquals(driver.manage().window().getSize().getWidth(), 1000);
            assertEquals(driver.manage().window().getSize().getHeight(), 600);
        } catch (RuntimeException e) {
            e.printStackTrace();
            fail("Should not to be here ...");
        }
    }

    @Test
    public void testRetainedPosition() {
        desiredCapabilities.setCapability("windowposition", "50, 70");
        desiredCapabilities.setCapability("browserClass", "TypingTestWidget");

        try {
            driver = CreateWebDriver();
            assertEquals(driver.manage().window().getPosition().getX(), 50);
            assertEquals(driver.manage().window().getPosition().getY(), 70);
        } catch (RuntimeException e) {
            e.printStackTrace();
            fail("Should not to be here ...");
        }
    }

    @Test
    public void testRestoreSize() {
        desiredCapabilities.setCapability("windowsize", "1000, 600");
        try {
            driver = CreateWebDriver();
            driver.get(pages.colorPage);
            assertEquals(driver.manage().window().getSize().getWidth(), 1000);
            assertEquals(driver.manage().window().getSize().getHeight(), 600);
            driver.get("qtwidget://XPathElementFindingTestWidget");
            driver.get(pages.colorPage);
            assertEquals(driver.manage().window().getSize().getWidth(), 1000);
            assertEquals(driver.manage().window().getSize().getHeight(), 600);
        } catch (RuntimeException e) {
            e.printStackTrace();
            fail("Should not to be here ...");
        }
    }

    @Test
    public void testRestorePosition() {
        desiredCapabilities.setCapability("windowposition", "50, 70");

        try {
            driver = CreateWebDriver();
            driver.get(pages.colorPage);
            assertEquals(driver.manage().window().getPosition().getX(), 50);
            assertEquals(driver.manage().window().getPosition().getY(), 70);
            driver.get("qtwidget://XPathElementFindingTestWidget");
            assertEquals(driver.manage().window().getPosition().getX(), 50);
            assertEquals(driver.manage().window().getPosition().getY(), 70);
            driver.get(pages.colorPage);
            assertEquals(driver.manage().window().getPosition().getX(), 50);
            assertEquals(driver.manage().window().getPosition().getY(), 70);
        } catch (RuntimeException e) {
            e.printStackTrace();
            fail("Should not to be here ...");
        }
    }

    @Test
    public void testRestoreSizeAndPosition() {
        desiredCapabilities.setCapability("windowsize", "1000, 600");
        desiredCapabilities.setCapability("windowposition", "50, 70");
        try {
            driver = CreateWebDriver();
            driver.get(pages.colorPage);
            assertEquals(driver.manage().window().getSize().getWidth(), 1000);
            assertEquals(driver.manage().window().getSize().getHeight(), 600);
            assertEquals(driver.manage().window().getPosition().getX(), 50);
            assertEquals(driver.manage().window().getPosition().getY(), 70);
            driver.get("qtwidget://XPathElementFindingTestWidget");
            driver.get(pages.colorPage);
            assertEquals(driver.manage().window().getSize().getWidth(), 1000);
            assertEquals(driver.manage().window().getSize().getHeight(), 600);
            assertEquals(driver.manage().window().getPosition().getX(), 50);
            assertEquals(driver.manage().window().getPosition().getY(), 70);
        } catch (RuntimeException e) {
            e.printStackTrace();
            fail("Should not to be here ...");
        }
    }

    private DesiredCapabilities desiredCapabilities;
    private DesiredCapabilities requiredCapabilities;

    private WebDriver CreateWebDriver() throws RuntimeException {
        QtWebDriverExecutor executor = QtWebKitDriver.createDefaultExecutor();
        return new RemoteWebDriver(executor, desiredCapabilities, requiredCapabilities);
    }

}
