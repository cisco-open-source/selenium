package org.openqa.selenium.qtwebkit.quick1_tests;

import org.junit.Test;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.testing.JUnit4TestBase;


import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ElementAttributeTest extends JUnit4TestBase {

    @Before
    public void setUp() throws Exception {
        driver.get(appServer.whereIs("quick1/ElementAttributeTest.qml"));
    }

    @Test
    public void testElementsDisabledOrEnabled() {
        WebElement inputElement = driver.findElement(By.id("notWorkingButton"));
        assertThat(inputElement.getAttribute("disabled"), equalTo(null));
        assertThat(inputElement.isEnabled(), is(false));

        inputElement = driver.findElement(By.id("workingButton"));
        assertThat(inputElement.getAttribute("disabled"), equalTo(null));
        assertThat(inputElement.isEnabled(), is(true));
    }

    @Test
    public void testElementsDisplayedOrNo() {

        WebElement inputElement = driver.findElement(By.id("notWorkingButton"));
        assertThat(inputElement.isDisplayed(), is(false));

        inputElement = driver.findElement(By.id("workingButton"));
        assertThat(inputElement.isDisplayed(), is(true));
    }

    @Test
    public void testGetTextFromPushButton()
    {
        WebElement element = driver.findElement(By.id("workingButton"));
        assertThat(element.getAttribute("text"), is("Working Button"));
        assertThat(element.getText(), is("Working Button"));
    }

    @Test
    public void testShouldThrowExceptionIfSendingKeysToElementDisabled() {
        WebElement disabledTextElement1 = driver.findElement(By.id("disabledTextElement"));
        assertThat(disabledTextElement1.isEnabled(),is(false));
        try {
            disabledTextElement1.sendKeys("foo");
            fail("Should have thrown exception");
        } catch (InvalidElementStateException e) {
            e.printStackTrace();
            // Expected
        }
        assertThat(disabledTextElement1.getText(), is(""));
    }

    @Test
    public void testShouldIndicateWhenATextAreaIsDisabled() {
        WebElement textArea= driver.findElement(By.id("areaWithDefaultText"));
        assertThat(textArea.isEnabled(), is(false));
    }

    @Test
    public void testShouldReturnTheContentsOfTextArea() {

        WebElement textArea= driver.findElement(By.id("areaWithDefaultText"));
        assertThat(textArea.getText(), is("Example text"));
    }

    @Test
    public void testCanRetrieveTheCurrentTextFormTextArea() {
        WebElement textArea = driver.findElement(By.id("workingArea"));
        assertEquals("", textArea.getText());
        textArea.sendKeys("hello world");
        assertEquals("hello world", textArea.getText());
    }

    @Test
    public void testCanRetrieveTheCurrentTextFormLineEdit() {
        WebElement txt = driver.findElement(By.id("enabledTextElement"));
        txt.sendKeys("Hello");
        assertEquals("Hello", txt.getText());
        assertThat(txt.getText(), is("Hello"));
        txt.clear();
        assertEquals("", txt.getText());
        txt.sendKeys("hello world");

        assertEquals("hello world", txt.getText());
        assertThat(txt.getText(), is("hello world"));
    }

    @Test
    public void testCanRetrieveTheCurrentValueOfATextFormFieldAsEmailInput() {
        WebElement element = driver.findElement(By.id("enabledTextElement"));
        assertEquals("", element.getText());
        element.sendKeys("hello@example.com");
        assertEquals("hello@example.com", element.getText());
    }

    @Test
    public void testIsVisibleLabel() {
        WebElement element = driver.findElement(By.id("displayedLabel"));
        assertThat(element.isDisplayed(), is(true));

        element = driver.findElement(By.id("notDisplayedLabel"));
        assertThat(element.isDisplayed(), is(false));
    }

    @Test
    public void testReturnTextOfLabel() {
        WebElement element = driver.findElement(By.id("displayedLabel"));
        assertThat(element.getAttribute("text"), is("Visible Label"));
        assertThat(element.getText(), is("Visible Label"));
    }

}
