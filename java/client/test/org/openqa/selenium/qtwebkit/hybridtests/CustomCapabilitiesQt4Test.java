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

public class CustomCapabilitiesQt4Test extends JUnit4TestBase {

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
        types.put("qml", true);
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
        types.put("qml", true);
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
        types.put("qml", false);
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
            fail("Should not to be here ... ");
        }
    }

    @Test
    public void testDesiredQtVersion() {
        Map<String, Object> types = new HashMap<String, Object>();
        types.put("html", true);
        types.put("qtVersion", "5.1.0");
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
        types.put("qtVersion", "4.1.0");
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
        destypes.put("qtVersion", "5.5.0");
        desiredCapabilities.setCapability(QtWebKitDriver.HYBRID, destypes);
        Map<String, Object> reqtypes = new HashMap<String, Object>();
        reqtypes.put("html", true);
        reqtypes.put("qtVersion", "4.8.0");
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
        QtWebDriverExecutor executor = QtWebKitDriver.createDefaultExecutor();
        return new RemoteWebDriver(executor, desiredCapabilities, requiredCapabilities);
    }

}

