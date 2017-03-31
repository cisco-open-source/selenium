/****************************************************************************
**
** Copyright © 1992-2014 Cisco and/or its affiliates. All rights reserved.
** All rights reserved.
** 
** $CISCO_BEGIN_LICENSE:APACHE$
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
** http://www.apache.org/licenses/LICENSE-2.0
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
**
** $CISCO_END_LICENSE$
**
****************************************************************************/

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

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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

    @Test
    public void testCanUsedOpenWindowFromPrevSessionIfCapReuseUISetToTrue() {

        WebDriver prevDriver = null;
        try {
            DesiredCapabilities caps = DesiredCapabilities.qtwebkit();
            QtWebDriverExecutor executor = QtWebKitDriver.createDefaultExecutor();
            prevDriver = new RemoteWebDriver(executor, caps);
            prevDriver.get("qtwidget://XPathElementFindingTestWidget");
            assertEquals(prevDriver.getTitle(), "Application Window");

            desiredCapabilities.setCapability(QtWebKitDriver.REUSE_UI, true);
            driver = CreateWebDriver();

            Set<String> allWindowHandles = driver.getWindowHandles();
            assertEquals(2, allWindowHandles.size());

            for (String handle : allWindowHandles) {
                driver.switchTo().window(handle);
                if (driver.getTitle().equals("Application Window")) {
                    break;
                }
            }
            assertEquals(driver.getTitle(), "Application Window");

        } catch (RuntimeException e) {
            e.printStackTrace();
            fail("Should not to be here ...");
        } finally {
            // if this caps set to 'true' prevDriver will be closed when new driver creates
        }
    }

    @Test
    public void testCanNotStartOtherSessionIfCapReuseUISetToFalse() {

        desiredCapabilities.setCapability(QtWebKitDriver.REUSE_UI, false);
        WebDriver otherDriver = null;
        try {
            driver = CreateWebDriver();
            driver.get("qtwidget://XPathElementFindingTestWidget");
            assertEquals(driver.getTitle(), "Application Window");
            DesiredCapabilities caps = DesiredCapabilities.qtwebkit();
            QtWebDriverExecutor executor = QtWebKitDriver.createDefaultExecutor();
            otherDriver = new RemoteWebDriver(executor, caps);
            fail("Should not have success ...");
        } catch (RuntimeException e) {
//            expected
        } finally {
            if (otherDriver != null)
                otherDriver.quit();
        }
    }

    @Test
    public void testCanNotStartOtherSessionIfCapReuseUIDoesNotSet() {
        WebDriver otherDriver = null;
        try {
            driver = CreateWebDriver();
            driver.get("qtwidget://XPathElementFindingTestWidget");
            assertEquals(driver.getTitle(), "Application Window");
            DesiredCapabilities caps = DesiredCapabilities.qtwebkit();
            QtWebDriverExecutor executor = QtWebKitDriver.createDefaultExecutor();
            otherDriver = new RemoteWebDriver(executor, caps);
            fail("Should not have success ...");
        } catch (RuntimeException e) {
//            expected
        } finally {
            if (otherDriver != null)
                otherDriver.quit();
        }
    }

    private DesiredCapabilities desiredCapabilities;
    private DesiredCapabilities requiredCapabilities;

    private WebDriver CreateWebDriver() throws RuntimeException {
        QtWebDriverExecutor executor = QtWebKitDriver.createDefaultExecutor();
        return new RemoteWebDriver(executor, desiredCapabilities, requiredCapabilities);
    }

}
