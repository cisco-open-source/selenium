package org.openqa.selenium.qtwebkit.quick_tests;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.util.logging.Logger;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;


public class FindingTest extends JUnit4TestBase {

    private static Logger log = Logger.getLogger(FindingTest.class.getName());

    @Before
    public void createWebDriver() {
        driver.get(pages.findingTest);
    }

    @Test
    public void testCanFindSimpleControl() {
        assertTrue(driver.findElement(By.id("inputElement")).isDisplayed());
    }

    @Test
    public void testCanFindHiddenControl() {
        assertNotNull(driver.findElement(By.id("mouseHotSpot")));
    }

    @Test
    public void testCanFindControlByClassName() {
        assertNotNull(driver.findElement(By.className("TextInput")));
    }

    @Test
    public void testCanFindHiddenControlByClassName() {
        assertNotNull(driver.findElement(By.className("MouseArea")));
    }

    @Test
    public void testCanNotFindNonExistElementByClassName() {
        try {
            assertNull(driver.findElement(By.className("NonExistedElement")));
        } catch (RuntimeException e) {
            //  this is expected
            assertThat(e, is(instanceOf(NoSuchElementException.class)));
        }
    }

    @Test
    public void testCanNotFindNonExistElementById() {
        try {
            driver.findElement(By.id("wrongId"));
        } catch (RuntimeException e) {
            //  this is expected
            assertThat(e, is(instanceOf(NoSuchElementException.class)));
        }
    }
}
