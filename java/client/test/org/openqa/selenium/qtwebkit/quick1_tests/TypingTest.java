package org.openqa.selenium.qtwebkit.quick1_tests;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Platform;
import org.openqa.selenium.testing.JUnit4TestBase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TypingTest extends JUnit4TestBase {

    @Before
    public void setUp() throws Exception {
        driver.get(appServer.whereIs("quick1/TypingTest.qml"));
    }

    @Test
    public void testQuoteMarks() {

        WebElement  inputElement = driver.findElement(By.id("enabledTextElement"));
        inputElement.clear();
        inputElement.sendKeys("\"");
        assertThat(inputElement.getText(),  is("\""));

        WebElement result = driver.findElement(By.id("result"));
        assertThat(result.getText(),  is("\""));

        inputElement = driver.findElement(By.id("workingArea"));
        inputElement.clear();
        inputElement.sendKeys("\"");
        assertThat(inputElement.getText(),  is("\""));

    }

    @Test
    public void testAtCharacter() {

        WebElement  inputElement = driver.findElement(By.id("enabledTextElement"));
        inputElement.clear();
        inputElement.sendKeys("@");
        assertThat(inputElement.getText(), is("@"));

        WebElement result = driver.findElement(By.id("result"));
        assertThat(result.getText(),  is("@"));

        inputElement = driver.findElement(By.id("workingArea"));
        inputElement.clear();
        inputElement.sendKeys("@");
        assertThat(inputElement.getText(), is("@"));

    }

    @Test
    public void testMixCharacter() {

        WebElement  inputElement = driver.findElement(By.id("enabledTextElement"));
        inputElement.clear();
        inputElement.sendKeys("me@eXample.com");
        assertThat(inputElement.getText(),  is("me@eXample.com"));

        WebElement result = driver.findElement(By.id("result"));
        assertThat(result.getText(),  is("me@eXample.com"));

        inputElement = driver.findElement(By.id("workingArea"));
        inputElement.clear();
        inputElement.sendKeys("me@eXample.com");
        assertThat(inputElement.getText(),  is("me@eXample.com"));
        assertThat(result.getText(), is("me@eXample.com"));

    }

    @Test
    public void testArrowNotPrintable() {

        WebElement  inputElement = driver.findElement(By.id("enabledTextElement"));
        inputElement.clear();
        inputElement.sendKeys(Keys.ARROW_LEFT);
        assertThat(inputElement.getText(), is(""));

        inputElement = driver.findElement(By.id("workingArea"));
        inputElement.clear();
        inputElement.sendKeys(Keys.ARROW_LEFT);
        assertThat(inputElement.getText(), is(""));

    }

    @Test
    public void testMixArrowAndOtherKeys() {

        WebElement  inputElement = driver.findElement(By.id("enabledTextElement"));
        inputElement.clear();
        inputElement.sendKeys("tet", Keys.ARROW_LEFT, "s");
        assertThat(inputElement.getText(), is("test"));

        inputElement = driver.findElement(By.id("workingArea"));
        inputElement.clear();
        inputElement.sendKeys("tet", Keys.ARROW_LEFT, "s");
        assertThat(inputElement.getText(), is("test"));

    }

    @Test
    public void testNumericNonShiftKeys() {

        WebElement element = driver.findElement(By.id("enabledTextElement"));
        element.clear();

        String numericLineCharsNonShifted = "`1234567890-=[]\\;,.'/42";
        element.sendKeys(numericLineCharsNonShifted);

        assertThat(element.getText(), is(numericLineCharsNonShifted));
    }

    @Test
    public void testNumericShiftKeys() {

        WebElement result = driver.findElement(By.id("result"));
        WebElement element = driver.findElement(By.id("enabledTextElement"));
        element.clear();

        String numericShiftsEtc = "~!@#$%^&*()_+{}:\"<>?|END~";
        element.sendKeys(numericShiftsEtc);

        assertThat(element.getText(), is(numericShiftsEtc));
        assertThat(result.getText(), is(numericShiftsEtc));
    }

    @Test
    public void testLowerCaseAlphaKeys() {

        WebElement element = driver.findElement(By.id("enabledTextElement"));
        element.clear();

        String lowerAlphas = "abcdefghijklmnopqrstuvwxyz";
        element.sendKeys(lowerAlphas);

        assertThat(element.getText(), is(lowerAlphas));
    }

    @Test
    public void testUppercaseAlphaKeys() {
        WebElement element = driver.findElement(By.id("enabledTextElement"));
        element.clear();

        String upperAlphas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        element.sendKeys(upperAlphas);

        assertThat(element.getText(), is(upperAlphas));
    }

    @Test
    public void testAllPrintableKeys() {

        WebElement element = driver.findElement(By.id("enabledTextElement"));
        element.clear();

        String allPrintable =
            "!\"#$%&'()*+,-./0123456789:;<=>?@ ABCDEFGHIJKLMNO" +
            "PQRSTUVWXYZ [\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
        element.sendKeys(allPrintable);

        assertThat(element.getText(), is(allPrintable));
    }

    @Test
    public void testArrowKeysAndPageUpAndDown() {

        WebElement element = driver.findElement(By.id("enabledTextElement"));
        element.clear();

        element.sendKeys("a" + Keys.LEFT + "b" + Keys.RIGHT +
                         Keys.UP + Keys.DOWN + Keys.PAGE_UP + Keys.PAGE_DOWN + "1");
        assertThat(element.getText(), is("ba1"));
    }

    @Test
    public void testHomeAndEndAndPageUpAndPageDownKeys() {
        // FIXME: macs don't have HOME keys, would PGUP work?
        if (Platform.getCurrent().is(Platform.MAC)) {
            return;
        }

        WebElement element = driver.findElement(By.id("enabledTextElement"));

        element.sendKeys("abc" + Keys.HOME + "0" + Keys.LEFT + Keys.RIGHT +
                         Keys.PAGE_UP + Keys.PAGE_DOWN + Keys.END + "1" + Keys.HOME +
                         "0" + Keys.PAGE_UP + Keys.END + "111" + Keys.HOME + "00");
        assertThat(element.getText(), is("0000abc1111"));
    }

    @Test
    public void testDeleteAndBackspaceKeys() {

        WebElement element = driver.findElement(By.id("enabledTextElement"));
        element.clear();

        element.sendKeys("abcdefghi");
        assertThat(element.getText(), is("abcdefghi"));

        element.sendKeys(Keys.LEFT, Keys.LEFT, Keys.DELETE);
        assertThat(element.getText(), is("abcdefgi"));

        element.sendKeys(Keys.LEFT, Keys.LEFT, Keys.BACK_SPACE);
        assertThat(element.getText(), is("abcdfgi"));
    }

    @Test
    public void testSpecialSpaceKeys() {
        WebElement element = driver.findElement(By.id("enabledTextElement"));
        element.clear();

        element.sendKeys("abcd" + Keys.SPACE + "fgh" + Keys.SPACE + "ij");
        assertThat(element.getText(), is("abcd fgh ij"));
    }

    @Test
    public void testSignsKeys() {

        WebElement element = driver.findElement(By.id("enabledTextElement"));
        element.clear();

        element.sendKeys("abcd" + Keys.MULTIPLY + Keys.SUBTRACT + Keys.ADD +
                         Keys.DECIMAL + Keys.SEPARATOR +
                         Keys.ADD + Keys.SEMICOLON + Keys.EQUALS + Keys.DIVIDE + "abcd");
        assertThat(element.getText(), is("abcd*-+.,+;=/abcd"));
    }

    @Test
    public void testFunctionKeys() {

        WebElement element = driver.findElement(By.id("enabledTextElement"));
        element.clear();

        element.sendKeys("FUNCTION" + Keys.F4 + "-KEYS" + Keys.F4);
        element.sendKeys("" + Keys.F4 + "-TOO" + Keys.F4);
        assertThat(element.getText(), is("FUNCTION-KEYS-TOO"));
    }

    @Test
    public void testShiftSelectionDeletes() {
        WebElement element = driver.findElement(By.id("enabledTextElement"));
        element.clear();

        element.sendKeys("abcd efgh");
        assertThat(element.getText(), is("abcd efgh"));

        element.sendKeys(Keys.SHIFT, Keys.LEFT, Keys.LEFT, Keys.LEFT);
        element.sendKeys(Keys.DELETE);
        assertThat(element.getText(), is("abcd e"));
    }

    @Test
    public void testChordControlHomeShiftEndDelete() {
        // FIXME: macs don't have HOME keys, would PGUP work?
        if (Platform.getCurrent().is(Platform.MAC)) {
            return;
        }

        WebElement element = driver.findElement(By.id("enabledTextElement"));
        element.clear();

        element.sendKeys("!\"#$%&'()*+,-./0123456789:;<=>?@ ABCDEFG");

        element.sendKeys(Keys.HOME);
        element.sendKeys("" + Keys.SHIFT + Keys.END);
        element.sendKeys(Keys.DELETE);
        assertThat(element.getText(), is(""));
    }

    @Test
    public void testChordReveseShiftHomeSelectionDeletes() {
        // FIXME: macs don't have HOME keys, would PGUP work?
        if (Platform.getCurrent().is(Platform.MAC)) {
            return;
        }
        WebElement element = driver.findElement(By.id("enabledTextElement"));
        element.clear();

        element.sendKeys("done" + Keys.HOME);
        assertThat(element.getText(), is("done"));

        element.sendKeys("" + Keys.SHIFT + "ALL " + Keys.HOME);
        assertThat(element.getText(), is("ALL done"));

        element.sendKeys(Keys.DELETE);
        assertThat(element.getText(), is("done"));

        element.sendKeys("" + Keys.END + Keys.SHIFT + Keys.HOME);
        assertThat(element.getText(), is("done"));

        element.sendKeys("" + Keys.DELETE);
        assertThat(element.getText(), is(""));
    }

    // control-x control-v here for cut & paste tests, these work on windows
    // and linux, but not on the MAC.

    // TO DO:implement identification keys letters
    // Issue MHA-659
    @Test
    public void testChordControlCutAndPaste() {
        // FIXME: macs don't have HOME keys, would PGUP work?
        if (Platform.getCurrent().is(Platform.MAC)) {
            return;
        }

        WebElement element = driver.findElement(By.id("enabledTextElement"));

        String paste = "!\"#$%&'()*+,-./0123456789:;<=>?@ ABCDEFG";
        element.sendKeys(paste);
        assertThat(element.getText(), is(paste));

        element.sendKeys(Keys.HOME);
        element.sendKeys("" + Keys.SHIFT + Keys.END);
        element.sendKeys("" + Keys.CONTROL + "x");
        assertThat(element.getText(), is(""));

        element.sendKeys("" + Keys.CONTROL, "v");

        // Cut the last 3 letters.
        element.sendKeys("" + Keys.LEFT + Keys.LEFT + Keys.LEFT +
                         Keys.SHIFT + Keys.END);

        element.sendKeys(Keys.CONTROL, "x");
        assertThat(element.getText(), is(paste.substring(0, paste.length() - 3)));

        // Paste the last 3 letters.
        element.sendKeys(Keys.CONTROL, "v");
        assertThat(element.getText(), is(paste));

        element.sendKeys(Keys.HOME);
        element.sendKeys(Keys.CONTROL, "v");
        element.sendKeys(Keys.CONTROL, "v" + "v");
        element.sendKeys(Keys.CONTROL, "v" + "v" + "v");
        assertThat(element.getText(), is("EFGEFGEFGEFGEFGEFG" + paste));

        element.sendKeys("" + Keys.END + Keys.SHIFT + Keys.HOME +
                         Keys.NULL + Keys.DELETE);
        assertThat(element.getText(), is(""));
    }

    // control-c control-v here for copy & paste tests, these work on windows
    // and linux, but not on the MAC

    // TO DO:implement identification keys letters
    // Issue MHA-659
    @Test
    public void testChordControlCopyAndPaste() {
        // FIXME: macs don't have HOME keys, would PGUP work?
        if (Platform.getCurrent().is(Platform.MAC)) {
            return;
        }

        WebElement textElement = driver.findElement(By.id("enabledTextElement"));

        String paste = "!0123 ABCD";
        textElement.sendKeys(paste);
        assertThat(textElement.getText(), is(paste));

        textElement.sendKeys(Keys.HOME);
        textElement.sendKeys("" + Keys.SHIFT + Keys.END);
        textElement.sendKeys("" + Keys.CONTROL + "c");

        WebElement textArea = driver.findElement(By.id("enabledTextElement"));

        textArea.sendKeys(Keys.CONTROL, "v");
        assertThat(textArea.getText(), is("!0123 ABCD"));

        textArea.sendKeys(Keys.CONTROL, "v");
        assertThat(textArea.getText(), is("!0123 ABCD!0123 ABCD"));

        textArea.sendKeys("" + Keys.END + Keys.SHIFT + Keys.HOME +
                          Keys.NULL + Keys.DELETE);
        assertThat(textArea.getText(), is(""));
    }

}