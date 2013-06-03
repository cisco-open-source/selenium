package org.openqa.selenium.qtwebkit.nativetests;

import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

public class BasicKeyboardInterfaceTest extends JUnit4TestBase {

    @Before
    public void setUp() throws Exception {
        driver.get("qtwidget://BasicKeyboardInterfaceTestWidget");
    }

    @Test
    public void testBasicKeyboardInput() {

        WebElement inputElement = driver.findElement(By.id("enabledTextElement"));

        inputElement.sendKeys("abc def");
        assertThat(inputElement.getText(), is("abc def"));
    }

    @Test
    public void testSendingKeyDownOnly() {

        WebElement keysEventInput = driver.findElement(By.id("enabledTextElement"));
        WebElement resultElement = driver.findElement(By.id("resultIsKeyDown"));

        keysEventInput.sendKeys("a");
        assertThat(resultElement.getText(), is("Key Down"));

    }

    @Test
    public void testSendingKeyUp() {

       WebElement inputElement = driver.findElement(By.id("enabledTextElement"));
       WebElement resultElement = driver.findElement(By.id("resultIsKeyUp"));

       inputElement.sendKeys("a");
       assertThat(resultElement.getText(), is("Key Up"));
    }

    @Test
    public void testSendingKeysShiftOnly() {

        WebElement inputElement = driver.findElement(By.id("enabledTextElement"));
        WebElement resultElement = driver.findElement(By.id("resultModifier"));

        inputElement.sendKeys(Keys.SHIFT);
        assertThat(resultElement.getText(), is("Shift"));
        assertThat(inputElement.getText(), is(""));

    }

    @Test
    public void testSendingKeyCtrlOnly() {

        WebElement inputElement = driver.findElement(By.id("enabledTextElement"));
        WebElement resultElement = driver.findElement(By.id("resultModifier"));

        inputElement.sendKeys(Keys.CONTROL);
        assertThat(resultElement.getText(), is("Ctrl"));
        assertThat(inputElement.getText(), is(""));

    }

    @Test
    public void testSendingKeyAltOnly() {

        WebElement inputElement = driver.findElement(By.id("enabledTextElement"));
        WebElement resultElement = driver.findElement(By.id("resultModifier"));

        inputElement.sendKeys(Keys.ALT);
        assertThat(resultElement.getText(), is("Alt"));
        assertThat(inputElement.getText(), is(""));

    }

    // TO DO:implement identification keys letters
    // Issue MHA-659
    @Test
    public void testSendingKeysWithShiftPressed() {

        WebElement inputElement = driver.findElement(By.id("enabledTextElement"));
        WebElement resultElement = driver.findElement(By.id("resultModifier"));

        inputElement.sendKeys("ab" + Keys.SHIFT);
        assertThat(resultElement.getText(), is("Shift"));

        assertThat(inputElement.getText(), is("AB"));

    }

    @Test
    public void testSendingKeysWithCtrlPressed() {

        WebElement inputElement = driver.findElement(By.id("enabledTextElement"));
        WebElement resultElement = driver.findElement(By.id("resultModifier"));

        inputElement.sendKeys("www" + Keys.CONTROL);
        assertThat(resultElement.getText(), is("Ctrl"));
        assertThat(inputElement.getText(), is("www"));

    }

    @Test
    public void testSendingKeysWithAltPressed() {

        WebElement inputElement = driver.findElement(By.id("enabledTextElement"));
        WebElement resultElement = driver.findElement(By.id("resultModifier"));

        inputElement.sendKeys("nnn" + Keys.ALT);
        assertThat(resultElement.getText(), is("Alt"));
        assertThat(inputElement.getText(), is("nnn"));

    }

}

