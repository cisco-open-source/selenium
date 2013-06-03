package org.openqa.selenium.qtwebkit.NativeTests;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class XPathElementFindingTest extends JUnit4TestBase {

    @Before
    public void setUp() throws Exception {
        driver.get("XPathElementFindingTestWidget");
    }

    @Test
    public void testShouldFindSingleElementByXPath() {
        WebElement element = driver.findElement(By.xpath("//QLabel"));
        assertThat(element.getText(), equalTo("This Is Caption"));
    }

    @Test
    public void testShouldThrowAnExceptionWhenThereNotFoundElement() {

        try {
            WebElement element = driver.findElement(By.xpath("//QLabel[@id='Not here']"));
            assertThat(element.getText(), equalTo(""));
            fail("Should not have succeeded");
        } catch (RuntimeException e) {
            // this is expected
        }
    }

    @Test
    public void testShouldFindSingleElementByXPathAndId() {
        WebElement element = driver.findElement(By.xpath("//QTextEdit[@id='workingArea']"));
        assertThat(element.getText(), equalTo("Example text"));
    }

    @Test
    public void testShouldFindSingleOfSeveralElementByXPathThroughId() {
        WebElement element = driver.findElement(By.xpath("//*[@id='noSelected']"));
        assertThat(element.getText(), equalTo("Second"));
    }

    @Test
    public void testShouldThrowAnExceptionWhenIdNotValid() {

        try {
            WebElement element = driver.findElement(By.xpath("//*[@id='not valid']"));
            assertThat(element.getText(), equalTo(""));
            fail("Should not have succeeded");
        } catch (RuntimeException e) {
            // this is expected
        }
    }

    @Test
    public void testShouldFindSingleElementByWindowTitleXPath() {
        try {
            driver.findElement(By.xpath("//*[@name='Application Window']"));
        } catch (NoSuchElementException e) {
            fail("Should be able to find element by window title");
        }
    }

    @Test
    public void testShouldThrowAnExceptionWhenWindowTitleNotValid() {

        try {
            WebElement element = driver.findElement(By.xpath("//*[@name='not valid']"));
            assertThat(element.getText(), equalTo(""));
            fail("Should not have succeeded");
        } catch (RuntimeException e) {
            // this is expected
        }
    }

    @Test
    public void testShouldBeAbleToIdentifyElementsByClass() {
        try {
            driver.findElement(By.xpath("//XPathElementFindingTestWidget"));
        } catch (NoSuchElementException e) {
            fail("Should be able to find element by class name");
        }
    }

    @Test
    public void testShouldFindElementsByXPath() {
        List<WebElement> listElem = driver.findElements(By.xpath("//QLineEdit"));
        assertThat(listElem.size(), equalTo(3));
    }

    @Test
    public void testShouldBeAbleToFindManyElementsRepeatedlyByXPath() {
        String xpathString = "//node()[contains(@id,'input')]";
        assertThat(driver.findElements(By.xpath(xpathString)).size(), equalTo(3));

        xpathString = "//node()[contains(@id,'nope')]";
        assertThat(driver.findElements(By.xpath(xpathString)).size(), equalTo(0));
    }

    public void testShouldThrowInvalidSelectorExceptionWhenXPathIsSyntacticallyInvalidInDriverFindElement() {

        try {
            driver.findElement(By.xpath("this][is not][valid"));
            fail("Should not have succeeded because the xpath expression is syntactically not correct");
        } catch (RuntimeException e) {
            // We expect an InvalidSelectorException because the xpath expression is syntactically invalid
            assertThat(e, is(instanceOf(InvalidSelectorException.class)));
        }
    }

    @Test
    public void testShouldThrowInvalidSelectorExceptionWhenXPathIsSyntacticallyInvalidInDriverFindElements() {

        try {
            driver.findElements(By.xpath("this][is not][valid"));
            fail("Should not have succeeded because the xpath expression is syntactically not correct");
        } catch (RuntimeException e) {
            // We expect an InvalidSelectorException because the xpath expression is syntactically
            // invalid
            assertThat(e, is(instanceOf(InvalidSelectorException.class)));
        }
    }

    @Test
    public void testShouldThrowInvalidSelectorExceptionWhenXPathReturnsWrongTypeInDriverFindElement() {

        try {
            driver.findElement(By.xpath("count(//input)"));
            fail("Should not have succeeded because the xpath expression does not select an element");
        } catch (RuntimeException e) {
            // We expect an exception because the XPath expression results in a number, not in an element
            assertThat(e, is(instanceOf(InvalidSelectorException.class)));
        }
    }

    @Test
    public void testShouldThrowInvalidSelectorExceptionWhenXPathReturnsWrongTypeInDriverFindElements() {

        try {
            driver.findElements(By.xpath("count(//input)"));
            fail("Should not have succeeded because the xpath expression does not select an element");
        } catch (RuntimeException e) {
            // We expect an exception because the XPath expression results in a number, not in an element
            assertThat(e, is(instanceOf(InvalidSelectorException.class)));
        }
    }

    @Test
    public void testShouldThrowInvalidSelectorExceptionWhenXPathReturnsWrongTypeInElementFindElement() {

        WebElement externalElement = driver.findElement(By.id("scrollArea"));
        try {
            externalElement.findElement(By.xpath("this][isnot][valid"));
            fail("Should not have succeeded because the xpath expression does not select an element");
        } catch (RuntimeException e) {
            // We expect an exception because the XPath expression results in a number, not in an element
            assertThat(e, is(instanceOf(InvalidSelectorException.class)));
        }
    }

    @Test
    public void testShouldThrowInvalidSelectorExceptionWhenXPathReturnsWrongTypeInElementFindElements() {

        WebElement externalElement = driver.findElement(By.id("scrollArea"));

        try {
            externalElement.findElements(By.xpath("this][isnot][valid"));
            fail("Should not have succeeded because the xpath expression does not select an element");
        } catch (RuntimeException e) {
            // We expect an exception because the XPath expression results in a number, not in an element
            assertThat(e, is(instanceOf(InvalidSelectorException.class)));
        }
    }

    @Test
    public void testShouldFindNestedElementByXPath() {
        WebElement button = driver.findElement(By.xpath("//QPushButton"));
        assertThat(button.getText(), is("Click here"));
    }

    @Test
    public void testShouldDriverFindNestedElementsByXPath() {

        List<WebElement> listElem = driver.findElements(By.xpath("//QPushButton"));
        assertThat(listElem.size(), greaterThan(0));
    }

    @Test
    public void testShouldDriverFindNestedElementByXPathThroughId() {
        WebElement button = driver.findElement(By.xpath("//*[@id='buttonInScrollArea']"));
        assertThat(button.getText(), is("Click here"));
    }

    @Test
    public void testShouldElementFindNestedElement() {

        WebElement externalElement = driver.findElement(By.id("scrollArea"));

        try {
            externalElement.findElement(By.xpath("//QPushButton"));
        } catch (RuntimeException e) {
            fail("Should have succeeded because the xpath expression select an element");
        }
    }

    @Test
    public void testShouldElementFindNestedElements() {

        WebElement externalElement = driver.findElement(By.id("scrollArea"));

        try {
            externalElement.findElements(By.xpath("//QPushButton"));
        } catch (RuntimeException e) {
            fail("Should have succeeded because the xpath expression select an element");
        }
    }

}
