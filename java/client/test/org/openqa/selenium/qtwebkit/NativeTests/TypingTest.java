package org.openqa.selenium.qtwebkit.NativeTests;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Platform;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.openqa.selenium.testing.JavascriptEnabled;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.Keys;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.openqa.selenium.testing.Ignore.Driver.ANDROID;
import static org.openqa.selenium.testing.Ignore.Driver.HTMLUNIT;
import static org.openqa.selenium.testing.Ignore.Driver.IPHONE;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA_MOBILE;
import static org.openqa.selenium.testing.Ignore.Driver.SAFARI;
import static org.openqa.selenium.testing.Ignore.Driver.SELENESE;

public class TypingTest extends JUnit4TestBase {

    @Before
    public void setUp() throws Exception {
        driver.get("TypingTestWidget");
    }

    @Test
    public void testQuoteMarks() {

        WebElement  inputElement = driver.findElement(By.id("enabeledTextElement"));
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

        WebElement  inputElement = driver.findElement(By.id("enabeledTextElement"));
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

        WebElement  inputElement = driver.findElement(By.id("enabeledTextElement"));
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

    @JavascriptEnabled
    @Ignore({IPHONE, SELENESE})
    @Test
    public void testArrowNotPrintable() {

        WebElement  inputElement = driver.findElement(By.id("enabeledTextElement"));
        inputElement.clear();
        inputElement.sendKeys(Keys.ARROW_LEFT);
        assertThat(inputElement.getText(), is(""));

        inputElement = driver.findElement(By.id("workingArea"));
        inputElement.clear();
        inputElement.sendKeys(Keys.ARROW_LEFT);
        assertThat(inputElement.getText(), is(""));

    }

    @Ignore(value = {HTMLUNIT, IPHONE, SELENESE, OPERA_MOBILE})
    @Test
    public void testMixArrowAndOtherKeys() {

        WebElement  inputElement = driver.findElement(By.id("enabeledTextElement"));
        inputElement.clear();
        inputElement.sendKeys("tet", Keys.ARROW_LEFT, "s");
        assertThat(inputElement.getText(), is("test"));

        inputElement = driver.findElement(By.id("workingArea"));
        inputElement.clear();
        inputElement.sendKeys("tet", Keys.ARROW_LEFT, "s");
        assertThat(inputElement.getText(), is("test"));

    }

    @JavascriptEnabled
    @Ignore(value = {SELENESE, ANDROID}, reason = "untested user agent")
    @Test
    public void testNumericNonShiftKeys() {

        WebElement element = driver.findElement(By.id("enabeledTextElement"));
        element.clear();

        String numericLineCharsNonShifted = "`1234567890-=[]\\;,.'/42";
        element.sendKeys(numericLineCharsNonShifted);

        assertThat(element.getText(), is(numericLineCharsNonShifted));
    }

    @JavascriptEnabled
    @Ignore(value = {HTMLUNIT, ANDROID, OPERA, OPERA_MOBILE},
            reason = "untested user agent")
    @Test
    public void testNumericShiftKeys() {

        WebElement result = driver.findElement(By.id("result"));
        WebElement element = driver.findElement(By.id("enabeledTextElement"));
        element.clear();

        String numericShiftsEtc = "~!@#$%^&*()_+{}:\"<>?|END~";
        element.sendKeys(numericShiftsEtc);

        assertThat(element.getText(), is(numericShiftsEtc));
        assertThat(result.getText(), is(numericShiftsEtc));
    }

    @JavascriptEnabled
    @Ignore(value = ANDROID, reason = "untested user agent")
    @Test
    public void testLowerCaseAlphaKeys() {

        WebElement element = driver.findElement(By.id("enabeledTextElement"));
        element.clear();

        String lowerAlphas = "abcdefghijklmnopqrstuvwxyz";
        element.sendKeys(lowerAlphas);

        assertThat(element.getText(), is(lowerAlphas));
    }

    @JavascriptEnabled
    @Ignore(value = {HTMLUNIT, OPERA, OPERA_MOBILE, ANDROID},
            reason = "untested user agents")
    @Test
    public void testUppercaseAlphaKeys() {
        WebElement element = driver.findElement(By.id("enabeledTextElement"));
        element.clear();

        String upperAlphas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        element.sendKeys(upperAlphas);

        assertThat(element.getText(), is(upperAlphas));
    }

    @JavascriptEnabled
    @Ignore(value = {HTMLUNIT, SELENESE, OPERA, ANDROID, OPERA_MOBILE},
            reason = "untested user agents")
    @Test
    public void testAllPrintableKeys() {

        WebElement element = driver.findElement(By.id("enabeledTextElement"));
        element.clear();

        String allPrintable =
                "!\"#$%&'()*+,-./0123456789:;<=>?@ ABCDEFGHIJKLMNO" +
                        "PQRSTUVWXYZ [\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
        element.sendKeys(allPrintable);

        assertThat(element.getText(), is(allPrintable));
    }

    @Ignore(value = {HTMLUNIT, IPHONE, SELENESE, ANDROID, OPERA_MOBILE},
            reason = "untested user agents")
    @Test
    public void testArrowKeysAndPageUpAndDown() {

        WebElement element = driver.findElement(By.id("enabeledTextElement"));
        element.clear();

        element.sendKeys("a" + Keys.LEFT + "b" + Keys.RIGHT +
                Keys.UP + Keys.DOWN + Keys.PAGE_UP + Keys.PAGE_DOWN + "1");
        assertThat(element.getText(), is("ba1"));
    }

    @JavascriptEnabled
    @Ignore(value = {HTMLUNIT, SELENESE, ANDROID, OPERA_MOBILE},
            reason = "untested user agents")
    @Test
    public void testHomeAndEndAndPageUpAndPageDownKeys() {
        // FIXME: macs don't have HOME keys, would PGUP work?
        if (Platform.getCurrent().is(Platform.MAC)) {
            return;
        }

        WebElement element = driver.findElement(By.id("enabeledTextElement"));

        element.sendKeys("abc" + Keys.HOME + "0" + Keys.LEFT + Keys.RIGHT +
                Keys.PAGE_UP + Keys.PAGE_DOWN + Keys.END + "1" + Keys.HOME +
                "0" + Keys.PAGE_UP + Keys.END + "111" + Keys.HOME + "00");
        assertThat(element.getText(), is("0000abc1111"));
    }

    @JavascriptEnabled
    @Ignore(value = {OPERA, HTMLUNIT, IPHONE, SELENESE, ANDROID, OPERA_MOBILE},
            reason = "untested user agents")
    @Test
    public void testDeleteAndBackspaceKeys() {

        WebElement element = driver.findElement(By.id("enabeledTextElement"));
        element.clear();

        element.sendKeys("abcdefghi");
        assertThat(element.getText(), is("abcdefghi"));

        element.sendKeys(Keys.LEFT, Keys.LEFT, Keys.DELETE);
        assertThat(element.getText(), is("abcdefgi"));

        element.sendKeys(Keys.LEFT, Keys.LEFT, Keys.BACK_SPACE);
        assertThat(element.getText(), is("abcdfgi"));
    }

    @JavascriptEnabled
    @Ignore(value = {HTMLUNIT, IPHONE, SELENESE}, reason = "untested user agents")
    @Test
    public void testSpecialSpaceKeys() {
        WebElement element = driver.findElement(By.id("enabeledTextElement"));
        element.clear();

        element.sendKeys("abcd" + Keys.SPACE + "fgh" + Keys.SPACE + "ij");
        assertThat(element.getText(), is("abcd fgh ij"));
    }

    @JavascriptEnabled
    @Ignore(value = {HTMLUNIT, IPHONE, SELENESE, ANDROID},
            reason = "untested user agents")
    @Test
    public void testSignsKeys() {

        WebElement element = driver.findElement(By.id("enabeledTextElement"));
        element.clear();

        element.sendKeys("abcd" + Keys.MULTIPLY + Keys.SUBTRACT + Keys.ADD +
                Keys.DECIMAL + Keys.SEPARATOR +
                Keys.ADD + Keys.SEMICOLON + Keys.EQUALS + Keys.DIVIDE + "abcd");
        assertThat(element.getText(), is("abcd*-+.,+;=/abcd"));
    }

    @JavascriptEnabled
    @Ignore(value = {OPERA, IPHONE, SELENESE, ANDROID},
            reason = "untested user agents, Opera: F4 triggers sidebar")
    @Test
    public void testFunctionKeys() {

        WebElement element = driver.findElement(By.id("enabeledTextElement"));
        element.clear();

        element.sendKeys("FUNCTION" + Keys.F4 + "-KEYS" + Keys.F4);
        element.sendKeys("" + Keys.F4 + "-TOO" + Keys.F4);
        assertThat(element.getText(), is("FUNCTION-KEYS-TOO"));
    }

    @JavascriptEnabled
    @Ignore(value = {HTMLUNIT, IPHONE, SELENESE, ANDROID, OPERA, SAFARI, OPERA_MOBILE},
            reason = "untested user agents. Opera: F2 focuses location bar" +
                    "Safari: issue 4221",
            issues = {4221})
    @Test
    public void testShiftSelectionDeletes() {
        WebElement element = driver.findElement(By.id("enabeledTextElement"));
        element.clear();

        element.sendKeys("abcd efgh");
        assertThat(element.getText(), is("abcd efgh"));

        element.sendKeys(Keys.SHIFT, Keys.LEFT, Keys.LEFT, Keys.LEFT);
        element.sendKeys(Keys.DELETE);
        assertThat(element.getText(), is("abcd e"));
    }

    @JavascriptEnabled
    @Ignore(value = {HTMLUNIT, SELENESE, ANDROID, OPERA, OPERA_MOBILE},
            reason = "untested user agents")
    @Test
    public void testChordControlHomeShiftEndDelete() {
        // FIXME: macs don't have HOME keys, would PGUP work?
        if (Platform.getCurrent().is(Platform.MAC)) {
            return;
        }

        WebElement element = driver.findElement(By.id("enabeledTextElement"));
        element.clear();

        element.sendKeys("!\"#$%&'()*+,-./0123456789:;<=>?@ ABCDEFG");

        element.sendKeys(Keys.HOME);
        element.sendKeys("" + Keys.SHIFT + Keys.END);
        element.sendKeys(Keys.DELETE);
        assertThat(element.getText(), is(""));
    }

    @JavascriptEnabled
    @Ignore(value = {HTMLUNIT, SELENESE, ANDROID, OPERA, OPERA_MOBILE},
            reason = "untested user agents")
    @Test
    public void testChordReveseShiftHomeSelectionDeletes() {
        // FIXME: macs don't have HOME keys, would PGUP work?
        if (Platform.getCurrent().is(Platform.MAC)) {
            return;
        }
        WebElement element = driver.findElement(By.id("enabeledTextElement"));
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

    @JavascriptEnabled
    @Ignore(value = {HTMLUNIT, SELENESE, ANDROID, OPERA, OPERA_MOBILE},
            reason = "untested user agents")
    @Test
    public void testChordControlCutAndPaste() {
        // FIXME: macs don't have HOME keys, would PGUP work?
        if (Platform.getCurrent().is(Platform.MAC)) {
            return;
        }

        WebElement element = driver.findElement(By.id("enabeledTextElement"));

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
    // and linux, but not on the MAC.

    @JavascriptEnabled
    @Ignore(value = {HTMLUNIT, SELENESE, ANDROID, OPERA, OPERA_MOBILE},
            reason = "untested user agents")
    @Test
    public void testChordControlCopyAndPaste() {
        // FIXME: macs don't have HOME keys, would PGUP work?
        if (Platform.getCurrent().is(Platform.MAC)) {
            return;
        }

        WebElement textElement = driver.findElement(By.id("enabeledTextElement"));

        String paste = "!0123 ABCD";
        textElement.sendKeys(paste);
        assertThat(textElement.getText(), is(paste));

        textElement.sendKeys(Keys.HOME);
        textElement.sendKeys("" + Keys.SHIFT + Keys.END);
        textElement.sendKeys("" + Keys.CONTROL + "c");

        WebElement textArea = driver.findElement(By.id("enabeledTextElement"));

        textArea.sendKeys(Keys.CONTROL, "v");
        assertThat(textArea.getText(), is("!0123 ABCD"));

        textArea.sendKeys(Keys.CONTROL, "v");
        assertThat(textArea.getText(), is("!0123 ABCD!0123 ABCD"));

        textArea.sendKeys("" + Keys.END + Keys.SHIFT + Keys.HOME +
                Keys.NULL + Keys.DELETE);
        assertThat(textArea.getText(), is(""));
    }

}