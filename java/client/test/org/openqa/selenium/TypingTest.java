/*
Copyright 2007-2012 Selenium committers
Copyright 2012 Software Freedom Conservancy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package org.openqa.selenium;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.openqa.selenium.WaitingConditions.elementValueToEqual;
import static org.openqa.selenium.testing.Ignore.Driver.ALL;
import static org.openqa.selenium.testing.Ignore.Driver.ANDROID;
import static org.openqa.selenium.testing.Ignore.Driver.FIREFOX;
import static org.openqa.selenium.testing.Ignore.Driver.HTMLUNIT;
import static org.openqa.selenium.testing.Ignore.Driver.IE;
import static org.openqa.selenium.testing.Ignore.Driver.IPHONE;
import static org.openqa.selenium.testing.Ignore.Driver.MARIONETTE;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA_MOBILE;
import static org.openqa.selenium.testing.Ignore.Driver.PHANTOMJS;
import static org.openqa.selenium.testing.Ignore.Driver.SAFARI;
import static org.openqa.selenium.testing.Ignore.Driver.QTWEBKIT;
import static org.openqa.selenium.testing.TestUtilities.getEffectivePlatform;
import static org.openqa.selenium.testing.TestUtilities.getFirefoxVersion;
import static org.openqa.selenium.testing.TestUtilities.isFirefox;
import static org.openqa.selenium.testing.TestUtilities.isNativeEventsEnabled;

import com.google.common.base.Joiner;

import org.junit.Test;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.openqa.selenium.testing.JavascriptEnabled;
import org.openqa.selenium.testing.drivers.Browser;

public class TypingTest extends JUnit4TestBase {

  @JavascriptEnabled
  @Ignore({ANDROID, MARIONETTE})
  @Test
  public void testShouldFireKeyPressEvents() {
    driver.get(pages.javascriptPage);

    WebElement keyReporter = driver.findElement(By.id("keyReporter"));
    keyReporter.sendKeys("a");

    WebElement result = driver.findElement(By.id("result"));
    assertThat(result.getText(), containsString("press:"));
  }

  @JavascriptEnabled
  @Test
  @Ignore(MARIONETTE)
  public void testShouldFireKeyDownEvents() {
    driver.get(pages.javascriptPage);

    WebElement keyReporter = driver.findElement(By.id("keyReporter"));
    keyReporter.sendKeys("I");

    WebElement result = driver.findElement(By.id("result"));
    assertThat(result.getText(), containsString("down:"));
  }

  @JavascriptEnabled
  @Test
  @Ignore(MARIONETTE)
  public void testShouldFireKeyUpEvents() {
    driver.get(pages.javascriptPage);

    WebElement keyReporter = driver.findElement(By.id("keyReporter"));
    keyReporter.sendKeys("a");

    WebElement result = driver.findElement(By.id("result"));
    assertThat(result.getText(), containsString("up:"));
  }

  @Test
  @Ignore(MARIONETTE)
  public void testShouldTypeLowerCaseLetters() {
    driver.get(pages.javascriptPage);

    WebElement keyReporter = driver.findElement(By.id("keyReporter"));
    keyReporter.sendKeys("abc def");

    assertThat(keyReporter.getAttribute("value"), is("abc def"));
  }

  @Test
  @Ignore(MARIONETTE)
  public void testShouldBeAbleToTypeCapitalLetters() {
    driver.get(pages.javascriptPage);

    WebElement keyReporter = driver.findElement(By.id("keyReporter"));
    keyReporter.sendKeys("ABC DEF");

    assertThat(keyReporter.getAttribute("value"), is("ABC DEF"));
  }

  @Test
  @Ignore(MARIONETTE)
  public void testShouldBeAbleToTypeQuoteMarks() {
    driver.get(pages.javascriptPage);

    WebElement keyReporter = driver.findElement(By.id("keyReporter"));
    keyReporter.sendKeys("\"");

    assertThat(keyReporter.getAttribute("value"), is("\""));
  }

  @Test
  @Ignore(MARIONETTE)
  public void testShouldBeAbleToTypeTheAtCharacter() {
    // simon: I tend to use a US/UK or AUS keyboard layout with English
    // as my primary language. There are consistent reports that we're
    // not handling i18nised keyboards properly. This test exposes this
    // in a lightweight manner when my keyboard is set to the DE mapping
    // and we're using IE.

    driver.get(pages.javascriptPage);

    WebElement keyReporter = driver.findElement(By.id("keyReporter"));
    keyReporter.sendKeys("@");

    assertThat(keyReporter.getAttribute("value"), is("@"));
  }

  @Test
  @Ignore(MARIONETTE)
  public void testShouldBeAbleToMixUpperAndLowerCaseLetters() {
    driver.get(pages.javascriptPage);

    WebElement keyReporter = driver.findElement(By.id("keyReporter"));
    keyReporter.sendKeys("me@eXample.com");

    assertThat(keyReporter.getAttribute("value"), is("me@eXample.com"));
  }

  @JavascriptEnabled
  @Ignore({IPHONE, MARIONETTE})
  @Test
  public void testArrowKeysShouldNotBePrintable() {
    driver.get(pages.javascriptPage);

    WebElement keyReporter = driver.findElement(By.id("keyReporter"));
    keyReporter.sendKeys(Keys.ARROW_LEFT);

    assertThat(keyReporter.getAttribute("value"), is(""));
  }

  @Ignore(value = {HTMLUNIT, IPHONE, OPERA_MOBILE, MARIONETTE})
  @Test
  public void testShouldBeAbleToUseArrowKeys() {
    driver.get(pages.javascriptPage);

    WebElement keyReporter = driver.findElement(By.id("keyReporter"));
    keyReporter.sendKeys("tet", Keys.ARROW_LEFT, "s");

    assertThat(keyReporter.getAttribute("value"), is("test"));
  }

  @JavascriptEnabled
  @Ignore(ANDROID)
  @Test
  public void testWillSimulateAKeyUpWhenEnteringTextIntoInputElements() {
    driver.get(pages.javascriptPage);

    WebElement element = driver.findElement(By.id("keyUp"));
    element.sendKeys("I like cheese");

    WebElement result = driver.findElement(By.id("result"));
    assertThat(result.getText(), equalTo("I like cheese"));
  }

  @JavascriptEnabled
  @Ignore({ANDROID})
  @Test
  public void testWillSimulateAKeyDownWhenEnteringTextIntoInputElements() {
    driver.get(pages.javascriptPage);

    WebElement element = driver.findElement(By.id("keyDown"));
    element.sendKeys("I like cheese");

    WebElement result = driver.findElement(By.id("result"));
    // Because the key down gets the result before the input element is
    // filled, we're a letter short here
    assertThat(result.getText(), equalTo("I like chees"));
  }

  @JavascriptEnabled
  @Ignore({ANDROID})
  @Test
  public void testWillSimulateAKeyPressWhenEnteringTextIntoInputElements() {
    driver.get(pages.javascriptPage);

    WebElement element = driver.findElement(By.id("keyPress"));
    element.sendKeys("I like cheese");

    WebElement result = driver.findElement(By.id("result"));
    // Because the key down gets the result before the input element is
    // filled, we're a letter short here
    assertThat(result.getText(), equalTo("I like chees"));
  }

  @JavascriptEnabled
  @Ignore({ANDROID, OPERA_MOBILE})
  @Test
  public void testWillSimulateAKeyUpWhenEnteringTextIntoTextAreas() {
    driver.get(pages.javascriptPage);

    WebElement element = driver.findElement(By.id("keyUpArea"));
    element.sendKeys("I like cheese");

    WebElement result = driver.findElement(By.id("result"));
    assertThat(result.getText(), equalTo("I like cheese"));
  }

  @JavascriptEnabled
  @Ignore({ANDROID, OPERA_MOBILE})
  @Test
  public void testWillSimulateAKeyDownWhenEnteringTextIntoTextAreas() {
    driver.get(pages.javascriptPage);

    WebElement element = driver.findElement(By.id("keyDownArea"));
    element.sendKeys("I like cheese");

    WebElement result = driver.findElement(By.id("result"));
    // Because the key down gets the result before the input element is
    // filled, we're a letter short here
    assertThat(result.getText(), equalTo("I like chees"));
  }

  @JavascriptEnabled
  @Ignore({ANDROID, OPERA_MOBILE})
  @Test
  public void testWillSimulateAKeyPressWhenEnteringTextIntoTextAreas() {
    driver.get(pages.javascriptPage);

    WebElement element = driver.findElement(By.id("keyPressArea"));
    element.sendKeys("I like cheese");

    WebElement result = driver.findElement(By.id("result"));
    // Because the key down gets the result before the input element is
    // filled, we're a letter short here
    assertThat(result.getText(), equalTo("I like chees"));
  }

  @JavascriptEnabled
  @Ignore(value = {FIREFOX, IE, ANDROID, MARIONETTE},
          reason = "firefox specific not yet tested in htmlunit. Firefox demands to have the "
                   + "focus on the window already., Android: Uses native key events.")
  @Test
  public void testShouldFireFocusKeyEventsInTheRightOrder() {
    driver.get(pages.javascriptPage);

    WebElement result = driver.findElement(By.id("result"));
    WebElement element = driver.findElement(By.id("theworks"));

    element.sendKeys("a");
    assertThat(result.getText().trim(), is("focus keydown keypress keyup"));
  }

  @JavascriptEnabled
  @Ignore(value = {HTMLUNIT, IE, IPHONE, PHANTOMJS, ANDROID, MARIONETTE},
          reason = "firefox-specific. Android uses prev/next.")
  @Test
  public void testShouldReportKeyCodeOfArrowKeys() {
    assumeFalse(Browser.detect() == Browser.opera &&
                getEffectivePlatform().is(Platform.WINDOWS));

    driver.get(pages.javascriptPage);

    WebElement result = driver.findElement(By.id("result"));
    WebElement element = driver.findElement(By.id("keyReporter"));

    element.sendKeys(Keys.ARROW_DOWN);
    checkRecordedKeySequence(result, 40);

    element.sendKeys(Keys.ARROW_UP);
    checkRecordedKeySequence(result, 38);

    element.sendKeys(Keys.ARROW_LEFT);
    checkRecordedKeySequence(result, 37);

    element.sendKeys(Keys.ARROW_RIGHT);
    checkRecordedKeySequence(result, 39);

    // And leave no rubbish/printable keys in the "keyReporter"
    assertThat(element.getAttribute("value"), is(""));
  }

  private static void checkRecordedKeySequence(WebElement element, int expectedKeyCode) {
    assertThat(element.getText().trim(),
               anyOf(is(String.format("down: %1$d press: %1$d up: %1$d", expectedKeyCode)),
                     is(String.format("down: %1$d up: %1$d", expectedKeyCode))));
  }

  @JavascriptEnabled
  @Ignore(value = {HTMLUNIT, IPHONE, ANDROID, MARIONETTE},
          reason = "untested user agents")
  @Test
  public void testShouldReportKeyCodeOfArrowKeysUpDownEvents() {
    assumeFalse(Browser.detect() == Browser.opera &&
                getEffectivePlatform().is(Platform.WINDOWS));

    driver.get(pages.javascriptPage);

    WebElement result = driver.findElement(By.id("result"));
    WebElement element = driver.findElement(By.id("keyReporter"));

    element.sendKeys(Keys.ARROW_DOWN);
    assertThat(result.getText().trim(), containsString("down: 40"));
    assertThat(result.getText().trim(), containsString("up: 40"));

    element.sendKeys(Keys.ARROW_UP);
    assertThat(result.getText().trim(), containsString("down: 38"));
    assertThat(result.getText().trim(), containsString("up: 38"));

    element.sendKeys(Keys.ARROW_LEFT);
    assertThat(result.getText().trim(), containsString("down: 37"));
    assertThat(result.getText().trim(), containsString("up: 37"));

    element.sendKeys(Keys.ARROW_RIGHT);
    assertThat(result.getText().trim(), containsString("down: 39"));
    assertThat(result.getText().trim(), containsString("up: 39"));

    // And leave no rubbish/printable keys in the "keyReporter"
    assertThat(element.getAttribute("value"), is(""));
  }

  @JavascriptEnabled
  @Ignore(value = {ANDROID, MARIONETTE}, reason = "untested user agent")
  @Test
  public void testNumericNonShiftKeys() {
    driver.get(pages.javascriptPage);

    WebElement element = driver.findElement(By.id("keyReporter"));

    String numericLineCharsNonShifted = "`1234567890-=[]\\;,.'/42";
    element.sendKeys(numericLineCharsNonShifted);

    assertThat(element.getAttribute("value"), is(numericLineCharsNonShifted));
  }

  @JavascriptEnabled
  @Ignore(value = {HTMLUNIT, ANDROID, OPERA, OPERA_MOBILE, MARIONETTE, QTWEBKIT},
          reason = "untested user agent")
  @Test
  public void testNumericShiftKeys() {
    driver.get(pages.javascriptPage);

    WebElement result = driver.findElement(By.id("result"));
    WebElement element = driver.findElement(By.id("keyReporter"));

    String numericShiftsEtc = "~!@#$%^&*()_+{}:\"<>?|END~";
    element.sendKeys(numericShiftsEtc);

    assertThat(element.getAttribute("value"), is(numericShiftsEtc));
    assertThat(result.getText().trim(), containsString(" up: 16"));
  }

  @JavascriptEnabled
  @Ignore(value = {ANDROID, MARIONETTE}, reason = "untested user agent")
  @Test
  public void testLowerCaseAlphaKeys() {
    driver.get(pages.javascriptPage);

    WebElement element = driver.findElement(By.id("keyReporter"));

    String lowerAlphas = "abcdefghijklmnopqrstuvwxyz";
    element.sendKeys(lowerAlphas);

    assertThat(element.getAttribute("value"), is(lowerAlphas));
  }

  @JavascriptEnabled
  @Ignore(value = {HTMLUNIT, OPERA, OPERA_MOBILE, ANDROID, MARIONETTE, QTWEBKIT},
          reason = "untested user agents")
  @Test
  public void testUppercaseAlphaKeys() {
    driver.get(pages.javascriptPage);

    WebElement result = driver.findElement(By.id("result"));
    WebElement element = driver.findElement(By.id("keyReporter"));

    String upperAlphas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    element.sendKeys(upperAlphas);

    assertThat(element.getAttribute("value"), is(upperAlphas));
    assertThat(result.getText().trim(), containsString(" up: 16"));
  }

  @JavascriptEnabled
  @Ignore(value = {HTMLUNIT, OPERA, ANDROID, OPERA_MOBILE, MARIONETTE, QTWEBKIT},
          reason = "untested user agents")
  @Test
  public void testAllPrintableKeys() {
    driver.get(pages.javascriptPage);

    WebElement result = driver.findElement(By.id("result"));
    WebElement element = driver.findElement(By.id("keyReporter"));

    String allPrintable =
        "!\"#$%&'()*+,-./0123456789:;<=>?@ ABCDEFGHIJKLMNO" +
        "PQRSTUVWXYZ [\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    element.sendKeys(allPrintable);

    assertThat(element.getAttribute("value"), is(allPrintable));
    assertThat(result.getText().trim(), containsString(" up: 16"));
  }

  @Ignore(value = {HTMLUNIT, IPHONE, ANDROID, OPERA_MOBILE, MARIONETTE},
          reason = "untested user agents")
  @Test
  public void testArrowKeysAndPageUpAndDown() {
    driver.get(pages.javascriptPage);

    WebElement element = driver.findElement(By.id("keyReporter"));

    element.sendKeys("a" + Keys.LEFT + "b" + Keys.RIGHT +
                     Keys.UP + Keys.DOWN + Keys.PAGE_UP + Keys.PAGE_DOWN + "1");
    assertThat(element.getAttribute("value"), is("ba1"));
  }

  @JavascriptEnabled
  @Ignore(value = {HTMLUNIT, ANDROID, OPERA_MOBILE, MARIONETTE},
          reason = "untested user agents")
  @Test
  public void testHomeAndEndAndPageUpAndPageDownKeys() {
    assumeFalse("FIXME: macs don't have HOME keys, would PGUP work?",
                getEffectivePlatform().is(Platform.MAC));

    driver.get(pages.javascriptPage);

    WebElement element = driver.findElement(By.id("keyReporter"));

    element.sendKeys("abc" + Keys.HOME + "0" + Keys.LEFT + Keys.RIGHT +
                     Keys.PAGE_UP + Keys.PAGE_DOWN + Keys.END + "1" + Keys.HOME +
                     "0" + Keys.PAGE_UP + Keys.END + "111" + Keys.HOME + "00");
    assertThat(element.getAttribute("value"), is("0000abc1111"));
  }

  @JavascriptEnabled
  @Ignore(value = {OPERA, HTMLUNIT, IPHONE, ANDROID, OPERA_MOBILE, MARIONETTE},
          reason = "untested user agents")
  @Test
  public void testDeleteAndBackspaceKeys() {
    driver.get(pages.javascriptPage);

    WebElement element = driver.findElement(By.id("keyReporter"));

    element.sendKeys("abcdefghi");
    assertThat(element.getAttribute("value"), is("abcdefghi"));

    element.sendKeys(Keys.LEFT, Keys.LEFT, Keys.DELETE);
    assertThat(element.getAttribute("value"), is("abcdefgi"));

    element.sendKeys(Keys.LEFT, Keys.LEFT, Keys.BACK_SPACE);
    assertThat(element.getAttribute("value"), is("abcdfgi"));
  }

  @JavascriptEnabled
  @Ignore(value = {HTMLUNIT, IPHONE, MARIONETTE}, reason = "untested user agents")
  @Test
  public void testSpecialSpaceKeys() {
    driver.get(pages.javascriptPage);

    WebElement element = driver.findElement(By.id("keyReporter"));

    element.sendKeys("abcd" + Keys.SPACE + "fgh" + Keys.SPACE + "ij");
    assertThat(element.getAttribute("value"), is("abcd fgh ij"));
  }

  @JavascriptEnabled
  @Ignore(value = {HTMLUNIT, IPHONE, ANDROID, MARIONETTE, QTWEBKIT},
          reason = "untested user agents")
  @Test
  public void testNumberpadKeys() {
    driver.get(pages.javascriptPage);

    WebElement element = driver.findElement(By.id("keyReporter"));

    element.sendKeys("abcd" + Keys.MULTIPLY + Keys.SUBTRACT + Keys.ADD +
                     Keys.DECIMAL + Keys.SEPARATOR + Keys.NUMPAD0 + Keys.NUMPAD9 +
                     Keys.ADD + Keys.SEMICOLON + Keys.EQUALS + Keys.DIVIDE +
                     Keys.NUMPAD3 + "abcd");
    assertThat(element.getAttribute("value"), is("abcd*-+.,09+;=/3abcd"));
  }

  @JavascriptEnabled
  @Ignore(value = {OPERA, IPHONE, ANDROID, IE, MARIONETTE},
          reason = "untested user agents, Opera: F4 triggers sidebar," +
                   "IE: F4 triggers address bar")
  @Test
  public void testFunctionKeys() {
    driver.get(pages.javascriptPage);

    WebElement element = driver.findElement(By.id("keyReporter"));

    element.sendKeys("FUNCTION" + Keys.F4 + "-KEYS" + Keys.F4);
    element.sendKeys("" + Keys.F4 + "-TOO" + Keys.F4);
    assertThat(element.getAttribute("value"), is("FUNCTION-KEYS-TOO"));
  }

  @JavascriptEnabled
  @Ignore(value = {HTMLUNIT, IPHONE, ANDROID, OPERA, SAFARI, OPERA_MOBILE, MARIONETTE},
          reason = "untested user agents. Opera: F2 focuses location bar" +
                   "Safari: issue 4221",
          issues = {4221})
  @Test
  public void testShiftSelectionDeletes() {
    driver.get(pages.javascriptPage);

    WebElement element = driver.findElement(By.id("keyReporter"));

    element.sendKeys("abcd efgh");
    assertThat(element.getAttribute("value"), is("abcd efgh"));

    element.sendKeys(Keys.SHIFT, Keys.LEFT, Keys.LEFT, Keys.LEFT);
    element.sendKeys(Keys.DELETE);
    assertThat(element.getAttribute("value"), is("abcd e"));
  }

  @JavascriptEnabled
  @Ignore(value = {HTMLUNIT, ANDROID, OPERA, OPERA_MOBILE, MARIONETTE},
          reason = "untested user agents")
  @Test
  public void testChordControlHomeShiftEndDelete() {
    assumeFalse("FIXME: macs don't have HOME keys, would PGUP work?",
                getEffectivePlatform().is(Platform.MAC));

    driver.get(pages.javascriptPage);

    WebElement result = driver.findElement(By.id("result"));
    WebElement element = driver.findElement(By.id("keyReporter"));

    element.sendKeys("!\"#$%&'()*+,-./0123456789:;<=>?@ ABCDEFG");

    element.sendKeys(Keys.HOME);
    element.sendKeys("" + Keys.SHIFT + Keys.END);
    assertThat(result.getText(), containsString(" up: 16"));

    element.sendKeys(Keys.DELETE);
    assertThat(element.getAttribute("value"), is(""));
  }

  @JavascriptEnabled
  @Ignore(value = {HTMLUNIT, ANDROID, OPERA, OPERA_MOBILE, MARIONETTE},
          reason = "untested user agents")
  @Test
  public void testChordReveseShiftHomeSelectionDeletes() {
    assumeFalse("FIXME: macs don't have HOME keys, would PGUP work?",
                getEffectivePlatform().is(Platform.MAC));

    driver.get(pages.javascriptPage);

    WebElement result = driver.findElement(By.id("result"));
    WebElement element = driver.findElement(By.id("keyReporter"));

    element.sendKeys("done" + Keys.HOME);
    assertThat(element.getAttribute("value"), is("done"));

    element.sendKeys("" + Keys.SHIFT + "ALL " + Keys.HOME);
    assertThat(element.getAttribute("value"), is("ALL done"));

    element.sendKeys(Keys.DELETE);
    assertThat(element.getAttribute("value"), is("done"));

    element.sendKeys("" + Keys.END + Keys.SHIFT + Keys.HOME);
    assertThat(element.getAttribute("value"), is("done"));
    assertThat( // Note: trailing SHIFT up here
                result.getText().trim(), containsString(" up: 16"));

    element.sendKeys("" + Keys.DELETE);
    assertThat(element.getAttribute("value"), is(""));
  }

  // control-x control-v here for cut & paste tests, these work on windows
  // and linux, but not on the MAC.

  @JavascriptEnabled
  @Ignore(value = {HTMLUNIT, ANDROID, OPERA, OPERA_MOBILE, MARIONETTE, QTWEBKIT},
          reason = "untested user agents")
  @Test
  public void testChordControlCutAndPaste() {
    assumeFalse("FIXME: macs don't have HOME keys, would PGUP work?",
                getEffectivePlatform().is(Platform.MAC));
    assumeFalse("FIXME: Fails in Firefox on Linux with native events",
                isFirefox(driver) &&
                isNativeEventsEnabled(driver) &&
                getEffectivePlatform().is(Platform.LINUX));

    driver.get(pages.javascriptPage);

    WebElement element = driver.findElement(By.id("keyReporter"));
    WebElement result = driver.findElement(By.id("result"));

    String paste = "!\"#$%&'()*+,-./0123456789:;<=>?@ ABCDEFG";
    element.sendKeys(paste);
    assertThat(element.getAttribute("value"), is(paste));

    element.sendKeys(Keys.HOME);
    element.sendKeys("" + Keys.SHIFT + Keys.END);
    assertThat(result.getText().trim(), containsString(" up: 16"));

    element.sendKeys(Keys.CONTROL, "x");
    assertThat(element.getAttribute("value"), is(""));

    element.sendKeys(Keys.CONTROL, "v");
    wait.until(elementValueToEqual(element, paste));

    // Cut the last 3 letters.
    element.sendKeys("" + Keys.LEFT + Keys.LEFT + Keys.LEFT +
                     Keys.SHIFT + Keys.END);

    element.sendKeys(Keys.CONTROL, "x");
    assertThat(element.getAttribute("value"), is(paste.substring(0, paste.length() - 3)));

    // Paste the last 3 letters.
    element.sendKeys(Keys.CONTROL, "v");
    assertThat(element.getAttribute("value"), is(paste));

    element.sendKeys(Keys.HOME);
    element.sendKeys(Keys.CONTROL, "v");
    element.sendKeys(Keys.CONTROL, "v" + "v");
    element.sendKeys(Keys.CONTROL, "v" + "v" + "v");
    assertThat(element.getAttribute("value"), is("EFGEFGEFGEFGEFGEFG" + paste));

    element.sendKeys("" + Keys.END + Keys.SHIFT + Keys.HOME +
                     Keys.NULL + Keys.DELETE);
    assertThat(element.getAttribute("value"), is(""));
  }

  @JavascriptEnabled
  @Test
  public void testShouldTypeIntoInputElementsThatHaveNoTypeAttribute() {
    driver.get(pages.formPage);

    WebElement element = driver.findElement(By.id("no-type"));

    element.sendKeys("should say cheese");
    assertThat(element.getAttribute("value"), is("should say cheese"));
  }

  @JavascriptEnabled
  @Ignore(value = {IPHONE, MARIONETTE}, reason = "untested user agents")
  @Test
  public void testShouldNotTypeIntoElementsThatPreventKeyDownEvents() {
    driver.get(pages.javascriptPage);

    WebElement silent = driver.findElement(By.name("suppress"));

    silent.sendKeys("s");
    assertThat(silent.getAttribute("value"), is(""));
  }

  @JavascriptEnabled
  @Ignore(value = {ANDROID, IE, IPHONE, SAFARI, OPERA, OPERA_MOBILE, PHANTOMJS, MARIONETTE, QTWEBKIT},
          reason = "HtmlUnit: failure, others: not tested")
  @Test
  public void testGenerateKeyPressEventEvenWhenElementPreventsDefault() {
    assumeFalse(isFirefox(driver) && getFirefoxVersion(driver) < 25);
    assumeTrue(((RemoteWebDriver)driver).getCapabilities().is(CapabilityType.HAS_NATIVE_EVENTS));
    driver.get(pages.javascriptPage);

    WebElement silent = driver.findElement(By.name("suppress"));
    WebElement result = driver.findElement(By.id("result"));

    silent.sendKeys("s");
    assertThat(result.getText().trim(), is(""));
  }

  @JavascriptEnabled
  @Ignore(value = {ANDROID, IPHONE, OPERA, SAFARI, OPERA_MOBILE, MARIONETTE},
          reason = "Android/iOS/Opera Mobile: does not support contentEditable;" +
                   "Safari/Selenium: cannot type on contentEditable with synthetic events")
  @Test
  public void testTypingIntoAnIFrameWithContentEditableOrDesignModeSet() {
    driver.get(pages.richTextPage);

    driver.switchTo().frame("editFrame");
    WebElement element = driver.switchTo().activeElement();
    element.sendKeys("Fishy");

    driver.switchTo().defaultContent();
    WebElement trusted = driver.findElement(By.id("istrusted"));
    WebElement id = driver.findElement(By.id("tagId"));

    assertThat(trusted.getText(), anyOf(
        equalTo("[true]"),
        // Chrome does not set a trusted flag.
        equalTo("[n/a]"),
        equalTo("[]")));
    assertThat(id.getText(), anyOf(equalTo("[frameHtml]"), equalTo("[theBody]")));
  }

  @JavascriptEnabled
  @Ignore(value = {FIREFOX, HTMLUNIT, OPERA, ANDROID, OPERA_MOBILE, MARIONETTE}, issues = {6711})
  @Test
  public void testNonPrintableCharactersShouldWorkWithContentEditableOrDesignModeSet() {
    assumeFalse("not tested on mac", getEffectivePlatform().is(Platform.MAC));

    driver.get(pages.richTextPage);

    driver.switchTo().frame("editFrame");
    WebElement element = driver.switchTo().activeElement();
    element.sendKeys("Dishy", Keys.BACK_SPACE, Keys.LEFT, Keys.LEFT);
    element.sendKeys(Keys.LEFT, Keys.LEFT, "F", Keys.DELETE, Keys.END, "ee!");

    assertEquals("Fishee!", element.getText());
  }

  @JavascriptEnabled
  @Test
  public void testShouldBeAbleToTypeOnAnEmailInputField() {
    driver.get(pages.formPage);
    WebElement email = driver.findElement(By.id("email"));
    email.sendKeys("foobar");
    assertThat(email.getAttribute("value"), equalTo("foobar"));
  }

  @Test
  public void testShouldBeAbleToTypeOnANumberInputField() {
    driver.get(pages.formPage);
    WebElement email = driver.findElement(By.id("age"));
    email.sendKeys("33");
    assertThat(email.getAttribute("value"), equalTo("33"));
  }

  @Ignore(value = {ANDROID, HTMLUNIT, IPHONE, OPERA, SAFARI, OPERA_MOBILE},
          reason = "Untested browsers;" +
                   " Safari: cannot type on contentEditable with synthetic events",
          issues = {3127})
  @Test
  public void testShouldBeAbleToTypeIntoEmptyContentEditableElement() {
    driver.get(pages.readOnlyPage);
    WebElement editable = driver.findElement(By.id("content-editable"));

    editable.clear();
    editable.sendKeys("cheese"); // requires focus on OS X

    assertThat(editable.getText(), equalTo("cheese"));
  }

  @Ignore(value = {ALL}, reason = "Untested except in Firefox", issues = {2825})
  @Test
  public void testShouldBeAbleToTypeIntoContentEditableElementWithExistingValue() {
    driver.get(pages.readOnlyPage);
    WebElement editable = driver.findElement(By.id("content-editable"));

    String initialText = editable.getText();
    editable.sendKeys(", edited");

    assertThat(editable.getText(), equalTo(initialText + ", edited"));
  }

  @Ignore(value = {ANDROID, HTMLUNIT, IE, IPHONE, OPERA, SAFARI, OPERA_MOBILE},
          reason = "Untested browsers;" +
                   " Safari: cannot type on contentEditable with synthetic events",
          issues = {3127})
  @Test
  public void testShouldBeAbleToTypeIntoTinyMCE() {
    driver.get(appServer.whereIs("tinymce.html"));
    driver.switchTo().frame("mce_0_ifr");

    WebElement editable = driver.findElement(By.id("tinymce"));

    editable.clear();
    editable.sendKeys("cheese"); // requires focus on OS X

    assertThat(editable.getText(), equalTo("cheese"));
  }

  @JavascriptEnabled
  @Ignore(value = {HTMLUNIT, SAFARI, OPERA, IPHONE, ANDROID}, reason = "Untested")
  @Test
  public void canSafelyTypeOnElementThatIsRemovedFromTheDomOnKeyPress() {
    driver.get(appServer.whereIs("key_tests/remove_on_keypress.html"));

    WebElement input = driver.findElement(By.id("target"));
    WebElement log = driver.findElement(By.id("log"));

    assertEquals("", log.getAttribute("value"));

    input.sendKeys("b");
    assertThat(getValueText(log), equalTo(Joiner.on('\n').join(
        "keydown (target)",
        "keyup (target)",
        "keyup (body)")));

    input.sendKeys("a");

    // Some drivers (IE, Firefox) do not always generate the final keyup event since the element
    // is removed from the DOM in response to the keypress (note, this is a product of how events
    // are generated and does not match actual user behavior).
    String expected = Joiner.on('\n').join(
        "keydown (target)",
        "keyup (target)",
        "keyup (body)",
        "keydown (target)",
        "a pressed; removing");
    assertThat(getValueText(log), anyOf(equalTo(expected), equalTo(expected + "\nkeyup (body)")));
  }

  private static String getValueText(WebElement el) {
    // Standardize on \n and strip any trailing whitespace.
    return el.getAttribute("value").replace("\r\n", "\n").trim();
  }
}
