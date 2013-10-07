package org.openqa.selenium.qtwebkit.nativetests;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.testing.JUnit4TestBase;

import static org.junit.Assert.fail;

public class StaleElementReferenceTest extends JUnit4TestBase {


    @Before
    public void setUp() throws Exception {
        driver.get("qtwidget://StaleElementReferenceTestWidget");
    }

    @Test
    public void testOldPage() {
        WebElement elem = driver.findElement(By.id("captionLabel"));
        driver.get("qtwidget://TypingTestWidget");
        try {
            elem.click();
            fail();
        } catch (StaleElementReferenceException e) {
            // do nothing. this is what we expected.
        }
    }

    @Test
    public void testShouldNotCrashWhenCallingGetSizeOnAnObsoleteElement() {
        WebElement elem = driver.findElement(By.id("captionLabel"));
        driver.get("qtwidget://TypingTestWidget");
        try {
            elem.getSize();
            fail();
        } catch (StaleElementReferenceException e) {
            // do nothing. this is what we expected.
        }
    }

    @Test
    public void testShouldNotCrashWhenQueryingTheAttributeTextOfAStaleElement() {
        WebElement elem = driver.findElement(By.xpath("//QLabel"));
        driver.get("qtwidget://TypingTestWidget");
        try {
            elem.getText();
            fail();
        } catch (StaleElementReferenceException e) {
            // do nothing. this is what we expected.
        }
    }
}
