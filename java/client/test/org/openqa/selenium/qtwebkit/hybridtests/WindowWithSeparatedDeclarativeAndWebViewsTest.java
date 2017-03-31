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
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WaitingConditions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class WindowWithSeparatedDeclarativeAndWebViewsTest extends JUnit4TestBase {

    @Before
    public void setUp() {
        driver.get("qtwidget://WindowWithSeparatedDeclarativeAndWebViewsTestWidget");
        currentWindow = driver.getWindowHandle();
        inputWebURL = driver.findElement(By.id("inputWebURL"));
        buttonToWeb = driver.findElement(By.id("openWebViewButton"));
        inputQmlURL = driver.findElement(By.id("inputQmlURL"));
        buttonToQml = driver.findElement(By.id("openQmlViewButton"));
        assertEquals(1, driver.getWindowHandles().size());
    }

    @After
    public void cleanUp() {
        for (String winHandle : driver.getWindowHandles()) {
            if (!currentWindow.equals(winHandle)) {
                driver.switchTo().window(winHandle);
                driver.close();
            }
        }
        driver.switchTo().window(currentWindow);
    }

    @Test
    public void testCanGetUrlFromQmlView() {
        inputQmlURL.sendKeys(appServer.whereIs("quick1/FindingTest.qml"));
        buttonToQml.click();
        for (String winHandle : driver.getWindowHandles()) {
            if (!currentWindow.equals(winHandle)) {
                driver.switchTo().window(winHandle);
                wait.until(windowUrlContains(driver, "FindingTest.qml"));
                break;
            }
        }
    }

    @Test
    public void testCanSwitchToNeededQMLViewAndBack() {
        inputQmlURL.sendKeys(appServer.whereIs("quick1/FindingTest.qml"));
        buttonToQml.click();
        String enteredUrl = inputQmlURL.getText();
        for (String winHandle : driver.getWindowHandles()) {
            if (!currentWindow.equals(winHandle)) {
                driver.switchTo().window(winHandle);
                break;
            }
        }
        assertEquals(enteredUrl, driver.getCurrentUrl());
        driver.close();
        driver.switchTo().window(currentWindow);
        assertEquals("qtwidget://WindowWithSeparatedDeclarativeAndWebViewsTestWidget",
                     driver.getCurrentUrl());

    }

    @Test
    public void testCanOpenViewsForEmptyPage() {
        buttonToWeb.click();
        buttonToQml.click();
        assertEquals(3, driver.getWindowHandles().size());
    }

    @Test
    public void testCanSwitchBetweenQmlViewAndWebView() {
        inputWebURL.sendKeys(pages.colorPage);
        buttonToWeb.click();
        String webWindow = null;
        for (String winHandle : driver.getWindowHandles()) {
            if (!currentWindow.equals(winHandle)) {
                webWindow = winHandle;
                driver.switchTo().window(webWindow);
                wait.until(windowUrlContains(driver, "colorPage.html"));
                break;
            }
        }
        assertNotNull(webWindow);
        driver.switchTo().window(currentWindow);
        wait.until(titleIs("Test Widget"));

        inputQmlURL.sendKeys(appServer.whereIs("quick1/ClickTest.qml"));
        buttonToQml.click();
        for (String winHandle : driver.getWindowHandles()) {
            if (!currentWindow.equals(winHandle)) {
                if (!webWindow.equals(winHandle)) {
                    driver.switchTo().window(winHandle);
                    wait.until(windowUrlContains(driver, "ClickTest.qml"));
                    break;
                }
            }
        }

        driver.switchTo().window(webWindow);
        wait.until(windowUrlContains(driver, "colorPage.html"));
    }

    @Test
    public void testCanOpenQmlViewAndWebViewTogether() {
        inputQmlURL.sendKeys(appServer.whereIs("quick1/ClickTest.qml"));
        buttonToQml.click();
        String qmlWindow = null;
        for (String winHandle : driver.getWindowHandles()) {
            if (!currentWindow.equals(winHandle)) {
                qmlWindow = winHandle;
                driver.switchTo().window(qmlWindow);
                wait.until(windowUrlContains(driver, "ClickTest.qml"));
                break;
            }
        }

        assertNotNull(qmlWindow);
        driver.switchTo().window(currentWindow);
        inputWebURL.sendKeys(pages.colorPage);
        buttonToWeb.click();
        for (String winHandle : driver.getWindowHandles()) {
            if (!currentWindow.equals(winHandle)) {
                if (!qmlWindow.equals(winHandle)) {
                    driver.switchTo().window(winHandle);
                    wait.until(windowUrlContains(driver, "colorPage.html"));
                    break;
                }
            }
        }

    }

    @Test
    public void testCanOpenAndCloseQmlViewAndWebView() {
        inputQmlURL.sendKeys(appServer.whereIs("quick1/ClickTest.qml"));
        buttonToQml.click();
        String qmlWindow = null;
        for (String winHandle : driver.getWindowHandles()) {
            if (!currentWindow.equals(winHandle)) {
                qmlWindow = winHandle;
                driver.switchTo().window(qmlWindow);
                wait.until(windowUrlContains(driver, "ClickTest.qml"));
                break;
            }
        }

        assertNotNull(qmlWindow);
        driver.switchTo().window(currentWindow);
        assertFalse(driver.getWindowHandle().equals(qmlWindow));

        inputWebURL.sendKeys(pages.colorPage);
        buttonToWeb.click();
        String webWindow = null;
        for (String winHandle : driver.getWindowHandles()) {
            if (!currentWindow.equals(winHandle)) {
                if (!qmlWindow.equals(winHandle)) {
                    webWindow = winHandle;
                    driver.switchTo().window(webWindow);
                    wait.until(windowUrlContains(driver, "colorPage.html"));
                    break;
                }
            }
        }
        assertNotNull(qmlWindow);
        assertTrue(driver.getWindowHandles().contains(qmlWindow));
        assertTrue(driver.getWindowHandles().contains(webWindow));

        driver.switchTo().window(qmlWindow);
        driver.close();
        assertFalse(driver.getWindowHandles().contains(qmlWindow));

        driver.switchTo().window(webWindow);
        driver.close();
        assertFalse(driver.getWindowHandles().contains(webWindow));

        driver.switchTo().window(currentWindow);
    }

    @Test
    public void testCanDownloadQmlIntoNoParentView() {
        buttonToQml.click();
        for (String winHandle : driver.getWindowHandles()) {
            if (!currentWindow.equals(winHandle)) {
                driver.switchTo().window(winHandle);
                break;
            }
        }
        driver.get(appServer.whereIs("quick1/FindingTest.qml"));
        wait.until(windowUrlContains(driver, "FindingTest.qml"));
    }

    @Test
    public void testCanNotLoadQmlToWebView() {
        inputWebURL.sendKeys(appServer.whereIs("quick1/FindingTest.qml"));
        buttonToWeb.click();
        for (String winHandle : driver.getWindowHandles()) {
            if (!currentWindow.equals(winHandle)) {
                driver.switchTo().window(winHandle);
                try {
                    driver.findElement(By.id("inputElement"));
                    fail("You don't have to be here...");
                } catch (NoSuchElementException e) {
                    //expected
                }
                break;
            }
        }
    }

    @Test
    public void testCanNotLoadWebViewToQmlView() {
        inputQmlURL.sendKeys(pages.colorPage);
        buttonToQml.click();
        for (String winHandle : driver.getWindowHandles()) {
            if (!currentWindow.equals(winHandle)) {
                driver.switchTo().window(winHandle);
                wait.until(windowUrlContains(driver, "colorPage.html"));
                break;
            }
        }
    }

    @Test
    public void testCheckNumberOfWindows() {
        assertEquals(1, driver.getWindowHandles().size());
        inputWebURL.sendKeys(pages.colorPage);
        buttonToWeb.click();
        assertEquals(2, driver.getWindowHandles().size());
        inputQmlURL.sendKeys(appServer.whereIs("quick1/FindingTest.qml"));
        buttonToQml.click();
        buttonToQml.click();
        assertEquals(4, driver.getWindowHandles().size());
    }

    private String currentWindow;
    private WebElement inputWebURL;
    private WebElement buttonToWeb;
    private WebElement inputQmlURL;
    private WebElement buttonToQml;

    private ExpectedCondition<Boolean> windowUrlContains(final WebDriver webdriver, final String url) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                String currentUrl = webdriver.getCurrentUrl();
                return currentUrl.contains(url);
            }
        };
    }

}
