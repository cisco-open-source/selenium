/****************************************************************************
**
** Copyright © 1992-2014 Cisco and/or its affiliates. All rights reserved.
** All rights reserved.
** 
** $CISCO_BEGIN_LICENSE:APACHE$
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
** http://www.apache.org/licenses/LICENSE-2.0
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
**
** $CISCO_END_LICENSE$
**
****************************************************************************/

package org.openqa.selenium.qtwebkit.quick_tests;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.testing.JUnit4TestBase;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class FormHandlingTest extends JUnit4TestBase {

    @Before
    public void setUp() throws Exception {
        driver.get(pages.formHandlingTest);
    }

    @Test
    public void testShouldClickOnSubmitInputElements() {
        WebElement element = driver.findElement(By.id("changeLabelButton"));
        element.click();

        assertThat(driver.findElement(By.id("notClickableLabel")).getText(), equalTo("Label changed"));
    }

    @Test
    public void testClickingOnUnclickableElementsDoesNothing() {

        WebElement element = driver.findElement(By.xpath("//Text"));
        try {
            element.click();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Clicking on the unclickable should be a no-op");
        }
    }

    @Test
    public void testShouldNotBeAbleToFindAFormThatDoesNotExist() {

        try {
            driver.findElement(By.id("there is no spoon")).click();
            fail("Should not have succeeded");
        } catch (RuntimeException e) {
            assertThat(e, is(instanceOf(NoSuchElementException.class)));
        }
    }

    @Test
    public void testShouldBeAbleToEnterTextIntoATextAreaBySettingItsValue() {
        WebElement textarea = driver.findElement(By.id("workingArea"));
        textarea.clear();
        String cheesy = "brie and cheddar";
        textarea.sendKeys(cheesy);
        assertThat(textarea.getText(), equalTo(cheesy));
    }

    @Test
    public void testSendKeysKeepsCapitalization() {
        WebElement textarea = driver.findElement(By.id("workingArea"));
        textarea.clear();
        String cheesey = "BrIe And CheDdar";
        textarea.sendKeys(cheesey);
        assertThat(textarea.getText(), equalTo(cheesey));
    }

    @Test
    public void testShouldEnterDataIntoFormFields() {
        WebElement element = driver.findElement(By.xpath("//TextInput[@id='inputElement']" ));
        String originalValue = element.getText();
        assertThat(originalValue, equalTo("Example text"));

        element.clear();
        element.sendKeys("some text");

        String newFormValue = element.getText();
        assertThat(newFormValue, equalTo("some text"));
    }

    @Test
    public void testSendingKeyboardEventsShouldAppendTextInInputs() {
        WebElement element = driver.findElement(By.id("inputElement"));
        element.clear();
        element.sendKeys("some");
        String value = element.getText();
        assertThat(value, is("some"));

        element.sendKeys(" text");
        value = element.getText();
        assertThat(value, is("some text"));
    }

    @Test
    public void testSendingKeyboardEventsShouldAppendTextInInputsWithExistingValue() {
        WebElement element = driver.findElement(By.id("inputElement"));
        element.sendKeys(". Some text");
        String value = element.getText();

        assertThat(value, is("Example text. Some text"));
    }

    @Test
    public void testSendingKeyboardEventsShouldAppendTextInTextAreas() {
        WebElement element = driver.findElement(By.id("workingArea"));
        assertThat(element.getText(), is("Example text"));

        element.sendKeys(". Some text");
        String value = element.getText();
        // for QTextEdit and QPlainTextEdit cursor set in the begin default text
        // and sendKeys() insert text before set by default
        assertThat(value, is(". Some textExample text"));
    }

    @Test
    public void testSendingKeyboardEventsShouldAppendTextinTextAreasToTextSendBefore() {
        WebElement element = driver.findElement(By.id("workingArea"));
        element.clear();

        element.sendKeys("Example text");
        element.sendKeys(". Some text");
        String value = element.getText();
        assertThat(value, is("Example text. Some text"));
    }

    @Test
    public void testShouldBeAbleToClearTextFromInputElements() {
        WebElement element = driver.findElement(By.id("emptyInput"));
        element.sendKeys("Some text");
        String value = element.getText();
        assertThat(value.length(), is(greaterThan(0)));

        element.clear();
        value = element.getText();

        assertThat(value.length(), is(0));
    }

    @Test
    public void testEmptyTextBoxesShouldReturnAnEmptyStringNotNull() {
        WebElement emptyTextBox = driver.findElement(By.id("emptyInput"));
        assertEquals(emptyTextBox.getText(), "");
    }

    @Test
    public void testShouldBeAbleToClearTextFromTextAreas() {
        WebElement element = driver.findElement(By.id("workingArea"));
        element.sendKeys("Some text");
        String value = element.getText();
        assertThat(value.length(), is(greaterThan(0)));

        element.clear();
        value = element.getText();

        assertThat(value.length(), is(0));
    }

}