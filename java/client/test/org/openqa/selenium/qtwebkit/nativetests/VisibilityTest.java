package org.openqa.selenium.qtwebkit.nativetests;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;

import org.openqa.selenium.testing.JUnit4TestBase;
import java.util.concurrent.Callable;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.openqa.selenium.TestWaiter.waitFor;

public class VisibilityTest extends JUnit4TestBase {

    @Before
    public void setUp() throws Exception {
        driver.get("qtwidget://VisibilityTestWidget");
    }

    @Test
    public void testShouldAllowTheUserToTellIfAnElementIsDisplayedOrNot() {

        assertThat(driver.findElement(By.id("displayedLabel")).isDisplayed(),
                is(true));
        assertThat(driver.findElement(By.id("notDisplayedLabel")).isDisplayed(), is(false));

    }

    @Test
    public void testVisibilityShouldTakeIntoWidgetParentVisibility() {

        WebElement element = driver.findElement(By.id("hiddenScrollArea"));
        WebElement externalElement = driver.findElement(By.id("buttonInScrollArea"));

        assertFalse(element.isDisplayed());
        assertFalse(externalElement.isDisplayed());
    }

    @Test
    public void testShouldModifyTheVisibilityOfAnElementDynamically() {

        WebElement element = driver.findElement(By.id("buttonCanHide"));
        assertTrue(element.isDisplayed());

        element.click();
        waitFor(elementNotToDisplayed(element));
        assertFalse(element.isDisplayed());
    }

    @Test
    public void testHiddenInputElementsAreNeverVisible() {

        WebElement shown = driver.findElement(By.id("buttonHidden"));

        assertFalse(shown.isDisplayed());
    }

    @Test
    public void testShouldNotBeAbleToClickOnAnElementThatIsNotDisplayed() {
        WebElement element = driver.findElement(By.id("buttonHidden"));

        try {
            element.click();
            fail("You should not be able to click on an invisible element");
        } catch (ElementNotVisibleException e) {
            // This is expected
        }
    }

    @Test
    public void testShouldNotBeAbleToTypeAnElementThatIsNotDisplayed() {
        WebElement element = driver.findElement(By.id("inputHidden"));

        try {
            element.sendKeys("You don't see me");
            fail("You should not be able to send keyboard input to an invisible element");
        } catch (ElementNotVisibleException e) {
            // This is expected
        }

        assertThat(element.getText(), is(not("You don't see me")));
    }

    @Test
    public void testZeroSizedDivIsShownIfDescendantHasSize() {

        WebElement element = driver.findElement(By.id("zeroLabel"));
        Dimension size = element.getSize();

        assertEquals("Should have 0 width", 0, size.width);
        assertEquals("Should have 0 height", 0, size.height);
        assertTrue(element.isDisplayed());
    }

    private Callable<Boolean> elementNotToDisplayed(final WebElement element) {
        return new Callable<Boolean>() {

            public Boolean call() throws Exception {
                return !element.isDisplayed();
            }
        };
    }

    @Test
    public void parentNodeVisibleWhenAllChildrenAreAbsolutelyPositionedAndOverflowIsHidden() {
        String url = "qtwidget://TypingTestWidget";
        driver.get(url);

        WebElement element = driver.findElement(By.id("result"));
        assertTrue(element.isDisplayed());
    }

    @Test
    public void tooSmallAWindowWithOverflowHiddenIsNotAProblem() {
        WebDriver.Window window = driver.manage().window();
        Dimension originalSize = window.getSize();

        try {
            // Short in the Y dimension
            window.setSize(new Dimension(1024, 500));

            String url = "qtwidget://TypingTestWidget";
            driver.get(url);

            WebElement element = driver.findElement(By.id("result"));
            assertTrue(element.isDisplayed());
        } finally {
            window.setSize(originalSize);
        }
    }

}
