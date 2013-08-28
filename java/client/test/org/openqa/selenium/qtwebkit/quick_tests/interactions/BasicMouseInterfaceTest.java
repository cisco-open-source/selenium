package org.openqa.selenium.qtwebkit.quick_tests.interactions;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.openqa.selenium.testing.JavascriptEnabled;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.openqa.selenium.TestWaiter.waitFor;
import static org.openqa.selenium.WaitingConditions.*;
import static org.openqa.selenium.testing.Ignore.Driver.*;

/**
 * Tests operations that involve mouse and keyboard.
 */
public class BasicMouseInterfaceTest extends JUnit4TestBase {

    @Before
    public void setUpWebDriver()
    {
        driver.get(pages.basicMouseInterfaceTest);
    }

    private Actions getBuilder(WebDriver driver) {
        return new Actions(driver);
    }

    private void performDragAndDropWithMouse() {

        WebElement toDrag = driver.findElement(By.id("itemToDrag"));
        Point oldPosition = toDrag.getLocation();

        int x_offset = 57;
        int y_offset = 36;

        Action dragItem = getBuilder(driver)
                            .clickAndHold(toDrag)
                            .moveByOffset(x_offset, y_offset)
                            .release()
                            .build();

        assertEquals(oldPosition, toDrag.getLocation());

        dragItem.perform();

        assertEquals(oldPosition.moveBy(x_offset, y_offset), toDrag.getLocation());
    }

    @Test
    @Ignore(QTWEBKIT)
    // we cannot simulate drag@drop with pure mouse actions. Need separated mouse command for this
    public void testDraggingElementWithMouseMovesItToAnotherList() {
        performDragAndDropWithMouse();
    }

    @Test
    public void testButtonDownUp() {

        WebElement targetElement = driver.findElement(By.id("pureButton"));

        Action buttonDown = getBuilder(driver).clickAndHold(targetElement).build();

        buttonDown.perform();

        Action buttonUp = getBuilder(driver).release().build();

        buttonUp.perform();

        assertEquals("Released", targetElement.getAttribute("lastButtonEvent"));
    }

    @JavascriptEnabled
    @Test
    public void testDoubleClick() {

        WebElement toDoubleClick = driver.findElement(By.id("pureButton"));

        Action dblClick = getBuilder(driver).doubleClick(toDoubleClick).build();

        dblClick.perform();
        String testFieldContent = TestWaiter.waitFor(
                    elementTextToEqual(toDoubleClick, "DoubleClicked"), 5, TimeUnit.SECONDS);
        assertEquals("Value should change to DoubleClicked.", "DoubleClicked",
                    testFieldContent);
    }

    @JavascriptEnabled
    @Test
    public void testContextClick() {

        WebElement toContextClick = driver.findElement(By.id("pureButton"));

        Action contextClick = getBuilder(driver).contextClick(toContextClick).build();

        contextClick.perform();
        assertEquals("Value should change to ContextClicked.", "ContextClicked",
                     toContextClick.getText());
    }

    @JavascriptEnabled
    @Test
    public void testMoveAndClick() {

        WebElement toClick = driver.findElement(By.id("pureButton"));

        Action contextClick = getBuilder(driver).moveToElement(toClick).click().build();

        contextClick.perform();

        waitFor(elementTextToEqual(toClick, "Clicked"));

        assertEquals("Value should change to Clicked.", "Clicked",
                     toClick.getText());
    }

    @JavascriptEnabled
    @Test
    public void testCannotMoveToANullLocator() {

        try {
          Action contextClick = getBuilder(driver).moveToElement(null).build();

          contextClick.perform();
          fail("Shouldn't be allowed to click on null element.");
        } catch (IllegalArgumentException expected) {
          // Expected.
        }
      }

}
