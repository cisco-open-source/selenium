package org.openqa.selenium.qtwebkit.quick1_tests;

import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
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

        driver.get(appServer.whereIs("quick1/WindowTest.qml"));

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

        driver.get(appServer.whereIs("quick1/WindowTest.qml"));

        String screenshot = getScreenshot().getScreenshotAs(BASE64);
        assertTrue(screenshot.length() > 0);
    }

    public TakesScreenshot getScreenshot() {
        return (TakesScreenshot) driver;
    }

    private boolean isAbleToTakeScreenshots(WebDriver driver) throws Exception {
        return driver instanceof TakesScreenshot;
    }
}
