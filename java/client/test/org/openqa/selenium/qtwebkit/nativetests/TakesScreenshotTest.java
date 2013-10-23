package org.openqa.selenium.qtwebkit.nativetests;

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

        driver.get("qtwidget://QWidget");
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

        driver.get("qtwidget://QWidget");
        String screenshot = getScreenshot().getScreenshotAs(BASE64);
        assertTrue(screenshot.length() > 0);
    }

    @Test
    public void testSaveElementScreenshotAsFile() throws Exception {
        if (!isAbleToTakeScreenshots(driver)) {
            return;
        }

        driver.get("qtwidget://ClickTestWidget");

        WebElement el = driver.findElement(By.id("pushBtn"));

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

        driver.get("qtwidget://ClickTestWidget");

        WebElement el = driver.findElement(By.id("pushBtn"));

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
