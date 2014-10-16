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

package org.openqa.selenium.qtwebkit.quick_tests;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.QtWebkitAugmenter;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.io.File;

import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.OutputType.BASE64;

public class TakesScreenshotTest extends JUnit4TestBase {
    @Test
    public void testSaveScreenshotAsFile() throws Exception {
        if (!isAbleToTakeScreenshots(driver)) {
            return;
        }

        driver.get(pages.windowTest);

        File tempFile = getScreenshot().getScreenshotAs(OutputType.FILE);
        assertTrue(tempFile.exists());
        assertTrue(tempFile.length() > 0);
        tempFile.delete();
    }

    @Test
    public void testCaptureToBase64() throws Exception {
        if (!isAbleToTakeScreenshots(driver)) {
            return;
        }

        driver.get(pages.windowTest);

        String screenshot = getScreenshot().getScreenshotAs(BASE64);
        assertTrue(screenshot.length() > 0);
    }

    @Test
    public void testSaveElementScreenshotAsFile() throws Exception {
        if (!isAbleToTakeScreenshots(driver)) {
            return;
        }

        driver.get(pages.clickTest);

        WebElement el = driver.findElement(By.id("button1"));

        File tempFile = getScreenshot((RemoteWebElement) el).getScreenshotAs(OutputType.FILE);
        assertTrue(tempFile.exists());
        assertTrue(tempFile.length() > 0);
        tempFile.delete();
    }

    @Test
    public void testCaptureElementScreenshotToBase64() throws Exception {
        if (!isAbleToTakeScreenshots(driver)) {
            return;
        }

        driver.get(pages.clickTest);

        WebElement el = driver.findElement(By.id("button1"));

        String screenshot = getScreenshot((RemoteWebElement) el).getScreenshotAs(BASE64);
        assertTrue(screenshot.length() > 0);
    }

    public TakesScreenshot getScreenshot() {
        return (TakesScreenshot) driver;
    }

    public TakesScreenshot getScreenshot(RemoteWebElement element) {
        return (TakesScreenshot)(new QtWebkitAugmenter().augment(element));
    }

    private boolean isAbleToTakeScreenshots(WebDriver driver) throws Exception {
        return driver instanceof TakesScreenshot;
    }
}
