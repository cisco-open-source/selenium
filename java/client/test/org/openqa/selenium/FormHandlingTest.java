/*
Copyright 2007-2009 Selenium committers

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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.openqa.selenium.testing.Ignore.Driver.ANDROID;
import static org.openqa.selenium.testing.Ignore.Driver.HTMLUNIT;
import static org.openqa.selenium.testing.Ignore.Driver.IE;
import static org.openqa.selenium.testing.Ignore.Driver.IPHONE;
import static org.openqa.selenium.testing.Ignore.Driver.MARIONETTE;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA_MOBILE;
import static org.openqa.selenium.testing.Ignore.Driver.PHANTOMJS;
import static org.openqa.selenium.testing.Ignore.Driver.SAFARI;
import static org.openqa.selenium.testing.Ignore.Driver.QTWEBKIT;
import static org.openqa.selenium.testing.TestUtilities.isIe6;
import static org.openqa.selenium.testing.TestUtilities.isIe7;

import org.junit.Test;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.openqa.selenium.testing.TestUtilities;

import java.io.File;
import java.io.IOException;

public class FormHandlingTest extends JUnit4TestBase {

  @Test
  public void testShouldClickOnSubmitInputElements() {
    driver.get(pages.formPage);
    driver.findElement(By.id("submitButton")).click();
    wait.until(titleIs("We Arrive Here"));
    assertThat(driver.getTitle(), equalTo("We Arrive Here"));
  }

  @Test
  public void testClickingOnUnclickableElementsDoesNothing() {
    driver.get(pages.formPage);
    try {
      driver.findElement(By.xpath("//body")).click();
    } catch (Exception e) {
      e.printStackTrace();
      fail("Clicking on the unclickable should be a no-op");
    }
  }

  @Ignore(value = ANDROID, reason = "The page is zoomed in because of the previous state"
                                    + "which causes the click to fail.")
  @Test
  public void testShouldBeAbleToClickImageButtons() {
    driver.get(pages.formPage);
    driver.findElement(By.id("imageButton")).click();
    wait.until(titleIs("We Arrive Here"));
    assertThat(driver.getTitle(), equalTo("We Arrive Here"));
  }

  @Test
  @Ignore(MARIONETTE)
  public void testShouldBeAbleToSubmitForms() {
    driver.get(pages.formPage);
    driver.findElement(By.name("login")).submit();
    wait.until(titleIs("We Arrive Here"));
  }

  @Test
  @Ignore(MARIONETTE)
  public void testShouldSubmitAFormWhenAnyInputElementWithinThatFormIsSubmitted() {
    driver.get(pages.formPage);
    driver.findElement(By.id("checky")).submit();
    wait.until(titleIs("We Arrive Here"));
  }

  @Test
  @Ignore(MARIONETTE)
  public void testShouldSubmitAFormWhenAnyElementWithinThatFormIsSubmitted() {
    driver.get(pages.formPage);
    driver.findElement(By.xpath("//form/p")).submit();
    wait.until(titleIs("We Arrive Here"));
  }

  @Test(expected = NoSuchElementException.class)
  @Ignore(value = {ANDROID, IPHONE, OPERA, PHANTOMJS, SAFARI, OPERA_MOBILE, MARIONETTE})
  public void testShouldNotBeAbleToSubmitAFormThatDoesNotExist() {
    driver.get(pages.formPage);
    driver.findElement(By.name("SearchableText")).submit();
  }

  @Test
  @Ignore(OPERA_MOBILE)
  public void testShouldBeAbleToEnterTextIntoATextAreaBySettingItsValue() {
    driver.get(pages.javascriptPage);
    WebElement textarea = driver.findElement(By.id("keyUpArea"));
    String cheesy = "brie and cheddar";
    textarea.sendKeys(cheesy);
    assertThat(textarea.getAttribute("value"), equalTo(cheesy));
  }

  @Ignore(value = {ANDROID, OPERA_MOBILE},
          reason = "Android: capitalization bug in ICS keeps caps on after a capital letter is sent")
  @Test
  public void testSendKeysKeepsCapitalization() {
    driver.get(pages.javascriptPage);
    WebElement textarea = driver.findElement(By
                                                 .id("keyUpArea"));
    String cheesey = "BrIe And CheDdar";
    textarea.sendKeys(cheesey);
    assertThat(textarea.getAttribute("value"), equalTo(cheesey));
  }

  @Ignore(value = {IPHONE, ANDROID, OPERA_MOBILE})
  @Test
  public void testShouldSubmitAFormUsingTheNewlineLiteral() {
    driver.get(pages.formPage);
    WebElement nestedForm = driver.findElement(By.id("nested_form"));
    WebElement input = nestedForm.findElement(By.name("x"));
    input.sendKeys("\n");
    wait.until(titleIs("We Arrive Here"));
    assertTrue(driver.getCurrentUrl().endsWith("?x=name"));
  }

  @Ignore({IPHONE, ANDROID, OPERA_MOBILE, MARIONETTE})
  @Test
  public void testShouldSubmitAFormUsingTheEnterKey() {
    driver.get(pages.formPage);
    WebElement nestedForm = driver.findElement(By.id("nested_form"));
    WebElement input = nestedForm.findElement(By.name("x"));
    input.sendKeys(Keys.ENTER);
    wait.until(titleIs("We Arrive Here"));
    assertTrue(driver.getCurrentUrl().endsWith("?x=name"));
  }

  @Test
  public void testShouldEnterDataIntoFormFields() {
    driver.get(pages.xhtmlTestPage);
    WebElement element = driver.findElement(By.xpath("//form[@name='someForm']/input[@id='username']"));
    String originalValue = element.getAttribute("value");
    assertThat(originalValue, equalTo("change"));

    element.clear();
    element.sendKeys("some text");

    element = driver.findElement(By
                                     .xpath("//form[@name='someForm']/input[@id='username']"));
    String newFormValue = element.getAttribute("value");
    assertThat(newFormValue, equalTo("some text"));
  }

  @Ignore(value = {IPHONE, ANDROID, SAFARI, OPERA, OPERA_MOBILE, MARIONETTE, QTWEBKIT},
          reason = "Does not yet support file uploads", issues = {4220})
  @Test
  public void testShouldBeAbleToAlterTheContentsOfAFileUploadInputElement() throws IOException {
    driver.get(pages.formPage);
    WebElement uploadElement = driver.findElement(By.id("upload"));
    assertThat(uploadElement.getAttribute("value"), equalTo(""));

    File file = File.createTempFile("test", "txt");
    file.deleteOnExit();

    uploadElement.sendKeys(file.getAbsolutePath());

    String uploadPath = uploadElement.getAttribute("value");
    assertTrue(uploadPath.endsWith(file.getName()));
  }

  @Ignore(value = {ANDROID, IPHONE, OPERA, SAFARI, OPERA_MOBILE, MARIONETTE, QTWEBKIT},
          reason = "Does not yet support file uploads", issues = {4220})
  @Test
  public void testShouldBeAbleToSendKeysToAFileUploadInputElementInAnXhtmlDocument()
      throws IOException {
    assumeFalse("IE before 9 doesn't handle pages served with an XHTML content type,"
                + " and just prompts for to download it",
                TestUtilities.isOldIe(driver));

    driver.get(pages.xhtmlFormPage);
    WebElement uploadElement = driver.findElement(By.id("file"));
    assertThat(uploadElement.getAttribute("value"), equalTo(""));

    File file = File.createTempFile("test", "txt");
    file.deleteOnExit();

    uploadElement.sendKeys(file.getAbsolutePath());

    String uploadPath = uploadElement.getAttribute("value");
    assertTrue(uploadPath.endsWith(file.getName()));
  }

  @Ignore(value = {IPHONE, ANDROID, OPERA, SAFARI, MARIONETTE, QTWEBKIT},
          reason = "Does not yet support file uploads", issues = {4220})
  @Test
  public void testShouldBeAbleToUploadTheSameFileTwice() throws IOException {
    File file = File.createTempFile("test", "txt");
    file.deleteOnExit();

    driver.get(pages.formPage);
    WebElement uploadElement = driver.findElement(By.id("upload"));
    assertThat(uploadElement.getAttribute("value"), equalTo(""));

    uploadElement.sendKeys(file.getAbsolutePath());
    uploadElement.submit();

    driver.get(pages.formPage);
    uploadElement = driver.findElement(By.id("upload"));
    assertThat(uploadElement.getAttribute("value"), equalTo(""));

    uploadElement.sendKeys(file.getAbsolutePath());
    uploadElement.submit();

    // If we get this far, then we're all good.
  }

  @Ignore(value = {IPHONE, OPERA}, reason = "iPhone: sendKeys implemented incorrectly.")
  @Test
  public void testSendingKeyboardEventsShouldAppendTextInInputs() {
    driver.get(pages.formPage);
    WebElement element = driver.findElement(By.id("working"));
    element.sendKeys("some");
    String value = element.getAttribute("value");
    assertThat(value, is("some"));

    element.sendKeys(" text");
    value = element.getAttribute("value");
    assertThat(value, is("some text"));
  }

  @Ignore(value = {ANDROID, HTMLUNIT, IPHONE, OPERA, OPERA_MOBILE},
          reason = "iPhone: sendKeys implemented incorrectly")
  @Test
  public void testSendingKeyboardEventsShouldAppendTextInInputsWithExistingValue() {
    driver.get(pages.formPage);
    WebElement element = driver.findElement(By.id("inputWithText"));
    element.sendKeys(". Some text");
    String value = element.getAttribute("value");

    assertThat(value, is("Example text. Some text"));
  }

  @Ignore(value = {IPHONE, ANDROID, OPERA_MOBILE},
          reason = "Not implemented going to the end of the line first;\n" +
                   "iPhone: sendKeys not implemented correctly")
  @Test
  public void testSendingKeyboardEventsShouldAppendTextInTextAreas() {
    driver.get(pages.formPage);
    WebElement element = driver.findElement(By.id("withText"));

    element.sendKeys(". Some text");
    String value = element.getAttribute("value");

    assertThat(value, is("Example text. Some text"));
  }

  @Test
  public void testEmptyTextBoxesShouldReturnAnEmptyStringNotNull() {
    driver.get(pages.formPage);
    WebElement emptyTextBox = driver.findElement(By.id("working"));
    assertEquals(emptyTextBox.getAttribute("value"), "");
  }

  @Test
  @Ignore(value = {ANDROID, HTMLUNIT, IPHONE, OPERA, PHANTOMJS, SAFARI, OPERA_MOBILE, MARIONETTE, QTWEBKIT},
          reason = "untested")
  public void handleFormWithJavascriptAction() {
    String url = appServer.whereIs("form_handling_js_submit.html");
    driver.get(url);
    WebElement element = driver.findElement(By.id("theForm"));
    element.submit();
    Alert alert = driver.switchTo().alert();
    String text = alert.getText();
    alert.accept();

    assertEquals("Tasty cheese", text);
  }

  @Ignore(value = {ANDROID, IPHONE, SAFARI, OPERA_MOBILE}, reason = "untested")
  @Test
  public void testCanClickOnASubmitButton() {
    checkSubmitButton("internal_explicit_submit");
  }

  @Ignore(value = {ANDROID, HTMLUNIT, IPHONE, SAFARI, OPERA_MOBILE}, reason = "untested")
  @Test
  public void testCanClickOnAnImplicitSubmitButton() {
    assumeFalse(isIe6(driver) || isIe7(driver) );
    checkSubmitButton("internal_implicit_submit");
  }

  @Ignore(value = {ANDROID, HTMLUNIT, IE, IPHONE, SAFARI, OPERA_MOBILE},
          reason = "IE, HtmlUnit: failed; Others: untested")
  @Test
  public void testCanClickOnAnExternalSubmitButton() {
    checkSubmitButton("external_explicit_submit");
  }

  @Ignore(value = {ANDROID, HTMLUNIT, IE, IPHONE, SAFARI, OPERA_MOBILE},
      reason = "IE, HtmlUnit: failed; Others: untested")
  @Test
  public void testCanClickOnAnExternalImplicitSubmitButton() {
    checkSubmitButton("external_implicit_submit");
  }

  private void checkSubmitButton(String buttonId) {
    driver.get(appServer.whereIs("click_tests/html5_submit_buttons.html"));
    String name = "Gromit";

    driver.findElement(By.id("name")).sendKeys(name);
    driver.findElement(By.id(buttonId)).click();

    wait.until(titleIs("Submitted Successfully!"));

    assertThat(driver.getCurrentUrl(), containsString("name="+name));
  }
}
